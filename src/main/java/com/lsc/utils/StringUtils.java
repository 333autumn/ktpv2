package com.lsc.utils;

/**
 * @author lsc
 * @version 1.0
 * @date 2022/6/16 21:02
 */

/**
 * 字符串工具类
 */
public class StringUtils {

    /**
     * 生成8位加课码
     */
    public static String getAddCourseCode(){
        StringBuilder addCourseCode=new StringBuilder();
        for (int i=0;i<8;i++){
            double flag=Math.random();
            //有一半概率为数字,一半概率为字母
            if (flag<=0.5) {
                int temp = (int) (Math.random() * 26);
                char a = (char) ('A' + temp);
                addCourseCode.append(a);
            }else {
                int temp = (int) (Math.random() * 10);
                addCourseCode.append(temp);
            }
        }
        return addCourseCode.toString();
    }
}
