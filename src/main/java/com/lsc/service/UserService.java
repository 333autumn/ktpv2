package com.lsc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lsc.eneity.User;

/**
 * @author lsc
 * @version 1.0
 * @date 2022/6/12 16:25
 */
public interface UserService extends IService<User> {
    User login(String username, String password);

    boolean isRepeat(String username);

    boolean update(User user);

    User getByUserName(String username);

    boolean add(User user);
}
