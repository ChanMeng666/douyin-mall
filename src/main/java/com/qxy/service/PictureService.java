package com.qxy.service;

import org.springframework.web.multipart.MultipartFile;

public interface PictureService {
    String savePicture(MultipartFile multipartFile);
}
