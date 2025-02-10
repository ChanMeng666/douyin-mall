package com.qxy;

import cn.dev33.satoken.SaManager;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@Configurable
@EnableScheduling
@MapperScan("com.qxy.dao")
public class Application {

    public static void main(String[] args){
        SpringApplication.run(Application.class);
        System.out.println("启动成功：sa-token配置如下：" + SaManager.getConfig());
    }

}
