package com.lsc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lsc.eneity.Annex;
import com.lsc.utils.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author lsc
 * @version 1.0
 * @date 2022/6/15 15:13
 */
public interface AnnexService extends IService<Annex> {
     ResponseResult upload(MultipartFile file);

    ResponseResult uploadAnnex(MultipartFile file, String taskId, String userId);

    boolean submit(MultipartFile file, String taskId, String userId, String remarks);
}
