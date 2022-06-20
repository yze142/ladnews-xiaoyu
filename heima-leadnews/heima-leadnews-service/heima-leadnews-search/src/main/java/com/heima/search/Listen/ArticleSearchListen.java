package com.heima.search.Listen;

import com.alibaba.fastjson.JSON;
import com.heima.model.schedule.vos.SearchArticleVo;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @program: heima-leadnews
 * @description: 监听文章新增消息
 * @author: hello.xaioyu
 * @create: 2022-06-15 10:13
 **/
@Component
@Slf4j
public class ArticleSearchListen {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @KafkaListener(topics ="article.es.sync.topic")
    public void  SearchListen(String  message){
     //接收消息
        SearchArticleVo searchArticleVo = JSON.parseObject(message, SearchArticleVo.class);
        String json = JSON.toJSONString(searchArticleVo);
        //消费消息
        //调用el添加进数据库
        IndexRequest request=new IndexRequest("app_info_article").id(searchArticleVo.getId().toString());
        request.source(json, XContentType.JSON);
        //发送请求
        try {
            restHighLevelClient.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {

            log.error("数据添加到数据失败捏");
        }


    }



}
