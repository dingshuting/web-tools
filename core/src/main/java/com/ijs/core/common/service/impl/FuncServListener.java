package com.ijs.core.common.service.impl;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.ijs.core.base.listener.MysqlBaseServiceListener;
import com.ijs.core.base.model.Func;
import com.ijs.core.exception.ServiceException;
@Service
public class FuncServListener extends MysqlBaseServiceListener {

	@Override
	public boolean doAction(ACTION action,Class t, Object... args) throws ServiceException {
		// TODO Auto-generated method stub
		Func func=(Func) args[0];
		func.setCreateTime(new Date());
		func.setStatus(1);
		func.setTypes(1);
		return true;
	}

	@Override
	public ACTION[] getAction() {
		// TODO Auto-generated method stub
		return new ACTION[]{ACTION.BEFOR_SAVE};
	}

	@Override
	public String getClassExOfService() {
		// TODO Auto-generated method stub
		return Func.class.getName();
	}
	
}
