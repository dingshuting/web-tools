/**
 * 
 */
package com.auth.service;

import com.ijs.core.base.model.User;

/**
 * @author Tairong
 *
 */
public interface SessionUserServ {
	
	public User loadUserByaccountNo(String name);
}
