package com.ijs.core.system.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ijs.core.base.Config;
import com.ijs.core.base.model.SysParameters;
import com.ijs.core.base.service.impl.GenericServImpl;
import com.ijs.core.common.dao.GenericDao;
import com.ijs.core.system.service.ParameterServ;
@Service
public class ParameterServImpl extends GenericServImpl implements ParameterServ{
	@Resource
	private GenericDao paramDao;
	
	public void cache() {
		List<SysParameters> sps = paramDao.find("from SysParameters");
		for(SysParameters sp : sps){
			Config.SYS_PARAMETER_MAP.put(sp.getId(), sp.getValue());
		}
	}
	/**
	 * the method can update the value of entity in the database and memory
	 * memo: only the value field of entity is updated when the method is executed
	 */
	public void update(SysParameters entity) {
		// TODO Auto-generated method stub
		SysParameters sp=paramDao.get(SysParameters.class,entity.getId());
		sp.setValue(entity.getValue());
		paramDao.update(sp);
		Config.SYS_PARAMETER_MAP.put(sp.getId(), sp.getValue());
	}
	@Override
	public void saveLock(String id, String status) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void updatesys(SysParameters sysyParameters) {
		// TODO Auto-generated method stub
		dao.update(sysyParameters);
		cache();
	}
	@Override
	public void save(SysParameters sysyParameters) {
		// TODO Auto-generated method stub
		dao.save(sysyParameters);
		cache();
	}
	@Override
	public SysParameters getDetail(String sysParameterId) {
		// TODO Auto-generated method stub
		return dao.get(SysParameters.class, sysParameterId);
	}
}
