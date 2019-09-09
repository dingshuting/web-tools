package com.ijs.mongo.service.impl;

import java.sql.Timestamp;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.utils.DateUtils;

import com.ijs.core.base.listener.MysqlBaseServiceListener;
import com.ijs.core.base.model.ExtraData;
import com.ijs.core.exception.ServiceException;
import com.ijs.mongo.dao.MongoDao;
import com.mongodb.BasicDBObject;

/**
 * 针对数据表ExtraData的基本功能扩展，此服务用于保存ExtraData的子列信息
 * @author Administrator
 *
 */
//@Service
public class MysqlLog extends  MysqlBaseServiceListener{
	protected final transient Log log = LogFactory.getLog(getClass());
	@Resource
	private MongoDao mongoDao;
	public boolean doAction(ACTION action,Class t, Object... args) throws ServiceException {
	
		// TODO Auto-generated method stub
		log.debug("execute method to save the cols of ExtraData in ExtraDataServ");
		
		if((args.length>0&&args[0] instanceof ExtraData) || args[1] instanceof ExtraData){
			switch (action) {
			case AFTER_SAVE:
			case AFTER_QUERY:
			case AFTER_UPDATE:
				BasicDBObject dbo = new BasicDBObject();
				
			/*	if(user!=null){
					dbo.put("operationId",user.getId().toString());
					dbo.put("operationName",user.getName());
					dbo.put("operationAccount",user.getAccountNo());
					dbo.put("operationPhone",user.getMobilePhone());
				}
				*/
				dbo.put("operationTime",DateUtils.formatDate(new Date(),"yyyy-MM-dd hh:mm:ss"));
				/*dbo.put("operationDb",collectionName);*/
				if( ACTION.AFTER_SAVE.equals(action)){
					dbo.put("execute","保存");
				}else if(ACTION.AFTER_DELETE.equals(action)){
					dbo.put("execute","删除");
				}else if(ACTION.AFTER_UPDATE.equals(action)){
					dbo.put("execute","修改");
				}else if(ACTION.AFTER_QUERY.equals(action)){
					dbo.put("execute","查询");
				}
				mongoDao.save(dbo, "sys_log");
				break;
			case BEFOR_SAVE:
				((ExtraData)args[0]).setCreateTime(new Timestamp(new Date().getTime()));
				break;
			default:
				break;
			}
		}
		return true;
	}
	
	
	
	public ACTION[] getAction() {
		// TODO Auto-generated method stub
		return new ACTION[]{ACTION.AFTER_SAVE,ACTION.AFTER_UPDATE,ACTION.BEFOR_SAVE,ACTION.AFTER_QUERY};
	}
	
}
