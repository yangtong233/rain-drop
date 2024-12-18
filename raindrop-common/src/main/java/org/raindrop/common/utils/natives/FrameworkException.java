package org.raindrop.common.utils.natives;

import org.raindrop.common.utils.string.Strs;

/**
 * created by yangtong on 2024/6/4 上午10:42
 *
 * @Description: 网络框架异常
 */
public class FrameworkException extends RuntimeException {
    public FrameworkException(ExceptionType exceptionType, String message) {
        this(exceptionType, message, null, (Object) null);
    }

    public FrameworkException(ExceptionType exceptionType, String message, Throwable throwable) {
        this(exceptionType, message, throwable, (Object) null);
    }

    public FrameworkException(ExceptionType exceptionType, String message, Object... args) {
        this(exceptionType, message, null, args);
    }

    public FrameworkException(ExceptionType exceptionType, String message, Throwable throwable, Object... args) {
        super(Strs.format("Type : {}, Msg : {}",
                        exceptionType,
                        args == null ? message : String.format(message, args)),
                throwable);
    }
}
