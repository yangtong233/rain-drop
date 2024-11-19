package org.raindrop.common.utils.natives;

/**
 * created by yangtong on 2024/6/4 上午10:58
 *
 * 网络框架需要经常用到的常量
 */
public final class Constants {
    public static final String UNREACHED = "Shouldn't be reached";

    //c语言字符串的结束符
    public static final byte NUT = (byte) '\0';

    //默认情况下的空byte数组
    public static final byte[] EMPTY_BYTES = new byte[0];

    //网络框架初始化
    public static final int INITIAL = 0;

    //启动中
    public static final int STARTING = 1;

    //运行中
    public static final int RUNNING = 2;

    //正在关闭
    public static final int CLOSING = 3;

    //已停止
    public static final int STOPPED = 4;
}
