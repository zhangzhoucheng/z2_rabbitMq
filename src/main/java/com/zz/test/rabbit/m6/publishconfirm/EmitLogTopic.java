package com.zz.test.rabbit.m6.publishconfirm;

import java.time.Duration;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.BooleanSupplier;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.Connection;
import com.zz.test.mqconnect.ConnectionUtil;

/**
 * 
 * <note>
 * Desc： Publisher Confirms 的异步模式
 * Use: 发布者确认，在topic基础上，对发送的消息进行一个异步确认;具体做法先保存sequenceNumber, multiple 到一个特定map，之后确认消息，移除map对应key
 * point:由于确认消息会阻塞该队列上的消息发布，所以效率会受到影响
 * @author jld.zhangzhou
 * @refactor for jld
 * @datetime 2020-11-09 09：54：33
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
        
        	/**
        	 * 发布确认
        	 * 策略：异步模式
        	 */
        	// 开启发布确认选项
        	channel.confirmSelect();
            
            //注意：下列的sequenceNumber数量可以在发布前通过Channel#getNextPublishSeqNo()获取
            long sequenceNumber0 = channel.getNextPublishSeqNo();
            System.out.println("发布前sequenceNumber=" + sequenceNumber0);
            
            /**
             * 消息确认回调
             */
            //临时存放 预先sequenceNumber的，map
            ConcurrentNavigableMap<Long, String> outstandingConfirms = new ConcurrentSkipListMap<>();//跳表实现的有序的key且线程安全的map；存取时间是log（N），和线程数几乎无关，高并发场景下优于ConcurrentHashMap；ref:https://www.cnblogs.com/java-zzl/p/9767255.html 
            //回调监听器的第一个函数式参数，
            ConfirmCallback cleanOutstandingConfirms = (sequenceNumber, multiple) -> {// sequenceNumber：消息数量；multiple =false 表示一个消息被确认，=true表示 <=sequenceNumber 数量的所有消息被确认
                
                // code when message is confirmed
                System.out.println(" [x] confirm '" + routingKey + "':'" + outstandingConfirms.get(sequenceNumber) + "',and status: sequenceNumber="+sequenceNumber + ",multiple="+ multiple);
               
            	if (multiple) {
                    ConcurrentNavigableMap<Long, String> confirmed = outstandingConfirms.headMap(
                      sequenceNumber, true
                    );
                    confirmed.clear();
                } else {
                    outstandingConfirms.remove(sequenceNumber);
                }

                //o.notify();
            };
            //消息确认监听器
            channel.addConfirmListener(cleanOutstandingConfirms, (sequenceNumber, multiple) -> {
            	String body = outstandingConfirms.get(sequenceNumber);
                System.out.format(
                  "Message with body %s has been nack-ed. Sequence number: %d, multiple: %b%n",
                  body, sequenceNumber, multiple
                );
                cleanOutstandingConfirms.handle(sequenceNumber, multiple);
                
             // code when message is nack-ed
            	System.out.println(" [x] confirm  nack-ed '" + routingKey + "':'" + outstandingConfirms.get(sequenceNumber) + "',and status: sequenceNumber="+sequenceNumber + ",multiple="+ multiple);
            	
            	//o.notify();
            });
           
            //消息数量
            int MESSAGE_COUNT = 5;
            long start = System.nanoTime();
            for (int i = 0; i < MESSAGE_COUNT; i++) {
                String body = String.valueOf(i);
                outstandingConfirms.put(channel.getNextPublishSeqNo(), body);
              //发布主题消息
                channel.basicPublish(EXCHANGE_NAME, routingKey, null, body.getBytes());
                System.out.println(" [x] Sent '" + routingKey + "':'" + body + "'");
            }

            if (!waitUntil(Duration.ofSeconds(60), () -> outstandingConfirms.isEmpty())) {
                throw new IllegalStateException("All messages could not be confirmed in 60 seconds");
            }

            long end = System.nanoTime();
            System.out.format("Published %,d messages and handled confirms asynchronously in %,d ms%n", MESSAGE_COUNT, Duration.ofNanos(end - start).toMillis());

	  
	        
	    }
	  }
	  
	  static boolean waitUntil(Duration timeout, BooleanSupplier condition) throws InterruptedException {
	        int waited = 0;
	        while (!condition.getAsBoolean() && waited < timeout.toMillis()) {
	            Thread.sleep(100L);
	            waited = +100;
	        }
	        return condition.getAsBoolean();
	    }
}
