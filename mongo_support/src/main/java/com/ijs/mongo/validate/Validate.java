package com.ijs.mongo.validate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.ValidationException;

import org.json.JSONArray;
import org.json.JSONObject;

import com.ijs.core.base.model.ExtraDataCols;
import com.ijs.core.common.dao.GenericDao;
public class Validate  {
	
	
	private static Map<String,IValidateRule> vrMaps=new HashMap<String, IValidateRule>();
	static{
		vrMaps.put("required",new ValidateRules.NotNullValidateRule());
		vrMaps.put("custom[email]",new ValidateRules.EmailValidateRule());
		vrMaps.put("illegalCharacter",new ValidateRules.IllegalCharacterValidateRule());
		vrMaps.put("minSize",new ValidateRules.MinLengthValidateRule(5));
		vrMaps.put("maxSize",new ValidateRules.MaxLengthValidateRule(255));
		vrMaps.put("custom[phone]",new ValidateRules.TelephoneValidateRule());
		vrMaps.put("fax",new ValidateRules.FaxValidateRule());
		vrMaps.put("custom[integer]",new ValidateRules.IntegerValidateRule());
		vrMaps.put("custom[number2]",new ValidateRules.NumberValidateRule());
		
	}

	/**
	 * 验证规则的接口
	 * @author Administrator
	 *
	 */
	interface IValidateRule {
		/**
		 * 验证是否符合规则
		 * @param data 要验证的参数
		 * @return 是否成功
		 */
		public void validate(Object data) throws ValidationException;
		/**
		 * 失败的提示内容
		 * @return
		 */
		public String getFailedHint();
		/**
		 * 设置验证的参数
		 * @param para
		 */
		public void setPara(String para);
		/**
		 * 获取验证的参数值，一般情况下都会有各自的默认值
		 * @return
		 */
		public String getPara();
		
	}
	/**
	 * 
	 * @param data  前台新增的数据
	 * @param vrcms 数据库中读取的字段
	 * @return
	 */
	public static List<String> validate(JSONObject data,List<IValidateRuleColMapper> vrcms,GenericDao genericDao) {
		if (data == null||vrcms==null) {
			throw new IllegalArgumentException("The 'data(要验证的对象)' and 'vrcms(数据库配置的列的集合)' for validation can not be null");
		}
		//验证字段是否匹配
		Validate.check(data, vrcms,genericDao);
		List<String> errors=new ArrayList<String>();
		for(IValidateRuleColMapper vcrm:vrcms){
			if(data.has(vcrm.getColCode())){
				Map<String,String> rks=solveValidatePara(vcrm.getRules());
				for(String rk:rks.keySet()){
					IValidateRule rule=getRule(rk,rks.get(rk));
					if(rule!=null){
						try {
							rule.validate(data.get(vcrm.getColCode()));
						} catch (ValidationException e) {
							// TODO Auto-generated catch block
							errors.add(vcrm.getColCn()+e.getMessage());
							continue;
						}
					}
				}
			}
		}
		return errors;
	}
	/**
	 * 获取规则实例
	 * @param key
	 * @return
	 */
	private static IValidateRule getRule(String k,String v){
		if(k.equals("custom")){
			k = "custom["+v+"]";
			v = null;
		}
		IValidateRule rule=vrMaps.get(k.trim());
		rule.setPara(v);
		return rule;
	}
	/**
	 * 拼接
	 * @param errors
	 * @return
	 */
	public static String getErrorString(List<String> errors){
		StringBuffer re=new StringBuffer();
		for(String e:errors){
			re.append(e).append(" ");
		}
		return re.toString();
	}
	/**
	 * 解析验证的参数为map形式，map的key为要验证的规则，value为验证规则的值
	 * @param para 类似于[required,min[0],max[255],dateRange[1~2],minSize[5],maxSize[10],maxItemSize[10]]的字符串
	 * @return 键值对的map
	 */
	public static Map<String,String> solveValidatePara(String para){
		Map<String,String> mp=new HashMap<String, String>(); 
		if(para!=null &&  !para.equals("")){
			String[] kvs=para.substring(1,para.length()-1).split(",");
			for(String kv:kvs){
				int i=kv.indexOf("[");
				String k=kv.substring(0,i!=-1?i:kv.length());
				String p=i!=-1?kv.substring(i+1,kv.lastIndexOf("]")):null;
				mp.put(k, p);
			}
		}
		return mp;
	}
	
	public static void check(JSONObject data,List<IValidateRuleColMapper> vrcms,GenericDao genericDao){
		//是否添加的字段数量，名称等于数据库的定义
		for(String dk:data.keySet()){
					boolean isExist=false;
					for(IValidateRuleColMapper m:vrcms){
						//字段是object类型
						ExtraDataCols edc = ((ExtraDataCols)m);
						if(m.getColCode().equals(dk)){
							if(edc.getColValType().equals("l") || edc.getColValType().equals("o")){
								ExtraDataCols col=new ExtraDataCols();
								col.setReferenceExtralDataId(edc.getId());
								List list =  genericDao.findByEntity(col);
								//当时对象的时候
								if(edc.getColValType().equals("l")){
									JSONArray json = new JSONArray(data.get(dk).toString());
									//JSONArray json = JSONArray.fromObject(data.get(dk).toString());
									for (int i = 0; i < json.length(); i++) {
										JSONObject jsono =  new JSONObject(json.getJSONObject(i).toString());
										Validate.check(jsono, list, genericDao);
									}
									isExist = true;
									break;
								}
								//当是对象的时候
								if(edc.getColValType().equals("o")){
									JSONObject jsono = new JSONObject(data.get(dk).toString());
									Validate.check(jsono, list, genericDao);
									isExist = true;
									break;
								}
							}
							isExist=true;
							break;
						}
					}
					if(!isExist)
					throw new IllegalArgumentException("检测到非法的数据字段:"+dk);
				}
	}
	
	public static void main(String[] args) {
		String array="[required,min[0],max[255],dateRange[1~2],minSize[5],maxSize[10],maxItemSize[10],illegalCharacter]";
		new Validate().solveValidatePara(array);
	}
}
