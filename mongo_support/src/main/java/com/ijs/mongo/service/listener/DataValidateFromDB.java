package com.ijs.mongo.service.listener;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.ValidationException;

import org.json.JSONObject;

import com.ijs.core.base.model.ExtraData;
import com.ijs.core.base.model.ExtraDataCols;
import com.ijs.core.base.model.User;
import com.ijs.core.common.dao.GenericDao;
import com.ijs.core.exception.ServiceException;
import com.ijs.mongo.service.ServiceListener;
import com.ijs.mongo.validate.Validate;
/**
 * 针对MDB的数据入库前的验证类，验证逻辑分为2大步：<br/>
 * 1、从关系型数据空中的表结构配置，验证数据列的有效性（判断列是否属于业务逻辑，如果不属于将验证不通过）<br/>
 * 2、根据列的其它配置对列的值进行验证的检验
 * @author Administrator
 */
//@Service
public class DataValidateFromDB extends BaseServiceListener implements ServiceListener {
	
	@Resource
	private GenericDao genericDao;
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean doAction(ACTION action, User user, String collectionName, Object... args) throws ServiceException {
		// TODO Auto-generated method stub
		ExtraData data=new ExtraData();
		data.setNameCode(collectionName);
		List<ExtraData> dd=genericDao.findByEntity(data,true);
		if(dd.size()>0){
			data=dd.get(0);
			ExtraDataCols col=new ExtraDataCols();
			col.setExtralDataId(data.getId());
			data.setCols(genericDao.findByEntity(col));
		}
		List list=data.getCols();
		List errors=Validate.validate(new JSONObject(args[0].toString()),list,genericDao);
		if(errors.size()>1){
			System.out.println(Validate.getErrorString(errors));
			throw new ValidationException(Validate.getErrorString(errors));
		}else{
			return true;
		}
	}

	public ACTION[] getAction() {
		// TODO Auto-generated method stub
		return new ACTION[]{ServiceListener.ACTION.BEFOR_SAVE,ServiceListener.ACTION.BEFOR_UPDATE};
	}

}
