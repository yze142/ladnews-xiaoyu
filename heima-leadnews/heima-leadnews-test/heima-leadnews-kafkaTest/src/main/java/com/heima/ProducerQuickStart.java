package com.heima;
import org.apache.kafka.clients.producer.*;

import java.util.Properties;
import java.util.concurrent.ExecutionException;


/**
 * @program: heima-leadnews
 * @description:
 * @author: hello.xaioyu
 * @create: 2022-06-13 12:21
 **/

public class ProducerQuickStart {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //创建kafka链接表示
    Properties properties=new Properties();
    //kakfa链接地址
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"192.168.200.130:9092");

        //消息key的序列化器
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        //消息value的序列化器
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        //创建生产者对象
        KafkaProducer<String, String> producer=new KafkaProducer<String, String>(properties);

        //发送消息封装发送的消息
        //参数1topic
        //第二个参数消息的key
        ProducerRecord<String, String> record=new ProducerRecord("topic-first","key-001","hello world kafak");

        //3.发送消息 recordMetadata数据偏移量
//        RecordMetadata recordMetadata = producer.send(record).get();
//        System.out.println(recordMetadata.offset());
        //3.1异步消息发送
        producer.send(record, new Callback() {
            @Override
            public void onCompletion(RecordMetadata metadata, Exception exception) {
                if (exception!=null){
                    System.out.println("记录异常消息到日志表中");
                }
                System.out.println(metadata.offset());
            }
        });



        //关闭消息通道 不关闭消息通道的话就会发送失败
        producer.close();


    }

}
