package com.ijs.mongo.service.listener;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import com.ijs.mongo.service.MongoService;
import com.ijs.mongo.service.ServiceListener;
import com.ijs.mongo.service.ServiceListenerRegister;
/**
 * 基础的服务监听实现类
 * @author Administrator
 *
 */
public abstract class BaseServiceListener implements ServiceListener,Ordered {
	protected final transient Log log = LogFactory.getLog(getClass());
	@Resource
	protected MongoService baseMongoServ;
	public BaseServiceListener() {
		// TODO Auto-generated constructor stub
		ServiceListenerRegister.addServiceListener(this);
	}
	/**
	 * 获取当前服务的排序顺序，排序在整个业务扩展接口中起着至关重要的作用，如先处理字段在处理日志等。
	 */
	public int getOrder() {
		// TODO Auto-generated method stub
		Order ord=this.getClass().getAnnotation(Order.class);
		if(ord!=null){
			return ord.value();
		}
		return -1;
	}
}
