package com.ijs.core.base.control;

import java.beans.PropertyDescriptor;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonInputMessage;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;

import com.ijs.core.base.Constants;
import com.ijs.core.base.model.ExtraData;
import com.ijs.core.exception.ServiceException;
import com.ijs.core.util.PageList;
import com.ijs.core.util.QueryParameters;
import com.ijs.core.util.Result;

/**
 * 	     基础的功能Control，该Control提供了基本的CURD和状态变更的功能，通过/{操作表}/操作项，链接形式</br>
 * 进行方法的调用，默认支持所有Mysql表的基本操作。此Control下的方法默认情况下均需要登录认证后方可进行下一步操作</br>
 * 其中modelkey为数据表（ExtralData）中设置的别名<br/>
 * 
 * 注：本Control均采用RESTFUL进行查询和修改等操作，因此所有的请求均需以json格式提交，其content-type也需要为application/json类型
 */
@RequestMapping("/mdyn/{modelkey}")
@Controller
public class GenericControl extends BaseControl{
	@Autowired 
	private Environment evn;
	@Resource(name="mvcValidator")
	private Validator validator;
	@Resource
	private GsonHttpMessageConverter gsonConver;
	
	/**
	 * 基本的列表查询功能，支持所有动态配置的查询即在ExtralData中进行了相应的配置
	 * @param pn 当前页
	 * @param modelkey 请求的ModelKey
	 * @param qp 查询条件工具类
	 * @param request
	 * @return PageList对象
	 */
	@RequestMapping("/list/{pn}")
	public @ResponseBody PageList list(@PathVariable("pn")Integer pn,@PathVariable("modelkey") String modelkey,@ModelAttribute("qp")QueryParameters qp,NativeWebRequest request){
		PageList list=new PageList();
		try {
			log.debug("execute the list of genricControl");
			Object obj = dataBinder(request, false,modelkey);
			list.setPerPageSize(qp.getCount());
			list.setCurrentPage(pn);
			genericServ.list(qp,obj,list);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("查询信息列表出错",e);
		}
		return list;
	}
	
	/**
	 * 保存修改信息
	 * @param modelkey 当前操作的数据表key
	 * @return
	 */
	@RequestMapping(value="/save",method = RequestMethod.POST)
	public @ResponseBody Result save(NativeWebRequest request,@PathVariable("modelkey") String modelkey){
		Result re = new Result();
		try {
			Object obj=dataBinder(request, true,modelkey);
			
			if(BeanUtils.getPropertyDescriptor(Class.forName(getModelClass(modelkey)), Constants.MODEL_ID_KEY).getReadMethod().invoke(obj)!=null){
				genericServ.update(obj);
			}else{
				genericServ.save(obj);
			}
			log.debug("execute the method 'save' of genericControl and the save obj is :"+obj);
			re.setData(obj);
			re.setCode(Result.CODE_SUCCESS);
			return re;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("信息添加出错",e);
			re.setCode(Result.CODE_ERROR);
			re.setDesc(e.getMessage());
		}
		return re;
	}
	/**
	 * 逻辑删除信息
	 * @param adress 传输以整个adress的json格式进行传输
	 * @return
	 */
	@RequestMapping("/remove/{aid}")
	public @ResponseBody Result remove(@PathVariable("aid")String aid,@PathVariable("modelkey") String modelkey){
		Result re = new Result();
		try {
			log.debug("execute the method 'remove' of genericControl and the id of para is : "+aid);
			genericServ.remove(Class.forName(getModelClass(modelkey)),aid);
			re.setCode(Result.CODE_SUCCESS);
			return re;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("删除信息错误",e);
			re.setCode(Result.CODE_ERROR);
			re.setDesc(e.getMessage());
		}
		return re;
	}
	/**
	 * 逻辑详情信息
	 * @param adress 传输以整个adress的json格式进行传输
	 * @return
	 */
	@RequestMapping("/info/{aid}")
	public @ResponseBody Object info(@PathVariable("aid")String aid,@PathVariable("modelkey") String modelkey){
		try {
			Object obj = genericServ.get(Class.forName(getModelClass(modelkey)),aid);
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("查询详情错误",e);
		}
		return null;
	}
	//查询所有数据
	@RequestMapping("/all")
	public @ResponseBody List<?> all(NativeWebRequest request,@PathVariable("modelkey") String modelkey){
		List<?> list = null;
		try {
			log.debug("execute the list of genricControl");
			Object obj = dataBinder(request, false,modelkey);
			list = genericServ.list(obj);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("查询信息列表出错",e);
		}
		return list;
	}
	
