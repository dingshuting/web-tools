package com.ijs.mongo.control;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ijs.core.base.control.BaseControl;
import com.ijs.core.base.model.ExtraData;
import com.ijs.core.base.model.User;
import com.ijs.core.util.PageList;
import com.ijs.mongo.service.MongoService;

/**
 * 与DynamicControl不同的是，DynamicControl均需要进行身份验证，并且查询数据均为自己独立的数据，并不会查询全部的业务数据，当然特殊业务除外
 * 针对动态数据的简单CURD的方法，此Control提供了基础的Api开放接口，通过此Control来获取对外的所有数据</br>
 * 在获取数据前根据数据表的API安全配置，来确认具体的api数据开放程度
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/mgapi/{coll_alias}")
public class BaseApiDynamicControl extends BaseControl {
	@Resource
	MongoService mongoService;
	/**
	 * 获取数据列表
	 * @param collAlias 数据表别名
	 * @param pn 当前页码
	 * @param queryObj 查询对象（json）字符串
	 * @param count 查询的每页条数
	 * @return 返回PageList对象
	 */
	@RequestMapping("/list/{pn}")
	public @ResponseBody PageList list(@PathVariable("coll_alias") String collAlias, @PathVariable("pn") Integer pn,
		 @RequestParam(value="count",required=false) Integer count) {
	
		PageList pl = new PageList();
		pl.setCurrentPage(pn);
		pl.setPerPageSize(count==null?0:count);
		User user=getCurrentUser();
		mongoService.query(getRequestContent(), getCollectionNameByAlias(collAlias), user, pl);
		return pl;
	}

	@RequestMapping("/info/{iid}")
	public @ResponseBody String get(@PathVariable("coll_alias") String collAlias,@PathVariable("iid")String iid) {
		ExtraData data = new ExtraData();
		data.setNameCodeAlias(collAlias);
		return  mongoService.get(iid, getCollectionNameByAlias(collAlias), getCurrentUser()).toString();
	}
	//根据表别名，查询到真正的数据表名称
	private String getCollectionNameByAlias(String alias) {
		ExtraData data = new ExtraData();
		data.setNameCodeAlias(alias);
		List<ExtraData> list = genericServ.list(data,true);
		if (list.size() > 0) {
			data = list.get(0);
			return data.getNameCode();
		}else{
			return alias;
		}
	}
}
