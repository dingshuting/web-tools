package com.ijs.core.base.control;

import java.io.BufferedReader;

/**
 * 
 */

import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.ijs.core.base.Constants;
import com.ijs.core.base.model.Func;
import com.ijs.core.base.model.User;
import com.ijs.core.base.service.DataDepend;
import com.ijs.core.base.service.GenericServ;
import com.ijs.core.util.CacheUtil;
import com.ijs.core.util.RequestModel;
import com.ijs.core.util.spring.BeanSelector;
import com.sun.xml.internal.messaging.saaj.util.Base64;


/**
 * 所有Control的父类，此类提供所有Control可能用的到最基本的功能
 */
public abstract class BaseControl {
	protected transient Log log;
	protected final String ERROR_CODE="999";
//	protected final String SUCCESS_CODE="1";
	//用于确认当前用户的身份扩展信息的依赖接口
	public static DataDepend userOwner;
	public BaseControl() {
		log=LogFactory.getLog(getClass());
	}
	public static ThreadLocal<Map<String,String[]>> requestPara=new ThreadLocal<>();
	@Resource
	protected GenericServ genericServ;
	@Resource(name="genericControl")
	protected GenericControl control;
	protected ApplicationContext  getApplicationContext(){
		ApplicationContext ctx = RequestContextUtils.findWebApplicationContext(getRequest(), getRequest().getServletContext());
		return ctx;
	}
	/**
	 * 获取当前的Request对象
	 * @return
	 */
	public static HttpServletRequest getRequest() {
		ServletRequestAttributes requestAttr= ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes());
		if(requestAttr!=null) {
			return requestAttr.getRequest();
		}else {
			return null;
		}
	}
	/**
	 * 获取request的请求参数
	 * @return
	 */
	public  static Map<String,String[]> getRequestPara(){
		if(requestPara.get()!=null) {
			return requestPara.get();
		}else if(getRequest()!=null) {
			return getRequest().getParameterMap();
		}else {
			return null;
		}
	}
	/**
	 * 获取指定的request参数
	 * @param key 参数的键值
	 * @return
	 */
	public static String getRequestPram(String key){
		HttpServletRequest request =getRequest();
		if(request!=null) {
			return request.getParameter(key);
		}else if(getRequestPara()!=null) {
			return getRequestPara().get(key).length>0?getRequestPara().get(key)[0]:null;
		}else {
			return null;
		}
	}
	//@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}
	/**
	 * Convenience method to get the userForm from the session
	 * 获取当前登录的用户信息，如果未登录则返回null
	 * @return the user's populated form from the session
	 */
    public static User getCurrentUser() {
    	User user=null;
    	if(getSecurityContext()!=null&&getSecurityContext().getAuthentication()!=null){
    		if(!getSecurityContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
    			Object obj =  getSecurityContext().getAuthentication().getPrincipal();
    			if(obj instanceof User){
    				user=(User) obj;
    			}else if( getSecurityContext().getAuthentication().getDetails() instanceof User) {
    				user=(User)getSecurityContext().getAuthentication().getDetails();
    			}
    		}
    	}
    	if(user==null&&getRequest()!=null) {
    		user=(User) getRequest().getAttribute("user");
    	}
        return user;
    }
    public static SecurityContext getSecurityContext() {
    	return SecurityContextHolder.getContext();
    }
    /**
     * 获取当前请求的功能信息
     * @return 功能对象
     */
    public static Func getCurrentFunc(){
    	return (Func) getRequest().getAttribute(Constants.FUNC_CURRENT);
    }
	/** 
	 * 对url进行Base64方式解码
	 * @param base64URL base64格式的字符串
	 * @param isConverted 是否进行了url编码
	 * @return
	 */
	protected String decodeBase64URL(String base64URL, boolean isConverted){
		if(isConverted){
			base64URL = base64URL.replaceAll("*", "=");
		}
		return Base64.base64Decode(base64URL);
	}

	/**
	 * 获取request的流信息
	 * @return 请求的字符串信息
	 */
	protected String getRequestContent(){
		HttpServletRequest request=getRequest();
		StringBuffer sb=new StringBuffer();
		try {
			BufferedReader input = new BufferedReader(new InputStreamReader(request.getInputStream()));
				String data = null;
				while((data = input.readLine())!=null){
					sb.append(data);
				}
				return sb.toString();
			} catch (IOException e) {
				e.printStackTrace();
			}
		  return "";
	}
	/**
	 * 获取 Requeset 消息体
	 * @return RequestModel
	 */
	public static RequestModel getRequestInfo(HttpServletRequest request) {
		/*HttpServletRequest request=getRequest();*/
		RequestModel rm=new RequestModel();
		rm.setUserAgent(request.getHeader("User-agent"));
		rm.setMethod(request.getMethod());
		rm.setRequestURI(request.getRequestURI());
		rm.setServletPath(request. getServletPath());
		rm.setServerName(request. getServerName());
		rm.setServerPort(request.getServerPort()+"");
		rm.setRemoteAddr(request.getRemoteAddr());
		rm.setRemoteHost(request.getRemoteHost());
		rm.setProtocol(request.getProtocol());
		Enumeration<String> ns=request.getHeaderNames();
		Map<String,String> heads=new HashMap<String, String>();
		while(ns.hasMoreElements()){
			String n=ns.nextElement();
			heads.put(n, request.getHeader(n));
		}
		rm.setHeads(heads);
		return rm;
	}

	/**
	 * 验证验证码是否正确，如果某个Control需要防止机器操作可调用此验证码进行预防
	 * @return
	 */
	protected boolean verifyCode(){
		String code=getRequest().getParameter(Constants.VERIFY_CODE_SESSION_NAME);
		try {
			if(code!=null&&!code.isEmpty()){
				boolean result=code.toLowerCase().equals(BeanSelector.getSession().getAttribute(Constants.VERIFY_CODE_SESSION_NAME).toString().toLowerCase());
				if(result){
					BeanSelector.getSession().removeAttribute(Constants.VERIFY_CODE_SESSION_NAME);
				}
				return result;
			}else{
				throw new ValidationException("验证码不能为空");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ValidationException("验证码有误");
		}
	}
	public static String getCity() {
		String city="";
		if(getCurrentUser().getSysDep()!=null&&getCurrentUser().getSysDep().getStatus()!=2) {
			city=getCurrentUser().getSysDep().getCity();
		}
		return city;
	}
	/**
	 * 验证短信验证码是否正确
	 * @param isRemove 验证完毕后是否无效验证码
	 * @return
	 */
	protected boolean verifySMSCode(boolean isRemove){
		String code=getRequest().getParameter(Constants.VERIFY_SMS_CODE_SESSION_NAME);
		String phone = getRequest().getParameter("phone");
		if(code!=null&&!code.isEmpty()){
			boolean result=code.equals(CacheUtil.cacheMap.get(phone));
			if(result&&isRemove){
				//getRequest().getSession().removeAttribute(Constants.VERIFY_SMS_CODE_SESSION_NAME);
				CacheUtil.cacheMap.remove(phone);
			}
			return result;
		}else{
			throw new ValidationException("短信验证码不能为空");
		}
	}
	
}
