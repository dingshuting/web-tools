package com.ijs.mongo.service.listener;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.http.client.utils.DateUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import com.ijs.core.base.model.User;
import com.ijs.core.common.dao.GenericDao;
import com.ijs.core.exception.ServiceException;
import com.ijs.mongo.dao.MongoDao;
import com.ijs.mongo.service.MongoServiceMessageListener;
import com.ijs.mongo.service.ServiceListener;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 针对时间节点的提醒监听
 * @author Administrator
 */
@Service
@Order(2)
public class LogListener extends BaseServiceListener implements ServiceListener {
	private MongoServiceMessageListener msml;
	@Resource
	private GenericDao genericDao;
	
	@Resource
	private MongoDao mongoDao;

	public boolean doAction(ACTION action, User user, String collectionName, Object... args) throws ServiceException {
		// TODO Auto-generated method stub
		
		BasicDBObject dbo = new BasicDBObject();
		if(user!=null){
			dbo.put("operationId",user.getId().toString());
			dbo.put("operationName",user.getName());
			dbo.put("operationAccount",user.getAccountNo());
			dbo.put("operationPhone",user.getMobilePhone());
		}
		
		dbo.put("operationTime",DateUtils.formatDate(new Date(),"yyyy-MM-dd hh:mm:ss"));
		dbo.put("operationDb",collectionName);
		if( ServiceListener.ACTION.AFTER_SAVE.equals(action)){
			dbo.put("execute","保存");
			dbo.put("data", ((DBObject)args[0]).get("_id").toString());
		}else if(ServiceListener.ACTION.BEFOR_DELETE.equals(action)){
			dbo.put("data", args[0].toString());
			dbo.put("execute","删除");
		}else if(ServiceListener.ACTION.AFTER_UPDATE.equals(action)){
			dbo.put("execute","修改");
			dbo.put("data", ((DBObject)args[1]).get("id").toString());
		}else if(ServiceListener.ACTION.AFTER_QUERY.equals(action)){
			dbo.put("execute","查询");
			dbo.put("data", args[0].toString());
		}
		mongoDao.save(dbo, "sys_log");
		return true;
	}
	
	public ACTION[] getAction() {
		// TODO Auto-generated method stub
		return new ACTION[] { ServiceListener.ACTION.AFTER_SAVE,ServiceListener.ACTION.AFTER_UPDATE,ServiceListener.ACTION.BEFOR_DELETE};
	}

	public MongoServiceMessageListener getMsml() {
		return msml;
	}

	public void setMsml(MongoServiceMessageListener msml) {
		this.msml = msml;
	}

}
