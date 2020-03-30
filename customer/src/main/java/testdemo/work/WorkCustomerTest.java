package testdemo.work;

import com.rabbitmq.client.*;
import utils.RabbitMQUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Description:
 * @author: guopy
 * @Date: 2020/3/28 17:18
 * @version: v1.0.0
 */
public class WorkCustomerTest {
    private static final String queueName = "test_work_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        System.out.println("001");
        /** 1.获取连接 */
        Connection newConnection = RabbitMQUtil.newConnection();
        /** 2.获取通道 */
        final Channel channel = newConnection.createChannel();
        channel.queueDeclare(queueName, false, false, false, null);
        /** 保证一次只分发一次 限制发送给同一个消费者 不得超过一条消息 */
        channel.basicQos(1);
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String msgString = new String(body, "UTF-8");
                System.out.println("消费者获取消息:" + msgString);
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {

                } finally {
                    /** 手动回执消息 */
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };
        /** 3.监听队列 */
        channel.basicConsume(queueName, false, defaultConsumer);
    }
}