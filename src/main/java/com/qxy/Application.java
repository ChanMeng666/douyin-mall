package com.qxy;

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
    }

}
