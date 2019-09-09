/**
 * 
 */
package com.ijs.core.base.service.impl;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.rowset.serial.SerialException;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.ijs.core.base.Constants;
import com.ijs.core.base.listener.MysqlServiceListener;
import com.ijs.core.base.listener.MysqlServiceListener.ACTION;
import com.ijs.core.base.model.BaseModel;
import com.ijs.core.base.model.User;
import com.ijs.core.base.service.GenericServ;
import com.ijs.core.common.dao.GenericDao;
import com.ijs.core.exception.ServiceException;
import com.ijs.core.util.IdGen;
import com.ijs.core.util.PageList;
import com.ijs.core.util.QueryParameters;
import com.ijs.core.util.model.JpqlModel;

/**
 * @author Tairong
 *
 */
@Service("genericServ")
public class GenericServImpl implements GenericServ {
	protected Logger log = org.apache.log4j.LogManager.getLogger(this.getClass());
	// 时间字段名称，用于list查询和排序
	private Map<ACTION, List<MysqlServiceListener>> listeners = new HashMap<ACTION, List<MysqlServiceListener>>();
	private final static String[] timeField = new String[] { "createTime", "addTime" };
	@Resource
	protected GenericDao dao;
	/**
	 * 查询list的公共方法，根据实体来进行查询，其中自动根据时间字段进行排序
	 */
	@Override
	public <T extends BaseModel> void list(T item, PageList list) {
		// TODO Auto-generated method stub
		try {
			if(item!=null&&item.getQp()!=null&&item.getQp().getCount()>0) {
				list.setPerPageSize(item.getQp().getCount());
			}
			JpqlModel jpql= dao.generateJpql(item, null,null,null);
			executeAction(ACTION.BEFOR_QUERY,item.getClass(), item, list,jpql);
			log.debug("execute the method<list> of 'GenericServImpl' and the jpql :" + jpql.toString());
			List<?> objs=dao.find(jpql.toString(), list.getCurrentPage(), list.getPerPageSize());
			list.setList(tidyResult(objs,item));
			list.setTotalSize(dao.count("*", jpql.toString()));
			executeAction(ACTION.AFTER_QUERY,item.getClass(), item, list);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
	}
	/**
	 * 整理结果，用于联表查询时返回多个数据，进行数据合并，不支持同时用
	 * @param res 返回的结果数据
	 * @param t 返回的主类类型
	 * @return 返回一个t类型的列表，其中子类将字符赋值给主类中对应的子类变量
	 * @throws Exception
	 */
	private <T>List<T> tidyResult(List<?> res,T t) throws Exception{
		List<T> result=new ArrayList<>();
		for(Object obj:res) {
			if(obj instanceof Object[]) {
				Object mc=null;
				for(Object subObj:(Object[])obj) {
					if(subObj.getClass().equals(t.getClass())){
						mc=subObj;
						break;
					}
				}
				for(Object subObj:(Object[])obj) {
					PropertyDescriptor[] pds=BeanUtils.getPropertyDescriptors(mc.getClass());
					for(PropertyDescriptor pd:pds) {
						if(pd.getPropertyType().equals(subObj.getClass())) {
							pd.getWriteMethod().invoke(mc, subObj);
						}
					}
				}
				result.add((T) mc);
			}
		}
		if(result.size()>0) {
			return result;
		}else {
			return (List<T>) res;
		}
		
	}

	@Override
	public <T> List<T> list(T entity) {
		// TODO Auto-generated method stub
		return dao.findByEntity(entity);
	}
	
	@Override
	public <T> void update(T entity) throws Exception {
		// TODO Auto-generated method stub
		executeAction(ACTION.BEFOR_UPDATE,entity.getClass(), entity);
		dao.update(entity);
		executeAction(ACTION.AFTER_UPDATE, entity.getClass(),entity);
	}

	@Override
	public <T> void remove(Class<T> t, Serializable id) throws Exception {
		// TODO Auto-generated method stub
		executeAction(ACTION.BEFOR_DELETE,t, id);
		dao.remove(t, id);
	}

	@Override
	public <T> List<T> getAll(Class<T> t) {
		// TODO Auto-generated method stub
		log.warn("基础服务类不提供getAll方法的实现");
		return dao.getAll(t);
	}

	@Override
	public <T> T get(Class<T> t, Serializable id) {
		// TODO Auto-generated method stub
		try {
			
			T obj;
			obj = dao.get(t, id);
			executeAction(ACTION.AFTER_GET,t, obj);
			return obj;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("获取详情错误"+t.getName(),e);
			//e.printStackTrace();
		}
		return null;
	}

	@Override
	public <T> void save(T entity) throws Exception {
		// TODO Auto-generated method stub
		executeAction(ACTION.BEFOR_SAVE,entity.getClass(), entity);
		PropertyDescriptor pd = new PropertyDescriptor(Constants.MODEL_ID_KEY, entity.getClass());
		
		if(pd.getReadMethod().getReturnType().getSimpleName().equals(String.class.getSimpleName())&&pd.getReadMethod().invoke(entity)==null) {
			pd.getWriteMethod().invoke(entity, IdGen.guid());
		}
		dao.save(entity);
		executeAction(ACTION.AFTER_SAVE,entity.getClass(), entity);
	}

	@Override
	public <T> void delete(Class<T> t, Serializable id) throws Exception { 
		// TODO Auto-generated method stub
		log.warn("基础服务类不提供delete方法的实现,直接调用GenericDao的方法");
		dao.remove(t, id);
	}

	@Override
	public <T> void changeStatus(T t) throws Exception {
		// TODO Auto-generated method stub
		try {
			StringBuffer jpql = new StringBuffer("update ");
			Field fstatus=null;
			Field fid=null;
			fstatus = t.getClass().getDeclaredField("status");
			if(fstatus==null) {
			 fstatus = t.getClass().getSuperclass().getDeclaredField("status");
			}
			 fid = t.getClass().getDeclaredField("id");
			if(fstatus==null) {
				 fid = t.getClass().getSuperclass().getDeclaredField("id");
			}
			
			Field.setAccessible(new Field[] { fstatus, fid }, true);
			PropertyDescriptor pds = new PropertyDescriptor(fstatus.getName(), t.getClass());
			Method getStatus = pds.getReadMethod();// 获得get方法
			PropertyDescriptor pdi = new PropertyDescriptor(fid.getName(), t.getClass());
			Method getId = pdi.getReadMethod();// 获得get方法
			jpql.append(t.getClass().getSimpleName());
			jpql.append(" set status=?").append(" where id=?");
			log.debug("-----status:"+getStatus.invoke(t));
			log.debug("-----id:"+getId.invoke(t));
			dao.executeJPQL(jpql.toString(), new Object[] { getStatus.invoke(t), getId.invoke(t) });
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new SerialException(e.getMessage());
		}
	}

	@Override
	public void addListener(MysqlServiceListener serviceListener) {
		// TODO Auto-generated method stub
		if (serviceListener != null) {
			for (ACTION i : serviceListener.getAction()) {
				if (listeners.get(i) == null) {
					List<MysqlServiceListener> ls = new ArrayList<MysqlServiceListener>();
					ls.add(serviceListener);
					if (listeners.get(i) == null) {
						listeners.put(i, ls);
					}
				} else {
					listeners.get(i).add(serviceListener);
				}
			}
		}
	}

	// 执行action动作
	private void executeAction(ACTION action,Class t, Object... args) {
		List<MysqlServiceListener> lis = listeners.get(action);
		if (lis != null) {
			for (MysqlServiceListener li : lis) {
				if (li.getClassExOfService()!=null&&t.getName().matches(li.getClassExOfService())&&!li.doAction(action,t, args)) {
					throw new ServiceException("the action-chain was broken on " + li.getClass().getSimpleName());
				}
			}
		}
	}

	@Override
	public <T> List<T> list(T entity, boolean noLike) {
		// TODO Auto-generated method stub
		return dao.findByEntity(entity, noLike);
	}
	@Override
	public <T> T listOne(T entity) {
		// TODO Auto-generated method stub
		
		return null;
	}
	public static void main(String[] args) {
		System.out.println(BeanUtils.getPropertyDescriptor(User.class, "title").getName());
	}

}
