package com.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.user.dao")
public class IkunUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(IkunUserApplication.class, args);
    }

}
