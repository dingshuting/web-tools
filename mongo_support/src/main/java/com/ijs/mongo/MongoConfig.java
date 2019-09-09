package com.ijs.mongo;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ijs.mongo.dao.MongoDao;
import com.mongodb.Mongo;

@Configuration
public class MongoConfig {
	//查询区间段的开始字段常量
	public static final String CRITERIA_START="start";
	//查询区间段的结束字段常量
	public static final String CRITERIA_END="end";
	//id的主键常量
	public static final String _ID="_id";
	//由于数据表查询会很频繁所以将其放置在内存中，减少查询处理时间，系统初始化时，初始化其内容
	public static Map<String, String> SYS_DATA_TABLE_MAP = new HashMap<String, String>();
	public interface IsPass{
		//待审核
		public static final int WATING_VERIFY=2;
		//通过
		public static final int PASS=1;
		
		//不通过
		public static final int NOTPASS=0;
	}
	public interface Status{
		/**
		 * 正常状态
		 */
		public static final int NORMAL=1;
		/**
		 * 无效状态
		 */
		public static final int DISABLE=0;
		public static final int REMOVED=-1;
		/**
		 * 待审核状态
		 */
		public static final int WATING_VERIFY=2;
		/**
		 * 拒绝状态
		 */
		public static final int REFUSE=3;
		
	}
	@Bean
	public MongoDao mongoDao(Mongo mongo){
		//spring-boot-app-database
		return new MongoDao(mongo,"local");
	}
}
