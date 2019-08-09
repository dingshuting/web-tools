package com.ijs.core.common.control;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ijs.core.util.CacheUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ijs.core.base.Config;
import com.ijs.core.base.Constants;
import com.ijs.core.base.control.BaseControl;
import com.ijs.core.base.model.DataDictionary;
import com.ijs.core.base.model.Region;
import com.ijs.core.base.model.SysUnits;
import com.ijs.core.common.model.VersionUpdateModel;
import com.ijs.core.common.service.RegionServ;
import com.ijs.core.system.service.ParameterServ;
import com.ijs.core.util.Result;
import com.ijs.core.util.ResultUtil;
import com.ijs.core.util.SendSMSUtil;
import com.ijs.core.util.SendSMSUtil.SMSType;
/**
 * 公共组件的Control,此Control不受安全框架限制即可访问，用于提供公共的组件信息，如区域、验证码等
 * @author Dustin
 *
 */
@SuppressWarnings("rawtypes")
@RequestMapping("/common")
@Controller
public class CommonControl extends BaseControl {
	@Resource
	RegionServ regionServ;
	@Autowired
	ApplicationContext context;
	private List<Region> cacheRegions;
	@Resource
	private ParameterServ paramterServ;

	/**
	 * 异步查询区域信息,当pid为0并且cid为空时，则默认取中国下的一级省份信息<br/>
	 * 初始化默认情况下，为避免重复读取数据库造成的效率低下，区域查询后默认缓存在本control中。
	 * 
	 * @param pid
	 *            父区域信息 ,当pid为0时取国家cid为pid
	 * @param rname
	 *            根据关键字查询区域
	 * @param cid
	 *            国家id
	 * @return 返回区域信息的JSON字符串
	 */
	@RequestMapping("/region")
	public @ResponseBody String findRegion(@RequestParam("pid") String pid,
			@RequestParam(value = "isdetail", required = false, defaultValue = "0") String isdetail,
			@RequestParam(value = "query", required = false) String rname,
			@RequestParam(value = "cid", required = false) Integer cid) {
		try {
			pid = pid.equals("0") ? (cid == null ? "142" : "" + cid) : pid;
			List<Region> regions = null;
			if (isdetail.equals("1")) {
				if (cacheRegions == null) {
					cacheRegions = genericServ.getAll(Region.class);
				} else {
					regions = cacheRegions;
				}
			} else {
				Region region=new Region();
				region.setParentId(pid);
				regions = genericServ.list(region);
			}
			String sa = Config.gson.toJson(regions);
			return sa;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ERROR_CODE;
	}
	/**
	 * 获取所有中国下的区域，并输出为jsong文件，可以下载
	 * @param response
	 * @return
	 */
	@RequestMapping("/regionToJson")
	public @ResponseBody String regionToJson(HttpServletResponse response) {
		Region regs = regionServ.getDetail("142");
		String json = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(regs.getChildRegs());
		 response.setContentType("application/force-download");// 设置强制下载不打开
		                 response.addHeader("Content-Disposition",
		                       "attachment;fileName=city.json");
		return json;
	}

	/**
	 * 获取数据字典的信息
	 * 
	 * @param pid
	 *            数据字段的type，就是父id
	 * @return
	 */
	@RequestMapping("/dd/info/{pid}")
	public @ResponseBody DataDictionary get(@PathVariable("pid") String pid) {
		try {
			return genericServ.get(DataDictionary.class, pid);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取数据字典的信息
	 * 
	 * @param pid
	 *            数据字段的type，就是父id
	 * @return
	 */
	@RequestMapping("/dd/list/{pid}")
	public @ResponseBody List<DataDictionary> findDataDictionary(@PathVariable("pid") String pid) {
		try {
			DataDictionary dd = new DataDictionary();
			dd.setPid(pid);
			return genericServ.list(dd, true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 删除数据字典的信息
	 * 
	 * @param pid
	 *            数据字段的type，就是父id
	 * @return
	 */
	@RequestMapping("/dd/rm/{did}")
	public @ResponseBody Result<DataDictionary> rmDataDictionary(@PathVariable("did") String did) {
		Result<DataDictionary> re = new Result<DataDictionary>();
		log.info("execute the rmDataDictionary of CommonControl");
		try {
			genericServ.delete(DataDictionary.class, did);
			re.setCode(Result.CODE_SUCCESS);
		} catch (Exception e) {
			log.error(e);
			re.setCode(Result.CODE_ERROR);
			re.setDesc(e.getMessage());
		}
		return re;
	}
	/**
	 * 获取当前的请求的sessionId
	 * @return
	 */
	@RequestMapping("/session")
	public @ResponseBody String getSessionId() {
		return getRequest().getSession().getId();
	}

	/**
	 * 保存数据字典的信息
	 * 
	 * @param 数据字段对象
	 * @return
	 */
	@RequestMapping("/dd/save")
	public @ResponseBody Result<DataDictionary> saveDataDictionary(@RequestBody DataDictionary dd) {
		Result<DataDictionary> re = new Result<DataDictionary>();
		log.info("execute the profile of CompanyControl");
		if(getCurrentUser()==null) {
			return null;
		}
		try {
			dd.setStatus(1);
			genericServ.update(dd);
			re.setData(dd);
			re.setCode(Result.CODE_SUCCESS);
		} catch (Exception e) {
			log.error(e);
			re.setCode(Result.CODE_ERROR);
			re.setDesc(e.getMessage());
		}
		return re;
	}

	/**
	 * 获取所有的单位信息
	 * 
	 * @param 数据字段对象
	 * @return
	 */
	@RequestMapping("/units")
	public @ResponseBody List<SysUnits> findUnits() {
		log.debug("execute the findUnits of CommonControl");
		try {
			SysUnits su = new SysUnits();
			su.setStatus(1);
			return genericServ.list(su);
		} catch (Exception e) {
			log.error(e);
		}
		return null;
	}
	/**
	 * 发送验证码信息，当小于间隔时间时将不允许发送
	 * @param request
	 * @param mobile 手机号
	 * @param t
	 * @return
	 */
	@RequestMapping("/sendsms")
	public @ResponseBody Result sendSMS(HttpServletRequest request, @RequestParam("mp") String mobile,@RequestParam(name="t",required=false)String t) {

		try {
			Object obj = null;//request.getSession().getAttribute(Constants.VERIFY_SMS_CODE_SESSION_TIME_NAME);
			log.debug("send-SMS last time is:" + obj + " and now is:" + new Date().getTime());
			if (obj != null) {
				long lastSendTime = (long) obj;
				long now = new Date().getTime();
				if (now - lastSendTime < Integer.parseInt(Config.SYS_PARAMETER_MAP.get(Config.SEND_SMS_INTERVAL))
						* 1000) {
					return ResultUtil.error(Result.CODE_ERROR, "发送间隔时间太短");
				}
			}
			String code = SendSMSUtil.createRandomVcode();
			//SMSTemplate tpl = context.getBean(SMSTemplate.class);
			//String verifyContent = tpl.getContent() + code;
			//request.getSession().setAttribute(Constants.VERIFY_SMS_CODE_SESSION_NAME, code);

			Map<String,String> parms=new HashMap<>();
			parms.put("code", code);
			CacheUtil.cacheMap.put(mobile,code);
			log.debug("SMS_CODE IS "+code);
			//log.debug("code in session is "+request.getSession().getAttribute(Constants.VERIFY_SMS_CODE_SESSION_NAME));
			if(Config.SYS_PARAMETER_MAP.get("is_send_sms").equals("1")) {
				if(t!=null) {
					SendSMSUtil.sendByAliyun(SMSType.valueOf(t), mobile,parms);
				}else {
					SendSMSUtil.sendByAliyun(SMSType.XIUGAI_MIMA, mobile,parms);
				}
			}else {
				log.info("SMS has been switched off");
			}
			//request.getSession().setAttribute(Constants.VERIFY_SMS_CODE_SESSION_TIME_NAME, new Date().getTime() / 1000);
			return ResultUtil.success();
		} catch (BeansException ex) {
			ex.printStackTrace();
			return ResultUtil.error(Result.CODE_NOFOUND, ex.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResultUtil.error(Result.CODE_ERROR, e.getMessage());

		}
	}

	@RequestMapping("/verifysmscode")
	public @ResponseBody Result sendSMS() {
		log.info("-----------start to verifysmscode");
		if (verifySMSCode(true)) {
			return ResultUtil.success();
		} else {
			return ResultUtil.error(Result.CODE_VALIDATE, "验证码有误");
		}

	}
	@RequestMapping("/upsp")
	public @ResponseBody Result upSysPara(@RequestParam("t")String t) {
		if(t.equals(Config.SYS_PARAMETER_MAP.get("operator_token"))) {
			paramterServ.cache();
		}else {
			return ResultUtil.error(Result.CODE_VALIDATE, "token dosen't match");
		}
			
		return ResultUtil.success();
	}
	@RequestMapping("/ckv")
	public @ResponseBody VersionUpdateModel checkVersion() {
		if(Config.SYS_PARAMETER_MAP.containsKey("current_version")) {
			VersionUpdateModel version=new Gson().fromJson(Config.SYS_PARAMETER_MAP.get("current_version"),VersionUpdateModel.class);
			return version;
		}else {
			return null;
		}
	}
	@RequestMapping("/ka")
	public @ResponseBody Result keepAlive(){
		
		return ResultUtil.success();
	}
	public static void main(String[] args) {
		System.out.println("76481533429-s".matches("[a-zA-Z]{0,3}\\d{9,}[\\-?\\d{0,}]{0,}"));
	}
}
