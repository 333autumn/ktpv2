package com.lsc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lsc.eneity.User;
import com.lsc.mapper.UserMapper;
import com.lsc.service.UserService;
import com.lsc.utils.DateUtils;
import com.lsc.utils.ResponseResult;
import com.lsc.utils.TokenUtils;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
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
            throw new RuntimeException("用户不存在,登录失败");
        }
        if (!Objects.equals(user.getPassword(), password)){
            throw new RuntimeException("密码错误,登录失败");
        }
        return user;
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
        return user!=null;
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
        return updateById(user);
    }

    /**
     * 根据用户名获取用户信息
     */
    @Override
    public User getByUserName(String username) {
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername,username);
        return getOne(queryWrapper);
    }

    /**
     * 新增用户
     */
    @Override
    public boolean add(User user) {
        //根据用户名唯一的特性判断用户是否存在
        if (isRepeat(user.getUsername())){
            log.error("用户名:"+user.getUsername()+"已存在,新增用户失败");
            return false;
        }
        user.setCreateTime(DateUtils.now());
        user.setUpdateTime(DateUtils.now());
        return save(user);
    }

    /**
     * 根据token返回用户的身份
     * @param token 请求头
     * @return 用户身份
     */
    @Override
    public String getStatus(String token) {
        //根据token获取用户id
        String userId= TokenUtils.getUserId(token);
        //根据用户id查询用户信息
        User user = getById(userId);
        if (Objects.isNull(user)){
            //返回一个不为1的字符串,这样就不会和老师的身份匹配,操作无法继续
            return "3";
        }
        return user.getStatus();
    }

    /**
     * 测试全局异常处理
     */
    @Override
    public void test() {
        throw new RuntimeException("RuntimeException");
    }


    /**
     * 根据用户id获取用户信息
     * @param userId 用户id
     */
    @Override
    public ResponseResult getInfoById(String userId) {
        User user=getById(userId);
        if (Objects.isNull(user)){
            return ResponseResult.error("用户不存在");
        }
        return ResponseResult.ok(user);
    }

}
