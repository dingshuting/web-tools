package com.ijs.core.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Random;

import com.ijs.core.base.model.User;
/**
 * 实例工具类，用于生成含有值的实例类
 * @author Dustin
 *
 */
public class InstantUtil {
	public static char[] testStringLib = new char[] { 'a', 'b', '总', '测', 'c', 'd', 'e', 'f', 'g', 'h', 'i', '一', '生',
			'或', '广', '需', '1', '#', '(', '%', 'z', '神', '看', '此', '空', '是', 'v', '方', 'b', '哭', '率', '库', '.', '`' };
	/**
	 * 生成一个指定类型的实例
	 * @param t 指定的Class类型
	 * @return 返回一个包含值的实例
	 */
	public static <T> T getInstant(Class<T> t) {
		try {
			T obj = t.newInstance();
			Field[] fields = null;
			if (Modifier.isAbstract(t.getSuperclass().getModifiers())) {
				fields = t.getSuperclass().getDeclaredFields();
			} else {
				fields = t.getDeclaredFields();
			}

			Field.setAccessible(fields, true);
			for (Field f : fields) {
				try {
					PropertyDescriptor pd = new PropertyDescriptor(f.getName(), t);
					Method getMethod = pd.getWriteMethod();// 获得get方法
					Type[] types = getMethod.getGenericParameterTypes();
					for (Type type : types) {
						if(type.toString().equals(String.class.toString())){
							getMethod.invoke(obj, getStringValRandom(new Random().nextInt(11)));
						}else if(type.toString().equals(Integer.class.toString())){
							getMethod.invoke(obj, new Random().nextInt(1500));
						}
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
				}
			}
			return obj;
			
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String getStringValRandom(int length){
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<length;i++){
		 Random random = new Random();
	       int s = random.nextInt(testStringLib.length);
		sb.append(testStringLib[s]);
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		System.out.println(getInstant(User.class).toString());
	}
}
