package com.lsc;

import com.lsc.controller.CourseController;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author lsc
 * @version 1.0
 * @date 2022/6/16 16:41
 */
@SpringBootTest
@Slf4j
public class CourseTests {

    @Autowired
    private CourseController courseController;

    /**
     * 测试课程归档
     */
    @Test
    public void test1(){
        System.out.println(courseController.archiveCourse("333"));
    }
}
