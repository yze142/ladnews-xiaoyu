package com.heima.wemedia.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @program: heima-leadnews
 * @description: 熔断配置
 * @author: hello.xaioyu
 * @create: 2022-06-09 13:01
 **/
@Configuration
@ComponentScan("com.heima.apis.article.fallback") //告诉熔断类在哪里
public class InitConfig {
}
