package com.ijs.mongo.service;

import java.util.List;
import java.util.Map;

import com.ijs.core.base.model.User;
import com.ijs.core.util.PageList;
import com.mongodb.DBObject;

public interface MongoService {
	/**
	 * 保存相关的用户的数据到指定的数据集中
	 * @param jsonObj 要保存的对象
	 * @param user 所属的用户
	 * @param to 要保存到的数据集中
	 */
	public DBObject save(Object saveObj, String to, User user);

	/**
	 * 根据查询条件查询相应的列表信息，并按分页进行相关的显示。
	 * @param queryObj 查询对象
	 * @param user 要查询的数据归属者
	 * @param pl 分页数据的容器
	 */
	public void query(Object queryObj,String from, User user, PageList pl);
	
	/**
	 * Old---Old---Old---Old---Old
	 */
	public void queryOld(Object queryObj,String from, User user, PageList pl);

	/**
	 * 获取单条数据信息
	 * @param key 主键key
	 * @param user 所属的用户
	 * @param from  从哪个数据集中获取
	 */
	public Object get(Object key, String from, User user);

	/**
	 * 更新对象信息
	 * @param updateObj 要更新的对象
	 * @param to 对象所在的数据集名称
	 * @param user 操作者
	 * @param excludes 排除掉的列，就是不参与更新的字段
	 * @return 返回受影响的行数
	 */
	public int update(Object queryObj,Object updateObj, String to, User user, List<String> excludes);
	/**
	 * 删除指定条件的数据
	 * @param queryWhere
	 * @param to
	 * @param user
	 */
	public int remove(Object queryWhere,String to,User user);
	/**
	 * 添加服务类的监听，从而扩展相关项目
	 * @param serviceListener
	 */
	public void addListener(ServiceListener serviceListener);
	/**
	 * 根据输入的信息查询指定的列表并返回，不进行分页操作
	 * @param queryObj
	 * @param from
	 * @param user
	 * @return
	 */
	public List<?> query(Object queryObj,String from, User user);
	
	/**
	 * 查询所有数据
	 * @param from
	 * @param user
	 * @return
	 */
	public List<?> queryAll(String from, User user);
	

	/**
	 * 统计所有数据
	 * @param from
	 * @param user
	 * @return
	 */
	public Map count(Object queryObj,String from, User user,String groups,String countCols);
	
	
}
