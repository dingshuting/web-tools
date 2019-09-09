package com.ijs.core.system.control;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ijs.core.base.Config;
import com.ijs.core.base.control.BaseControl;
import com.ijs.core.base.model.Func;
import com.ijs.core.base.model.User;
import com.ijs.core.base.service.SessionUserServ;
import com.ijs.core.system.service.MyprofileServ;
import com.ijs.core.system.service.UserServ;
import com.ijs.core.util.Result;
import com.ijs.core.util.security.MD5PasswordEncoder;

/**
 * 
 * @author Tairong
 *
 */
@Controller
@RequestMapping("/profile")
public class MyprofileControl extends BaseControl {

	@Resource(name = "myprofileServ")
	private MyprofileServ myprofileServ;
	private static final String FILE_DIR = "myprofile/";

	/**
	 * 查看用户
	 * 
	 * @return
	 */
	@RequestMapping("/info")
	public @ResponseBody Result<User> profile() {
		Result<User> re = new Result<User>();
		log.info("execute the profile of CompanyControl");
		try {
			re.setData(getCurrentUser());
			re.setCode(Result.CODE_SUCCESS);
		} catch (Exception e) {
			log.error(e);
		}
		return re;
	}

	/**
	 * 保存用户信息
	 * 
	 * @return
	 */
	@RequestMapping("/save")
	public @ResponseBody Result<User> save(@ModelAttribute User user) {
		Result<User> re = new Result<User>();
		log.info("execute the profile of CompanyControl");
		try {
			User cu = getCurrentUser();
			cu.setMobilePhone(user.getMobilePhone());
			cu.setOfficePhone(user.getOfficePhone());
			cu.setQQ(user.getQQ());
			cu.setEmail(user.getEmail());
			this.myprofileServ.update(cu);
			// modify session user
			re.setCode(Result.CODE_SUCCESS);
			re.setDesc("个人信息保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			re.setCode(Result.CODE_ERROR);
			re.setDesc("个人保存失败，错误信息：" + e.getMessage());
		}

		return re;
	}

	/**
	 * 保存密码（当前用户的�?
	 * 
	 * @return
	 */
	@RequestMapping("/savepwd")
	public @ResponseBody Result<User> savepwd(@RequestParam("oldpwd") String oldPwd, @ModelAttribute User user) {
		Result<User> re = new Result<User>();
		log.info("execute the savepwd of ProfileControl");
		try {
			// 验证用户旧密�?
			if (!MD5PasswordEncoder.valid(getCurrentUser().getPassword(), oldPwd, "")) {
				re.setDesc("个人密码修改错误，非法的旧密码，请重新输入！");
				re.setCode(Result.CODE_VALIDATE);
				return re;
			}

			// 加密新密�?
			User uu = getCurrentUser();
			uu.setPassword(MD5PasswordEncoder.encode(user.getPassword(), ""));
			genericServ.update(uu);
			// modify session user
			re.setDesc("密码保存成功");
			re.setCode(Result.CODE_SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			log.equals(e);
			re.setDesc("修改失败" + e.getMessage());
			re.setCode(Result.CODE_ERROR);
		}
		return re;
	}
	@RequestMapping("/funcs")
	public @ResponseBody String getFuncOfCU() {
		List<Func> func = getCurrentUser().getFuncs().loadFuncs2("00");
		return Config.gson.toJson(func);
	}

	/**
	 * reset the password of specified account, the action has to verify the SMSCode
	 * 
	 * @param npwd
	 * @param account
	 * @return
	 */
	@RequestMapping("/resetpwd")
	public @ResponseBody Result resetPwd(@RequestParam("npwd") String npwd, @RequestParam("phone") String phone) {
		Result result = new Result<>();
		if (verifySMSCode(true)) {
			try {
				User user = new User();
				user.setMobilePhone(phone);
				user.setStatus(2);
				List<User> users = genericServ.list(user);
				if (users.size() > 0) {
					users.get(0).setPassword(MD5PasswordEncoder.encode(npwd, ""));
					genericServ.update(users.get(0));
					result.setCode(Result.CODE_SUCCESS);
				}else {
					result.setCode(Result.CODE_ERROR);
					result.setDesc("手机号码不存在");
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result.setCode(Result.CODE_ERROR);
			}
		} else {
			result.setDesc("验证码有误");
			result.setCode(Result.CODE_ERROR);
		}
		return result;
	}

}
