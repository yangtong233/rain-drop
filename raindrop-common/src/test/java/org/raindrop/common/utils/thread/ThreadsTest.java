package org.raindrop.common.utils.thread;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.raindrop.common.utils.concurrent.GuardedObject;
import org.raindrop.common.utils.concurrent.Threads;

import java.util.concurrent.TimeUnit;

/**
 * created by yangtong on 2024/6/10 01:10:26
 * 多线程测试类型
 */
@Slf4j
public class ThreadsTest {
    @Test
    public void testGuardedObject() {
        GuardedObject<Long> guarded = new GuardedObject<>();
        //消费者源源不断地消费数据
        new Thread(() -> {
            while (true) {
                //一旦产生数据，立即消费。最多等待1999ms
                log.info("消费者收到消息:{}", guarded.get());
            }
        }, "消费者").start();

        //生产者源源不断地产生数据，
        new Thread(() -> {
            while (true) {
                long l = System.currentTimeMillis();
                log.info("生产者产生消息:{}", l);
                guarded.set(l);
                Threads.sleep(TimeUnit.SECONDS, 2);
            }
        }, "生产者").start();

        //保证主线程不退出
        Threads.sleep(TimeUnit.SECONDS, 10);
    }

    @Test
    public void testSubmit() {
        Threads.submit(() -> {
            log.info("正在执行任务");
            Threads.sleep(TimeUnit.SECONDS, 5);
            return 114514;
        }, (result) -> {
            log.info("收集执行结果{}", result);
        });
        Threads.sleep(TimeUnit.SECONDS, 6);
    }
}
