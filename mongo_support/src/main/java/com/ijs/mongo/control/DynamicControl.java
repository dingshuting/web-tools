package com.ijs.mongo.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ijs.core.base.control.BaseControl;
import com.ijs.core.base.model.ExtraData;
import com.ijs.core.util.PageList;
import com.ijs.core.util.Result;
import com.ijs.core.util.ResultUtil;
import com.ijs.mongo.service.MongoService;
import com.mongodb.DBObject;

/**
 * 针对动态数据的简单CURD的方法，此Control提供一个公共的针对数据所有者的基础操作Control。此Control会查询、管理与自己息息相关的数据
 * @author Administrator
 */
@Controller
@RequestMapping("/dyn/{coll_alias}")
public class DynamicControl extends BaseControl {
	@Resource
	MongoService mongoService;

	/**
	 * Old---Old---Old---Old---Old
	 */
	@Deprecated
	@RequestMapping("/oldlist/{pn}")
	public @ResponseBody Result<?> list(@PathVariable("coll_alias") String collAlias, @PathVariable("pn") Integer pn, @RequestParam(required=false) Integer perPageSize,@RequestBody Map<String, String> queryObj) {
		try {
			PageList pl=new PageList();
			pl.setPerPageSize(perPageSize);
			pl.setCurrentPage(pn);
			mongoService.queryOld(queryObj, getCollectionNameByAliasOld(collAlias), null, pl);
			return ResultUtil.success(pl);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtil.error(Result.CODE_ERROR, e.getMessage());
		}
	}
	
	@RequestMapping("/list/{pn}")
	public @ResponseBody PageList list(HttpServletRequest request,@PathVariable("coll_alias") String collAlias, @PathVariable("pn") Integer pn,
			@RequestParam( value="count",required=false) Integer count) {
		PageList pl = new PageList();
		pl.setCurrentPage(pn);
		pl.setPerPageSize(count==null?0:count);
		try {
			mongoService.query(getRequestContent(), getCollectionNameByAlias(collAlias), getCurrentUser(), pl);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			pl.setList(new ArrayList());
		}
		return pl;
	}

	@RequestMapping("/info/{iid}")
	public @ResponseBody String get(@PathVariable("coll_alias") String collAlias,@PathVariable("iid")String iid) {
		ExtraData data = new ExtraData();
		data.setNameCodeAlias(collAlias);
		Object obj=mongoService.get(iid, getCollectionNameByAlias(collAlias), getCurrentUser());
		if(obj!=null){
			return obj.toString();
		}
		return "{name:'数据无效'}";
	}
	
	@RequestMapping("/getOld/{iid}")
	public @ResponseBody Result<?> getOld(@PathVariable("coll_alias") String collAlias,@PathVariable("iid")String iid) {
		try {
			ExtraData data = new ExtraData();
			data.setNameCodeAlias(collAlias);
			return ResultUtil.success(mongoService.get(iid, getCollectionNameByAlias(collAlias), getCurrentUser()));
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtil.error(Result.CODE_ERROR, e.getMessage());
		}
	}
	
	@RequestMapping("/save")
	public @ResponseBody Result save(@PathVariable("coll_alias") String collAlias) {
		Result result=new Result();
		result.setCode(Result.CODE_SUCCESS);
		try {
			mongoService.save(getRequestContent(), getCollectionNameByAlias(collAlias), getCurrentUser());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.setCode(ERROR_CODE);
			result.setDesc(e.getMessage());
		}
		return result;
	}
	
	@RequestMapping("/remove/{iid}")
	public @ResponseBody Result remove(@PathVariable("coll_alias") String collAlias,@PathVariable("iid")String iid) {
		Result result=new Result();
		result.setCode(Result.CODE_SUCCESS);
		try {
			mongoService.remove("{_id:'"+iid+"'}", getCollectionNameByAlias(collAlias), getCurrentUser());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.setCode(ERROR_CODE);
			result.setDesc(e.getMessage());
		}
		return result;
	}
	/**
	 * 将2条相关数据进行绑定关联
	 * @param collAlias 要关联的原始表数据
	 * @param did 要关联的原始表数据id
	 * @param target 要将原始数据关联到的表名
	 * @param tid 要关联到的数据表id
	 * @return 绑定结果
	 */
	@RequestMapping("/databind/{target}/{did}/{tid}")
	public @ResponseBody Result dataBind(@PathVariable("coll_alias") String collAlias,@PathVariable("target")String target,@PathVariable("did") String did,@PathVariable("tid") String tid) {
		String collTName=getCollectionNameByAlias(target);
		String collDName=getCollectionNameByAlias(collAlias);
		DBObject dbo=((DBObject)mongoService.get(tid,collTName  ,null));
		dbo.put(collDName+"Id", did);
		mongoService.update("{_id:"+tid+"}",dbo,collTName , getCurrentUser(), new ArrayList<String>());
		return ResultUtil.success();
	}
	/**
	 * 确认信息或者审核信息的接口,用于改变数据的状态cs-changeStatus
	 * @param collAlias 操作的表别名
	 * @param iid 数据标识id
	 * @param dataStatus 与数据字典相互对应的状态值
	 * @return
	 */
	@RequestMapping("/cs/{iid}")
	public @ResponseBody Result changeStatus(@PathVariable("coll_alias") String collAlias,@PathVariable("iid")String iid,@RequestParam("s") String dataStatus) {
		Result result=new Result();
		result.setCode(Result.CODE_SUCCESS);
		try {
			Object obj=mongoService.get(iid, collAlias, getCurrentUser());
			if(obj!=null){
				((DBObject)obj).put("status", dataStatus);
				mongoService.update("{_id:"+iid+"}", obj, collAlias, getCurrentUser(), new ArrayList<String>());
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.setCode(ERROR_CODE);
			result.setDesc(e.getMessage());
		}
		return result;
	}
	
	@RequestMapping("/all/{pn}")
	public @ResponseBody PageList allList(@PathVariable("coll_alias") String collAlias, @PathVariable("pn") Integer pn,
		@RequestParam( value="count",required=false) Integer count) {
		PageList pl = new PageList();
		pl.setCurrentPage(pn);
		mongoService.query(getRequestContent(), getCollectionNameByAlias(collAlias), null, pl);	
		return pl;
	}
	
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
	
	@RequestMapping("/count")
	public @ResponseBody Map count(@PathVariable("coll_alias") String collAlias,@RequestBody(required=false) String queryObj,@RequestBody(required=false) String gropus,@RequestBody(required=false) String countCols) {
		return mongoService.count(queryObj, getCollectionNameByAlias(collAlias), getCurrentUser(),gropus,countCols);
	}
	
	private String getCollectionNameByAliasOld(String alias) {
//		log.debug(getRequest().getRequestURI()+"--->>>"+getRequestInfo().toString());
		return getCollectionNameByAlias(alias);
		/*ExtraData data = new ExtraData();
		data.setNameCodeAlias(alias);
		List<ExtraData> list = genericServ.list(data);
		if (list.size() > 0) {
			data = list.get(0);
			return data.getNameCode();
		}
		return null;*/
	}
}
