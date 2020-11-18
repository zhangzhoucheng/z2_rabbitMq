package com.zz.test.mqconnect;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class ConnectionUtil {
	
	private static final String [] INFOLEVEL = {"error","info","warning"};
	private static final String [] TOPICINFO = {"quick.orange.rabbit","quick.orange.fox","lazy.brown.fox","lazy.orange.elephant","lazy.pink.rabbit","quick.brown.fox"};
	
	public static Connection getConnection() throws Exception {
        //定义连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置服务地址
        factory.setHost("localhost");
        //端口
        factory.setPort(5672);
        //设置账号信息，用户名、密码、vhost
        factory.setVirtualHost("firstHost");
        factory.setUsername("admin");
        factory.setPassword("admin");
        // 通过工程获取连接
        Connection connection = factory.newConnection();
        return connection;
    }
	
	public static Object getRandomBytype(String type) {
		if("INFOLEVEL".equals(type)) {
			return INFOLEVEL[(int) (Math.floor(Math.random()*INFOLEVEL.length))];
		}
		if("TOPICINFO".equals(type)) {
			return TOPICINFO[(int) (Math.floor(Math.random()*TOPICINFO.length))];
		}
		return type;
		
	}

}
