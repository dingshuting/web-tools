package com.ijs.mongo.control;

import javax.annotation.Resource;

import org.apache.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ijs.core.base.control.BaseControl;
import com.ijs.core.base.model.User;
import com.ijs.core.util.PageList;
import com.ijs.mongo.service.MongoService;

@Controller
@RequestMapping("/sys_log")
public class SysLogControl extends BaseControl{
	
	
	@Resource
	private MongoService mongoService;
	
	/**
	 * 分页查询
	 * @param pn 分页
	 * @param request 查询条件 
	 * @return 数据集
	 */
	@RequestMapping("/list/{pn}")
	public @ResponseBody PageList list(@PathVariable("pn")Integer pn,HttpRequest request){
		PageList list = new PageList();
		list.setPerPageSize(10);
		list.setCurrentPage(pn);
		User user = getCurrentUser();
		try {
			mongoService.query(getRequestContent(), "sys_log", user, list);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("日志列表查询异常",e);
		}
		return list;
	}
	
	/**
	 * 得到当前查询对象
	 * @param pn格式 find/id值
	 * @return 查询的对象
	 */
	@RequestMapping("/find/{pn}")
	public @ResponseBody Object find(@PathVariable("pn")String pn){
		User user = getCurrentUser();
		Object obj = null;
		try {
			Object id = pn;
			obj = mongoService.get(id, "sys_log", user);
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("日志详情查询异常",e);
		}
		return obj;
	}
	
	/**
	 * 删除日志信息
	 * @param pn格式 /delete/{key:value} json字符串
	 * @return 删除的数量
	 */
	@RequestMapping("/delete/{pn}")
	public @ResponseBody Object delete(@PathVariable("pn")String pn){
		User user = getCurrentUser();
		Object obj = null;
		try {
			obj = mongoService.remove(pn, "sys_log", user);
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("日志删除异常",e);
		}
		return obj;
	}
	
}
