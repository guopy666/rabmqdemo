package testdemo.routing;

import com.rabbitmq.client.*;
import utils.RabbitMQUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Description: 路由模式——模拟错误日志
 * @author: guopy
 * @Date: 2020/3/28 21:31
 * @version: v1.0.0
 */
public class RoutErrorCustomerTest {
    private static final String QUEUE_NAME = "consumer_error";
    private static final String EXCHANGE_NAME = "test_routing_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        System.out.println("error消费者启动");
        /* 1.创建新的连接 */
        Connection connection = RabbitMQUtil.newConnection();
        /* 2.创建通道 */
        Channel channel = connection.createChannel();
        /* 3.消费者关联队列 */
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        /* 4.消费者绑定交换机 参数1 队列 参数2交换机 参数3 routingKey */
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "error");
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String msg = new String(body, "UTF-8");
                System.out.println("error消费者获取生产者消息:" + msg);
            }
        };
        /* 5.消费者监听队列消息 */
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }

}