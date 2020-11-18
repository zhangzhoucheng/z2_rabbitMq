package com.zz.test.rabbit.m5.rpc;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.zz.test.mqconnect.ConnectionUtil;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

public class RPCClient implements AutoCloseable {

    private Connection connection;
    private Channel channel;
    private String requestQueueName = "rpc_queue";

    public RPCClient() throws Exception {

    	//get connection
        connection = ConnectionUtil.getConnection();
        channel = connection.createChannel();
    }

    public static void main(String[] argv) throws Exception  {
    	
        try (RPCClient fibonacciRpc = new RPCClient()) {
        	
            for (int i = 0; i < 1; i++) {
                String i_str = Integer.toString(i);
                System.out.println(" [x] Requesting fib(" + i_str + ")");
                
                //request,and call
                String response = fibonacciRpc.call(i_str);
                System.out.println(" [.] Got '" + response + "'");
            }
            
        } catch (IOException | TimeoutException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    public String call(String message) throws IOException, InterruptedException {
        final String corrId = UUID.randomUUID().toString();

        //获取随机一个queue，用于replyTo的返回队列
        String replyQueueName = channel.queueDeclare().getQueue();
        
        //设置corrId（用于唯一标识，连接response），replyTo （当server处理完后，返回给replyQueueName=rpc_queue 队列），从而从中获取响应。
        AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();

        //发布消息
        channel.basicPublish("", requestQueueName, props, message.getBytes("UTF-8"));

        //临时封锁队列。
        final BlockingQueue<String> response = new ArrayBlockingQueue<>(1);

        //消费消息
        String ctag = channel.basicConsume(replyQueueName, true, (consumerTag, delivery) -> {
            if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                response.offer(new String(delivery.getBody(), "UTF-8"));
            }
        }, consumerTag -> {
        });

        String result = response.take();
        channel.basicCancel(ctag);
        return result;
    }

    public void close() throws IOException {
        connection.close();
    }
}
