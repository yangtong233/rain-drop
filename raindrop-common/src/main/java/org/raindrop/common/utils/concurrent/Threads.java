package org.raindrop.common.utils.concurrent;

import org.raindrop.common.exception.BaseException;

import java.util.concurrent.*;
import java.util.function.Consumer;

/**
 * created by yangtong on 2024/6/9 23:46:48
 * 多线程工具类
 */
public class Threads {

    private final static ExecutorService executorService;
    /**
     * CPU核心数量
     */
    private final static int CPU_CORES = Runtime.getRuntime().availableProcessors();

    static {
        executorService = new ThreadPoolExecutor(CPU_CORES, (int) (CPU_CORES * 1.5), 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(1024));
    }

    /**
     * 线程睡眠
     *
     * @param duration 睡眠时间
     */
    public static void sleep(long duration) {
        sleep(TimeUnit.MILLISECONDS, duration);
    }

    /**
     * 线程睡眠
     *
     * @param timeUnit 睡眠时间单位
     * @param duration 睡眠时间
     */
    public static void sleep(TimeUnit timeUnit, long duration) {
        try {
            timeUnit.sleep(duration);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 不响应中断的sleep
     *
     * @param time 时间,单位毫秒
     */
    public static void sleepUnInterrupted(long time) {
        boolean interrupted = false;
        try {
            long remaining = time;
            long start = System.currentTimeMillis();
            while (remaining > 0) {
                try {
                    Thread.sleep(remaining);
                } catch (InterruptedException e) {
                    interrupted = true;
                }
                remaining = time - (System.currentTimeMillis() - start);
            }
        } finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * 执行没有返回值任务
     *
     * @param task 任务对象
     */
    public static void execute(Runnable task) {
        executorService.execute(task);
    }

    /**
     * 执行没有返回值多个任务，只有任务全部完成，才会继续执行后续代码
     *
     * @param tasks 任务数组
     */
    public static void execute(Runnable... tasks) {
        try {
            CountDownLatch latch = new CountDownLatch(tasks.length);

            for (Runnable task : tasks) {
                execute(() -> {
                    try {
                        task.run();
                    } finally {
                        latch.countDown();
                    }
                });
            }

            //阻塞，找到所有任务执行结束
            latch.await();
        } catch (InterruptedException e) {
            throw new BaseException(e);
        }
    }

    /**
     * 提交有返回值的任务
     *
     * @param task
     * @return
     */
    public static Future<?> submit(Callable<?> task) {
        return executorService.submit(task);
    }

    /**
     * 提交一个耗时任务，耗时任务完成后，执行回调函数
     *
     * @param task     耗时任务
     * @param callback 回调函数
     */
    public static <T> void submit(Callable<T> task, Consumer<T> callback) {
        execute(() -> {
            GuardedObject<T> guardedObject = new GuardedObject<>();
            guardedObject.set(task);
            T response = guardedObject.get();
            callback.accept(response);
        });
    }
}
