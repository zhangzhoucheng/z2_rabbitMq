package com.zz.test.rabbit.m6.publishconfirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import com.zz.test.mqconnect.ConnectionUtil;

public class ReceiveLogsTopic {
	private static final String EXCHANGE_NAME = "topic_logs";

	  public static void main(String[] argv) throws Exception {

	    Connection connection = ConnectionUtil.getConnection();
	    Channel channel = connection.createChannel();

	    //设置exchange type=主题
	    channel.exchangeDeclare(EXCHANGE_NAME, "topic");
	    String queueName = channel.queueDeclare().getQueue();

	    if (argv.length < 1) {
	        System.err.println("Usage: ReceiveLogsTopic [binding_key]...");
	        System.exit(1);
	    }

	    /**
	     * 测试时候，开启3个服务，分别传入参数 "*.orange.*";"*.*.rabbit","lazy.#";"#";。 
	     */
	    String arg = "";
	    for (String bindingKey : argv) {
	    	//当前服务绑定 启动参数 到routingKey（可以多个）
	        channel.queueBind(queueName, EXCHANGE_NAME, bindingKey);
	        arg+=bindingKey+",";
	    }

	    System.out.println("my queueName is :"+queueName+",routingKey:"+arg);
	    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

	    //回调
	    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
	        String message = new String(delivery.getBody(), "UTF-8");
	        System.out.println(" [x] Received '" +
	            delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
	    };
	    //消费
	    channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
	    
	  }
}
