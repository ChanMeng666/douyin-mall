package com.qxy.service;

import org.springframework.web.multipart.MultipartFile;

public interface PictureService {
    /**
     * 上传图片
     * @param multipartFile
     * @return
     */
    String savePicture(MultipartFile multipartFile);

    /**
     * 上传图片
     * @param multipartFile
     * @return
     */
    String upload(MultipartFile multipartFile);
}
