package com.zz.test.rabbit.m1.workqueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;
import com.zz.test.mqconnect.ConnectionUtil;


/**
 * <a>https://www.rabbitmq.com/tutorials/tutorial-two-java.html</a>
 * 官网有一段很清晰的定义：
 * The main idea behind Work Queues (aka: Task Queues) is to avoid doing a resource-intensive task immediately 
 * and having to wait for it to complete. Instead we schedule the task to be done later. 
 * We encapsulate a task as a message and send it to a queue. A worker process running in the background will 
 * pop the tasks and eventually execute the job. When you run many workers the tasks will be shared between them.
 */
/**
 * 
 * <note>
 * Desc： Work queues model
 * TO:Distributing tasks among workers
 * Use:消息发布到队列queue，queue发给多个worker中的一个，可以设置发给空闲的一个。
 * @author jld.zhangzhou
 * @refactor for jld
 * @datetime 2019-08-01 16:17:12
 * @location mobile base 3th,BeiJing 
 * version  1.0
 *  
 * @REVISIONS: 
 * Version 	        Date 		         Author             Location                   Description          
 * ------------------------------------------------------------------------------------------------------  
 * 1.0 		  2019-08-01 16:17:12    jld.zhangzhou     mobile base 3th,BeiJing      1.create the class            
 * </note>
 */
public class NewTask {
	private static final String TASK_QUEUE_NAME = "task_queue1";

	  public static void main(String[] argv) throws Exception {	
		  
	    //指定资源的try，退出时，会自动关闭资源，是不是很优雅啊
	    try (Connection connection = ConnectionUtil.getConnection();
	         Channel channel = connection.createChannel()) {
	        channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
	        String message = String.join(" ", argv);

	
	        //props开启持久化，但也许并不真的持久化，和boolean durable = true 呼应
	        channel.basicPublish("", TASK_QUEUE_NAME,
	                MessageProperties.PERSISTENT_TEXT_PLAIN,
	                message.getBytes("UTF-8"));
	        System.out.println(" [x] Sent '" + message + "'");
	    }
	    
	   
	}
  }
	  
