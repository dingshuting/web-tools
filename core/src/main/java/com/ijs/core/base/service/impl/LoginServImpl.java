package com.ijs.core.base.service.impl;


import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ijs.core.base.model.User;
import com.ijs.core.base.service.LoginServ;
import com.ijs.core.base.service.SessionUserServ;
import com.ijs.core.common.dao.GenericDao;
@Service
public class LoginServImpl implements  LoginServ{
	protected Logger log = org.apache.log4j.LogManager.getLogger(this.getClass());
	@Resource
	private GenericDao genericDao;
	@Resource
	private SessionUserServ sessionUserServ;
	/**
	 * {@inheritDoc}
	 */		
	public User loadUserByUsername(String username)
			throws UsernameNotFoundException {
		return sessionUserServ.loadUserByaccountNo(username);
	}
	
	
	

}
