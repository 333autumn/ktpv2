package com.lsc.utils;

import java.util.UUID;

/**
 * 生成文件路径工具类
 */
public class PathUtils {

    public static String generateFilePath(String fileName){
        //uuid作为文件名
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        //后缀和文件后缀一致
        int index = fileName.lastIndexOf(".");
        // test.jpg -> .jpg
        String fileType = fileName.substring(index);
        return uuid + fileType;
    }
}