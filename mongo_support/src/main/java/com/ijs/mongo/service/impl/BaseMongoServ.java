package com.ijs.mongo.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.ijs.core.base.control.BaseControl;
import com.ijs.core.base.model.User;
import com.ijs.core.exception.ServiceException;
import com.ijs.core.util.PageList;
import com.ijs.mongo.MongoConfig;
import com.ijs.mongo.dao.MongoDao;
import com.ijs.mongo.model.BSONObjectUtil;
import com.ijs.mongo.service.MongoService;
import com.ijs.mongo.service.ServiceListener;
import com.ijs.mongo.service.ServiceListener.ACTION;
import com.ijs.mongo.service.listener.AddDefaultFields;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

/**
 * 基础实现类，整个过程均采用JSON格式进行相关的保存
 * 
 * @author Administrator
 *
 */
@Service
public class BaseMongoServ implements MongoService {
	private Map<ACTION, List<ServiceListener>> listeners = new HashMap<ACTION, List<ServiceListener>>();
	protected final transient Log log = LogFactory.getLog(getClass());
	@Resource
	private MongoDao mongoDao;
	public DBObject save(Object saveObj, String to, User user) {
		// TODO Auto-generated method stub
		if (saveObj != null && to != null && !to.isEmpty()) {
			JSONObject obj = converToJson(saveObj);
			if (obj.has("id") && !obj.get("id").equals("")) {
				update("{_id:'" + obj.get("id") + "'}", saveObj, to, user, new ArrayList<String>());
				return null;
			}
			executeAction(ServiceListener.ACTION.BEFOR_SAVE, user, to, obj);
			DBObject so = BSONObjectUtil.getDBObject(obj);
			mongoDao.save(so, to);
			executeAction(ServiceListener.ACTION.AFTER_SAVE, user, to, so);
			return so;
		} else {
			throw new ServiceException("The args 'saveObj' or 'to' can not be null!");
		}

	}

	// 执行action动作
	private void executeAction(ACTION action, User user, String collName, Object... args) {
		List<ServiceListener> lis = listeners.get(action);
		if (lis != null) {
			for (ServiceListener li : lis) {
				if (!li.doAction(action, user, collName, args)) {
					throw new ServiceException("the action-chain was broken on " + li.getClass().getSimpleName());
				}
			}
		}
	}

	// 将string转换成json对象
	public JSONObject converToJson(Object obj) {
		JSONObject pojo;
		if (obj != null) {
			if (obj instanceof String) {
				pojo = new JSONObject(obj.toString());
				{
				}
			} else if (obj instanceof JSONObject) {
				pojo = (JSONObject) obj;
			} else {
				throw new IllegalArgumentException(
						"there was a unknow argument is saveObj that type can be String and JSONObject");
			}
			return pojo;
		} else {
			return null;
		}
	}
	
	/**
	 * Old---Old---Old---Old---Old
	 */
	public JSONObject converToJsonOld(Object obj) {
		JSONObject pojo;
		if (obj != null) {
			if (obj instanceof String) {
				pojo = new JSONObject(obj.toString());{}
			} else if (obj instanceof JSONObject) {
				pojo = (JSONObject) obj;
			} else if (obj instanceof Map<?, ?>) {
				pojo = new JSONObject((Map<?, ?>)obj);
			} else if (obj instanceof Collection<?>) {
				pojo = new JSONObject((Collection<?>)obj);
			} else {
				throw new IllegalArgumentException(
						"there was a unknow argument is saveObj that type can be String and JSONObject");
			}
			return pojo;
		} else {
			return null;
		}
	}

	public void query(Object queryObj, String from, User user, PageList pl) {
		// TODO Auto-generated method stub
		Query query = new Query();
		if (queryObj != null && !queryObj.toString().isEmpty() && !queryObj.equals("{}") && !queryObj.equals("null")) {
			Criteria criteria = mongoDao.generateQuery(null, converToJson(queryObj));
			if (criteria != null)
				query.addCriteria(criteria);
		}
		executeAction(ServiceListener.ACTION.BEFOR_QUERY, user, from, query, pl);
		pl.setList(BSONObjectUtil.getJsonSimples(mongoDao.find(query, from, pl.getCurrentPage(), pl.getPerPageSize())));
		pl.setTotalSize((int) mongoDao.count(query, from));
		executeAction(ServiceListener.ACTION.AFTER_QUERY, user, from, query, pl);
	}
	
