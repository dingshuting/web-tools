package com.ijs.core.system.service;

import com.ijs.core.base.model.User;
import com.ijs.core.base.service.GenericServ;
/**
 * 用于对个人信息的相关操作，其针对于已经登录的用户，进行个人信息或者密码等信息的变更和查看
 * @author Dustin
 *
 */
public interface MyprofileServ extends GenericServ{
	/**
	 * 修改指定用户的密码，其中用户的标志id和password字段不能为空
	 * @param user 用户信息其中id和password字段不能为null值
	 */
	public void savePWD(User user);
	/**
	 * 修改当前用户的联系方式
	 * @param user 用户实例，不能为空，并且主键id不能为空，当联系方式为空时则会更新联系方式为null
	 */
	public void updateContactWay(User user);
}
