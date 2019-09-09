
package com.ijs.core.base.service;

import java.io.Serializable;
import java.util.List;

import com.ijs.core.base.listener.MysqlServiceListener;
import com.ijs.core.base.model.BaseModel;
import com.ijs.core.util.PageList;
import com.ijs.core.util.QueryParameters;


/**
 *  @author Tairong Zou
 *
 */
public interface GenericServ{
	
	<T>List<T> list(T entity);
	<T>T listOne(T entity);
	<T>List<T> list(T entity,boolean noLike);
	<T>T get(Class<T> t,Serializable id);
	<T>void save(T entity)throws Exception;
	<T>void update(T entity) throws Exception;
	/**
	 * 逻辑删除
	 * @param id
	 * @throws Exception
	 */
	<T> void remove(Class<T> t,Serializable id) throws Exception;
	<T> List<T> getAll(Class<T> t);
    /**
     * 物理删除
     * @param id
     * @throws Exception
     */
	<T> void delete(Class<T> t,Serializable id)throws Exception;
    
	<T extends BaseModel>void list(T t, PageList list);
	/**
	 * 变更实体的状态
	 * @param entity 主键标识id字段和status字段不能为空，如果没有status字段则会抛异常
	 * @throws Exception
	 */
	<T>void changeStatus(T entity)throws Exception;

	void addListener(MysqlServiceListener sl);
}
