package org.raindrop.common.utils.wheel;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.raindrop.common.utils.concurrent.Threads;
import org.raindrop.common.utils.timer.Task;
import org.raindrop.common.utils.timer.TimeWheel;

/**
 * created by yangtong on 2024/6/11 22:00:05
 * 时间轮测试类
 */
@Slf4j
public class TimeWheelTest {
    @Test
    public void testWheel() throws InterruptedException {
        TimeWheel timeWheel = TimeWheel.wheel();
        timeWheel.start();
        timeWheel.addJob(() -> log.info("hello world"), 2000);
        Task task = timeWheel.addJob(() -> log.info("hello wheel"), 0, 500);
        Threads.sleep(5000);
        //主动停止周期性任务
        task.stop();

        Task task1 = timeWheel.addJob(() -> log.info("hello task1"), 10000, 10000);
        //立即执行一次任务
        task1 = task1.doNow();
        Threads.sleep(5000);
        task1 = task1.doNow();
        Threads.sleep(5000);
        task1 = task1.doNow();
        Threads.sleep(1000);
        timeWheel.stop();

    }
}
