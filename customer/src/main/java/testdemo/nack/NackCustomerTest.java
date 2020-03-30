package testdemo.nack;

import com.rabbitmq.client.*;
import utils.RabbitMQUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Description: 消息退回
 * @author: guopy
 * @Date: 2020/3/29 10:16
 * @version: v1.0.0
 */
public class NackCustomerTest {

    private static final String exchangeName = "test_nack_exchange";
    private static final String routingKey = "ack.#";
    private static final String exchangeType = "topic";
    private static final String queueName = "nack_queue";

    public static void main(String[] args) throws IOException, TimeoutException {

        /* 1.创建新的连接 */
        Connection connection = RabbitMQUtil.newConnection();
        /* 2.创建通道 */
        final Channel channel = connection.createChannel();

        channel.exchangeDeclare(exchangeName,exchangeType,true,false,false,null);
        channel.queueDeclare(queueName,false,false,false,null);
        channel.queueBind(queueName,exchangeName,routingKey);

        System.out.println("consumer启动 .....");

        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                try{
                    Thread.sleep(2000);
                }catch (Exception e){

                }
                Integer num = (Integer)properties.getHeaders().get("num");
                if(num==0){
                    channel.basicNack(envelope.getDeliveryTag(),false,true);
                    String message = new String(body, "UTF-8");
                    System.out.println("consumer端的Nack消息是： " + message);
                }else {
                    channel.basicAck(envelope.getDeliveryTag(),false);
                    String message = new String(body, "UTF-8");
                    System.out.println("consumer端的ack消息是： " + message);
                }
            }
        };
        //消息要能重回队列，需要设置autoAck的属性为false，即在回调函数中进行手动签收
        channel.basicConsume(queueName,false,consumer);
    }
}