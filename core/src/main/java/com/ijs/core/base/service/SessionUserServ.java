/**
 * 
 */
package com.ijs.core.base.service;

import com.ijs.core.base.model.User;

/**
 * 用户登录的接口
 */
public interface SessionUserServ {
	/**
	 * 加载指定的
	 * @param name
	 * @return
	 */
	public User loadUserByaccountNo(String name);
}
