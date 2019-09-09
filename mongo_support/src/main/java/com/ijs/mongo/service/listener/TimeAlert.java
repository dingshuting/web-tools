package com.ijs.mongo.service.listener;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import com.ijs.core.base.model.ExtraData;
import com.ijs.core.base.model.ExtraDataCols;
import com.ijs.core.base.model.User;
import com.ijs.core.common.dao.GenericDao;
import com.ijs.core.exception.ServiceException;
import com.ijs.mongo.service.MongoServiceMessageListener;
import com.ijs.mongo.service.ServiceListener;
import com.mongodb.DBObject;

/**
 * 针对时间节点的提醒监听
 * 
 * @author Administrator
 */
@Service
@Order(3)
public class TimeAlert extends BaseServiceListener implements ServiceListener {
	private MongoServiceMessageListener msml;
	@Resource
	private GenericDao genericDao;

	public boolean doAction(ACTION action, User user, String collectionName, Object... args) throws ServiceException {
		// TODO Auto-generated method stub
		log.debug("执行针对时间节点的提醒监听器，监听对象为"+collectionName);
		if (msml != null) {
			ExtraData data = new ExtraData();
			data.setNameCode(collectionName);
			List<ExtraData> dd = genericDao.findByEntity(data);
			if (dd.size() > 0) {
				data = dd.get(0);
				ExtraDataCols col = new ExtraDataCols();
				col.setExtralDataId(data.getId());
				data.setCols(genericDao.findByEntity(col));
			}
			List<ExtraDataCols> list = data.getCols();
			for (ExtraDataCols col : list) {
				/*if (col.getColValType().equals(ExtraDataCols.COL_VAL_TYPE.DATE_TIME) && col.getIsAlert() == 1) {
					msml.sendMessage(args[0],render(col.getMessageTpl(),args[0]));
				}*/
			}
		}else{
			log.warn("消息监听器为空，无法触发时间字段的提醒功能");
		}
		return true;
	}
	/**
	 * 将消息模版渲染成字符串
	 * @param tpl 模版
	 * @param obj 当前的对象
	 * @return
	 */
	private String render(String tpl,Object obj){
		if(obj instanceof DBObject){
			for(String key:((DBObject)obj).keySet()){
				tpl=tpl.replaceAll("{"+key+"}", ((DBObject)obj).get(key).toString());
			}
		}
		return tpl;
	}
	
	public ACTION[] getAction() {
		// TODO Auto-generated method stub
		return new ACTION[] { ServiceListener.ACTION.AFTER_SAVE };
	}

	public MongoServiceMessageListener getMsml() {
		return msml;
	}

	public void setMsml(MongoServiceMessageListener msml) {
		this.msml = msml;
	}

}
