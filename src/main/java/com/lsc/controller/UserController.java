package com.lsc.controller;

import com.alibaba.fastjson.JSON;
import com.lsc.eneity.User;
import com.lsc.service.impl.UserServiceImpl;
import com.lsc.utils.ResponseResult;
import com.lsc.utils.TokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author lsc
 * @version 1.0
 * @date 2022/6/12 17:09
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/user")
public class UserController {

    private final UserServiceImpl userService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public ResponseResult login(@RequestParam String username,@RequestParam String password) {
        log.info("username==>{},password==>{}",username,password);
        //判断用户密码是否正确
        User user=userService.login(username,password);
        if (user!=null){
            //生成token,返回给前端
            String token= TokenUtils.generateToken(user);
            //获取登陆用户的信息
            User loginUser=userService.getByUserName(username);
            Map<String, Object> data=new HashMap<>();
            data.put("token",token);
            data.put("userInfo",loginUser);
            return ResponseResult.ok("登录成功",data);
        }
        log.error("用户登录,密码错误");
        return ResponseResult.error("登录失败");
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public ResponseResult register(@RequestBody User user){
        log.info("用户注册:user==>{}", JSON.toJSONString(user));
        if (Objects.isNull(user)){
            log.error("用户注册,传入参数为空");
            return ResponseResult.error("传入参数为空");
        }
        if (userService.add(user)){
            return ResponseResult.ok("用户注册成功");
        }
        return ResponseResult.error("用户注册失败");
    }

    /**
     * 判断用户名是否重复
     */
    @GetMapping("/isRepeat")
    public boolean isRepeat(@RequestParam String username){
        if (Objects.isNull(username)){
            log.error("判断用户名是否重复传入参数为空");
            return true;
        }
        log.info("判断用户名是否重复,username==>{}",username);
        return userService.isRepeat(username);
    }

    /**
     * 修改用户信息
     * 需要token
     */
    @PostMapping("/update")
    public ResponseResult update(@RequestBody User user){
        if (userService.update(user)){
            return ResponseResult.ok("修改用户信息成功");
        }
        return ResponseResult.error("修改用户信息失败");
    }

}
