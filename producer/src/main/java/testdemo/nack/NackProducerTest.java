package testdemo.nack;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import utils.RabbitMQUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 消息退回
 * @author: guopy
 * @Date: 2020/3/29 10:16
 * @version: v1.0.0
 */
public class NackProducerTest {
    private static final String exchangeName = "test_nack_exchange";
    private static final String routingKey = "ack.save";

    public static void main(String[] args) throws Exception{

        /** 1.创建新的连接 */
        Connection connection = RabbitMQUtil.newConnection();
        /** 2.创建通道 */
        Channel channel = connection.createChannel();

        //通过在properties设置来标识消息的相关属性
        for(int i=0;i<5;i++){
            Map<String, Object> headers = new HashMap<String, Object>();
            headers.put("num",i);
            AMQP.BasicProperties properties = new AMQP.BasicProperties().builder()
                    .deliveryMode(2)                    // 传送方式 2:持久化投递
                    .contentEncoding("UTF-8")           // 编码方式
                    //.expiration("10000")              // 过期时间
                    .headers(headers)                  //自定义属性
                    .build();
            String message = "hello this is ack message ....."  + i;
            System.out.println(message);
            channel.basicPublish(exchangeName,routingKey,true,properties,message.getBytes());
        }
    }
}