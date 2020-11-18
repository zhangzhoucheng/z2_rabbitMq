package com.zz.test.thread;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * 
 * <note>
 * Desc： 线程本地变量，是在每个调用的线程中，构建副本
 * use：1、可以不用为每个线程去创建ThreadLocal，节约资源。2、避免了单一变量 的阻塞问题。
 * eg：1、用于数据库连接connectionHolder。2、用于session
 * @author jld.zhangzhou
 * @refactor for jld
 * @datetime 2019-08-30 16:30:06
 * @location mobile base 3th,BeiJing 
 * version  1.0
 *  
 * @REVISIONS: 
 * Version 	        Date 		         Author             Location                   Description          
 * ------------------------------------------------------------------------------------------------------  
 * 1.0 		  2019-08-30 16:30:06    jld.zhangzhou     mobile base 3th,BeiJing      1.create the class            
 * </note>
 */
public class TestLocalThread {
	private static ThreadLocal<Connection> connectionHolder = new ThreadLocal<Connection>(){  
	    public Connection initialValue() {  
	        try {
				return DriverManager.getConnection("");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;  
	    }  
	};  
	  
	public static Connection getConnection() {
	    return connectionHolder.get();  
	}  


}
