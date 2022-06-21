package com.lsc.config;

import com.lsc.utils.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 */
@RestControllerAdvice
@Slf4j
public class MyExceptionHandler {

    /**
     * 处理所有异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseResult exceptionHandler(Exception e) {
        log.error("全局异常处理捕获到Exception异常,异常信息==>{}", e.getMessage());
        return ResponseResult.error(e.getMessage());
    }

    /**
     * 处理运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseResult exceptionHandler(RuntimeException e) {
        e.printStackTrace();
        log.error("全局异常处理捕获到RuntimeException异常,异常信息==>{}", e.getMessage());
        return ResponseResult.error(e.getMessage());
    }

}