	/**
	 * 修改状态信息，由于各种业务逻辑均涉及到状态的变更，因此提供公共的状态变更方法
	 * @param aid 标识id
	 * @param dataStatus 状态的变更值
	 * @param modelkey 变更的表别名
	 * @return
	 */
	@RequestMapping("/cs/{aid}")
	public @ResponseBody String changeStatus(@PathVariable("aid")String aid,@RequestParam("s") String dataStatus,@PathVariable("modelkey") String modelkey){
		try {
			log.debug("execute the changeStatus of genricControl");
			Object obj = genericServ.get(Class.forName(getModelClass(modelkey)),aid);
			PropertyDescriptor pds = new PropertyDescriptor("status", obj.getClass());
			pds.getWriteMethod().invoke(obj, Integer.parseInt(dataStatus));
			genericServ.changeStatus(obj);
			return "200";
		} catch (Exception e) {
			e.printStackTrace();
			log.error("修改状态出错",e);
			return "999";
		}
	}
	/**
	 * 获取当前请求的ModelClass,获取到的class用于将request-body内容转换成指定的class实例</br>
	 * 获取方式有2钟：</br>
	 * 	1、登录后对应的func中添加ModelClass属性</br>
	 * 	2、直接从属性文件中获取配置，此种方法可以在调用api未登录时使用</br>
	 * @param modelName model在配置表中的别名
	 * @return model的全路径
	 */
	private String getModelClass(String modelName) {
		// TODO Auto-generated method stub
		if(getCurrentFunc()!=null&&getCurrentFunc().getModelClass()!=null) {
			return getCurrentFunc().getModelClass();
		}else {
			Object classfullName=evn.getProperty("model."+modelName);
			if(classfullName==null) {
				ExtraData extraData=new ExtraData();
				extraData.setNameCodeAlias(modelName);
				List<ExtraData> extraDataList=genericServ.list(extraData);
				if(extraDataList!=null&&extraDataList.size()>0) {
					classfullName=extraDataList.get(0).getModelName();
				}else {
				log.error("the modelClass was null. You can plus 'model.{full path of your classes}' to the end of application.xml ");
				}
			}
			return classfullName!=null?classfullName.toString():null;
		}
		
	}
	/**
	 * 
	 * 初始化请求的参数并将请求转换成bean，该方法执行原理如下：
	 *  0、spring-security 判断当前用户的登录状态
	 * 	1、ControlAllInterceptor 拦截器判断链接的权限有效性，并设置当前访问的功能
	 *  2、获取当前的访问功能
	 *  3、读取功能对于的Model类路径获取到class类型
	 *  4、读取当前request-content
	 *  5、通过json转换，并根据第3步获取的class类型，转换成class对应的实例
	 *  7、将实例用于相应的CURD操作
	 * @param request
	 * @param isValidate
	 * @return
	 * @throws ValidationException
	 * @throws Exception
	 */
	private Object dataBinder(NativeWebRequest request,boolean isValidate,@PathVariable("modelkey") String modelkey) 
			throws ValidationException,Exception {
		Object obj=null;
		try {
			log.debug("the url is:"+getRequest().getRequestURI());
			Class targetClazz = Class.forName(getModelClass(modelkey));
			log.debug("get the target class :"+targetClazz);
			HttpServletRequest nrequest=(HttpServletRequest) request.getNativeRequest(ServletRequest.class);
			obj=gsonConver.read(targetClazz,new MappingJacksonInputMessage(nrequest.getInputStream(),null));
			if(obj==null)
				obj=targetClazz.newInstance();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ServiceException("Class "+getModelClass(modelkey)+" was not fount or "+e.getMessage());
		} 
		ServletRequestDataBinder binder = extraDefaultData(request, obj);
		BindingResult result=binder.getBindingResult();
		if(isValidate){
			if(result.getErrorCount()>0){
				StringBuffer sb=new StringBuffer();
				 for(ObjectError err:result.getAllErrors()){
					 sb.append("(").append(err.getCode()+":"+err.getDefaultMessage()).append(")");
				 }
				 throw new ValidationException(sb.toString());
			}
		}
		return result.getTarget();
	}
	/**
	 * 扩展对象的默认数据值，如owner，addTime等，并返回一个DataBinder。
	 * 在dataBinder方法对请求的路径
	 * @param request request对象
	 * @param obj 非空的实例对象并且该对象为数据库的model
	 * @return 
	 */
	private ServletRequestDataBinder extraDefaultData(NativeWebRequest request, Object obj) {
		ServletRequestDataBinder binder=new ServletRequestDataBinder(obj);//(ServletRequestDataBinder) webDataBinder.createBinder(request, obj, "obj");
		binder.bind(request.getNativeRequest(ServletRequest.class));
		MutablePropertyValues propertyValues=new MutablePropertyValues();
//		propertyValues.add("owner", getCurrentUser().getOwner());
		binder.bind(propertyValues);
		binder.setValidator(validator);
		return binder;
	}
}
