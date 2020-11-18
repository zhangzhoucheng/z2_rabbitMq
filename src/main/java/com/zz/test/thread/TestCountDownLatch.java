package com.zz.test.thread;

import java.util.concurrent.CountDownLatch;

/**
 * 
 * <note>
 * Desc：CountDownLatch用法 ，CountDownLatch是一个计数器，为0可以唤醒CountDownLatch.await 阻塞的线程。
 * @use:1、CountDownLatch end = new CountDownLatch(N); //构造对象时候 需要传入参数N
        2、end.await()  能够阻塞线程 直到调用N次end.countDown() 方法才释放线程
        3、end.countDown() 可以在多个线程中调用  计算调用次数是所有线程调用次数的总和
 * @author jld.zhangzhou
 * @refactor for jld
 * @datetime 2019-08-30 16:27:04
 * @location mobile base 3th,BeiJing 
 * version  1.0
 *  
 * @REVISIONS: 
 * Version 	        Date 		         Author             Location                   Description          
 * ------------------------------------------------------------------------------------------------------  
 * 1.0 		  2019-08-30 16:27:04    jld.zhangzhou     mobile base 3th,BeiJing      1.create the class            
 * </note>
 */
public class TestCountDownLatch {

    public static void main(String[] args) {
       CountDownLatch begin = new CountDownLatch(1);
       CountDownLatch end = new CountDownLatch(2);

       for(int i=0; i<2; i++){
           Thread thread = new Thread(new Player(begin,end));
           thread.start();
       }

       try{
           System.out.println("the race begin");
           begin.countDown();
           end.await();
           System.out.println("the race end");
       }catch(Exception e){
            e.printStackTrace();
       }

    }
}


/**
 * 选手
 */
class Player implements Runnable{

    private CountDownLatch begin;

    private CountDownLatch end;

    Player(CountDownLatch begin,CountDownLatch end){
        this.begin = begin;
        this.end = end;
    }

    public void run() {
        
        try {
            begin.await();
            System.out.println(Thread.currentThread().getName() + " arrived !");;
            end.countDown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
