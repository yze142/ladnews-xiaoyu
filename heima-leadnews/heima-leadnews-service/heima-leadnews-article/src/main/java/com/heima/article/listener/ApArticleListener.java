package com.heima.article.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.heima.article.servce.ApArticleConfigService;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @program: heima-leadnews
 * @description: 监听文章
 * @author: hello.xaioyu
 * @create: 2022-06-14 13:45
 **/
@Component
@Slf4j
public class ApArticleListener {

    @Autowired
    private ApArticleConfigService apArticleConfigService;


    @KafkaListener(topics = "wm.news.up.or.down.topic")
    public void onMessage(String message){
        Map map = JSON.parseObject(message, Map.class);
        apArticleConfigService.updateByMap(map);
        log.info("article端文章配置修改，articleId={}",map.get("articleId"));


        apArticleConfigService.updateByMap(map);

    }


}
