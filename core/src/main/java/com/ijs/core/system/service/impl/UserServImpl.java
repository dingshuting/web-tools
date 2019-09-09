package com.ijs.core.system.service.impl;

import com.ijs.core.base.Config;
import com.ijs.core.base.model.Role;
import com.ijs.core.base.model.User;
import com.ijs.core.base.service.impl.GenericServImpl;
import com.ijs.core.common.dao.GenericDao;
import com.ijs.core.common.service.SaveAccessory;
import com.ijs.core.exception.ServiceException;
import com.ijs.core.system.service.UserServ;
import com.ijs.core.util.IdGen;
import com.ijs.core.util.PageList;
import com.ijs.core.util.QueryParameters;
import com.ijs.core.util.UploadUtil;
import com.ijs.core.util.security.MD5PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author wubin 本类主要查询用户信息及对用户信息的增删改
 */
@Service("userServ")
public class UserServImpl extends GenericServImpl implements UserServ, SaveAccessory {
	@Resource
	GenericDao dao;


	/**
	 * 查询用户分页列表
	 * 
	 * @param user
	 *            查询列表参数
	 * @param list
	 *            传入分页参数，并返回分页后的数据集合
	 */
	public void list(QueryParameters qp, User user, PageList list) {
		// TODO Auto-generated method stub
		StringBuffer jpql = new StringBuffer();
		jpql.append("from User u where accountNo!='sa'");
		if (qp.getStartTime() != null && !qp.getStartTime().isEmpty()) {
			jpql.append(" and u.regTime>='").append(qp.getStartTime()).append("'");
		}
		if (qp.getEndTime() != null && qp.getEndTime() != "") {
			jpql.append(" and u.regTime<='").append(qp.getEndTime()).append("'");
		}
		//jpql.append(dao.generateJpql(user, true, "u"));
		jpql.append(" order by u.regTime desc");
		list.setTotalSize(dao.count("*", jpql.toString()));
		List userList = dao.find(jpql.toString(), list.getCurrentPage(), list.getPerPageSize());
		list.setList(userList);

	}

	/**
	 * 更新用户的信息，其中账户、密码
	 */
	public void update(User user) {
		// TODO Auto-generated method stub
		User user1 = new User();
		if (user.getId() != null) {
			user1 = dao.get(User.class, user.getId());
		}
		user1.setAccountNo(user.getAccountNo().toLowerCase());
		if (user.getPassword() != null && user.getNewPassword() != null) {
			if (MD5PasswordEncoder.encode(user.getPassword(), "").equals(user1.getPassword())) {
				user1.setPassword(MD5PasswordEncoder.encode(user.getPassword(), ""));
				if (user.getNewPassword() != null) {
					user1.setPassword(MD5PasswordEncoder.encode(user.getNewPassword(), ""));
				}
			} else {
				user1.setPassword(MD5PasswordEncoder.encode(user.getNewPassword(), ""));
			}
		} else {
			user1.setPassword(user1.getPassword());
		}
		user1.setName(user.getName());
		user1.setEmail(user.getEmail());
		user1.setMobilePhone(user.getMobilePhone());
		user1.setHeadCover(user.getHeadCover());
		user1.setTitle(user.getTitle());
		user1.setOfficePhone(user.getOfficePhone());
		user1.setRoles(user.getRoles());
		user1.setOwner(user.getOwner());
		user1.setOrgId(user.getOrgId());
		user1.setStatus(user.getStatus());
		user1.setTitleType(user.getTitleType());
		user1.setSysDepId(user.getSysDepId());
		dao.update(user1);
	}

	@Override
	public <T> void save(T entity) throws Exception {
		// TODO Auto-generated method stub
		User user = (User) entity;
		if (user != null) {
			user.setAccountNo(user.getAccountNo().toLowerCase());
			user.setPassword(MD5PasswordEncoder.encode(user.getPassword(), ""));
			user.setName(user.getName());
			user.setRegTime(new Timestamp(new Date().getTime()));
			user.setId(IdGen.uuid());
			// user.setOwner(user.getId());
			user.setSign("0");
			//user.setStatus(User.UserStatus.NORMAL);
			dao.save(user);
		} else {
			throw new ServiceException("保存用户时用户参数不能为空");
		}
	}

	@Override
	public void saveAccessory(String uid, String url, String type) {
		// TODO Auto-generated method stub
		User user = dao.get(User.class, Integer.parseInt(uid));
		user.setHeadCover(url);
		dao.update(user);
	}

	/*
	 * @Override public boolean checkPassword(User user) {
	 * 
	 * User user1 = dao.get(User.class, user.getId()); Md5PasswordEncoder md5 =
	 * new Md5PasswordEncoder(); String password =
	 * md5.encodePassword(user.getPassword(), ""); if
	 * (password.equals(user1.getPassword())) { return true; } else { return
	 * false; } }
	 */
	/**
	 * 更改用户状态为删除状态，并且删除用户的角色信息
	 */
	public void remove(Integer id) {
		this.dao.executeJPQL("update User set status=" + User.UserStatus.INVALID + " where id=?", new Object[] { id });
		this.dao.executeJPQL("delete UserRole ur where  ur.id.userId=?", new Object[] { id });
	}

