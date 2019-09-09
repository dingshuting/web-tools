package com.ijs.core.system.control;

import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ijs.core.base.Config;
import com.ijs.core.base.Constants;
import com.ijs.core.base.control.BaseControl;
import com.ijs.core.base.model.Role;
import com.ijs.core.base.model.SysDep;
import com.ijs.core.system.service.RoleServ;
import com.ijs.core.util.Result;


/**
 * 整个角色的控制层，涵盖了角色的CURD基础功能
 */
@Controller
@RequestMapping("/org")
public class RoleControl extends BaseControl {


	@Resource
	private RoleServ roleServ;
	/**
	 * 角色信息列表的查询,由于角色分了层级所以其无需再次进行分页处理
	 * @param pid 角色的父id，如果第一级则为部门id
	 * @return 返回角色的列表信息，其返回的Role中的children不为空，但其子项的Children为空，需要时在发起调用此方法即可。
	 */
	@RequestMapping("/rlist")
	public @ResponseBody String list(@RequestParam(name="id",required=false,defaultValue="0")String pid){
		if(pid.equals("0")){
			return Config.gson.toJson(departList(true));
		}else{
			Role role=new Role();
			role.setParentId(pid);
			role.setOwner(getCurrentUser().getOwner());
			return Config.gson.toJson(roleServ.list(role));
		}
	}
	/**
	 * 获取当前企业的部门信息列表，由于部门并不会太多不涉及分页查询<br/>
	 * 查询当前登录用户所属企业的所有部门信息
	 * @return
	 */
	@RequestMapping("/dlist")
	public @ResponseBody List<SysDep> departList(@RequestParam(name="hasFunc",defaultValue="1")boolean hasFunc){
		SysDep sysd=new SysDep();
		sysd.setOwner(getCurrentUser().getOwner());
		List<SysDep> deps=genericServ.list(sysd);
		for(SysDep dep:deps){
			Role role=new Role();
			role.setParentId(dep.getId());
			role.setOwner(getCurrentUser().getOwner());
			List<Role> roles=roleServ.list(role);
			for(Role tr:roles) {
				tr.setChildren(loadRoleChildren(tr,hasFunc));
				if(hasFunc)
				tr.setFuncs(roleServ.getFunc(tr));
			}
			dep.setChildren(roles);
		}
		return deps;
	}
	private List<Role> loadRoleChildren(Role role,boolean hasFunc){
		Role qrole=new Role();
		qrole.setParentId(role.getId());
		qrole.setOwner(getCurrentUser().getOwner());
		if(hasFunc)
		role.setFuncs(roleServ.getFunc(role));
		List<Role> roles=roleServ.list(qrole);
		if(roles!=null&&roles.size()>0) {
			for(Role tr:roles) {
				tr.setChildren(loadRoleChildren(tr,hasFunc));
			}
		}
		return roles;
	}
	/**
	 * 获取角色的明细信息其自动查询了关联的功能信息
	 * @param rid 角色的id，其为路径变量
	 * @return 返回了包含功能列表的角色对象
	 */
	@RequestMapping("/info/{rid}")
	public @ResponseBody Role info(@PathVariable("rid") String rid){
		Role role=(Role) genericServ.get(Role.class, rid);
		return role;
		
	}
	/**
	 * 添加角色信息，添加时会默认添加到当前登录用户所属的企业中
	 * @param role 角色对象，其中角色中的funcs（功能项）需大于1，
	 * @return
	 */
	@RequestMapping("/saver")
	public @ResponseBody Result<Role> addRole(@RequestBody Role role){
		Result<Role> result=new Result<Role>();
		try {
			role.setOwner(getCurrentUser().getOwner());
			role.setTypes(0);
			role.setOrgId(0);
			role.setStatus(1);
			role.setApplyToSystem(1);
			if(role.getId()!=null){
				roleServ.update(role);
			}else{
				role.setId(UUID.randomUUID().toString());
				roleServ.save(role);	
			}
			result.setData(role);
			result.setCode(Result.CODE_SUCCESS);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			result.setCode(Result.CODE_ERROR);
			result.setDesc(e.getMessage());
			log.error(e);
		}
		return result;
	}
	/**
	 * 添加或修改部门信息
	 * @param dep
	 * @return
	 */
	@RequestMapping("/saved")
	public @ResponseBody Result<SysDep> addDep(@RequestBody SysDep dep){
		Result<SysDep> result=new Result<SysDep>();
		try {
			dep.setOwner(getCurrentUser().getOwner());
			if(dep.getId()!=null){
				genericServ.update(dep);
			}else{
				dep.setId(UUID.randomUUID().toString());
				dep.setStatus(1);
				genericServ.save(dep);
			}
			result.setData(dep);
			result.setCode(Result.CODE_SUCCESS);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			result.setCode(Result.CODE_ERROR);
			result.setDesc(e.getMessage());
			log.error(e);
		}
		return result;
	}
	@RequestMapping("/remover/{rid}")
	public @ResponseBody Result removeR(@PathVariable("rid")String rid){
		Result<Role> result=new Result<Role>();
		try {
			if(!roleServ.hasUser(rid)){
				roleServ.remove(Role.class, rid);
				result.setCode(Result.CODE_SUCCESS);
			}else{
				result.setCode(Result.CODE_ERROR);
				result.setDesc("角色下还有用户不能删除");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.setCode(Result.CODE_ERROR);
			result.setDesc(e.getMessage());
			log.error(e);
		}
		return result;
	}
	@RequestMapping("/removed/{did}")
	public  @ResponseBody Result removeD(@PathVariable("did")String did){
		Result result=new Result();
		try {
			if(genericServ.get(SysDep.class,did).getStatus()==2) {
				throw new Exception("系统部门不能删除");
			}
			genericServ.remove(SysDep.class, did);
			result.setCode(Result.CODE_SUCCESS);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e);
			result.setCode(Result.CODE_VALIDATE);
			result.setDesc(e.getMessage()); 
		}
		return result;
	}
}
