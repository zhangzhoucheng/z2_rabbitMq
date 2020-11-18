package com.zz.test.rabbit.m2.publishSubscribe;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.zz.test.mqconnect.ConnectionUtil;

/**
 * 发布订阅模式：生产者生产的消息通过设定的模式（如下列子广播模式faout)发布到交换机后，所有的队列可以进行匹配订阅（由于faout，可设置RoutingKey为 ""，在消费者那可以捕捉RoutingKey= ""）;，进而消费
 * 官网灰常精辟的阐释，设计的模式，以及用来干什么的。
 * In the previous tutorial we created a work queue. The assumption behind a work queue is that each task 
 * is delivered to exactly one worker. In this part we'll do something completely different -- we'll deliver 
 * a message to multiple consumers. This pattern is known as "publish/subscribe".
 * 
 * To illustrate the pattern, we're going to build a simple logging system. It will consist of two programs -- 
 * the first will emit log messages and the second will receive and print them.

 * In our logging system every running copy of the receiver program will get the messages. That way we'll be able 
 * to run one receiver and direct the logs to disk; and at the same time we'll be able to run another receiver and 
 * see the logs on the screen.

* Essentially, published log messages are going to be broadcast to all the receivers
 */

/**
 * 交换机概念：生产者和队列之间的媒介，消息经过它之后以设定的模式发送到队列
 * Instead, the producer can only send messages to an exchange. An exchange is a very simple thing. On one side 
 * it receives messages from producers and the other side it pushes them to queues. The exchange must know 
 * exactly what to do with a message it receives. Should it be appended to a particular queue? Should it 
 * be appended to many queues? Or should it get discarded. The rules for that are defined by the exchange type.
 */


/**
 * 
 * <note>
 * Desc： Publish/Subscribe model
 * To:Sending messages to many consumers at once
 * Use:将消息发送到exchange，之后每个queue都可以进行订阅接受这个消息
 * @author jld.zhangzhou
 * @refactor for jld
 * @datetime 2019-08-01 16:21:37
 * @location mobile base 3th,BeiJing 
 * version  1.0
 *  
 * @REVISIONS: 
 * Version 	        Date 		         Author             Location                   Description          
 * ------------------------------------------------------------------------------------------------------  
 * 1.0 		  2019-08-01 16:21:37    jld.zhangzhou     mobile base 3th,BeiJing      1.create the class            
 * </note>
 */
public class EmitLog {
	private static final String EXCHANGE_NAME = "logs";

	  public static void main(String[] argv) throws Exception {
	    try (Connection connection = ConnectionUtil.getConnection();
	         Channel channel = connection.createChannel()) {
	    	
	    	//设置exchange，且类型是 fanout：发给所有。（direct, topic, headers and fanout：直接、主题、标题和扇出）
	        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");//如果不设置arg0,则默认 EXCHANGE_NAME=“”；

	        String message = argv.length < 1 ? "info: Hello World!" :
	                            String.join(" ", argv);

	        //发布交换，此时发布消息 的RoutingKey为 "";
	        channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
	        System.out.println(" [x] Sent '" + message + "'");
	    }
	  }
}
