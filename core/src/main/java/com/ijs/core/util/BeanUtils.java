package com.ijs.core.util;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ijs.core.base.Constants;
import com.ijs.core.base.model.DataDictionary;
import com.ijs.core.base.model.ExtraDataCols;
import com.ijs.core.common.dao.GenericDao;
import com.ijs.core.util.spring.BeanSelector;


public class BeanUtils extends org.springframework.beans.BeanUtils {
	/**
	 * 比较两个对象，并获取那些字段不同，及个不同字段的值的变，其中不同的class类型不能比较，实例中id值不相同不能比较<br/>
	 * 	如:getDifferent(user1={id:1,name:1,age:2},user2={id:1,name:2,age:2}) return={name:[1,2]}<br/>
	 * 
	 * note:本方法依赖ExtralData对象，因此不能离开此框架单独使用,换句话说就是此方法用于比对有ORM映射的model，并且在ExtralData中有数据model的描述
	 * @param source 对象1
	 * @param target 对象2
	 * @return Map<String,String[]> map的key为值不相同的列的名字，值为size为2的数组，0-source的值  1-target的值 2-列的中文名
	 */
	public static Map<String,String[]> getDifferent(Object source,Object target) throws Exception{
		GenericDao dao=(GenericDao) BeanSelector.getBean("genericDao");
		if(!source.getClass().equals(target.getClass())) {
			throw new Exception("source and target must be a and the same class.");
		}
		try {
			 if(BeanUtils.getPropertyDescriptor(source.getClass(), "id") != null) {
				PropertyDescriptor pd=BeanUtils.getPropertyDescriptor(source.getClass(), "id");
				Object sid=pd.getReadMethod().invoke(source);
				Object tid=pd.getReadMethod().invoke(target);
				if(!sid.equals(tid)) {
					throw new Exception("the id of source was not equals to id of target");
				}
			}
			List<ExtraDataCols> comEds= dao.find("select DISTINCT edc  from ExtraData ed,ExtraDataCols edc where edc.extralDataId=ed.id and ed.nameCode='"+source.getClass().getSimpleName()+"'");
			Map<String,String[]> difs=new HashMap<>();
			PropertyDescriptor[] pds=BeanUtils.getPropertyDescriptors(source.getClass());
			for(PropertyDescriptor pd:pds) {
				if(pd.getPropertyType().equals(List.class)||pd.getPropertyType().equals(Map.class)){
					continue;
				}
				Object orgVal=pd.getReadMethod().invoke(source);
				Object newVal=pd.getReadMethod().invoke(target);
				if(pd.getPropertyType().equals(Date.class)) {
					orgVal=DateUtil.dateFormat((Date) orgVal, "yyyy-MM-dd HH:mm:ss");
					newVal=DateUtil.dateFormat((Date) newVal, "yyyy-MM-dd HH:mm:ss");
				}
				if (orgVal != null&&!orgVal.equals(newVal)) {
					String[] val=new String[3];
					for(ExtraDataCols col:comEds) {
						if(col.getColCode().equals(pd.getName())) {
							switch(col.getColValType()) {
								case Constants.COL_VAL_TYPE_FV:
									val[0]=(orgVal!=null?dao.get(DataDictionary.class,orgVal.toString()).getName():null);
									val[1]=(newVal!=null?dao.get(DataDictionary.class,newVal.toString()).getName():null);
									break;
								case  Constants.COL_VAL_TYPE_TIME:
									val[0]=(orgVal!=null?DateUtil.dateFormat((Date) orgVal, null):null);
									val[1]=(newVal!=null?DateUtil.dateFormat((Date) newVal,null):null);
									break;
								case Constants.COL_VAL_TYPE_STRING:
								case Constants.COL_VAL_TYPE_INTEGER:
								case Constants.COL_VAL_TYPE_DOUBLE:
									val[0]=(orgVal!=null?orgVal.toString():null);
									val[1]=(newVal!=null?newVal.toString():null);
									break;
								default:
									
									break;
							}
							val[2]=col.getColName();
							difs.put(pd.getName(), val);
							break;
						}
					}
				}
			}
			return difs;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw e;
		}
	}
	
	public static List<Class<?>> getsuperClass(Class<?> calzz){
		List<Class<?>> listSuperClass = new ArrayList<Class<?>>();
		Class<?> superclass = calzz.getSuperclass();
		while (superclass != null) {
			if(superclass.getName().equals("java.lang.Object")){
				break;
			}
			listSuperClass.add(superclass);
			superclass = superclass.getSuperclass();
		}
		return listSuperClass;
	}
}
