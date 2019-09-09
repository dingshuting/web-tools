package com.ijs.mongo.service;

import com.ijs.core.base.model.User;
import com.ijs.core.exception.ServiceException;
/**
 * 服务类的监听接口，用于扩展基础的数据服务层的功能，其相当于一个简单的AOP配置<br/>
 * 此接口仅仅针对项目中MongoDB的数据服务
 * @author Administrator
 *
 */
public interface ServiceListener {
	public static enum ACTION{
		/**
		 * 保存前执行的方法标记，参数如下：<br/>
		 * JSONObject-要保存到数据库的对象
		 */
		BEFOR_SAVE,
		/**
		 * 保存后执行的方法标记，参数如下：<br/>
		 * DBObject-已经保存到数据库的数据，注意与保存前的参数不一致
		 */
		AFTER_SAVE,
		/**
		 * 执行更新前执行的方法标记，其参数顺序如下：<br/>
		 * 1、Query, 要更新对象的查询条件<br/> 
		 * 2、DBObject, 实际要更新的参数对象<br/>
		 * 3、List<String>,要排除的更新字段
		 */
		BEFOR_UPDATE,
		/**
		 * 执行更新前执行的方法标记，其参数顺序如下：<br/>
		 * 1、Query, 要更新对象的查询条件<br/> 
		 * 2、DBObject, 实际要更新的参数对象<br/>
		 * 3、List<String>,要排除的更新字段
		 */
		AFTER_UPDATE,
		/**
		 * 执行获取详情方法（get)后的执行标记，参数顺序为，单个查询后的数据对象
		 */
		AFTER_GET,
		/**
		 * 执行删除前执行的方法，参数如下：<br/>
		 * 1、 Query, 执行删除的查询条件<br/>
		 */
		BEFOR_DELETE,
		/**
		 * 执行删除前执行的方法，参数如下：<br/>
		 * 1、 Query, 执行删除的查询条件<br/>
		 */
		AFTER_DELETE,
		/**
		 * 查询方法的参数，参数顺序为，Query，PageList<br/>
		 * 统计方法的参数，参数为当个Query，无分页
		 */
		BEFOR_QUERY,
		/**
		 * 查询方法的参数，参数顺序为，Query，PageList<br/>
		 * 统计方法的参数，参数为当个Query，无分页
		 */
		AFTER_QUERY
	}
	/**
	 * 触发监听的动作，默认的前三项参数所有的调用者均遵从该规则，后面采用可扩展参数，根据具体调用者的参数而定
	 * @param args 在业务中的相关的方法参数,其顺序遵从调用service方法的传出参数顺序
	 * @param user  当前的操作者用户
	 * @param action  当前的触发的动作
	 * @param action  当前业务操作的集合名称
	 * @return 是否继续下一步动作
	 */
	public boolean doAction(ACTION action,User user,String collectionName,Object... args) throws ServiceException;
	/**
	 * 获取当前监听的动作标识，标识参考接口中定义的常量，当该接口的实现类标记某个标识时，在该标识对应的动作发生后将自动调用对应的doAction方法
	 * @return
	 */
	public ACTION[] getAction();
}
