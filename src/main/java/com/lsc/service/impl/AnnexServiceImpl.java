package com.lsc.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.lsc.eneity.Annex;
import com.lsc.mapper.AnnexMapper;
import com.lsc.service.AnnexService;
import com.lsc.utils.PathUtils;
import com.lsc.utils.ResponseResult;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author lsc
 * @version 1.0
 * @date 2022/6/15 15:13
 */
@Service
@Slf4j
public class AnnexServiceImpl extends ServiceImpl<AnnexMapper, Annex>  implements AnnexService {

    //七牛云ak
    private static final String ACCESS_KEY = "18DymQGyuR9H0ABb50ocntJ3TdSjqVX1zpVGQBqT";
    //七牛云sk
    private static final String SECRET_KEY = "qNIKHwKiy3M3ETTW2rT-FEQOh7spdiCKO7LufbxZ";
    //七牛云存储空间名称
    private static final String BUCKET = "ktp";

    /**
     * 附件上传
     */
    @Override
    public ResponseResult upload(MultipartFile file) {
        //获取原始文件名
        String originalFilename = file.getOriginalFilename();
        //如果判断通过上传文件到OSS
        String fileName = PathUtils.generateFilePath(originalFilename);
        log.info("文件保存路径为==>{}", fileName);
        return uploadOss(file, fileName);
    }

    /**
     * 上传文件到七牛云
     * @param file     要上传的文件
     * @param fileName 保存到七牛云的文件名
     */
    private ResponseResult uploadOss(MultipartFile file, String fileName) {
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        String token = auth.uploadToken(BUCKET);
        Configuration cfg = new Configuration(Region.huadong());
        UploadManager uploadManager = new UploadManager(cfg);
        try {
            InputStream inputStream = file.getInputStream();
            Response res = uploadManager.put(inputStream, fileName, token, null, null);
            //打印返回信息
            log.info("上传文件到七牛云返回信息==>{}", JSON.toJSONString(res));
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(res.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            log.info("key==>{}",putRet.key);
            log.info("hash==>{}",putRet.hash);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseResult.error("上传文件失败");
        }
        return ResponseResult.ok("上传文件成功", "http://rdqbc06ar.hd-bkt.clouddn.com/" +fileName);
    }

}
