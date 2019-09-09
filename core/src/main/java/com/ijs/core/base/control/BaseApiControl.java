package com.ijs.core.base.control;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;

import com.ijs.core.util.Result;
/**
 * api的公共Control，URL以/api开头安全框架是不进行安全验证的，其它方法直接使用父类的基本CURD即可
 * @author Dustin
 *
 */
@Controller
@RequestMapping("/api/{modelkey}")
public class BaseApiControl extends GenericControl {
	/**
	 * 重写了写入的操作，避免恶意攻击
	 */
	@Override
	public Result save(NativeWebRequest request, String modelkey) {
		// TODO Auto-generated method stub
		log.error("save can be used on the api Control");
		return null;
	}
	
	@Override
	public String changeStatus(String aid, String dataStatus, String modelkey) {
		// TODO Auto-generated method stub
		return super.changeStatus(aid, dataStatus, modelkey);
	}
}
