package com.oupeng.joke.spider.producer;

import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import kafka.serializer.StringEncoder;
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

    @Autowired
    private Environment env;

    @PostConstruct
    private  void initProducer() {

        Properties prop = new Properties();

        prop.put("metadata.broker.list",env.getProperty("metadata.broker.list"));
        prop.put("zookeeper.connect",env.getProperty("zookeeper.connect"));
        prop.put("request.required.acks",env.getProperty("request.required.acks"));
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
