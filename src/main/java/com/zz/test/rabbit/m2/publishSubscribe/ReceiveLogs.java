package com.zz.test.rabbit.m2.publishSubscribe;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import com.zz.test.mqconnect.ConnectionUtil;

public class ReceiveLogs {
	private static final String EXCHANGE_NAME = "logs";

	  public static void main(String[] argv) throws Exception {

	    Connection connection = ConnectionUtil.getConnection();
	    Channel channel = connection.createChannel();

	    //声明交换，获取logs 这个交换，类型是 fanout(fanout会忽略routeKey的值，即“”）
	    channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
	    
	    //获取管道的队列名（可以是定义好的）
	    String queueName = channel.queueDeclare().getQueue();
	    
	    //将上队列绑定exchange
	    channel.queueBind(queueName, EXCHANGE_NAME, "");

	    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

	    //传递的回调函数
	    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
	        String message = new String(delivery.getBody(), "UTF-8");
	        System.out.println(" [x] Received '" + message + "'");
	    };
	    
	    //进行消费
	    channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
	  }
}
