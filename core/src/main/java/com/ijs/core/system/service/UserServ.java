package com.ijs.core.system.service;

import com.ijs.core.base.model.User;
import com.ijs.core.base.service.GenericServ;
import com.ijs.core.common.service.SaveAccessory;
import com.ijs.core.exception.ServiceException;
import com.ijs.core.util.PageList;
import com.ijs.core.util.QueryParameters;

import java.util.List;

public interface UserServ extends GenericServ,SaveAccessory{
	
	/**
	 * 查询用户分页列表
	 * @param user 查询列表参数
	 * @param list 传入分页参数，并返回分页后的数据集合
	 */
	public void list(QueryParameters qp,User user,PageList list);
	
	/**
	 * 根据当前账号查询对象
	 * @param user 账号信息不能为空
	 * @return User 当前账号用户
	 */
	public User findByAccount(String account);
	/**
	 * 停用用户，停用用户后用户不能在登录到系统进行相关操作
	 * 1、修改用户状态
	 * @param user
	 */
	public void stopUser(User user);
	/**
	 * 启用用户
	 * 1、修改用户状态
	 * @param user
	 */
	public void startUser(User user);
	
	/**
	 * 验证帐号是否存在
	 * @param accountNo
	 * @return
	 */
	public boolean validateAccountNo(String accountNo);
	
	/**
	 * 企业重置密码 初始化为6个1
	 * @param user 用户实体类
	 * @param AccountNo 用户登陆账号不能为空
	 * @param id 主键id不能为空
	 * @return
	 */
	public String resetPwd(String uid);
	
	/**
	 * 根据企业ID 查询企业下所有用户
	 * @return
	 */
	public List<User> findByOwner(String owner);
	
	/**
	 * 验证原始密码是否一致
	 * @param user 修改的新老密码信息
	 * @return true 一致 
	 */
	public boolean checkPassword(User user);
	/**
	 * 验证指定用户是否存在，此方法可根据多个维度进行验证，如：用户的工号、手机号、电子邮箱等
	 * @param user 用户的实体对象，其要验证的相关字段不能为空
	 * @return 是否存在，不存在返回false
	 * @throws ServiceException 当存在是跑出异常，异常message为异常说明
	 */
	public boolean validateUser(User user)throws ServiceException;

	/**
	 * 1、保存微信用户
	 * @param user
	 */
	public void saveWxUser(User user);

}
