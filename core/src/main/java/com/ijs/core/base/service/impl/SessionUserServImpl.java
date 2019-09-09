package com.ijs.core.base.service.impl;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ijs.core.base.Config;
import com.ijs.core.base.model.Func;
import com.ijs.core.base.model.Role;
import com.ijs.core.base.model.SysDep;
import com.ijs.core.base.model.User;
import com.ijs.core.base.service.SessionUserServ;

@Service("sessionUserServ")
public class SessionUserServImpl extends GenericServImpl implements  SessionUserServ{
	protected Logger log = org.apache.log4j.LogManager.getLogger(this.getClass());
	@Autowired 
	private Environment evn;
	public User loadUserByaccountNo(String username)
			throws UsernameNotFoundException {
		List<User> users = null;
		StringBuffer jpql = new StringBuffer();
		try {
			jpql.append("from User u")
					.append(" where u.status=2 and (u.accountNo='")
					.append(username.toLowerCase())
					.append("'")
					.append(" or u.orgId='")
					.append(username.toLowerCase())
					.append("'")
					.append(" or u.mobilePhone='")
					.append(username.toLowerCase())
					.append("')");
			users = dao.find(jpql.toString());
			jpql = null;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("load User by name failed");
		}
		
		if (users == null || users.isEmpty()) {
			throw new UsernameNotFoundException("User '" + username+ "' not found...");
		} else {
			User user = users.get(0);
			if(user.getSysDepId()!=null) {
				user.setSysDep(dao.get(SysDep.class, user.getSysDepId()));
			}
			//load function
			//if user is administraotr then load all func
			//else load func list of user`s role
			jpql = new StringBuffer();
			Iterator<GrantedAuthority> it = user.getAuthorities().iterator();		
			boolean isSA = false;
			boolean isAdmin=false;
			while(it.hasNext()){
				GrantedAuthority ga=it.next();
				
				if(ga.getAuthority()!=null && ga.getAuthority().equals(Config.ROLE_SA)){
					isSA = true;
					break;
				}else if(ga.getAuthority()!=null && ga.getAuthority().equals(Config.ROLE_ADMIN)){
					isAdmin=true;
					user.setIsAdmin(1);
					break;
				}
			}
			String funcTypes="0,1";
			if(evn.getProperty("system.funcType") == null){
				log.warn("the type of funcs was not specified, so '0,1' types will be seted by default");
			}else{
				funcTypes=evn.getProperty("system.funcType");
			}
			
			if(isSA){
				jpql.append("from Func where types in("+funcTypes+") and status=1");
			}else if(isAdmin){
				jpql.append("select DISTINCT f from Func f, RoleFunc rf, UserRole ur")
				.append(" where f.id=rf.id.funcId and rf.id.rolId=ur.id.roleId and f.types in("+funcTypes+") and f.status=1 and ur.id.userId='").append(user.getId()).append("'");
			}else{
				jpql.append("select DISTINCT f from Func f, RoleFunc rf, UserRole ur")
					.append(" where f.id=rf.id.funcId and rf.id.rolId=ur.id.roleId and f.types in("+funcTypes+") and f.status=1 and ur.id.userId='").append(user.getId()).append("'");
			}
			List<Func> func=dao.find(jpql.toString());
			user.getFuncs().putFuncs(func);
			user.getAuthorities().add(new Role("ROLE_USER","ROLE_USER"));
			return user;
		}
	
	}

	
}
