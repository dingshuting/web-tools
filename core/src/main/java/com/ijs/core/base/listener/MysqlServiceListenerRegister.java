package com.ijs.core.base.listener;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ijs.core.base.service.GenericServ;
/**
 * 数据服务层的监听注册器，通过此注册器统一管理所有基于基本数据外所扩展的业务
 * @author Administrator
 *
 */
public class MysqlServiceListenerRegister {
	protected final static transient Log log = LogFactory.getLog(MysqlServiceListenerRegister.class);
	private static List<MysqlServiceListener> sls=new ArrayList<MysqlServiceListener>();
	
	public static void addServiceListener(MysqlServiceListener sl){
		sls.add(sl);
	}
	public static void removeServiceListener(MysqlServiceListener osl){
		for(MysqlServiceListener sl:sls){
			if(sl.getClass().getName().equals(osl.getClass().getName())){
				sls.remove(sl);
			}
		}
	}
	public static List<MysqlServiceListener> getSls(){
		return sls;
	}
	/**
	 * 注入监听的实现，使监听程序生效
	 * @param genericServ
	 */
	public static void startListen(GenericServ genericServ){
		for(MysqlServiceListener sl:sls){
			log.debug("注入监听器：'"+sl.getClass().getName());
			genericServ.addListener(sl);
		}
		
	}
}
