package com.zz.test.rabbit.m3.routing;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import com.zz.test.mqconnect.ConnectionUtil;

public class ReceiveLogsDirect {
	private static final String EXCHANGE_NAME = "direct_logs";

	  public static void main(String[] argv) throws Exception {
	    Connection connection = ConnectionUtil.getConnection();
	    Channel channel = connection.createChannel();

	    //声明交换，且对应生产者配置type=”direct“
	    channel.exchangeDeclare(EXCHANGE_NAME, "direct");
	    
	    //随机生成一个队列，当然也可以制定名字
	    String queueName = channel.queueDeclare().getQueue();

	    if (argv.length < 1) {
	        System.err.println("Usage: ReceiveLogsDirect [info] [warning] [error]");
	        System.exit(1);
	    }

	    /**
	     * 根据启动参数，将该queueName配置对应的 routingKey，可以是1对n，或者1对1
	     */
	    String arg = "";
	    for (String severity : argv) {
	        channel.queueBind(queueName, EXCHANGE_NAME, severity);
	        arg+=severity+",";
	    }
	    System.out.println("my queueName is :"+queueName+",routingKey:"+arg);
	    
	    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

	    //消息接受 回调函数
	    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
	        String message = new String(delivery.getBody(), "UTF-8");
	        System.out.println(" [x] Received '" +
	            delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
	    };
	    
	    //进行消费（autoAck=true，自动确认，）
	    channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
	  }
}
