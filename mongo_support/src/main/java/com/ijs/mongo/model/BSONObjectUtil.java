package com.ijs.mongo.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ijs.mongo.MongoConfig;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;

public class BSONObjectUtil {
	/**
	 * 将JSON对象转化成数据库的保存对象
	 * @param obj 要保存到数据库的json对象
	 * @return 返回可以保存到数据库的对象
	 */
	public static DBObject getDBObject(JSONObject obj){
		try {
			Iterator<String> ite=obj.keys();
			Map<String,Object> ks=new HashMap<String,Object>();
			while(ite.hasNext()){
				String key=(String) ite.next();
				Object vo=obj.get(key);
				if(vo instanceof JSONObject){
					ks.put(key, getDBObject((JSONObject) vo));
				}else if(vo instanceof JSONArray){
					JSONArray ja=(JSONArray) vo;
					List<Object> tv=new ArrayList<Object>();
					for(int i=0;i<ja.length();i++){
						if(ja.get(i) instanceof JSONObject){
							tv.add(getDBObject((JSONObject) ja.get(i)));
						}else{
							tv.add(ja.get(i));
						}
					}
					ks.put(key,tv);
				}else {
					ks.put(key,vo);
				}
			}
			return BasicDBObjectBuilder.start(ks).get();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}
	public static List<DBObject> getJsonSimples(List<DBObject> dbos) {
		// TODO Auto-generated method stub
		for(DBObject o:dbos){
			getJsonSimple(o);
		}
		return dbos;
	}
	/**
	 * 将数据库对象转换成json字符串
	 * @param dbo 数据的对象
	 * @return json的string串
	 */
	public static DBObject getJsonSimple(DBObject dbo){
		try {
			if(dbo==null)return null;
			for(String key:dbo.keySet()){
				Object vo=dbo.get(key);
				if(vo instanceof DBObject){
					if(key.equals(MongoConfig._ID)){
						dbo.put("id",getJsonSimple((DBObject) vo));
					}
				}else{
					if(key.equals(MongoConfig._ID)){
						dbo.put("id", vo.toString());
						break;
					}
				}
			}
			return dbo;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	public static <T> List<T> sameInArray(T[] t1, T[] t2) {  
	    List<T> list1 = Arrays.asList(t1);  
	    List<T> list2 = new ArrayList<T>();  
	    for (T t : t2) {  
	        if (!list1.contains(t)) {  
	            list2.add(t);  
	        }  
	    }  
	    return list2;  
	}  
	
}
