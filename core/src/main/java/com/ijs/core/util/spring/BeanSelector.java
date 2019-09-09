/**
 * 
 */
package com.ijs.core.util.spring;

import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;

import com.ijs.core.base.control.BaseControl;
import com.ijs.core.component.SessionCache;

/**
 * @author Tairong
 *
 */
public class BeanSelector{
	
	public static ApplicationContext applicationContext = null;
		
	/**
	 * 获得一个Spring Bean实例
	 * @param beanName
	 * @return
	 */
	public static Object getBean(String beanName) {
		if(applicationContext!=null)
			return applicationContext.getBean(beanName);
		else
			return null;
	}
	
	public static HttpSession getSession() {
		SessionCache sc=null;
		try {
			sc=applicationContext.getBean(SessionCache.class);
			return sc.getSession(BaseControl.getRequest());
		}catch(Exception e) {
			return BaseControl.getRequest().getSession();
		}
	}
}
