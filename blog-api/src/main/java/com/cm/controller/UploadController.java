package com.cm.controller;

import com.cm.utils.QiniuUtils;
import com.cm.vo.Result;
import com.qiniu.common.QiniuException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/upload")
public class UploadController {

    @Autowired
    private QiniuUtils qiniuUtils;

//          上传图片到七牛云
    @PostMapping
    public Result upload(@RequestParam("image") MultipartFile file){
//        获取原始文件名称
        String originalFilename = file.getOriginalFilename();
//        为了保证唯一性，设置随机名称
        String fileName = UUID.randomUUID().toString()+"."+ StringUtils.substringAfter(originalFilename,".");
//            调用方法上传图片
        boolean upload = qiniuUtils.upload(file,fileName);
        if(upload){
//            图片上传成功
//            记住一定要返回全路径！!!!!
            return Result.Success(QiniuUtils.url + fileName);
        }
            return Result.Fail(20001,"上传失败");
    }
}
