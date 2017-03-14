package test;

import java.util.Properties;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import kafka.serializer.StringEncoder;

/**
 * Created by Administrator on 2017/3/10.
 */
public class ProducerTest {
    public static void main(String[] args) {

        Properties props = new Properties();
        //根据这个配置获取metadata,不必是kafka集群上的所有broker
        props.put("metadata.broker.list", "172.18.100.86:9092");
        //消息传递到broker时的序列化方式
        props.put("serializer.class", StringEncoder.class.getName());
        //zk集群
        props.put("zookeeper.connect", "172.18.100.86:2181");
        //是否获取反馈
        //0是不获取反馈(消息有可能传输失败)
        //1是获取消息传递给leader后反馈(其他副本有可能接受消息失败)
        //-1是所有in-sync replicas接受到消息时的反馈
        props.put("request.required.acks", "0");
        //props.put("partitioner.class", MyPartition.class.getName());

        //创建Kafka的生产者, key是消息的key的类型, value是消息的类型
        Producer<Integer, String> producer = new Producer<Integer, String>(
                new ProducerConfig(props));

        int count = 0;
        while(true) {
            String message = "message-" + ++count;
            //消息主题是test
            KeyedMessage<Integer, String> keyedMessage = new KeyedMessage<Integer, String>("joke_text", message);
            //message可以带key, 根据key来将消息分配到指定区, 如果没有key则随机分配到某个区
//          KeyedMessage<Integer, String> keyedMessage = new KeyedMessage<Integer, String>("test", 1, message);
            producer.send(keyedMessage);
            System.out.println("send: " + message);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