	public Map count(Object queryObj, String from, User user,String groups,String countCols){
		Query query = new Query();
		Criteria criteria = new Criteria();
		if (queryObj != null && !queryObj.toString().isEmpty() && !queryObj.equals("{}") && !queryObj.equals("null")) {
			criteria = mongoDao.generateQuery(null, converToJson(queryObj));
			if (criteria != null)
				query.addCriteria(criteria);
		}
		executeAction(ServiceListener.ACTION.BEFOR_QUERY, user, from, query);
		Map total = mongoDao.count(criteria,from,groups,countCols);
		executeAction(ServiceListener.ACTION.AFTER_QUERY, user, from, query);
		return total;
	}
	
	/**
	 * Old---Old---Old---Old---Old
	 */
	public void queryOld(Object queryObj, String from, User user, PageList pl) {
		// TODO Auto-generated method stub
		Query query = new Query();
		if (queryObj != null &&!queryObj.toString().isEmpty() && !queryObj.equals("{}") && !queryObj.equals("null") && !queryObj.toString().equals("{}")) {
			Criteria criteria  = mongoDao.generateQuery(null, converToJsonOld(queryObj));
			if(criteria != null)
			query.addCriteria(criteria);
		}
		executeAction(ServiceListener.ACTION.BEFOR_QUERY, user, from, query, pl);
		query.with(new Sort(Direction.DESC,AddDefaultFields.CREATE_TIME));
		pl.setList(BSONObjectUtil.getJsonSimples(mongoDao.find(query, from, pl.getCurrentPage(), pl.getPerPageSize())));
		pl.setTotalSize((int) mongoDao.count(query, from));
		executeAction(ServiceListener.ACTION.AFTER_QUERY, user, from, query, pl);
	}

	public List<?> query(Object queryObj, String from, User user) {
		// TODO Auto-generated method stub
		PageList pl = new PageList();
		pl.setPerPageSize(200);
		pl.setCurrentPage(1);
		query(queryObj, from, user, pl);
		return pl.getList();
	}

	public Object get(Object key, String from, User user) {
		// TODO Auto-generated method stub
		DBObject obj = mongoDao.get(from, key);
		executeAction(ServiceListener.ACTION.AFTER_GET, user, from, obj);
		return BSONObjectUtil.getJsonSimple(obj);
	}

	public int update(Object queryObj, Object updateObj, String to, User user, List<String> excludes) {
		// TODO Auto-generated method stub
		if (queryObj != null && updateObj != null) {
			DBObject dbo = null;
			if (updateObj instanceof DBObject) {
				dbo = (DBObject) updateObj;
			}
			if (dbo == null) {
				JSONObject obj = converToJson(updateObj);
				dbo = BSONObjectUtil.getDBObject(obj);
			}
			Query query = new Query();
			/*
			 * if(dbo.containsField(MongoConfig._ID)){
			 * query.addCriteria(Criteria.where(MongoConfig._ID).is(dbo.get(
			 * MongoConfig._ID).toString()));
			 * 
			 * }else{
			 */
			Criteria cri = mongoDao.generateQuery(null, converToJson(queryObj));
			if (cri != null) {
				query.addCriteria(cri);
				dbo.removeField(MongoConfig._ID);
			} else {
				throw new ServiceException("the queryObj cann't be null, when execute the update");
			}
			// }
			// executeAction(ServiceListener.ACTION.BEFOR_QUERY, user, from,
			// query, pl);
			executeAction(ServiceListener.ACTION.BEFOR_UPDATE, user, to, query, dbo, excludes);
			int er = mongoDao.update(query, to, dbo, excludes.toArray(new String[0]));
			executeAction(ServiceListener.ACTION.AFTER_UPDATE, user, to, query, dbo, excludes);
			return er;
		} else {
			throw new ServiceException(
					"The args 'queryObj' or 'updateObj' was null, both of them cann't be null when execute the update method");
		}

	}

	public int remove(Object queryWhere, String to, User user) {
		// TODO Auto-generated method stub
		if (queryWhere != null && to != null) {
			Query query = new Query(mongoDao.generateQuery(null, converToJson(queryWhere)));
			executeAction(ServiceListener.ACTION.BEFOR_DELETE, user, to, query);
			WriteResult er = mongoDao.remove(query, to);
			executeAction(ServiceListener.ACTION.AFTER_DELETE, user, to, query);
			return er.getN();
		} else {
			return 0;
		}
	}

	public void addListener(ServiceListener serviceListener) {
		// TODO Auto-generated method stub
		if (serviceListener != null) {
			for (ACTION i : serviceListener.getAction()) {
				if (listeners.get(i) == null) {
					List<ServiceListener> ls = new ArrayList<ServiceListener>();
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

	public List<?> queryAll(String from, User user) {
		executeAction(ServiceListener.ACTION.BEFOR_QUERY, user, from, null,null);
		List<?> list = BSONObjectUtil.getJsonSimples(mongoDao.getAll(from));
		executeAction(ServiceListener.ACTION.AFTER_QUERY, user, from, null,null);
		return list;
	}

}
