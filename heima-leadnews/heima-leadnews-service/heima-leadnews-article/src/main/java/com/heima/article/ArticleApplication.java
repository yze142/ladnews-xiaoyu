package com.heima.article;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.heima.article.mapper")
@EnableAsync  //开启异步调用
public class ArticleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArticleApplication.class,args);
    }
}
