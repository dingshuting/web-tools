package com.ijs.core.common.aop.exception;
import org.apache.log4j.Logger;

public class ServiceExcption extends RuntimeException {
	
	protected Logger log = org.apache.log4j.LogManager.getLogger(this.getClass());
	
	public ServiceExcption(String info){
		log.info(info);
	}
	
 }
