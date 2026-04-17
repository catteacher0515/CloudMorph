package com.pingyu.cloudmorph;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy = true)
@MapperScan("com.pingyu.cloudmorph.mapper")
public class CloudMorphApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudMorphApplication.class, args);
    }
}
