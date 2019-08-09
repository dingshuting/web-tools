package com.ijs.core.base.listener;

import com.ijs.core.base.model.User;
import com.ijs.core.exception.ServiceException;
/**
 * 服务类的监听接口，用于扩展基础的数据服务层的功能，其相当于一个简单的AOP配置，此接口专门针对MySql设置
 * @author Administrator
 *
 */
public interface MysqlServiceListener {
	/**
	 * 触发动作的类型
	 * @author Administrator
	 *
	 */
	public static enum ACTION{
		BEFOR_SAVE,
		AFTER_SAVE,
		BEFOR_UPDATE,
		AFTER_UPDATE,
		AFTER_GET,
		BEFOR_DELETE,
		AFTER_DELETE,
		BEFOR_QUERY,
		AFTER_QUERY
	}
	/**
	 * 触发监听的动作
	 * @param args 在业务中的相关的方法参数,其顺序遵从调用service方法的传出参数顺序
	 * @param user  当前的操作者用户
	 * @param action  当前的触发的动作
	 * @param action  当前业务操作的集合名称
	 * @return 是否继续下一步动作
	 */
	public boolean doAction(ACTION action,Class t,Object... args) throws ServiceException;
	/**
	 * 获取当前监听的动作标识，标识参考接口中定义的常量
	 * @return
	 */
	public ACTION[] getAction();

}
