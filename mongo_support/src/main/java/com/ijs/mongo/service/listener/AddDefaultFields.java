package com.ijs.mongo.service.listener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.validation.ValidationException;

import org.json.JSONObject;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.ijs.core.base.Constants;
import com.ijs.core.base.model.ExtraData;
import com.ijs.core.base.model.ExtraDataCols;
import com.ijs.core.base.model.User;
import com.ijs.core.common.dao.GenericDao;
import com.ijs.core.exception.ServiceException;
import com.ijs.mongo.MongoConfig;
import com.ijs.mongo.service.ServiceListener;
import com.mongodb.DBObject;
/**
 * 针对MDB的数据入库前的验证类，验证逻辑分为2大步：<br/>
 * 1、从关系型数据空中的表结构配置，验证数据列的有效性（判断列是否属于业务逻辑，如果不属于将验证不通过）<br/>
 * 2、根据列的其它配置对列的值进行验证的检验
 * @author Administrator
 */
@Service
@Order(1)
public class AddDefaultFields extends BaseServiceListener implements ServiceListener {
	public static final String CREATE_TIME="create_time";
	public static final String OWNER="owner";
	public static final String USER_ID="userId";
	public static final String NAME="name";
	public static final String ID="id";
	public static final String USER="user";
	public static final String ADD_USER="add_user";
	public static final String STATUS="status";
	public static final String UPDATE_TIME="update_time";
	public static final String UPDATE_USER="update_user";
	public static final String ISPASS="isPass";
	@Resource
	private GenericDao genericDao;
	public boolean doAction(ACTION action, User user, String collectionName, Object... args) throws ServiceException {
		ExtraData data=new ExtraData();
		data.setNameCode(collectionName);
		List<ExtraData> dd=genericDao.findByEntity(data,true);
		if(dd.size()<1){
			throw new ValidationException("collectionName 数据异常，未找到对应的数据表");
		}
		if(dd.size()>0){
			data=dd.get(0);
			ExtraDataCols cols=new ExtraDataCols();
			cols.setExtralDataId(data.getId());
			data.setCols(genericDao.findByEntity(cols));
		}
		
		switch (action) {
		
		case BEFOR_SAVE : 
			JSONObject obj=(JSONObject) args[0];
			obj.put(CREATE_TIME, new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date()));
			//判断是否审核
			if(!obj.has(STATUS))
				obj.put(STATUS, MongoConfig.Status.NORMAL);
			if(data.getIsAuditing()==1){
				obj.put(ISPASS, MongoConfig.IsPass.WATING_VERIFY);
			}
			if(user!=null&&!obj.has(USER)){
				JSONObject u=new JSONObject();
				u.put(ID, user.getId());
				u.put(NAME, user.getName());
				u.put(OWNER, user.getOwner());
				obj.put(USER,u);
				obj.put(OWNER,user.getOwner());
			}
			break;
		case BEFOR_UPDATE:
			DBObject obj1=(DBObject) args[1];
			obj1.put(UPDATE_TIME, new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date()));
			obj1.put(UPDATE_USER, user!=null?user.getAccountNo():null);
			break;
		case BEFOR_QUERY:
			Query query=(Query) args[0];
			if(query==null)return true;
			if(null != user && !Constants.SA_ID.equals(user.getOwner())){//排除超级管理员sa
				if(user.getOwner()==null){
					throw new ValidationException("尚未进行认证");
				}
			}
			//过滤大字段，否则在list查询时将验证影响效率
			query.with(new Sort(Direction.DESC,CREATE_TIME));
			for(ExtraDataCols col:data.getCols()){
				if(col.getColValType().equals("h")||col.getColValType().equals("text")||col.getColValType().equals("bl")||col.getColValType().equals("ol")){
					query.fields().exclude(col.getColCode());
				}
			}
			break;
		default:
			break;
		}
		return true;
		// TODO Auto-generated method stub
		
	}

	public ACTION[] getAction() {
		// TODO Auto-generated method stub
		return new ACTION[]{ACTION.BEFOR_SAVE,ACTION.BEFOR_UPDATE,ACTION.BEFOR_QUERY};
	}

}
