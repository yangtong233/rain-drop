package org.raindrop.common.utils.timer;

/**
 * created by yangtong on 2024/6/12 下午4:40
 *
 * 基础时间轮
 */
public interface IWheel {

    /**
     * 命运的齿轮开始按照既定的周期缓缓转动
     */
    void start();

    /**
     * 命运的齿轮停止
     *
     * @throws InterruptedException 中断异常
     */
    void stop() throws InterruptedException;

}
