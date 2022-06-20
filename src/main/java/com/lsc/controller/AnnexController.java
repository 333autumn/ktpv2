package com.lsc.controller;

import com.lsc.service.impl.AnnexServiceImpl;
import com.lsc.utils.ResponseResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/annex")
public class AnnexController {

    private final AnnexServiceImpl annexService;

    /**
     * 用户上传附件
     */
    @PostMapping("/uploadAnnex")
    public ResponseResult uploadAnnex(@RequestPart("file") MultipartFile file,@RequestParam String taskId
            ,@RequestParam String userId){
        log.info("用户上传附件:taskId==>{},userId==>{}",taskId,userId);
        return annexService.uploadAnnex(file,taskId,userId);

    }

    /**
     * 上传文件测试
     */
    @PostMapping("/upload")
    public ResponseResult upload(MultipartFile file){
        return annexService.upload(file);
    }

}
