package com.lsc;

import com.alibaba.fastjson.JSON;
import com.lsc.controller.UserController;
import com.lsc.eneity.User;
import com.lsc.mapper.UserMapper;
import com.lsc.service.impl.UserServiceImpl;
import com.lsc.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;

@SpringBootTest
@Slf4j
class BasicTests {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserController userController;

    @Autowired
    private UserServiceImpl userService;

    /**
     * 测试mybatis-plus dao
     */
    @Test
    public void testMybatisPlusDao() {
        List<User> users = userMapper.selectList(null);
        log.info("users:{}", JSON.toJSONString(users));
        log.error("333");
    }

    /**
     * 测试添加用户
     */
    @Test
    public void testAddUser(){
        User user=new User();
        user.setUsername("1982260078");
        user.setPassword("li5688231");
        boolean save = userService.save(user);
        log.info("是否添加成功:{}",save);
    }

    /**
     * 测试生成token
     */
    @Test
    public void testGenerateToken(){
        User user=new User();
        user.setUserId("333");
        String token=TokenUtils.generateToken(user);
        log.info("生成的token为:{}",token);
        log.info("userId:{}",TokenUtils.getUserId(token));
    }

    /**
     * 测试token解析
     */
    @Test
    public void testVerifyToken(){
        String token= "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJsc2MiLCJpZCI6IjEiLCJleHAiOjE2NTUxNjQ3MDh9.U6M286k_4OgnU8cjJJ-qQD0DylxxS2EQAzEmomD5q_k";
//        log.info("userId:{}",TokenUtils.getUserId(token));
//        log.info("token验证结果:{}",TokenUtils.verifyToken(token));
    }

//    /**
//     * 测试全局异常处理
//     */
//    @Test
//    public void testExceptionHandler(){
//        System.out.println(userController.test());
//    }



}
