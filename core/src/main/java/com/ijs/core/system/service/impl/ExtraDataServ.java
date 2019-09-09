package com.ijs.core.system.service.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.ijs.core.base.listener.MysqlBaseServiceListener;
import com.ijs.core.base.model.ExtraData;
import com.ijs.core.base.model.ExtraDataCols;
import com.ijs.core.base.model.Func;
import com.ijs.core.exception.ServiceException;

/**
 * 针对数据表ExtraData的基本功能扩展，此服务用于保存ExtraData的子列信息
 * @author Administrator
 *
 */
@Service
public class ExtraDataServ extends  MysqlBaseServiceListener{
	protected final transient Log log = LogFactory.getLog(getClass());
	public boolean doAction(ACTION action,Class t, Object... args) throws ServiceException {
		// TODO Auto-generated method stub
		log.debug("execute method to save the cols of ExtraData in ExtraDataServ");
			switch (action) {
			case AFTER_SAVE:
				saveChildCols((ExtraData) args[0]);
				saveFunc((ExtraData) args[0]);
				break;
			case AFTER_UPDATE:
				updateChildCols((ExtraData) args[0]);
				updateFunc((ExtraData) args[0]);
				break;
			case BEFOR_SAVE:
				((ExtraData)args[0]).setCreateTime(new Timestamp(new Date().getTime()));
				break;
			default:
				break;
			}
		return true;
	}
	//修改列值
	private void updateChildCols(ExtraData ed){
		delete(ed);
		for(ExtraDataCols edc:ed.getCols()){
			edc.setId(UUID.randomUUID().toString());
			edc.setExtralDataId(ed.getId());
			dao.save(edc);
			log.debug("data<"+edc.toString()+"> has saved");
		}
	}
	//修改功能表功能名字
	public void updateFunc(ExtraData extraData){
		List<Func> funcs= dao.find("from Func f where f.extraDataId = '"+extraData.getId()+"' and f.parentId = '10000000'");
		if(funcs.size()>0){
			funcs.get(0).setName(extraData.getName()+"管理");
		}
	}
	//保存列的值
	private void saveChildCols(ExtraData ed){
		for(ExtraDataCols edc:ed.getCols()){
			edc.setId(UUID.randomUUID().toString());
			edc.setExtralDataId(ed.getId());
			dao.save(edc);
			log.debug("data<"+edc.toString()+"> has saved");
		}
	}
	//创建保存当前表，列表，修改，新增，删除，详情功能
	private void saveFunc(ExtraData ed){
		//列表管理
			Func func = new Func();
			func.setId(UUID.randomUUID().toString());
			func.setParentId("10000000");
			func.setTypes(1);
			func.setName(ed.getName()+"管理");
			func.setLevel(2);
			func.setDisplayType(1);
			func.setTogo("common.common");
			func.setStatus(1);
			func.setIcon("");
			func.setFnOr(1);
			func.setFnDesc("1");
			func.setCreateTime(new Date());
			func.setUrl("/"+ed.getNameCodeAlias()+"/list");
			func.setExtraDataId(ed.getId());
			dao.save(func);
		//删除按钮
			Func deleteFunc = new Func();
			deleteFunc.setId(UUID.randomUUID().toString());
			deleteFunc.setParentId(func.getId());
			deleteFunc.setTypes(1);
			deleteFunc.setName("删除");
			deleteFunc.setCreateTime(new Date());
			deleteFunc.setLevel(3);
			deleteFunc.setDisplayType(3);
			deleteFunc.setTogo("remove");
			deleteFunc.setStatus(1);
			deleteFunc.setIcon("");
			deleteFunc.setFnOr(1);
			deleteFunc.setFnDesc("1");
			deleteFunc.setUrl("/"+ed.getNameCodeAlias()+"/remove");
			deleteFunc.setExtraDataId(ed.getId());
			dao.save(deleteFunc);
		//修改按钮
			Func updateFunc = new Func();
			updateFunc.setId(UUID.randomUUID().toString());
			updateFunc.setParentId(func.getId());
			updateFunc.setTypes(1);
			updateFunc.setName("修改");
			updateFunc.setCreateTime(new Date());
			updateFunc.setLevel(3);
			updateFunc.setDisplayType(3);
			updateFunc.setTogo("toUpdate");
			updateFunc.setStatus(1);
			updateFunc.setIcon("");
			updateFunc.setFnOr(1);
			updateFunc.setFnDesc("1");
			updateFunc.setUrl("/"+ed.getNameCodeAlias()+"/update");
			updateFunc.setExtraDataId(ed.getId());
			dao.save(updateFunc);
			//修改保存按钮
			Func updateFuncBut = new Func();
			updateFuncBut.setId(UUID.randomUUID().toString());
			updateFuncBut.setParentId(updateFunc.getId());
			updateFuncBut.setTypes(1);
			updateFuncBut.setName("保存");
			updateFuncBut.setLevel(3);
			updateFuncBut.setCreateTime(new Date());
			updateFuncBut.setDisplayType(2);
			updateFuncBut.setTogo("saveOrUpdate");
			updateFuncBut.setStatus(1);
			updateFuncBut.setIcon("btn btn-primary");
			updateFuncBut.setFnOr(1);
			updateFuncBut.setFnDesc("1");
			updateFuncBut.setUrl("");
			updateFuncBut.setExtraDataId(ed.getId());
			dao.save(updateFuncBut);
		//新增按钮	
			Func saveFunc = new Func();
			saveFunc.setId(UUID.randomUUID().toString());
			saveFunc.setParentId(func.getId());
			saveFunc.setTypes(1);
			saveFunc.setName("新增");
			saveFunc.setLevel(3);
			saveFunc.setCreateTime(new Date());
			saveFunc.setDisplayType(2);
			saveFunc.setTogo("toAdd");
			saveFunc.setStatus(1);
			saveFunc.setIcon("btn btn-primary");
			saveFunc.setFnOr(1);
			saveFunc.setFnDesc("1");
			saveFunc.setUrl("/"+ed.getNameCodeAlias()+"/save");
			saveFunc.setExtraDataId(ed.getId());
			dao.save(saveFunc);
			//新增保存按钮
			Func saveFuncBut = new Func();
			saveFuncBut.setId(UUID.randomUUID().toString());
			saveFuncBut.setParentId(saveFunc.getId());
			saveFuncBut.setTypes(1);
			saveFuncBut.setCreateTime(new Date());
			saveFuncBut.setName("保存");
			saveFuncBut.setLevel(3);
			saveFuncBut.setDisplayType(2);
			saveFuncBut.setTogo("saveOrUpdate");
			saveFuncBut.setStatus(1);
			saveFuncBut.setIcon("btn btn-primary");
			saveFuncBut.setFnOr(1);
			saveFuncBut.setFnDesc("1");
			saveFuncBut.setUrl("");
			saveFuncBut.setExtraDataId(ed.getId());
			dao.save(saveFuncBut);
			
			//详情按钮	
			Func infoFunc = new Func();
			infoFunc.setId(UUID.randomUUID().toString());
			infoFunc.setParentId(func.getId());
			infoFunc.setTypes(1);
			infoFunc.setName("详情");
			infoFunc.setLevel(3);
			infoFunc.setCreateTime(new Date());
			infoFunc.setDisplayType(3);
			infoFunc.setTogo("showDetail");
			infoFunc.setStatus(1);
			infoFunc.setIcon("");
			infoFunc.setFnOr(1);
			infoFunc.setFnDesc("1");
			infoFunc.setUrl("/"+ed.getNameCodeAlias()+"/info");
			infoFunc.setExtraDataId(ed.getId());
			dao.save(infoFunc);
	}
	
	//删除当前表下所有列的信息
	public void delete(ExtraData extraData){
		dao.executeJPQL("delete ExtraDataCols edc where edc.extralDataId = '"+extraData.getId()+"'");
	};
	
	
	
	public ACTION[] getAction() {
		// TODO Auto-generated method stub
		return new ACTION[]{ACTION.AFTER_SAVE,ACTION.AFTER_UPDATE,ACTION.BEFOR_SAVE,ACTION.BEFOR_QUERY};
	}
	@Override
	public String getClassExOfService() {
		// TODO Auto-generated method stub
		return ExtraData.class.getName();
	}
	
}
