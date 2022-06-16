package com.lsc.config;

import com.lsc.utils.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 统一异常处理
 */
@Slf4j
@RestControllerAdvice
public class ExceptionHandlerConfig {

    /**
     * 运行时异常处理
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseResult exceptionHandler(RuntimeException e) {
        // 把错误信息输入到日志中
        log.error(e.getMessage());
        return ResponseResult.error(e.getMessage());
    }

}
