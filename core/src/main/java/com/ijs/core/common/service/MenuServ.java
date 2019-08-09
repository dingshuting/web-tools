/**
 * 
 */
package com.ijs.core.common.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.ijs.core.base.model.Func;
import com.ijs.core.base.service.GenericServ;


public interface MenuServ extends GenericServ{
	
	public List<Func> getFuncs(String parent, HttpSession session);
	
	/**
	 * 根据URL来获取当前的功能
	 * @param url
	 * @return 
	 */
	Func getFuncByUrl(String url, HttpSession session);
}
