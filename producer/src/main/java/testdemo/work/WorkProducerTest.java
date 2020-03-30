package testdemo.work;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import utils.RabbitMQUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Description: 工作模式
 * @author: guopy
 * @Date: 2020/3/28 17:15
 * @version: v1.0.0
 */
public class WorkProducerTest {
    private static final String queueName = "test_work_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        /** 1.获取连接 */
        Connection newConnection = RabbitMQUtil.newConnection();
        /** 2.创建通道 */
        Channel channel = newConnection.createChannel();
        /**3.创建队列声明 */
        channel.queueDeclare(queueName, false, false, false, null);
        /**保证一次只分发一次 限制发送给同一个消费者 不得超过一条消息 */
        channel.basicQos(1);
        for (int i = 1; i <= 50; i++) {
            String msg = "生产者消息_" + i;
            System.out.println("生产者发送消息:" + msg);
            /**4.发送消息 */
            channel.basicPublish("", queueName, null, msg.getBytes());
        }
        channel.close();
        newConnection.close();
    }

}