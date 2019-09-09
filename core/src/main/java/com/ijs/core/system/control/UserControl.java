package com.ijs.core.system.control;


import com.google.gson.GsonBuilder;
import com.ijs.core.base.Constants;
import com.ijs.core.base.control.BaseControl;
import com.ijs.core.base.model.Role;
import com.ijs.core.base.model.SysDep;
import com.ijs.core.base.model.User;
import com.ijs.core.system.service.RoleServ;
import com.ijs.core.system.service.impl.UserServImpl;
import com.ijs.core.util.PageList;
import com.ijs.core.util.QueryParameters;
import com.ijs.core.util.Result;
import com.ijs.core.util.ResultUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Set;
/**
 * 对用户信息的curd操作。
 */
@Controller
@RequestMapping("/user")
public class UserControl extends BaseControl{
	//用户查询数据库对象
	
	@Resource(name="userServ")
	public UserServImpl userServ;
	@Resource(name="roleServ")
	private RoleServ roleServ;
	
	/** 
	 *  分页查询当前登录用户所属企业下的所有用户集合
	 * @param pn 当前页码
	 * @param qp 时间的查询条件，不为空时将按照用户的注册时间进行时间段查询
	 * @param user 查询条件，其中owner字段不支持前台传参，后台默认设置当前登录用户
	 * @return 当前用户集合
	 */
	@RequestMapping("/list/{pn}")
	public @ResponseBody PageList findPageListSh(@PathVariable("pn")Integer pn,@ModelAttribute("qp")QueryParameters qp,@ModelAttribute("user")User user){
		PageList list=new PageList();
		try {
			list.setPerPageSize(5);
			list.setCurrentPage(pn);
			if(user!=null && user.getOwner()!=null){
			}else{
				user.setOwner(getCurrentUser().getOwner());
			}
			userServ.list(qp,user, list);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("查询用户列表出错",e);
		}
		return list;
	}
	
	/**
	 *  查询用户详情信息，其包含了用户的整个信息，包括：用户的角色列表，用户的部门信息
	 *  @param uid 用户的唯一标识ID.
	 * @return 当前查询的用户明细信息
	 */
	@RequestMapping("/info/{uid}")
	public @ResponseBody User findUserInfo(@PathVariable("uid")String uid){
		try {
			User user = genericServ.get(User.class,uid);
			if(user.getSysDepId()!=null&&!"".equals(user.getSysDep()));
				user.setSysDep(genericServ.get(SysDep.class, user.getSysDepId()));
				if(userOwner!=null) {
					user.setIdentity(userOwner.obtain(user));
				}
			//String body= new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(user);
			return user;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("查询用户详情出错",e);
		}
		return null;
	}
	/**
	 * 保存用户信息,用户默认保存在当前登录用户下的所属企业中，如果owner值不为null则不做任何操作，
	 * 当用户存在时则无法继续保存
	 * @param user 要保存的用户对象
	 * @return Result标准结果
	 */
	@RequestMapping(value="/save",method = RequestMethod.POST)
	public @ResponseBody Result save(@RequestBody User user){
		Result<User> re = new Result<User>();
		try {
			boolean flag = true;
			if(getCurrentUser().getOwner()==null||getCurrentUser().getOwner().isEmpty()){
				re.setCode(Result.CODE_VALIDATE);
				re.setDesc("用户尚未得到认证，无法添加用户");
				return re;
			}
			//当为sa管理员添加用户时，owner字段只能在前台选择添加，当为其它用户时则添加的用户归属为当前登录用户,同时如果选择的部门为后台运营部门时，则默认为管理员下属。
			if(user.getOwner()!=null && !user.getOwner().equals("") ){
				user.setOwner(user.getOwner());
			} 
			if(user.getSysDepId().equals("2")) {
				user.setOwner(Constants.SA_ID);
			}
			//暂时不考虑saas化，只要单主体运营公司
			/*if(!getCurrentUser().getOwner().equals(Constants.SA_ID)) {
				user.setOwner(getCurrentUser().getOwner());
			}*/
			if(user.getId()==null || user.getId().isEmpty()){
				if(this.userServ.validateUser(user)){
					re.setCode(Result.CODE_NAME);	
					re.setDesc("该账户已经被占用，请更换其它");
					return re;
				}
				if(user.getRoleStr() != null){
					String role[] = user.getRoleStr().split(",");
					for(int i=0; i<role.length; i++){
						user.getRoles().add(new Role(role[i]));
					}
				}
				user.getRoles().add(new Role("1002"));
				if(user.getUserType()==null||user.getUserType().equals("")){//这里为后台添加用户
					user.setUserType(1);//运营用户
					user.setPassType(1);
				}
				userServ.save(user);
			}else{
				if(user.getRoleStr() != null){
					String role[] = user.getRoleStr().split(",");
					Set<Role> roles = new HashSet<Role>();
					user.setRoles(roles);
					for(int i=0; i<role.length; i++){
						user.getRoles().add(new Role(role[i]));
					}
				}
				user.getRoles().add(new Role("1002"));
				if(user.getUserType()==null||user.getUserType().equals("")){//这里为后台修改用户
					user.setUserType(1);//运营用户
					user.setPassType(1);
				}
				userServ.update(user);
			}
			if(flag){
				re.setCode(Result.CODE_SUCCESS);
			}else{
				re.setCode(Result.CODE_VALIDATE);
			}
			re.setData(user);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("添加用户出错",e);
			re.setCode(Result.CODE_ERROR);
			re.setDesc("添加用户出错 "+e.getMessage());
		}
		return re;
	}
	
	/**
	 * 删除用户信息，当前为物理删除用户 
	 * @param id 
	 * @return
	 */
	@RequestMapping("/removeuser")
	public @ResponseBody String removeUser(@RequestParam("id")String id){
		Result re = new Result();
		try {
			this.userServ.remove(User.class,id);
			re.setCode(Result.CODE_SUCCESS);
			
		} catch (Exception e) {
			e.printStackTrace();
			re.setCode(Result.CODE_ERROR);
		}
		String body= new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(re);
		return body;
	}
	@RequestMapping("/cu")
	public @ResponseBody User getCurrentUserOfLogin(@RequestParam(name="isDetail",required=false,defaultValue="1")Integer isDetail){
		User user=getCurrentUser();
		if(isDetail!=1&&user!=null){
			user=genericServ.get(User.class, user.getId());
			user.setFuncs(null);
			user.setRoles(null);
			if(user!=null&&user.getOwner()!=null&&userOwner!=null){
	    		if(user.getIdentity()==null)
	    		user.setIdentity(userOwner.obtain(user));
	    	}
		}
		return user;
	}
	
	@RequestMapping("/addUserSetting")
	public @ResponseBody Result<?> addUserSign(@RequestParam("sk")String sk,@RequestParam("sv")String sv){
		if(sk!=null)
		getCurrentUser().getSetting().put(sk, sv);
		return ResultUtil.success();
	
	}
	@RequestMapping("/cuLoginout")
	public @ResponseBody void removeCurrentUserOfLogin(){
		HttpSession session=getRequest().getSession();
		session.invalidate();
	}
}
