package com.heima;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;

/**
 * @program: heima-leadnews
 * @description: 消费者
 * @author: hello.xaioyu
 * @create: 2022-06-13 19:33
 **/

public class ConsumerQuickStart {

    public static void main(String[] args) {

        //创建kafka链接表示
        Properties properties=new Properties();
        //kakfa链接地址
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"192.168.200.130:9092");

        //消费者组
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "group2");

        //消息key的序列化器
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        //创建消费者
        KafkaConsumer<String, String> kafkaConsumer=new KafkaConsumer<String, String>(properties);
         //订阅主题
        kafkaConsumer.subscribe(Collections.singletonList("topic-first"));
        //拉取消息
        while (true) {
            ConsumerRecords<String, String> poll = kafkaConsumer.poll(Duration.ofMillis(10000));
            for (ConsumerRecord<String, String> stringStringConsumerRecord : poll) {
                System.out.println(stringStringConsumerRecord.key());
                System.out.println(stringStringConsumerRecord.value());
                System.out.println(stringStringConsumerRecord.offset());
                System.out.println(stringStringConsumerRecord.partition());

                //同步偏移量
                kafkaConsumer.commitSync();

            }


        }


    }

}
