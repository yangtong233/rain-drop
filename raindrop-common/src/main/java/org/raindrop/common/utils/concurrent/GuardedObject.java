package org.raindrop.common.utils.concurrent;

import cn.hutool.core.exceptions.ExceptionUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.raindrop.common.exception.BaseException;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * 保护性暂停模式(GuardedSuspension)-同步模式
 * 使用while循环来解决虚假唤醒问题，如果一个线程被虚假唤醒，就会进入下一轮循环并继续等待
 * 要点:
 * 1.一个线程需要将一个结果传递到另一个线程，让他们关联到一个GuardedObject
 * 2.一个线程需要将结果不断地传递到另一个线程(生产者/消费者)
 * 3.一个线程要等待某个条件成立或另一个线程的执行结果
 * join，Future的实现均采用了该模式
 */
@Data
@Slf4j
public class GuardedObject<T> {
    //该GuardedObject的唯一id
    private Long id;
    //保存结果，生产者产生这个结果，消费者使用这个结果
    private volatile T response;

    public GuardedObject() {
        this(System.currentTimeMillis());
    }

    public GuardedObject(Long id) {
        this.id = id;
    }

    /**
     * 获取response，如果还没有响应则阻塞
     *
     * @return 响应
     */
    public synchronized T get() {

        try {
            while (this.response == null) {
                this.wait();
            }
            return this.response;
        } catch (InterruptedException e) {
            log.error(ExceptionUtil.stacktraceToString(e));
            return null;
        } finally {
            this.response = null;
            this.notifyAll();
        }
    }

    /**
     * 获取response，带超时效果
     *
     * @param timeout 超时时间
     * @return 响应
     */
    public synchronized T get(long timeout) {
        //开始时间
        long beginTime = System.currentTimeMillis();
        //经历时间
        long passedTime = 0;
        while (this.response == null) {
            if (passedTime >= timeout) {
                break;
            }
            try {
                //每轮的等待时间逐渐减小，因为passedTime经历的时间在变大
                this.wait(timeout - passedTime);
            } catch (InterruptedException e) {
                log.error(ExceptionUtil.stacktraceToString(e));
                return null;
            } finally {
                //每轮循环结束计算总共经历的时间
                passedTime = System.currentTimeMillis() - beginTime;
            }
        }
        T response = this.response;
        this.response = null;
        this.notifyAll();
        return response;
    }

    /**
     * 产生response
     *
     * @param response 响应
     */
    public synchronized void set(T response) {
        try {
            while (this.response != null) {
                this.wait();
            }
            this.response = response;
        } catch (InterruptedException e) {
            throw new BaseException(e);
        } finally {
            this.notifyAll();
        }
    }

    /**
     * 根据指定任务产生response
     * @param task
     */
    public synchronized void set(Callable<T> task) {
        try {
            this.response = task.call();
        } catch (Exception e) {
            throw new BaseException(e);
        } finally {
            this.notifyAll();
        }
    }
}