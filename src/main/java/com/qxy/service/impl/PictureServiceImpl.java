package com.qxy.service.impl;


import com.qxy.common.exception.AppException;
import com.qxy.common.response.ResponseCode;
import com.qxy.service.PictureService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class PictureServiceImpl implements PictureService {

    @Value("${pic.local-path}")
    private String localPath;

    @Override
    public String savePicture(MultipartFile multipartFile) {
        //创建目录
        File dir = new File(localPath);
        if(!dir.exists()) {
            dir.mkdirs();
        }

        //创建索引
        String filename=multipartFile.getOriginalFilename();
        assert null!=filename;
        String suffix = filename.substring(filename.lastIndexOf("."));
        filename = UUID.randomUUID() +suffix;

        //图片保存

        try {
            multipartFile.transferTo(new File(localPath + "/" + filename));
        } catch (IOException e) {
            throw new AppException(ResponseCode.UN_ERROR.getCode(),ResponseCode.UN_ERROR.getInfo());
        }
        return filename;
    }
}
