package com.qxy.test.service;



import com.aliyun.oss.OSS;
import com.aliyun.oss.model.PutObjectRequest;
import com.qxy.common.exception.AppException;
import com.qxy.service.PictureService;
import com.qxy.service.impl.PictureServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class PictureServiceImplTest {

    @Mock
    private OSS ossClient;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private PictureServiceImpl pictureService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSavePicture_Success() throws IOException {
        // 模拟文件上传
        when(multipartFile.getOriginalFilename()).thenReturn("test.jpg");
        when(multipartFile.getInputStream()).thenReturn(mock(InputStream.class));

        // 调用方法
        String result = pictureService.savePicture(multipartFile);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.startsWith("https://"));

        // 验证OSS上传方法被调用
        verify(ossClient, times(1)).putObject(any(PutObjectRequest.class));
    }

    @Test
    void testSavePicture_IOException() throws IOException {
        // 模拟文件上传失败
        when(multipartFile.getOriginalFilename()).thenReturn("test.jpg");
        when(multipartFile.getInputStream()).thenThrow(new IOException("File read error"));

        // 验证异常
        AppException exception = assertThrows(AppException.class, () -> {
            pictureService.savePicture(multipartFile);
        });

        assertEquals("文件上传失败", exception.getMessage());
    }
}
