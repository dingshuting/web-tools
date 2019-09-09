package com.ijs.core.common.control;


import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ijs.core.base.control.BaseControl;
import com.ijs.core.common.service.RegionServ;
@Controller
public class ForwardControl extends BaseControl {
	/**
	 * 默认跳转到index。html用于处理前后分离，跳转默认页面的问题
	 * @return
	 */
	@RequestMapping("/")
	public String forward(){
		log.info("to index.html");
		return "redirect:index.html";
	}
	
	
	
}
