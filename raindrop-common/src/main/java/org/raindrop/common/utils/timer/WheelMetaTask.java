package org.raindrop.common.utils.timer;

/**
 * 时间轮任务的元数据，包含任务对象、执行时间、执行周期、执行次数
 * 注册到时间轮上时，将其转化为Job
 *
 * @param mission   任务对象
 * @param execMilli 第一次执行时间
 * @param period    周期性任务执行间隔
 * @param time      任务执行次数
 */
public record WheelMetaTask(
        Runnable mission,
        long execMilli,
        long period,
        int time
) {
}