package com.ijs.core.base.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.ijs.core.base.model.User;
 
public interface LoginServ  extends UserDetailsService{

	public User loadUserByUsername(String username)
			throws UsernameNotFoundException ;
}
