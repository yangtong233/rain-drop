package org.raindrop.common.exception;

import cn.hutool.core.exceptions.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.raindrop.common.enums.Resp;
import org.raindrop.common.utils.concurrent.Threads;
import org.raindrop.common.web.R;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public R<?> baseExceptionHandler(BaseException e) {
        log.error(ExceptionUtil.stacktraceToString(e));
        return R.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public R<?> exceptionHandler(Exception e) {
        log.error(ExceptionUtil.stacktraceToString(e));
        return R.error(Resp.OTHER_ERROR.code, e.getClass().getName() + " -> " +e.getMessage());
    }

    /**
     * 记录异常日志
     */
    private void logging(Exception e) {
        Threads.execute(() -> {
            //TODO 记录异常日志
        });
    }
}
