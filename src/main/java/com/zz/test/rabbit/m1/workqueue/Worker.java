package com.zz.test.rabbit.m1.workqueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import com.zz.test.mqconnect.ConnectionUtil;

public class Worker {

	 private static final String TASK_QUEUE_NAME = "task_queue";

	  public static void main(String[] argv) throws Exception {
		//获取连接和管道
	    final Connection connection = ConnectionUtil.getConnection();
	    final Channel channel = connection.createChannel();
	    
	    //耐用的，true，开启持久，会持久消息到disk，当然也会在缓存中，其表现并不完美
	    boolean durable = true;
	    
	    //队列声明
	    channel.queueDeclare(TASK_QUEUE_NAME, durable, false, false, null);
	    
	    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

	    //指定消费能力为1，当前消费者只能被发送一个消息去处理，当处理一个消息时候，不会接受到其它消息。如果消费能力2，则该work可以多处理一个，等到第一个任务 ack后，接着处理
	    channel.basicQos(1);
	    
	    //Callback interface to be notified when a message is delivered.
	    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
	        String message = new String(delivery.getBody(), "UTF-8");

	        System.out.println(" [x] Received '" + message + "'");
	        try {
	            doWork(message);
	        } finally {
	            System.out.println(" [x] Done");
	            
	            boolean ack=false;
	            //当ack=false,确认当前delivery，true，确认当前标签以及之前的all message
	            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), ack);
	        }
	    };
	    
	    //是否开启自动确认（建议不开启，解决消息传递后没有被消费真正处理的问题）
	    boolean autoAck = false;
	    channel.basicConsume(TASK_QUEUE_NAME, autoAck, deliverCallback, consumerTag -> { });
	  }

	  private static void doWork(String task) {
	    for (char ch : task.toCharArray()) {
	        if (ch == '.') {
	            try {
	                Thread.sleep(10000);
	            } catch (InterruptedException _ignored) {
	                Thread.currentThread().interrupt();
	            }
	        }
	    }
	  }
}
