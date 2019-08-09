/**
 * 
 */
package com.ijs.core.system.service;

import java.util.List;

import com.ijs.core.base.model.Func;
import com.ijs.core.base.model.Role;
import com.ijs.core.base.service.GenericServ;


/**
 *提供角色的基本功能定义
 */
public interface RoleServ extends GenericServ{
	/**
	 * 验证部门下的角色名称是否重复
	 * @param name 角色名称
	 * @param depId 部门id
	 * @return true|false
	 */
	public boolean validateName(String name,String depId);
	

	/**
	 * 判断该角色是否已有用户使用，若有则不能删除角色
	 * @param rid 角色主键id
	 * @return true代表拥有用户
	 */
	public boolean hasUser(String rid);
	
	public List<Func> getFunc(Role role);

}
