package com.lsc.utils;

import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * bean拷贝工具类
 */
public class BeanCopyUtils {

    private BeanCopyUtils(){

    }

    public static <V>V copyBean(Object source,Class<V> clazz){
        V result=null;

        try {
            result=clazz.newInstance();
            BeanUtils.copyProperties(source,result);
        }  catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static <O,V>List<V> copyBeanList(List<O> source,Class<V> clazz) {
        return source.stream()
                .map(o -> copyBean(o, clazz))
                .collect(Collectors.toList());
    }

}