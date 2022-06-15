package com.lsc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lsc.eneity.Task;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author lsc
 * @version 1.0
 * @date 2022/6/15 15:09
 */
@Mapper
public interface TaskMapper extends BaseMapper<Task> {
}
