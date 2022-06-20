package com.heima.article.stream;

import com.alibaba.fastjson.JSON;
import com.heima.model.mess.UpdateArticleMess;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Time;
import java.time.Duration;

/**
 * @program: heima-leadnews
 * @description: stream聚合处理
 * @author: hello.xaioyu
 * @create: 2022-06-20 13:16
 **/
@Configuration
@Slf4j
public class HotArticleStreamHandler {
//
//    @Bean
//    public KStream<String, String> Kstream(StreamsBuilder streamsBuilder) {
//
//        //接收消息
//        KStream<String, String> stream = streamsBuilder.stream("HOT_ARTICLW_SCORAE_TOPIC");
//        //聚合流式处理
//        stream.map(((key, value) -> {
//                    //json转换成对象
//                    UpdateArticleMess updateArticleMess = JSON.parseObject(value, UpdateArticleMess.class);
//                    //重置消息的key(文章id)，value(文章的加粉类型：和值)
//                    return new KeyValue<>(updateArticleMess.getArticleId(),
//                            updateArticleMess.getType() + "" + updateArticleMess.getAdd());
//                }))
//        //按照文章id进行聚合
//        .groupBy((key, value) -> key)
//            //时间窗口
//            .windowedBy(TimeWindows.of(Duration.ofSeconds(10)))
//                /**
//                 * 自行完成聚合操作
//                 */
//            .aggregate(new Initializer<Object>() {
//                /**
//                 * 初始方法 聚合之后的value
//                 * @return 消息的value
//                 */
//                @Override
//                public Object apply() {
//                    return null;
//                }
//            }, new Aggregator<Long, String, Object>() {
//
//                /**
//                 * 真正的聚合操作，返回值是消息的value
//                 * @param key
//                 * @param value
//                 * @param aggValue
//                 * @return
//                 */
//                @Override
//                //消息的key  第三个参数是聚合后的值
//                public Object apply(Long key, String value, Object aggValue) {
//                    return null;
//                }
//            }, Materialized.as("hot-atricle-steam-count-001"))
//                .toStream()
//                //发送消息
//              .to("HOT_ARTICLE_INCR_HANDLE_TOPIC");
//
//
//
//        return stream;
//
//
//    }
}
