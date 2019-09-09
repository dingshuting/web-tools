package com.ijs.core.common.aop.exception;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.Collection;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.ThrowsAdvice;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.ijs.core.base.control.BaseControl;
import com.ijs.core.base.model.Func;
import com.ijs.core.base.model.SysLog;
import com.ijs.core.base.model.User;
import com.ijs.core.base.service.GenericServ;

@Aspect
@Component
public class AopLog implements ThrowsAdvice {
	@Resource
	protected GenericServ genericServ;
	@Pointcut("execution(public * com.ijs..control.*.*(..))")
	public void webLog() {
	}
	@Pointcut("execution(public * com.ijs.controller.*.*(..))")
	public void exLog() {
		
	}
	protected Logger log = org.apache.log4j.LogManager.getLogger(this.getClass());

	@AfterThrowing(throwing = "ex", pointcut = "execution(* com.ijs..service.*(..))")
	public void doRecoveryActions(Throwable ex) {
		try {
			StringWriter swriter=new StringWriter();
			PrintWriter writer=new PrintWriter(swriter);
			ex.printStackTrace(writer);
			SysLog log=getLog(swriter.toString());
			log.setTypes(0);
			genericServ.save(log);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String getContent(JoinPoint jp) {
		StringBuffer con=new StringBuffer("Class->").append(jp.getTarget().getClass())
				.append(jp.getSignature().getName())
				.append("\r\t");
		con.append("para ↓");
		if(jp.getArgs().length>0) {
			for(Object obj:jp.getArgs()) {
				if(obj==null) {
					con.append("null");
					continue;
				}
				if(obj instanceof Collection) {
					Collection<?> list=(Collection)obj;
					con.append("List->\r\t\t\t size->").append(list.size());
					if(list.size()>0) {
						Object obj1=list.iterator().next();
						con.append("\r\t\t\t obj->").append(obj1.getClass().getName());
						con.append("\r\t\t\t\t obj->").append(new Gson().toJson(obj1));
					}
				}else if(null!=obj&&null!=BeanUtils.getPropertyDescriptor(obj.getClass(), "id")) {
					try {
						con.append("\r\t\t\t id->").append(BeanUtils.getPropertyDescriptor(obj.getClass(), "id").getReadMethod().invoke(obj, null));
						con.append("\r\t\t\t class->").append(obj.getClass().getName()).append("↓\r").append("\t\t");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else {
					con.append("\r\t\t\t").append(obj.getClass()).append("->");
					con.append(obj.toString());
				}
			}
		}
		return con.toString();
	}
	private SysLog getLog(String content) {
		SysLog log=new SysLog();
		log.setLogTime(new Timestamp(System.currentTimeMillis()));
		if(BaseControl.getRequest()!=null)
		log.setRequestUrl(BaseControl.getRequest().getRequestURI());
		User user=BaseControl.getCurrentUser();
		if(user!=null) {
			Func func = user.getFuncs().getFuncByUrl(log.getRequestUrl());
			if(func!=null) {
				log.setFunctionId(func.getId());
				log.setTitle(func.getName());
			}
		}
		if(log.getRequestUrl()!=null&&log.getRequestUrl().matches(".*update.*|.*save.*|.*modify.*|.*bind.*|.*add.*|.*delete.*|.*remove.*|.*set.*")) {
			log.setTypes(1);	
		}else {
			log.setTypes(2);
			return null;
		}
		log.setContent(content);
		if(user!=null) {
			log.setOperator(user.getAccountNo());
		}
		return log;
		
	}
	/**
	 * 后置通知
	 * 
	 * @param jp
	 *            连接点
	 * @param result
	 *            返回值
	 */
	@After("exLog()")
	public void doAfter(JoinPoint jp) {
		try {
			SysLog log=getLog(getContent(jp));
			if(log!=null)
				genericServ.save(log);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
