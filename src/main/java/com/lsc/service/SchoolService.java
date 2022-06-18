package com.lsc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lsc.eneity.School;

/**
 * @author lsc
 * @version 1.0
 * @date 2022/6/13 15:35
 */
public interface SchoolService extends IService<School> {
    void add(String schoolName);

    boolean isRepeat(String schoolName);
}
