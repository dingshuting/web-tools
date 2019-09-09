package com.ijs.core.component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * session缓存操作类，为了解决在stateless时，模拟session的相关功能。
 * @author Dustin
 *
 */
public interface SessionCache {
	/**
	 * 根据给定的id创建一个缓存Session
	 * @param sessionId
	 */
	public void create(HttpServletRequest request);
	/**
	 * 获取指定的session 
	 * @param sessionId 
	 * @return 返回一个HttpSession
	 */
	public HttpSession getSession(HttpServletRequest request);
	/**
	 * 移除当前的session
	 * @param sessionId
	 */
	public void remove(HttpServletRequest request);
}
