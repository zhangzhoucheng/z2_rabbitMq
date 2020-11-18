package com.zz.test.rabbit.m3.routing;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.zz.test.mqconnect.ConnectionUtil;

/**
 * 
 * <note>
 * Desc： Routing model
 * Use:基于订阅模型。通过设置exchangeDeclare->direct,这时候通过启动参数，可以设置不同routingKey，这样运行多个进程后，
 *     该exchange就可以发布routingKey不同的订阅消息。这时候消费者 的channel.queueBind（）可以给queue配置一个或多个routingKey，用来接收对应
 *     routingKey的消息，进行订阅。
 * @author jld.zhangzhou
 * @refactor for jld
 * @datetime 2019-08-01 18:09:42
 * @location mobile base 3th,BeiJing 
 * version  1.0
 *  
 * @REVISIONS: 
 * Version 	        Date 		         Author             Location                   Description          
 * ------------------------------------------------------------------------------------------------------  
 * 1.0 		  2019-08-01 18:09:42    jld.zhangzhou     mobile base 3th,BeiJing      1.create the class            
 * </note>
 */
public class EmitLogDirect {
	private static final String EXCHANGE_NAME = "direct_logs";

	  public static void main(String[] argv) throws Exception {

	    try (Connection connection = ConnectionUtil.getConnection();
	         Channel channel = connection.createChannel()) {
	    	
	    	//声明交换，同时type=direct，用于routingKey动态接受订阅。
	        channel.exchangeDeclare(EXCHANGE_NAME, "direct");

	        //运行参数，这样可以根据参数不同，启动多个应用，设置不同的routingKey
	       /* String severity = getSeverity(argv);
	        String message = getMessage(argv);*/
	        
	        String severity = (String) ConnectionUtil.getRandomBytype("INFOLEVEL");
	        String message = "msg ***!"+Math.floor(Math.random()*100);
	        
	        //发布消息到交换EXCHANGE_NAME，并且指定routingKey=severity，
	        channel.basicPublish(EXCHANGE_NAME, severity, null, message.getBytes("UTF-8"));
	        System.out.println(" [x] Sent '" + severity + "':'" + message + "'");
	    }
	  }
}
