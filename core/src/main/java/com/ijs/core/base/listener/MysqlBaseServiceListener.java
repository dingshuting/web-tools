package com.ijs.core.base.listener;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ijs.core.base.control.BaseControl;
import com.ijs.core.base.model.User;
import com.ijs.core.base.service.GenericServ;
import com.ijs.core.common.dao.GenericDao;

public abstract class MysqlBaseServiceListener implements MysqlServiceListener {
	protected final transient Log log = LogFactory.getLog(getClass());
	@Resource
	protected GenericDao dao;
	@Resource
	protected GenericServ genericServ;
	public MysqlBaseServiceListener() {
		// TODO Auto-generated constructor stub
		log.debug("实例化MYSQL基础功能的扩展监听--》"+this.getClass().getName());
		MysqlServiceListenerRegister.addServiceListener(this);
	}
	protected boolean isLogin(){
		User user=BaseControl.getCurrentUser();
		if(user==null){
			return false;
		}else{
			return true;
		}
	}
	protected boolean isAccessByAPI(){
		if(BaseControl.getRequestInfo(BaseControl.getRequest()).getRequestURI().indexOf("api/") > -1){
			return true;
		}else{
			return false;
		}
	}
	protected User getCurrentUser(){
		return BaseControl.getCurrentUser();
	}
}
