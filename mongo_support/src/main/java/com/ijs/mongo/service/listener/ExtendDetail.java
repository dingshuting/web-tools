package com.ijs.mongo.service.listener;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.http.client.utils.DateUtils;
import org.json.JSONObject;
import org.springframework.data.mongodb.util.DBObjectUtils;
import org.springframework.stereotype.Service;

import com.ijs.core.base.model.User;
import com.ijs.core.common.dao.GenericDao;
import com.ijs.core.exception.ServiceException;
import com.ijs.mongo.dao.MongoDao;
import com.ijs.mongo.model.BSONObjectUtil;
import com.ijs.mongo.service.MongoServiceMessageListener;
import com.ijs.mongo.service.ServiceListener;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 对获取详情的数据，进行扩展，扩展规则主要为扩展引用对象，并未实例赋值，
 * 如字段中包含了****Id,则代表引用字段，查询该数据并以****作为字段赋值。
 * @author Administrator
 */
public class ExtendDetail extends BaseServiceListener {
	@Resource
	private GenericDao genericDao;
	
	@Resource
	private MongoDao mongoDao;

	public boolean doAction(ACTION action, User user, String collectionName, Object... args) throws ServiceException {
		// TODO Auto-generated method stub
		DBObject data=(DBObject) args[0];
		if(data==null||data.keySet()==null||data.keySet().size()<1){
			return true;
		}
		Object[] keys=data.keySet().toArray();
		for(int i=0;i<keys.length;i++){
			String key=(String) keys[i];
			if(key.endsWith("Id")){
				String collName=key.substring(0, key.lastIndexOf("Id"));
				switch (action) {
				case AFTER_GET:
					if(collName.equals("user")){
						data.put(collName,BSONObjectUtil.getDBObject(new JSONObject(genericDao.get(User.class, data.get(key).toString()))));
					}else{
						data.put(collName,mongoDao.get(collName, data.get(key)));
					}
					break;
				case BEFOR_SAVE:
					if(data.containsField(collName)&&!collName.equals("user")){
						data.removeField(collName);
					}
					break;
				case BEFOR_UPDATE:
					if(data.containsField(collName)&&!collName.equals("user")){
						data.removeField(collName);
					}
					break;	
				default:
					break;
				}
			}
		}
		return true;
	}
	public ACTION[] getAction() {
		// TODO Auto-generated method stub
		return new ACTION[] { ServiceListener.ACTION.AFTER_GET};
	}

}
