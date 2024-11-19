package org.raindrop.common.utils.timer;

/**
 * 基于cron表达式的时间轮任务的元数据，包含任务对象、cron表达式
 * 注册到时间轮上时，将其转化为Job
 *
 * @param mission 任务对象
 * @param cron    cron表达式
 */
public record CronWheelMetaTask(
        Runnable mission,
        String cron
) {
}