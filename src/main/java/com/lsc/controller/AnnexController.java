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

//    /**
//     * 上传附件
//     */
//    @PostMapping("/upload")
//    public ResponseResult uploadFile(MultipartFile file, @RequestParam String taskId, @RequestParam String userId) {
//        //获取原始文件名称
//        String originName = file.getOriginalFilename();
//
//        if (!originName.contains(".")) {
//            log.error("上传附件为空");
//            return ResponseResult.error("上传附件不能为空");
//        }
//
//        //获取文件名后缀
//        String last = originName.split("\\.")[1];
//
//        //拼接真实路径
//        String realPath = "D:\\ktpFiles";
//        log.info("文件真实路径为==>{}", realPath);
//
//        File folder = new File(realPath);
//        if (!folder.exists()) {
//            folder.mkdirs();
//        }
//        //拼接新文件名
//        String newName = UUID.randomUUID() + "." + last;
//        log.info("新文件名为==>{}", newName);
//
//        log.info("文件路径为==>{}", realPath + "\\" + newName);
//
//        //保存文件
//        try {
//            file.transferTo(new File(folder, newName));
//            return ResponseResult.ok("文件上传成功");
//        } catch (IOException e) {
//            e.printStackTrace();
//            return ResponseResult.error("文件上传失败");
//        }
//    }

    @PostMapping("/upload")
    public ResponseResult uploadImg(MultipartFile file){
        return annexService.upload(file);
    }

}
