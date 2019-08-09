/**
 * 
 */
package com.ijs.core.base.unit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ijs.core.base.Config;
import com.ijs.core.base.Constants;
import com.ijs.core.base.model.Func;

/**
 * 用户权限容器
 * @author Tairong
 *
 */
public class UserFuncContainer  implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4031129212091823691L;

	private Map<String, Func> funcMap;
		
	public UserFuncContainer(){
		funcMap = new TreeMap<String, Func>();
	}
	
	/**
	 * 验证指定功能是否存在
	 * @param funcId
	 * @return
	 */
	public boolean hasFunc(String funcId){
		return this.funcMap.containsKey(funcId);
	}
	
	/**
	 * 根据功能编号获得功能实例
	 * @param funcId
	 * @return
	 */
	public Func loadFunc(String funcId){
		if(funcId == null){
			return null;
		}
		return this.funcMap.get(funcId);
	}
	
	/**
	 * 区得指定父功能子功能功列表
	 * @param parentFuncId
	 * @return
	 */
	public List<Func> loadFuncs(String parentFuncId){
		List<Func> funcs = new ArrayList<Func>();
		for(Func func : this.funcMap.values()){
			if(func.getParentId().equals(parentFuncId)){
				funcs.add(func);
			}
		}
		
		return funcs;
	}
	
	/**
	 * 区得指定父功能子功能功列表，排除非菜单显示项
	 * @param parentFuncId
	 * @return
	 */
	public List<Func> loadFuncs2(String parentFuncId){
		List<Func> funcs = new ArrayList<Func>();
		for(Func func : this.funcMap.values()){
			if(func.getParentId().equals(parentFuncId)  ){
				func.setSubFuncs(loadFuncs2(func.getId()));
				funcs.add(func);
			}
		}
		fnOr(funcs);
		return funcs;
	}
	
	/**
	 * 向容器添加功能
	 * @param funcs 
	 */
	public void putFuncs(List<Func> funcs){
		for(Func func : funcs){
			//System.out.println("Func: " + func.getId() + "." + func.getName());
			this.funcMap.put(func.getId(), func);
		}
	}
	
	public Func getFuncByUrl(String url){
		for(Func func : this.funcMap.values()){
			//System.out.println(func);
			if(func.getUrl()!=null&&!func.getUrl().isEmpty()){
				if(url.indexOf(func.getUrl())>-1){
					return func;
				}
			}
			
		}
			return null;
	}
	/**
	 * @return the funcMap
	 */
	public Map<String, Func> getFuncMap() {
		//System.out.println(funcMap.size());
		return funcMap;
	}
	
	public String getZtreeJson(){
		List<Func> rootfunc=loadallFunc("00");
		for(Func tf:rootfunc){
			tf.setSubFuncs(loadallFunc(tf.getId()));
		}
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		return gson.toJson(rootfunc);
	}
	
	private List<Func> loadallFunc(String parentFuncId){
		List<Func> funcs=loadFuncs(parentFuncId);
		for(Func tf:funcs){
			tf.setSubFuncs(loadallFunc(tf.getId()));
		}
		return funcs;
	}
	private void fnOr(List<Func> source){
		Comparator<Func> com=new Comparator<Func>() {
			
			@Override
			public int compare(Func o1, Func o2) {
				// TODO Auto-generated method stub
				
				if(o1.getFnOr()!=null&&o2.getFnOr()!=null)
					return o1.getFnOr()-o2.getFnOr();
				else
					return 0;
			}
		};
		Collections.sort(source,com);
	}
}
