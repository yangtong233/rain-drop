package org.raindrop.common.utils.timer;

/**
 * created by yangtong on 2024/6/11 23:01:46
 */
public interface Task {
    /**
     * 停止当前定时任务
     */
    void stop();

    /**
     * 立即执行一次定时任务
     */
    Task doNow();
}
