package com.zz.test.thread;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * <note>
 * @Desc： ReentrantLock 重入锁（非公平和公平）
 * @use：ReentrantLock是一个可重入且独占式的锁，它具有与使用synchronized监视器锁相同的基本行为和语义，但与synchronized关键字相比，
 *      它更灵活、更强大，增加了轮询、超时、中断等高级功能。ReentrantLock，顾名思义，它是支持可重入锁的锁，是一种递归无阻塞的同步
 *      机制。除此之外，该锁还支持获取锁时的公平和非公平选择
 * @remk1:new MyReentrantLock(true).lock//上锁的作用在哪里？，就是线程访问该lock资源需有 其它线程unlock，或者自己是拥有者,具体表现是testLockAffect 会打顺序打印10，9，8，7，6，5，4，3，2，1。
 *        如果不lock，则表现出资源竞争，打印很乱    
 * @author jld.zhangzhou
 * @refactor for jld
 * @datetime 2019-08-30 16:02:34
 * @location mobile base 3th,BeiJing 
 * version  1.0
 *  
 * @REVISIONS: 
 * Version 	        Date 		         Author             Location                   Description          
 * ------------------------------------------------------------------------------------------------------  
 * 1.0 		  2019-08-30 16:02:34    jld.zhangzhou     mobile base 3th,BeiJing      1.create the class            
 * </note>
 */
class TestReetrantLock {
	
	/**
	 * 线程计数器，用于多个线程执行，当计数器0的时候不再等待。
	 */
    private static CountDownLatch start;
    
    /*
     * volatile 强制线程修改的变量。同步到所有线程。
     */
    private static volatile int countFlag = 1;
    
    private static int testLockAffect = 10;
    
    private static class MyReentrantLock extends ReentrantLock {
        public MyReentrantLock(boolean fair) {
            super(fair);
        }
 
        public Collection<Thread> getQueuedThreads() {
        	//System.out.println("@:"+super.getQueuedThreads());
            List<Thread> arrayList = new ArrayList<Thread>(super.getQueuedThreads());
            Collections.reverse(arrayList);
            return arrayList;
        }
    }
    
    
    /**
     * 
     * <note>
     * Desc： 构建一个worker，lock=new MyReentrantLock();该锁lock 较于asynchronized有着更加强大之处，
     * @author jld.zhangzhou
     * @refactor for jld
     * @datetime 2019-08-30 16:37:24
     * @location mobile base 3th,BeiJing 
     * version  1.0
     *  
     * @REVISIONS: 
     * Version 	        Date 		         Author             Location                   Description          
     * ------------------------------------------------------------------------------------------------------  
     * 1.0 		  2019-08-30 16:37:24    jld.zhangzhou     mobile base 3th,BeiJing      1.create the class            
     * </note>
     */
    private static class Worker extends Thread {
        private Lock lock;
 
        public Worker(Lock lock) {
            this.lock = lock;
        }
 
        @Override
        public void run() {
            try {
                start.await();//要等start=0后，才唤醒，也就是testLock中的5个线程都初始化完成。
            } catch (InterruptedException e) {
            	e.printStackTrace();
            }
            // 连续两次打印当前的Thread和等待队列中的Thread
            for (int i = 0; i < 2; i++) {
            	
                lock.lock();
                try {
                    if(countFlag == 1) {
                    	countFlag --;
                    }else {
                    	
                    }
                    System.out.println("Lock by [" + getName() + "], Waiting by " + ((MyReentrantLock) lock).getQueuedThreads()+"@testLockAffect:"+testLockAffect--);
                }finally {
                    lock.unlock();
                }
            }
        }
 
        public String toString() {
            return getName();
        }
    }
 
    public static void main(String[] args) {
		Lock fairLock = new MyReentrantLock(true);
		Lock unfairLock = new MyReentrantLock(false);
		
		testLock(fairLock);
//		testLock(unfairLock);
	}
 
    private static void testLock(Lock lock) {
        start = new CountDownLatch(1);
        for (int i = 0; i < 5; i++) {
            Thread thread = new Worker(lock);
            thread.setName("" + i);
            thread.start();
        }
        start.countDown();
    }
}

