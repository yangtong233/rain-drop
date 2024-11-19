package org.raindrop.common.utils.timer;

/**
 * created by yangtong on 2024/6/11 下午5:11
 * 时钟工具类
 */
public final class Clocks {

    private Clocks() {
        throw new UnsupportedOperationException();
    }

    /**
     * 获取当前毫秒时间戳
     */
    public static long current() {
        return System.currentTimeMillis();
    }

    /**
     * 获取当前纳秒时间戳
     */
    public static long nano() {
        return System.nanoTime();
    }

    /**
     * 计算自指定时间戳以来经过的纳秒数
     */
    public static long elapsed(long nano) {
        return nano() - nano;
    }
}