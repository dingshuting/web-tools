package com.ijs.mongo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.ValidationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.gson.Gson;
import com.ijs.core.base.control.BaseControl;
import com.ijs.core.util.Tools;
import com.ijs.mongo.model.CacheData;

@Service
public class CacheUtilByMongo {
	@Resource
	private MongoService mongoService;
	private static final String CACHE_TABLE_NAME="keshihua_cache";
	private static Map<String,Long> lastUpdate=new HashMap<String,Long>();
	//线程更新间隔，在此间隔内不再对数据进行更新
	private static final Integer UPDATE_INTERVAL=600000;
	protected final static transient Log log = LogFactory.getLog(CacheUtilByMongo.class);
	public class BackThread extends Thread{
		AsyncObtainVal obtainVal;
		String scId;
		String key;
		public BackThread(String scId, AsyncObtainVal obtainVal,String key) {
			// TODO Auto-generated constructor stub
			this.obtainVal=obtainVal;
			this.key=key;
			this.scId=scId;
		}
		@Override
		public void run() {
			//同时将springSecurity和当前线程的请求属性同步到此线程中
			// TODO Auto-generated method stub
			Object res =obtainVal.obtionVal(scId);
			mongoService.remove("{key:'"+key+"'}", CACHE_TABLE_NAME, null);
			mongoService.save(new Gson().toJson(new CacheData(key, new Gson().toJson(res))),
					CACHE_TABLE_NAME, null);
		}
	}
	public static interface AsyncObtainVal{
		public Object obtionVal(String scId);
	}
	/**
	 * 添加缓存数据到mongo数据库中
	 * @param key 缓存标识
	 * @param val 缓存值，可以是任何对象，方法将自动转换成json串进行保存
	 * @param obtainVal 异步函数，如果此函数不为null，则通过异步进行保存val从函数结果中获取参数val将不使用，否则则同步保存参数中的key和val
	 */
	public void add(final String key, Object val,final AsyncObtainVal obtainVal,String owner) {
		if(Tools.isEmpty(key)) {
			throw new ValidationException("Both key can't be null");
		}
		if(obtainVal==null) {
			mongoService.save(new Gson().toJson(new CacheData(key, new Gson().toJson(val))),
					CACHE_TABLE_NAME, null);
			return;
		}
		if(!lastUpdate.containsKey(key)||System.currentTimeMillis()-lastUpdate.get(key)>UPDATE_INTERVAL) {
			lastUpdate.put(key, System.currentTimeMillis());
			new BackThread(owner,obtainVal,key).start();
		}else {
			log.info(key+"->"+(UPDATE_INTERVAL/1000/60)+"分钟内已经更新过，不再重复刷新");
		}
	}
	public String get(String key) {
		List<?> cache = mongoService.query("{key:'"+key+"'}", CACHE_TABLE_NAME, null);
		Object result = null;
		if (cache.size() > 0) {
			result = cache.get(0);
		}else {
			return null;
		}
		CacheData res = new Gson().fromJson(result.toString(), CacheData.class);
		return res.getVal().toString();
	}
	public void remove(String key) {
		mongoService.remove("{key:'"+key+"'}", CACHE_TABLE_NAME, null);
	}
}
