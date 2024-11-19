package org.raindrop.common.utils.timer;

import io.netty.util.internal.shaded.org.jctools.queues.atomic.MpscUnboundedAtomicArrayQueue;
import lombok.extern.slf4j.Slf4j;
import org.raindrop.common.exception.BaseException;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;

/**
 * created by yangtong on 2024/6/11 下午5:16
 * <p>
 * 时间轮实现类
 */
@Slf4j
public final class DefaultTimeWheel implements TimeWheel {
    //取消任务的标志位
    private static final int CANCEL_MISSION = -1;
    //该原子变量保证单例
    private static final AtomicBoolean instanceFlag = new AtomicBoolean(false);
    //表示时间轮结束的任务，如果时间轮线程发现新任务队列中有exitTask，就停止时间轮
    private static final WheelMetaTask exitTask = new WheelMetaTask(null, Long.MIN_VALUE, Long.MIN_VALUE, 1);
    /**
     * mask = slots - 1， 当slots是2的次幂时，mask是其掩码，用于方便求余
     * 比如slots是1000，则mask是0111，通过currentSlot & mask得到余数，也就是当前槽位在时间轮上的索引
     * 所以slots必须是2的次幂
     */
    private final int mask;
    /**
     * 由mask >> 1计算而来，用于判断访问TreeSet集合的时机
     * 如果(slot & cMask) == 0，则访问waitSet，判断是否需要将元素从集合转移至时间轮中，也就是时间轮开始时和转到一半时访问一次waitSet
     */
    private final int cMask;
    //每个时间槽的毫秒间隔时间
    private final long tick;
    //每个时间槽的纳秒间隔时间，根据tick计算得来
    private final long tickNano;
    /**
     * 由slots * tick 进行计算得来，代表了时间轮中可容纳的最大毫秒延迟的任务
     * 当时间轮指针从0出发移动再返回至0，总共经历了bound毫秒
     * 如果某个任务的延迟时间超过了bound，则会被放入waitSet中
     */
    private final long bound;

    //时间轮，本质是数组，通过求余模拟时间轮的刻度
    private final Job[] wheel;
    /**
     * 存放时间间隔较长的任务
     * waitSet中的任务执行期限均要超过bound值，那么我们只需要以小于bound值的间隔周期访问TreeSet集合即可
     */
    private final TreeSet<Job> waitSet = new TreeSet<>(Job::compareTo);
    /**
     * 新任务就暂时存放在这个队列，等待时间轮线程将其放入对应插槽
     * 取消任务、停止时间轮本身也是任务，也放在这个队列里
     */
    private final Queue<WheelMetaTask> newTaskQueue;

    /**
     * 时间轮的计数线程
     */
    private final Thread wheelThread;

    //单例
    public static final TimeWheel INSTANCE = new DefaultTimeWheel(TimeWheel.slots, TimeWheel.tick);

    /**
     * 保证单例
     *
     * @param slots 时间轮槽位数
     * @param tick  时间轮槽位间隔
     */
    private DefaultTimeWheel(int slots, long tick) {
        if (!instanceFlag.compareAndSet(false, true)) {
            throw new BaseException("时间轮必须是单例的");
        }
        if (slots < 256) {
            throw new BaseException("时间轮槽位数必须太小");
        }
        //保证slots是2的次幂，方便使用currentSlot & (slots - 1)进行取余操作
        if (Integer.bitCount(slots) != 1) {
            throw new BaseException("时间轮槽位数必须是2的次幂");
        }
        this.mask = slots - 1;
        this.tick = tick;
        this.tickNano = Duration.ofMillis(tick).toNanos();
        this.bound = slots * tick;
        this.cMask = mask >> 1;
        this.newTaskQueue = new MpscUnboundedAtomicArrayQueue<>(1024);
        this.wheel = new Job[slots];
        //创建一个虚拟线程作为时间轮线程，但并不马上启动
        this.wheelThread = Thread.ofPlatform().name("time-wheel-thead").unstarted(this::run);
    }

    @Override
    public void start() {
        //启动时间轮线程
        wheelThread.start();
        log.info("命运的齿轮开始按照既定的周期缓缓转动...");
    }

