package com.qxy.service.impl;


import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.qxy.common.exception.AppException;
import com.qxy.common.response.ResponseCode;
import com.qxy.service.PictureService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class PictureServiceImpl implements PictureService {

    //    @Value("${aliyun.oss.endpoint}")
    private String endpoint = "https://oss-cn-beijing.aliyuncs.com";

    //    @Value("${aliyun.oss.accessKeyId}")
    private String accessKeyId = "LTAI5tEeQAGv1CjKXxasS2Lz";

    //    @Value("${aliyun.oss.accessKeySecret}")
    private String accessKeySecret = "1LAlRjejysxM9W9GCm3qM3wPPHbD4C";

    //    @Value("${aliyun.oss.bucketName}")
    private String bucketName = "douyinmall1";

    private OSS ossClient;

    public PictureServiceImpl() {
        // 在构造函数中初始化 OSSClient
        this.ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }

    public void setOssClient(OSS ossClient) {
        this.ossClient = ossClient;
    }

    @Override
    public String savePicture(MultipartFile multipartFile) {
        // 生成唯一的文件名
        String originalFilename = multipartFile.getOriginalFilename();
        assert originalFilename != null;
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename = UUID.randomUUID() + suffix;

        try {
            // 上传文件到OSS
            ossClient.putObject(new PutObjectRequest(bucketName, filename, multipartFile.getInputStream()));

            return "https://" + filename;
        } catch (IOException e) {
            throw new AppException(ResponseCode.UN_ERROR.getCode(), "文件上传失败");
        } finally {
            // 关闭OSSClient
            ossClient.shutdown();
        }
    }

    @Override
    public String upload(MultipartFile multipartFile) {
        return savePicture(multipartFile);
    }
}
