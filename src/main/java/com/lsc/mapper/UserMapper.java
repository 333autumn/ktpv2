package com.lsc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lsc.eneity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author lsc
 * @version 1.0
 * @date 2022/6/12 16:24
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
