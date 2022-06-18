package com.lsc.utils;

import java.util.Date;

/**
 * @author lsc
 * @version 1.0
 * @date 2022/6/16 20:49
 */
public class DateUtils {

    /**
     * 获取当前时间
     */
    public static Date now(){
        return new Date(System.currentTimeMillis());
    }
}