    @Override
    public void stop() throws InterruptedException {
        //插入一个exitTask任务，时间轮线程下一次循环发现新任务队列中有exitTask，就停止时间轮
        if (!newTaskQueue.offer(exitTask)) {
            throw new BaseException("新任务队列满了");
        }
        //走到这说明停止任务注册成功，等待时间轮的停止
        wheelThread.join();
        log.info("命运的齿轮已经停止...");
    }

    @Override
    public Task addJob(Runnable mission, long delay) {
        //只执行一次的任务，执行间隔period没意义
        return addWheelTask(mission, delay, 0, 1);
    }

    @Override
    public Task addJob(Runnable mission, long delay, long period) {
        if (period <= tick) {
            throw new BaseException("任务执行频率太高，应大于:[{}]", tick);
        }
        //对于无限执行的周期性任务，执行次数设置为Integer.MAX_VALUE
        return addWheelTask(mission, delay, period, Integer.MAX_VALUE);
    }

    @Override
    public Task addJob(Runnable mission, long delay, long period, int time) {
        if (period <= tick) {
            throw new BaseException("周期性任务执行频率不能超过时间轮刻度{}ms", tick);
        }
        if (time < 0) {
            throw new BaseException("任务执行次数不合法:time[{}]", time);
        }

        return addWheelTask(mission, delay, period, time);
    }

    /**
     * 添加新任务，等待着时间轮线程将其注册到时间轮上
     *
     * @param mission 任务对象
     * @param delay   第一次执行延迟
     * @param period  周期性任务执行间隔，如果是一次性任务则为null
     * @return 当前任务的对象的反注册任务
     */
    private Task addWheelTask(Runnable mission, long delay, long period, int time) {
        //得到当前毫秒
        long current = Clocks.current();
        //封装对应的时间轮任务
        WheelMetaTask wheelMetaTask = new WheelMetaTask(mission, current + delay, period, time);
        //该任务的反注册任务
        WheelMetaTask cancelTask = new WheelMetaTask(mission, current + delay, 1, CANCEL_MISSION);
        //注册新任务
        if (!newTaskQueue.offer(wheelMetaTask)) {
            throw new BaseException("新任务队列满了");
        }
        //添加反注册任务并返回，让调用者可以控制任务的停止的运行
        return new Task() {
            //立即停止该任务
            @Override
            public void stop() {
                if (!newTaskQueue.offer(cancelTask)) {
                    throw new BaseException("新任务队列满了");
                }
            }

            //立即执行一次该任务
            @Override
            public Task doNow() {
                if (!newTaskQueue.offer(new WheelMetaTask(mission, Clocks.current(), 0, 1))) {
                    throw new BaseException("新任务队列满了");
                }
                //停止旧任务
                stop();
                return addWheelTask(mission, delay, period, time);
            }
        };

    }

