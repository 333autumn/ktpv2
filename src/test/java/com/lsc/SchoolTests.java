package com.lsc;

import com.lsc.controller.SchoolController;
import com.lsc.service.impl.SchoolServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author lsc
 * @version 1.0
 * @date 2022/6/16 14:43
 */
@SpringBootTest
@Slf4j
public class SchoolTests {

    @Autowired
    private SchoolServiceImpl schoolService;

    @Autowired
    private SchoolController schoolController;

    /**
     * 测试新增学校
     */
    @Test
    public void testAddSchool(){
        String school="龙兴理工大学";
        System.out.println(schoolController.saveSchool(school));
    }

}
