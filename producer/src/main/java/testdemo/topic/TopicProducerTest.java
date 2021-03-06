package testdemo.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import utils.RabbitMQUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Description: 主题模式
 * @author: guopy
 * @Date: 2020/3/30 16:58
 * @version: v1.0.0
 */
public class TopicProducerTest {
    private static final String EXCHANGE_NAME = "my_topic_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        /** 1.创建新的连接 */
        Connection connection = RabbitMQUtil.newConnection();
        /** 2.创建通道 */
        Channel channel = connection.createChannel();
        /** 3.绑定的交换机 参数1交互机名称 参数2 exchange类型 */
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
        /** 4.发送消息 */
        String routingKey = "log.info.error";
        String msg = "topic_exchange_msg：" + routingKey;
        System.out.println("[send] = " + msg);
        channel.basicPublish(EXCHANGE_NAME, routingKey, null, msg.getBytes());
        /** 5.关闭通道、连接 */
        channel.close();
        connection.close();
        /** 注意：如果消费没有绑定交换机和队列，则消息会丢失 */
    }
}