	public User get(String id) {
		// 用户信息&角色
		User user = this.dao.get(User.class, id);
		return user;
	}

	/**
	 * 验证指定账号是否存在
	 * 
	 * @param username
	 * @return true 存在；false 不存在
	 */
	@Override
	public boolean validateAccountNo(String accountNo) {
		try {
			StringBuffer jpql = new StringBuffer();
			jpql.append("from User u where u.accountNo = '").append(accountNo.toLowerCase()).append("'");
			Integer count = this.dao.count("id", jpql.toString());
			return count > 0 ? true : false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public User findByAccount(String account) {
		// TODO Auto-generated method stub
		StringBuffer jpql = new StringBuffer();
		jpql.append("from User u where u.accountNo = '").append(account.toLowerCase()).append("'");
		List<User> users = dao.find(jpql.toString());
		return users.size() > 0 ? users.get(0) : null;
	}

	@Override
	public String resetPwd(String account) {
		// 重置密码
		String newPwd = Config.SYS_PARAMETER_MAP.get(Config.RESET_PASSWORD);// 重置密码可在数据库中更改
		if (newPwd == null) {
			newPwd = "111111";
		}
		String pwd = MD5PasswordEncoder.encode(newPwd, account);
		dao.executeJPQL("update from User set password=? where status=" + User.UserStatus.NORMAL);
		return newPwd;
	}

	@Override
	public void stopUser(User user) {
		// TODO Auto-generated method stub
		User u = get(user.getId());
		u.setStatus(User.UserStatus.INVALID);
		dao.update(u);
	}

	@Override
	public void startUser(User user) {
		// TODO Auto-generated method stub
		User u = get(user.getId());
		u.setStatus(User.UserStatus.NORMAL);
		dao.update(u);
	}

	@Override
	public void removeAccessory(String rid, String type) throws ServiceException {
		// TODO Auto-generated method stub
		if (rid != null) {
			User user = get(rid);
			UploadUtil.removeFileByUrl(user.getHeadCover());
		} else {
			throw new ServiceException("在删除用户头像时参数rid不能为空");
		}

	}

	/**
	 * 根据企业ID 查询企业下所有用户
	 * 
	 * @return
	 */
	@Override
	public List<User> findByOwner(String owner) {
		List<User> users = dao.find("from User u where u.owner='" + owner + "'");
		return users;
	}

	/**
	 * 验证原始密码是否一致
	 * 
	 * @param user
	 *            修改的新老密码信息
	 * @return true 一致
	 */
	@Override
	public boolean checkPassword(User user) {
		User user1 = dao.get(User.class, user.getId());
		if (user.getPassword() != null && user.getNewPassword() != null) {
			if (MD5PasswordEncoder.encode(user.getPassword(), "").equals(user1.getPassword())) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

	@Override
	public boolean validateUser(User user) throws ServiceException{
		// TODO Auto-generated method stub
		StringBuffer jpql = new StringBuffer();
		jpql.append("from User u where u.accountNo = '").append(user.getAccountNo().toLowerCase()).append("'")
		.append(" or u.mobilePhone ='").append(user.getMobilePhone()).append("'")
		.append(" or u.orgId ='").append(user.getOrgId()).append("'");
		List<User> users = this.dao.find(jpql.toString());
		if(users.size()>0) {
			User tu=users.get(0);
			String res="";
			if(tu.getAccountNo().equals(user.getAccountNo())) {
				res+="账号已存在";
			}
			if(tu.getOrgId()!=null&&tu.getOrgId().equals(user.getOrgId())) {
				res+=" 工号已存在";
			}
			if(tu.getMobilePhone()!=null&&tu.getMobilePhone().equals(user.getMobilePhone())) {
				res+=" 手机号已存在";
			}
			throw new ServiceException(res);
		}
		return false;
	}

	@Override
	public void saveWxUser(User user) {
		User wxuser =user;
		if (user != null) {
			wxuser.setAccountNo("wx_"+user.getAccountNo());
			wxuser.setHeadCover(user.getHeadCover());
			wxuser.setName(user.getName());
			wxuser.getRoles().add(new Role("B5A4E247-145C-4DFA-A412-0D638532EFB8"));
			wxuser.setOwner(null);//默认测试服务中心
			wxuser.setPassword(MD5PasswordEncoder.encode("123456", ""));
			wxuser.setName(user.getName());
			wxuser.setRegTime(new Timestamp(new Date().getTime()));
			wxuser.setId(IdGen.uuid());
			wxuser.setCreateTime(new Timestamp(new Date().getTime()));
			wxuser.setUserType(2);
			wxuser.setPassType(2);
			wxuser.setStatus(User.UserStatus.NORMAL);
			dao.save(wxuser);
		} else {
			throw new ServiceException("保存用户时用户参数不能为空");
		}
	}
}
