package com.lsc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lsc.eneity.Grade;

/**
 * @author lsc
 * @version 1.0
 * @date 2022/6/15 15:11
 */
public interface GradeService extends IService<Grade> {
    boolean correct(Grade grade);
}
