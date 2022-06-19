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

    @ExceptionHandler(Exception.class)
    public ResponseResult exceptionHandler(Exception e) {
        log.error("全局异常处理捕获到Exception异常,异常信息==>{}", e.getMessage());
        return ResponseResult.error(e.getMessage());
    }


    @ExceptionHandler(RuntimeException.class)
    public ResponseResult exceptionHandler(RuntimeException e) {
        log.error("全局异常处理捕获到RuntimeException异常,异常信息==>{}", e.getMessage());
        return ResponseResult.error(e.getMessage());
    }

}