    /**
     * 时间轮线程的主体方法
     * 1.获取当前槽位对应的时间，计算出下一个槽位的位置及其对应时间
     * 2.检索newTaskQueue，执行任务的注册与反注册，并判断是否需要退出时间轮
     * 3.使用当前槽位和cMask进行 按位与 操作，来判断是否需要检索waitSet结构，将临近执行的任务转移到时间轮中
     * 4.执行当前槽位中的所有任务
     */
    private void run() {
        //得到当前纳秒和毫秒数
        long nano = Clocks.nano();
        long milli = Clocks.current();
        int slot = 0;
        //时间轮线程不断循环
        while (true) {
            final long currentMilli = milli;
            final int currentSlot = slot;
            //计算下一次执行的时间
            nano = nano + tickNano;
            milli = milli + tick;
            //计算当前槽位
            slot = (slot + 1) & mask;

            //从taskQueue中取出新任务，注册到时间轮上
            for (; ; ) {
                log.debug("开始新一轮循环，时间轮当前刻度：{},总刻度：{}", currentSlot, mask + 1);
                //WheelTask是时间轮任务，需要转换成Job并注册
                final WheelMetaTask task = newTaskQueue.poll();
                //取不到新任务了，则跳出循环
                if (task == null) {
                    break;
                }
                //时间轮停止
                else if (task == exitTask) {
                    return;
                }
                //开始处理新任务
                else {
                    Job job = new Job(task.mission(), task.execMilli(), task.period(), task.time());
                    long delayMillis = Math.max(task.execMilli() - currentMilli, 0);
                    int pos = calculatePos(currentSlot, delayMillis);
                    //取消任务
                    if (task.time() == CANCEL_MISSION) {
                        cancelJob(job, pos);
                    }
                    //注册新任务的操作放在最后，避免新任务一注册就被取消了
                    else {
                        registerJob(job, pos, delayMillis);
                    }
                }
            }

            //判断该不该访问waitSet，并将waitSet中的任务转移到时间轮中
            if ((currentSlot & cMask) == 0) {
                Iterator<Job> iterator = waitSet.iterator();
                while (iterator.hasNext()) {
                    Job job = iterator.next();
                    long delayMillis = job.execMilli - currentMilli;
                    //计算下一次任务的延迟时间，如果小于bound，则说明可以放入时间轮
                    if (delayMillis < bound) {
                        int pos = calculatePos(currentSlot, delayMillis);
                        iterator.remove();
                        registerJob(job, pos, delayMillis);
                    }
                    //如果某一个任务延迟时间大于bound，那么后面的任务肯定大于bound，直接break
                    else {
                        break;
                    }
                }
            }

            //得到时间轮当前槽位上的任务链表
            Job current = wheel[currentSlot];
            //遍历任务并执行
            while (current != null) {
                //得到下一个任务，避免被后面的current.next = null影响
                Job next = current.next;
                //只有任务的pos跟当前槽位的索引匹配才执行任务，这是为了方便取消任务
                if (current.pos == currentSlot) {
                    //使用虚拟线程来执行任务，避免任务阻塞时间轮线程
                    Thread.ofVirtual().name("worker-thead").start(current.mission);
                    long period = current.period;
                    //如果该任务的剩余执行次数不为0，则继续执行
                    if (current.remain > 0) {
                        current.execMilli = current.execMilli + period;
                        int pos = calculatePos(currentSlot, period);
                        //如果该任务下一次还能执行的话，就重新注册到时间轮上
                        if (current.subRemain() > 0) {
                            registerJob(current, pos, period);
                        } else {
                            current.next = null;
                        }
                    }
                }
                //如果槽位与pos不匹配，则不执行任务并也将next置为null
                else {
                    current.next = null;
                }
                //继续遍历下一个任务
                current = next;
            }
            //当前槽位的所有任务都执行完毕了，将其置空
            wheel[currentSlot] = null;

            //休眠一段时间后，进入下一个槽位
            LockSupport.parkNanos(nano - Clocks.nano());
        }
    }

    /**
     * 注册一次性任务
     *
     * @param job         任务
     * @param pos         时间轮刻度
     * @param delayMillis 延迟毫秒
     */
    private void registerJob(Job job, int pos, long delayMillis) {
        //延迟时间大于bound，说明该任务至少下一轮才能执行，放进waitSet中
        if (delayMillis >= bound) {
            job.pos = -1;
            waitSet.add(job);
        }
        //直接放进时间轮中
        else {
            job.pos = pos;
            Job current = wheel[pos];
            //如果当前槽位中已经有任务了，就将新任务插入到链表的头部
            if (current != null) {
                job.next = current;
            }
            wheel[pos] = job;
        }
    }

    /**
     * 反注册任务
     * 根据时间轮槽位索引定位到指定槽位，然后再取消指定的一次性job任务
     * 对于waitSet中的任务，我们是直接将其移除
     * 对于时间轮中的任务，我们其实并不需要立刻将其移除，只需要将其pos值修改为-1，那么时间轮线程就不会执行该任务
     *
     * @param job 任务
     * @param pos 槽位索引
     */
    private void cancelJob(Job job, int pos) {
        if (!waitSet.remove(job)) {
            //如果waitSet不包含job，则进入到这里
            //从时间轮上找到当前job链表
            Job current = wheel[pos];
            //遍历链表，找到job并将其pos置为-1
            while (current != null) {
                if (current.mission == job.mission) {
                    //时间轮线程会跳过pos为-1的任务，等价于取消任务
                    current.pos = -1;
                    return;
                } else {
                    current = current.next;
                }
            }
        }
    }

    /**
     * 根据延迟时间计算当前槽位的索引
     *
     * @param currentSlot 当前槽位
     * @param delayMillis 延迟时间
     * @return 槽位索引
     */
    private int calculatePos(int currentSlot, long delayMillis) {
        return (int) ((currentSlot + (delayMillis / tick)) & mask);
    }

}
