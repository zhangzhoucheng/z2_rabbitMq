package com.zz.test.rabbit;

import java.io.IOException;



import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import com.zz.test.mqconnect.ConnectionUtil;

public class ReceiveMeg {
	private final static String MEG_TEST1 = "meg_test2";
	private final static String QUEUE_NAME = MEG_TEST1;
	
	/**
	 * Desc:新版的Rec 模型，解决内存溢出等问题Desc:
	 * @author jld.zhangzhou
	 * @datetime 2019-07-23 10:54:55
	 * @modify_record:
	 * @throws Exception
	 */

	@SuppressWarnings("unused")
	public static void main(String []args) throws Exception {
	        Connection connection = ConnectionUtil.getConnection();
	        Channel channel = connection.createChannel();

	        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
	        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

	        //lambda表达式实现只有一个方法的interface：DeliverCallback
	        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
	            String message = new String(delivery.getBody(), "UTF-8");
	            System.out.println(" [x] Received '" + message + "'");
	        };
	        
	        //上lambda表达式，可以用下列匿名方法实现
	        /*DeliverCallback deliverCallback1 = new DeliverCallback() {
				
				@Override
				public void handle(String consumerTag, Delivery message) throws IOException {
					String message1 = new String(message.getBody(), "UTF-8");
		            System.out.println(" [x] Received '" + message1 + "'");
					
				}
			};*/
	        
	        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
	    
	}
	
	
	
	/**
	 * Desc:老版消息获取方式。
	 * @author jld.zhangzhou
	 * @datetime 2019-07-23 10:53:38
	 * @modify_record:
	 * @throws Exception
	 */
	public void testReceiveMeg() throws Exception {
		
		/*// 获取到连接以及mq通道
        Connection connection = ConnectionUtil.getConnection();
        // 从连接中创建通道
        Channel channel = connection.createChannel();
        // 声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // 定义队列的消费者
        QueueingConsumer consumer = new QueueingConsumer(channel);

        // 监听队列
        channel.basicConsume(QUEUE_NAME, true, consumer);

        // 获取消息
        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody());
            System.out.println(" [x] Received '" + message + "'");
        }*/
	}


}
