package org.raindrop.common.utils.lock;

import org.raindrop.common.exception.BaseException;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * created by yangtong on 2024/6/7 下午12:28
 * 封装了ReentrantLock锁，配合try-with-resources进行自动释放
 */
public record Mutex(Lock lock) implements AutoCloseable {
    public Mutex {
        if (lock == null) {
            throw new BaseException("lock不能为空");
        }
    }

    public Mutex() {
        this(new ReentrantLock());
    }

    public Mutex acquire() {
        lock.lock();
        return this;
    }

    @Override
    public void close() {
        lock.unlock();
    }
}
