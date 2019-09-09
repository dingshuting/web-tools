package com.ijs.core.system.service.impl;


import org.springframework.stereotype.Service;

import com.ijs.core.base.model.User;
import com.ijs.core.base.service.impl.GenericServImpl;
import com.ijs.core.exception.ServiceException;
import com.ijs.core.system.service.MyprofileServ;
/**
 * 个人信息的实现类
 * @author Dustin
 *
 */
@Service("myprofileServ")
public class MyprofileServImpl extends GenericServImpl implements MyprofileServ {
			
	public void updateContactWay(User user) {
		if(user!=null&&user.getId()!=null) {
			dao.executeJPQL("update User set mobilePhone=?, officePhone=? where id=?"
							, new Object[]{user.getMobilePhone(), user.getOfficePhone(), user.getId()});
			dao.executeJPQL("update User set QQ=?, email=? where id=?"
					, new Object[]{user.getQQ(), user.getEmail(), user.getId()});
		}else {
			throw new ServiceException("用户信息不能为空");
		}
	}
	
	public void savePWD(User user) {
		dao.executeJPQL("update User set password=? where id=?"
				, new Object[]{user.getPassword(), user.getId()});
	}
}
