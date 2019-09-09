package com.ijs.core.base.listener;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ijs.core.base.control.BaseControl;
import com.ijs.core.base.model.User;
import com.ijs.core.base.service.GenericServ;
import com.ijs.core.common.dao.GenericDao;

public abstract class MysqlBaseServiceListener implements MysqlServiceListener {
	protected transient Log log;
	@Resource
	protected GenericDao dao;
	@Resource
	protected GenericServ genericServ;
	public MysqlBaseServiceListener() {
		// TODO Auto-generated constructor stub
		MysqlServiceListenerRegister.addServiceListener(this);
		log=LogFactory.getLog(getClass());
		log.debug("实例化MYSQL基础功能的扩展监听--》"+this.getClass().getName());
	}
	/**
	 * 判断当前用户是否进行登录
	 * @return
	 */
	protected boolean isLogin(){
		User user=BaseControl.getCurrentUser();
		if(user==null){
			return false;
		}else{
			return true;
		}
	}
	/**
	 * 判断是否为api模式访问
	 * @return
	 */
	protected boolean isAccessByAPI(){
		if(BaseControl.getRequestInfo(BaseControl.getRequest()).getRequestURI().indexOf("api/") > -1){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 直接过去当前登录用户
	 * @return
	 */
	protected User getCurrentUser(){
		return BaseControl.getCurrentUser();
	}
}
