package org.raindrop.common.utils.lock;

import org.junit.Test;

/**
 * created by yangtong on 2024/6/7 下午12:34
 *
 * @Description: 测试State类是否具有正常锁的功能
 */
public class StateTest {

    private static int number = 0;
    private static final State state = new State();

    /**
     * 没有使用state的情况，线程不安全
     */
    @Test
    public void testWithoutState() throws InterruptedException {
        // 线程1：执行 100W 次 number+1 操作
        Thread t1 = new Thread(StateTest::add);
        t1.start();

        // 线程2：执行 100W 次 number-1 操作
        Thread t2 = new Thread(StateTest::sub);
        t2.start();

        // 等待线程 1 和线程 2，执行完，打印 number 最终的结果
        t1.join();
        t2.join();
        System.out.println("number 最终结果：" + number);
    }

    /**
     * 使用了state的情况，线程安全
     */
    @Test
    public void testWithState() throws InterruptedException {
        // 线程1：执行 100W 次 number+1 操作
        Thread t1 = new Thread(StateTest::add1);
        t1.start();

        // 线程2：执行 100W 次 number-1 操作
        Thread t2 = new Thread(StateTest::sub1);
        t2.start();

        // 等待线程 1 和线程 2，执行完，打印 number 最终的结果
        t1.join();
        t2.join();
        System.out.println("number 最终结果：" + number);
    }

    static void add() {
        for (int i = 0; i < 1000000; i++) {
            number++;
        }
    }

    static void sub() {
        for (int i = 0; i < 1000000; i++) {
            number--;
        }
    }

    static void add1() {
        for (int i = 0; i < 1000000; i++) {
            try(Mutex _ = state.withMutex()){
                //线程想要进入try块，就必须获取state锁
                number++;
            }
        }
    }

    static void sub1() {
        for (int i = 0; i < 1000000; i++) {
            try(Mutex _ = state.withMutex()){
                //线程想要进入try块，就必须获取state锁
                number--;
            }
        }
    }

}
