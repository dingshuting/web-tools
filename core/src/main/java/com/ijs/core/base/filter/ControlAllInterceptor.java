package com.ijs.core.base.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ijs.core.base.Config;
import com.ijs.core.base.Constants;
import com.ijs.core.base.control.BaseControl;
import com.ijs.core.base.model.Func;
import com.ijs.core.base.model.User;
import com.ijs.core.util.spring.BeanSelector;

/**
 * 
 * the SpringInterceptor judge a access permission for the request and the
 * request of mobile devices. if the request is completed by a mobile device,
 * that the handle will add a suffix that appoint the parameter its name is
 * "mobileSuffix" after the ViewName in ModelAndView.
 * 
 * 自定义的SpringMVC拦截器，用于权限的判断
 * @author Dustin
 * 
 * 
 */
public class ControlAllInterceptor extends HandlerInterceptorAdapter {
	public ControlAllInterceptor() {
		// TODO Auto-generated constructor stub
	}
	public ControlAllInterceptor(String excludeUrl){
		setExcludeUrl(excludeUrl);;
	}
	protected Logger log = org.apache.log4j.LogManager.getLogger(this.getClass());
	private String[] excludeUrlarray;
	/**
	 * 不进行权限验证的url，多个用,分隔
	 */
	private String excludeUrl;

	/**
	 * the handle is executed after a method of control executed.<br/>
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		super.afterCompletion(request, response, handler, ex);
	}

	/**
	 * the method judge if the request has permissions to access the system
	 * 判断是否有权限访问系统，通过判断请求的url是否在已有的功能权限中可以匹配到，匹配到说明有权限，否则返回401
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// TODO Auto-generated method stub
		boolean isPass = false;
		request.setAttribute(Config.CTX,request.getContextPath() );
		String requestUrl = request.getRequestURI();
		log.debug("serverName have got it is:" + requestUrl+" and the request is :"+BaseControl.getRequestInfo(request).toString());
		//BeanSelector.getSession();
		if (isExclude(requestUrl)||isStatic(requestUrl)) {
			return true;
		}
		
		User user = BaseControl.getCurrentUser();
		if(user==null) {
			log.debug("the chain was broken , cause there is no user logined");
			response.setStatus(401);
			return false;
		}
		Func func = user.getFuncs().getFuncByUrl(requestUrl);
		if (func == null) {
			Object currentFuncId = request.getAttribute(Constants.FUNC_CURRENT);
			if (currentFuncId == null) {
				log.error("the func【"+requestUrl+"】 was not found in the list of funcs");
				response.setStatus(401);
				isPass = false;
				return isPass;
			}
			if (user.getFuncs().getFuncMap().get(currentFuncId) != null) {
				log.warn("there is a optionUrl that requestUrl is:" + requestUrl + " and the currentUser is "
						+ user.getAccountNo());
				isPass = true;
			} else {
				log.error("The func of requestUrl('" + requestUrl + "') is null, please use a valid url.");

				isPass = false;
			}
		} else {
			log.debug("the func of requestUrl is :" + func.getId() + "-" + func.getName());
			request.setAttribute(Constants.FUNC_CURRENT, func);
			isPass = true;
		}
		if (isPass) {
			if (Config.SYS_PARAMETER_MAP.get(Config.IS_SEACH_ECHO).equals(Constants.TURN_ON)) {
				echo(request);
			}
			return super.preHandle(request, response, handler);
		} else {
			// 没有权限的
			response.setStatus(401);
			return false;
		}
	}
	
	private static boolean isStatic(String requestUrl) {
		// TODO Auto-generated method stub
		return requestUrl.matches(".*\\.(html|htm|gif|jpg|jpeg|bmp|png|ico|txt|js|css|ejs|tff|woff|woff2|map|apk|mp3|mav|mp4|avi|wav|json)");
	}

	/**
	 * 判断是否排除的url，如果匹配到则不对该url进行权限验证
	 * 
	 * @param url
	 *            访问的url
	 * @return
	 */
	private boolean isExclude(String url) {
		for (String ex : excludeUrlarray) {
			if (url.matches(ex.trim().replace("*", ".*"))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 将request中的请求值从新设置到request中，用于查询条件的回显操作
	 * 
	 * @param request
	 */
	private void echo(HttpServletRequest request) {
		java.util.Enumeration<String> en = request.getParameterNames();
		String param = null;
		while (en.hasMoreElements()) {
			param = en.nextElement();
			request.setAttribute(param, request.getParameter(param));
		}

	}
	
	public void setExcludeUrl(String excludeUrl) {
		this.excludeUrl = excludeUrl;
		if (this.excludeUrl != null) {
			excludeUrlarray = excludeUrl.split(",");
		}
	}



}
