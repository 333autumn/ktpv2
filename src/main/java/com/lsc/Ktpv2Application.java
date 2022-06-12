package com.lsc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//扫描mapper文件夹
@MapperScan("com.lsc.mapper")
public class Ktpv2Application {

    public static void main(String[] args) {
        SpringApplication.run(Ktpv2Application.class, args);
    }

}
