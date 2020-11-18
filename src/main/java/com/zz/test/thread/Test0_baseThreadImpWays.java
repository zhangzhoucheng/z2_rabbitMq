package com.zz.test.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class Test0_baseThreadImpWays {
	
	public static void main(String[] args) throws Exception {
		
		System.out.println("****************************way1：实现Runnable");
		//way1：实现Runnable
		
		for(int i = 0; i<5; i++) {
			runnableImpl run1 = new runnableImpl();
			new Thread(run1).start();
		}
		
		System.out.println("****************************way2:继承thread");
		//way2:继承thread
		inheritThreadImpl run3 = new inheritThreadImpl();
		for(int i = 0; i<5; i++) {
			new Thread(run3).start();
		}
		
		System.out.println("****************************way2:FutureTask");
		//way3:FutureTask实现，可以获取返回值
		List<FutureTask<List>> flist = new ArrayList<>();
		List<List> stlist = new ArrayList<>();
		for(int i = 0; i<15; i++) {
			callThread run2 = new callThread("hh"+i);
			FutureTask<List> ft = new FutureTask<>(run2);
			Thread th = new Thread(ft);
			th.start();
			flist.add(ft);
			
		}
		for(FutureTask<List> f : flist) {
			System.out.println("@fv:"+f.get().toString());//当执行f.get的时候，会等待当前f执行完，才会get到值。
		}
		System.out.println("stlist:"+stlist.toString());
		
		
		
		
	}
}

class runnableImpl implements  Runnable {
    static int i = 10;
	@Override
	public  void run() {
		System.out.println("@i:"+i--);
		
	}
}


class inheritThreadImpl extends Thread {
	static int i = 10;
	public void run() {  
		System.out.println("MyThread.run()"+i--);
		try {
			this.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }  
}


class  callThread<List> implements Callable<List>{
	static int i = 10;
	private String  arg =  null;
	public callThread (String arg) {
		this.arg = (String) arg;
	}

	@Override
	public List call() throws Exception {
		ArrayList<String> list = new ArrayList<String>();
		list.add(arg);
		if(arg.equals("hh4")) {
			Thread.sleep(10000);
		}
		System.out.println(arg+",@:"+i--);
		return  (List) list;
	}
	
}
