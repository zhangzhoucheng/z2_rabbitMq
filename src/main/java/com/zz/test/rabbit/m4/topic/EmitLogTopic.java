package com.zz.test.rabbit.m4.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.zz.test.mqconnect.ConnectionUtil;

/**
 * 
 * <note>
 * Desc： topic model
 * Use: 在routing模型基础上，给routingKey赋值   *.*.* 的模式，从而匹配到 对应的queue，可以完成多条件匹配（即消费者的一个 routingKey 'lazy.#'
 *     可以匹配lazy开头主题消息。当然一个消费者channele可以配置多个routingKey，一个routingKey完成模糊匹配，或者直接匹配，很灵活)
 * @author jld.zhangzhou
 * @refactor for jld
 * @datetime 2019-08-02 15:26:28
 * @location mobile base 3th,BeiJing 
 * version  1.0
 *  
 * @REVISIONS: 
 * Version 	        Date 		         Author             Location                   Description          
 * ------------------------------------------------------------------------------------------------------  
 * 1.0 		  2019-08-02 15:26:28    jld.zhangzhou     mobile base 3th,BeiJing      1.create the class            
 * </note>
 */
public class EmitLogTopic {
	private static final String EXCHANGE_NAME = "topic_logs";

	  public static void main(String[] argv) throws Exception {

	    try (Connection connection = ConnectionUtil.getConnection();
	         Channel channel = connection.createChannel()) {

	    	//设置exchange为主题模式。
	        channel.exchangeDeclare(EXCHANGE_NAME, "topic");

	      //运行参数，这样可以根据参数不同，启动多个应用，设置不同的routingKey
	        String routingKey = argv[0];
        	String message = "topic,msg=" + argv[0];
	        
	        //懒得修改启动参数，通过随机形式
	        /*String routingKey = (String) ConnectionUtil.getRandomBytype("TOPICINFO");
	        String message = "msg ***!"+Math.floor(Math.random()*100);*/
	        
	        //发布主题消息
	        channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes("UTF-8"));
	        System.out.println(" [x] Sent '" + routingKey + "':'" + message + "'");
	    }
	  }
}
