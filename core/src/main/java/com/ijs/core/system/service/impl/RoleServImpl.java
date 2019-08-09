package com.ijs.core.system.service.impl;

import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ijs.core.base.model.AbstractFunc;
import com.ijs.core.base.model.AbstractRole;
import com.ijs.core.base.model.Func;
import com.ijs.core.base.model.Role;
import com.ijs.core.base.model.RoleFunc;
import com.ijs.core.base.model.RoleFuncId;
import com.ijs.core.base.service.impl.GenericServImpl;
import com.ijs.core.common.dao.GenericDao;
import com.ijs.core.system.service.RoleServ;

/**
 * 
 * 本类主要查询用户信息及对用户信息的增删改
 */
@Service("roleServ")
public class RoleServImpl extends GenericServImpl  implements RoleServ {
	@Resource
	GenericDao dao;
	
	public List<Role> list(Role entity) {
		// TODO Auto-generated method stub
		entity.setTypes(1);
		entity.setStatus(1);
		List<Role> rs=super.list(entity);
		return rs;
	}
	public List<Func> getFunc(Role entity){
		String jpql=" select DISTINCT f from Func f,RoleFunc rf where rf.id.rolId=? and rf.id.funcId=f.id";
		return dao.find(jpql,entity.getId());
		
	}
	@Override
	public <T> void save(T entity) throws Exception {
		// TODO Auto-generated method stub
		save((Role)entity);
	}
	public void save(Role role) {
			role.setTypes(1);
			role.setStatus(1);
			role.setApplyToSystem(1);
			role.setId(UUID.randomUUID().toString());
			dao.save(role);
			if(role.getFuncs()!=null)
			for(Func func:((Role) role).getFuncs()) {
				RoleFunc rf=new RoleFunc(new RoleFuncId(func.getId(), ((Role) role).getId()));
				dao.save(rf);
			}
	}
	
	
	public boolean validateName(String name,String depId) {
		StringBuffer jpql = new StringBuffer();
		
		jpql.append("from Role where name = '").append(name).append("' and orgId="+depId);
		Integer count = this.dao.count("id", jpql.toString());
		
		if(count > 0){
			return true;
		}else{
			return false;
		}
	}
	
	@Override
	public <T> void update(T role) throws Exception {
		// TODO Auto-generated method stub
		((Role) role).setStatus(1);
		((Role) role).setApplyToSystem(1);
		this.dao.update(role);
		dao.executeJPQL("delete RoleFunc rf where rf.id.rolId=?",((Role) role).getId());
		for(Func func:((Role) role).getFuncs()) {
			RoleFunc rf=new RoleFunc(new RoleFuncId(func.getId(), ((Role) role).getId()));
			dao.save(rf);
		}
	}

	@Override
	public boolean hasUser(String rid) {
		List<Object> obj = (List<Object>) this.dao.executeSqlQuery("select count(s.user_ID) from sys_user_role s where s.role_ID='"+rid+"'");
		if(obj.size()>0){
			Integer count=Integer.parseInt(obj.get(0).toString());
			if(count>0){
				return true;
			}
		}
		return false;
	}




	
}
