package testdemo.easy;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import utils.RabbitMQUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Description: 简单模式
 * @author: guopy
 * @Date: 2020/3/28 16:59
 * @version: v1.0.0
 */
public class EasyProducerTest {
    /** 队列名称 */
    private static final String QUEUE_NAME = "rabdemo_queue1";

    public static void main(String[] args) throws IOException, TimeoutException {
        /** 1.获取连接 */
        Connection newConnection = RabbitMQUtil.newConnection();
        /** 2.创建通道 */
        Channel channel = newConnection.createChannel();
        /** 3.创建队列声明 */
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        String msg = "我是生产者生成的消息";
        System.out.println("生产者发送消息:" + msg);
        /** 4.发送消息 */
        channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
        channel.close();
        newConnection.close();
    }
}