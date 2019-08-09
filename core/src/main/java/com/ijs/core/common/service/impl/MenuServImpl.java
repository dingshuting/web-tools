package com.ijs.core.common.service.impl;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ijs.core.base.Constants;
import com.ijs.core.base.model.Func;
import com.ijs.core.base.model.User;
import com.ijs.core.base.service.impl.GenericServImpl;
import com.ijs.core.common.service.MenuServ;

@Service("menuServ")
public class MenuServImpl extends GenericServImpl implements  MenuServ{
	protected Logger log = org.apache.log4j.LogManager.getLogger(this.getClass());

	@Override
	public List<Func> getFuncs(String parent, HttpSession session) {		
		//Get func container of current user from session.
		return ((User)session.getAttribute(Constants.CURRENT_USER_KEY)).getFuncs().loadFuncs2(parent);
	}

	@Override
	public Func getFuncByUrl(String url,HttpSession session) {
		// TODO Auto-generated method stub
		return ((User)session.getAttribute(Constants.CURRENT_USER_KEY)).getFuncs().getFuncByUrl(url);
		
	}

	
}
