package com.oupeng.joke.spider.producer;

import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import kafka.serializer.StringEncoder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Component
public class KafkaProducer {


    private static Logger logger = LoggerFactory.getLogger(KafkaProducer.class);

    //创建Kafka的生产者, key是消息的key的类型, value是消息的类型
    private  Producer<Integer, String> producer ;

    private String brokerList="172.18.100.86:9092";
    private String zookeeper="172.18.100.86:2181";
    private String acks="1";

    @Autowired
    private Environment env;

    @PostConstruct
    private  void initProducer() {

        Properties prop = new Properties();
        String b=env.getProperty("metadata.broker.list");
        if(StringUtils.isNotBlank(b)){
            brokerList=b;
        }
        String z=env.getProperty("zookeeper.connect");
        if(StringUtils.isNotBlank(b)){
            zookeeper=z;
        }
        String a=env.getProperty("request.required.acks");
        if(StringUtils.isNotBlank(b)){
            acks=a;
        }


        prop.put("metadata.broker.list",brokerList);
        prop.put("zookeeper.connect",zookeeper);
        prop.put("request.required.acks",acks);
        prop.put("serializer.class", StringEncoder.class.getName());

        //创建Kafka的生产者, key是消息的key的类型, value是消息的类型
       producer = new Producer<Integer, String>(
                new ProducerConfig(prop));
    }



    public void sendMessage(String message) {

        //消息主题是test
        KeyedMessage<Integer, String> keyedMessage = new KeyedMessage<Integer, String>("joke_text", message);
        //message可以带key, 根据key来将消息分配到指定区, 如果没有key则随机分配到某个区
        producer.send(keyedMessage);
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            logger.error("send message fail",e.getMessage());
        }
    }


}
