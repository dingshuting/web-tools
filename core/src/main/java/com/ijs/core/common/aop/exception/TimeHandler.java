package com.ijs.core.common.aop.exception;


import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class TimeHandler implements MethodInterceptor {

	protected Logger logger = org.apache.log4j.LogManager.getLogger(this
			.getClass());

	@Override
	// 重写invoke()方法
	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		long procTime = System.currentTimeMillis();
//		if(methodInvocation.getArguments().length>0){
//			logger.log(Level.INFO, methodInvocation.getArguments()[0] + "   开始执行     "
//					+ methodInvocation.getMethod() + "   方法    ");
//		}
		try {
			Object result = methodInvocation.proceed();
			return result;
		} finally {
//			// 计算执行时间
//			procTime = System.currentTimeMillis() - procTime;
//			if(methodInvocation.getArguments().length>0){
//				logger.log(Level.INFO, methodInvocation.getArguments()[0] + "    执行     "
//						+ methodInvocation.getMethod() + " 方法结束");
//			}
//			if(methodInvocation.getMethod().getName()!=null){
//				logger.log(Level.INFO, "    执行     "
//						+ methodInvocation.getMethod().getName() + "   方法共用了   "
//						+ procTime + "毫秒");
//			}
			
		}
	
	}
	  
}
