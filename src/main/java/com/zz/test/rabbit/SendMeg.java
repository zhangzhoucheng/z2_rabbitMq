package com.zz.test.rabbit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.zz.test.mqconnect.ConnectionUtil;

/**
 * 
 * <note>
 * Desc： base model Mq
 * Use:消息发送给queue，消费者进行消费。（这是一个基础模型）
 * @author jld.zhangzhou
 * @refactor for jld
 * @datetime 2019-08-01 16:16:22
 * @location mobile base 3th,BeiJing 
 * version  1.0
 *  
 * @REVISIONS: 
 * Version 	        Date 		         Author             Location                   Description          
 * ------------------------------------------------------------------------------------------------------  
 * 1.0 		  2019-08-01 16:16:22    jld.zhangzhou     mobile base 3th,BeiJing      1.create the class            
 * </note>
 */
public class SendMeg {
	private final static String QUE_NAME = "meg_test2";

    public static void main(String[] argv) throws Exception {
        //获取到连接
        Connection connection = ConnectionUtil.getConnection();
        
        //从连接中创建通道
        Channel channel = connection.createChannel();

        //声明（创建）队列
        channel.queueDeclare(QUE_NAME, false, false, false, null);
        
        //消息的内容
        String message = "Hello rabbitmq10:1!";
        String message1 = "Hello rabbitmq10：2!";
        
        channel.basicPublish("", QUE_NAME, null, message.getBytes());
        
        System.out.println(" @@@队列meg_test1，发送消息： '" + message + "'");
        
        channel.basicPublish("", QUE_NAME, null, message1.getBytes());
        //关闭通道和连接
        channel.close();
        connection.close();
    }

}
