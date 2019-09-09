package com.auth.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.auth.service.SessionUserServ;
import com.ijs.core.base.model.User;
import com.ijs.core.common.dao.GenericDao;

@Service("ssoUserServ")
public class SessionUserServImpl implements  SessionUserServ{
	protected Logger log = org.apache.log4j.LogManager.getLogger(this.getClass());
	@Autowired
	private GenericDao dao;
	public User loadUserByaccountNo(String username)
			throws UsernameNotFoundException {
		List<User> users = null;
		StringBuffer jpql = new StringBuffer();
		
		try {			
			jpql.append("from User u")
					.append(" where u.status=2 and u.accountNo='")
					.append(username.toLowerCase())
					.append("'");
			users = dao.find(jpql.toString());
			jpql = null;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("load User by name failed");
		}
		
		if (users == null || users.isEmpty()) {
			throw new UsernameNotFoundException("User '" + username+ "' not found...");
		} else {
			User user = users.get(0);
			return user;
		}
	
	}

}
