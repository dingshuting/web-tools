package com.ijs.mongo.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.OrderComparator;

public class ServiceListenerRegister {
	protected final static transient Log log = LogFactory.getLog(ServiceListenerRegister.class);
	private static List<ServiceListener> sls=new ArrayList<ServiceListener>();
	
	public static void addServiceListener(ServiceListener sl){
		sls.add(sl);
	}
	public static void removeServiceListener(ServiceListener osl){
		for(ServiceListener sl:sls){
			if(sl.getClass().getName().equals(osl.getClass().getName())){
				sls.remove(sl);
			}
		}
	}
	public static List<ServiceListener> getSls(){
		return sls;
	}
	public static void startListen(MongoService mongoService){
		OrderComparator.sort(sls);
		for(ServiceListener sl:sls){
			log.info("注入监听器：'"+sl.getClass().getName());
			mongoService.addListener(sl);
		}
		
	}
}
