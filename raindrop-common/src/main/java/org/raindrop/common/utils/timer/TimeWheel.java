package org.raindrop.common.utils.timer;

/**
 * created by yangtong on 2024/6/11 下午5:03
 * 时间轮接口
 */
public sealed interface TimeWheel extends IWheel permits DefaultTimeWheel {
    //时间轮默认有4096个插槽
    int slots = Integer.getInteger("time-wheel.slots", 65536);
    //时间轮插槽的刻度默认是10ms
    long tick = Long.getLong("time-wheel.tick", 10);

    static TimeWheel wheel() {
        return DefaultTimeWheel.INSTANCE;
    }

    /**
     * 用于向时间轮中添加需要单次执行的定时任务
     *
     * @param mission 任务对象
     * @param delay   延迟执行时间，ms
     * @return 定时任务的一个句柄，可以通过调用它来对该定时任务实现取消
     */
    Task addJob(Runnable mission, long delay);

    /**
     * 向时间轮中添加需要周期执行的定时任务
     *
     * @param mission 任务对象
     * @param delay   任务第一次执行延迟时间，ms
     * @param period  任务每次执行间隔
     * @return 定时任务的一个句柄，可以通过调用它来对该定时任务实现取消
     */
    Task addJob(Runnable mission, long delay, long period);

    /**
     * 向时间轮中添加需要周期执行的定时任务
     *
     * @param mission 任务对象
     * @param delay   任务第一次执行延迟时间，ms
     * @param period  任务每次执行间隔
     * @param time    执行次数
     * @return 定时任务的一个句柄，可以通过调用它来对该定时任务实现取消
     */
    Task addJob(Runnable mission, long delay, long period, int time);
}