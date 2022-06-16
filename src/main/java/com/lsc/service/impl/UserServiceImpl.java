package com.lsc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lsc.eneity.User;
import com.lsc.mapper.UserMapper;
import com.lsc.service.UserService;
import com.lsc.utils.DateUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;


/**
 * @author lsc
 * @version 1.0
 * @date 2022/6/12 16:25
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     */
    @Override
    public User login(String username, String password) {
        //根据用户名查询用户
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername,username);
        User user=getOne(queryWrapper);
        if (Objects.isNull(user)){
            return null;
        }
        if (Objects.equals(user.getPassword(), password)){
            return user;
        }
        return null;
    }

    /**
     * 判断用户名是否重复
     * @param username 用户名
     */
    @Override
    public boolean isRepeat(String username) {
        //根据用户名查询用户
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername,username);
        User user=getOne(queryWrapper);
        return user==null;
    }

    /**
     * 修改用户信息
     */
    @Override
    public boolean update(User user) {
        //判断用户是否存在
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserId,user.getUserId());
        if (getOne(queryWrapper)==null){
            return false;
        }
        return save(user);
    }

    /**
     * 根据用户名获取用户信息
     */
    @Override
    public User getByUserName(String username) {
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername,username);
        User user=getOne(queryWrapper);
        return user;
    }

    /**
     * 新增用户
     */
    @Override
    public boolean add(User user) {
        //根据用户名唯一的特性判断用户是否存在
        if (isRepeat(user.getUsername())){
            return false;
        }
        user.setCreateTime(DateUtils.now());
        user.setUpdateTime(DateUtils.now());
        return save(user);
    }
}
