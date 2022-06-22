package com.lsc.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.lsc.eneity.Annex;
import com.lsc.mapper.AnnexMapper;
import com.lsc.service.AnnexService;
import com.lsc.utils.DateUtils;
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
import java.util.Objects;

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
     * 上传文件测试
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
     * 上传附件
     * @param file 文件
     * @param taskId 作业id
     * @param userId 上传人id
     */
    @Override
    public ResponseResult uploadAnnex(MultipartFile file, String taskId, String userId) {
        //获取原始文件名
        String originalFilename=file.getOriginalFilename();
        //使用文件名生成工具类生成不重复的文件名
        String fileName = PathUtils.generateFilePath(originalFilename);
        //文件上传到七牛云进行保存
        ResponseResult responseResult=uploadOss(file,taskId+"/"+fileName);
        if (responseResult.getCode()==500){//状态码为500说明上传失败
            return ResponseResult.error(responseResult.getMsg());
        }
        //如果文件在七牛云保存成功,将文件路径保存到数据库中
        //获取文件路径
        String path=responseResult.getData().toString();
        //封装Annex对象
        Annex annex=new Annex();
        annex.setTaskId(taskId);
        annex.setOwnerId(userId);
        annex.setPath(path);
        annex.setCreateTime(DateUtils.now());
        annex.setUpdateTime(DateUtils.now());
        if (save(annex)){
            return ResponseResult.ok("文件保存成功",path);
        }
        return ResponseResult.error("文件保存失败");
    }

    /**
     * 提交作业
     * @param file 文件
     * @param taskId 作业id
     * @param userId 用户id
     * @param remarks 提交详情(除了附件之外的文字内容)
     * @return
     */
    @Override
    public boolean submit(MultipartFile file, String taskId, String userId, String remarks) {
        //获取原始文件名
        String originalFilename = file.getOriginalFilename();
        //使用文件名生成工具类生成不重复的文件名
        String fileName = PathUtils.generateFilePath(originalFilename);
        //文件上传到七牛云进行保存
        ResponseResult responseResult = uploadOss(file, taskId + "/" + fileName);
        if (responseResult.getCode() == 500) {//状态码为500说明上传失败
            throw new RuntimeException("附件保存到七牛云失败");
        }
        //如果文件在七牛云保存成功,将文件路径保存到数据库中
        //获取文件路径
        String path = responseResult.getData().toString();
        //封装Annex对象
        Annex annex = new Annex();
        annex.setTaskId(taskId);
        annex.setOwnerId(userId);
        annex.setPath(path);
        annex.setRemarks(remarks);
        annex.setCreateTime(DateUtils.now());
        annex.setUpdateTime(DateUtils.now());
        //判断是否已经提交过作业
        LambdaQueryWrapper<Annex> annexQW=new LambdaQueryWrapper<>();
        annexQW.eq(Annex::getOwnerId,userId)
                .eq(Annex::getTaskId,taskId);

        Annex annex1=getOne(annexQW);
        if (Objects.isNull(annex1)){
            //新增作业
            log.info("新增作业");
            if (!save(annex)) {
                throw new RuntimeException("数据库保存附件失败");
            }
            return true;
        }else {
            //更新作业
            log.info("更新作业");
            LambdaUpdateWrapper<Annex> updateWrapper=new LambdaUpdateWrapper<>();
            updateWrapper.eq(Annex::getTaskId,taskId)
                    .eq(Annex::getOwnerId,userId);

            if (!update(annex,updateWrapper)){
                throw new RuntimeException("数据库更新附件失败");
            }
            return false;
        }
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
            log.info("key==>{}", putRet.key);
            log.info("hash==>{}", putRet.hash);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseResult.error("上传文件失败");
        }
        return ResponseResult.ok("上传文件成功", "http://rdqbc06ar.hd-bkt.clouddn.com/" + fileName);
    }
}
