package com.ijs.mongo.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.DocumentCallbackHandler;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.ijs.mongo.MongoConfig;
import com.ijs.mongo.model.BSONObjectUtil;
import com.ijs.mongo.validate.IValidateRuleColMapper;
import com.ijs.mongo.validate.Validate;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBRef;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;

@Repository
public class MongoDao extends MongoTemplate {
	protected final transient Log log = LogFactory.getLog(getClass());
	public MongoDao(Mongo mongo, String databaseName) {
		super(mongo, databaseName);
		// TODO Auto-generated constructor stub
	}

	public MongoDao(Mongo mongo) {

		super(mongo, mongo.getDatabaseNames().get(0));
	}

	/**
	 * 获取数据表的全部数据
	 * 
	 * @param collectionName
	 *            表名
	 * @return
	 */
	public List<DBObject> getAll(String collectionName) {
		
		return getAll(collectionName, null);
	}

	/**
	 * 获取数据表的全部数据,并且可以对单条数据进行过滤动作
	 * 
	 * @param collectionName
	 *            表名
	 * @param hander
	 *            过滤的接口实例
	 * @return 查询后的集合
	 */
	public List<DBObject> getAll(String collectionName, final DocumentCallbackHandler hander) {
		final List<DBObject> objs = new ArrayList<DBObject>();
		log.debug("executing getAll of MongoDao and the collectionName is :"+collectionName);
		this.executeQuery(new Query(), collectionName, new DocumentCallbackHandler() {

			public void processDocument(DBObject dbObject) throws MongoException, DataAccessException {
				// TODO Auto-generated method stub
				objs.add(dbObject);
				if (hander != null) {
					hander.processDocument(dbObject);
				}
			}
		});
		return objs;
	}

	/**
	 * 获取指定key的实体对象
	 * 
	 * @param collectionName
	 *            集合名称
	 * @param key
	 *            主键值，为String的唯一值
	 * @return 数据对象
	 */
	public DBObject get(String collectionName, Object key) {
		Query query = new Query();
		query.addCriteria(Criteria.where(MongoConfig._ID).is(key));
		List<DBObject> objs = this.find(query, collectionName);
		return objs.size() > 0 ? objs.get(0) : null;
	}

	/**
	 * 根据输入的查询条件进行指定数据集的查询动作
	 * 
	 * @param query
	 *            查询对象，具体使用方法参考相关的API文档
	 * @param collectionName
	 *            数据集合名称
	 * @return 查询的结果对象结合
	 */
	public List<DBObject> find(Query query, String collectionName) {

		return find(query, collectionName, null);
	}

	public List<DBObject> find(Query query, String collectionName, final DocumentCallbackHandler handler) {
		final List<DBObject> objs = new ArrayList<DBObject>();
		log.debug("executing find method in MongoDao that for get the list of result from '"+collectionName+"' and the Query is :"+query.toString());
		executeQuery(query, collectionName, new DocumentCallbackHandler() {
			public void processDocument(DBObject dbObject) throws MongoException, DataAccessException {
				// TODO Auto-generated method stub
				
				if (handler != null) {
					handler.processDocument(dbObject);
				} else {
					objs.add(dbObject);
				}

			}
		});
		return objs;
	}

	/**
	 * 根据输入的查询条件进行指定数据集的查询动作
	 * 
	 * @param query
	 *            查询对象，具体使用方法参考相关的API文档
	 * @param collectionName
	 *            数据集合名称
	 * @return 查询的结果对象结合
	 */
	public List<DBObject> find(Query query, String collectionName, Integer currentPage, Integer preNum) {
		
		return find(query, collectionName, currentPage, preNum, null);
	}
	/**
	 * 分页查询，增加自己对结果的处理接口
	 * 
	 * @param query
	 * @param collectionName
	 * @param currentPage
	 * @param preNum
	 * @param handler
	 * @return
	 */
	public List<DBObject> find(Query query, String collectionName, Integer currentPage, Integer preNum,
			final DocumentCallbackHandler handler) {
		
		//count(new Criteria(), collectionName,null,null);
		
		log.debug("executing find method in MongoDao that for get the list of result from "+collectionName+" by paging  and the Query is :"+query.toString());
		if (query == null) {
			return this.getAll(collectionName);
		}
		final List<DBObject> objs = new ArrayList<DBObject>();
		if (currentPage != null && preNum != null) {
			query.skip((currentPage-1) * preNum);
			query.limit(preNum);
		}
		executeQuery(query, collectionName, new DocumentCallbackHandler() {
			public void processDocument(DBObject dbObject) throws MongoException, DataAccessException {
				// TODO Auto-generated method stub
				if (handler != null) {
					handler.processDocument(dbObject);
				} else {
					objs.add(dbObject);
				}

			}
		});
		return objs;
	}
	
	/**
	 * 统计查询
	 * 
	 * @param criteria 查询条件
	 * @param collectionName  数据库名
	 * @param groups  数据格式  groups  = "test, ..."; 一个或多个分组字段，按顺序分组
	 * @param countCols 数据格式   countCols = "status, ..." 一个或多个要统计的字段
	 * @return 返回单个字段的分组统计map  或者 返回当前查询数据条数
	 */
	public Map count(Criteria criteria, String collectionName,String groups,
			String countCols) {
		//criteria.where("status").is(1);
		/*collectionName = "videos";
		countCols="test";
		groups  = "status";*/
		Map map = new HashMap<String, String>();
		if(groups!=null && !groups.isEmpty() && !countCols.isEmpty() &&countCols!=null){
			String splis[]  = countCols.split(",");
			for (int i = 0; i < splis.length; i++) {
				 Aggregation aggregation = Aggregation.newAggregation(
						 //查询条件
						 Aggregation.match(criteria),
						 //分组条件和统计字段
						 Aggregation.group(groups).sum(splis[i]).as(splis[i])
				);
				 //聚合条件，查询库名，查询结构转换对象类
		         AggregationResults<Object> aggregate  = aggregate(aggregation,collectionName,Object.class);
		         //获取查询值
		         BasicDBList bdbl =(BasicDBList) aggregate.getRawResults().get("result");
		         for (int j = 0; j < bdbl.size(); j++) {
		         	BasicDBObject obj=(BasicDBObject) bdbl.get(j);
		         	map.put(splis[i], obj.getString(splis[i]));
				 }
			}
			System.out.println(map.toString());
		}else{
			Query query = new Query();
			query.addCriteria(criteria);
			Object total = (int)count(query, collectionName);
			map.put("total",total.toString());
		}
		return map;
		
		/*
		 * countCols = "[{'status':'status'},{'test':'test'}]";
		 * JSONArray json = new JSONArray(countCols);
			for (int i = 0; i < json.length(); i++) {
			JSONObject jsObj = ((JSONObject)json.get(i));
			Iterator it = jsObj.keys();
            while (it.hasNext()) {  
            	Long total = 0L;
                String key = (String) it.next(); 
                Object value = jsObj.get(key);
                //聚合对象
                Aggregation aggregation = Aggregation.newAggregation(Aggregation.match(criteria),Aggregation.group(value.toString()).sum(value.toString()).as(value.toString()));
                AggregationResults<Object> aggregate  = aggregate(aggregation,collectionName,Object.class);
                BasicDBList bdbl =(BasicDBList) aggregate.getRawResults().get("result");
                for (int j = 0; j < bdbl.size(); j++) {
                	 BasicDBObject obj=(BasicDBObject) bdbl.get(j);
					System.out.println(obj.getString("_id")+"  ______  "+obj.getString(value.toString()));
				}
            }
		}*/
		
		  /* 
         *  String reduce = "function(doc, aggr){" +
                 "            aggr.count += 1;" +
                 "        }";
         DBObject result = getCollection(collectionName).group(new BasicDBObject(value.toString(), 1), 
                 query.getQueryObject(), 
                 new BasicDBObject("count", 0),
                 reduce);
         
         Map map = result.toMap();*/
		
		/* GroupOperation groupOperation = Aggregation.group("status").sum("status").as("titleTotal");  
        Aggregation aggregation = Aggregation.newAggregation(groupOperation);
        Object obj = aggregate(aggregation,collectionName,Object.class);*/
	}

	/**
	 * 保存指定的对象到指定的数据表中
	 * 
	 * @param collectionName
	 *            数据表名称
	 * @param obj
	 *            要保存的数据对象
	 * @return 包含主键的对象
	 */
	public Object save( DBObject obj,String collectionName) {
		log.debug("save the obj("+obj.toString()+") to "+collectionName);
		Object k=this.insertDBObject(collectionName, obj, Object.class);
		if(k!=null){
			obj.put(MongoConfig._ID, k);
		}
		return obj;
	}

	/**
	 * 修改指定数据集的对象
	 * 
	 * @param collectionName
	 *            数据集名称
	 * @param obj
	 *            数据对象，更新动作_id字段不能为空
	 * @param exclude
	 *            不更新的字段
	 * @return 受影响的行数
	 */
	public int update(Query query, String collectionName, DBObject obj, String... exclude) {
		log.debug("executing update method of "+collectionName+" Query:"+query.toString()+" and to"+obj.toString());
		Update update= Update.fromDBObject(obj, exclude);
		return this.updateFirst(query,update, collectionName).getN();
	}

	public WriteResult delete(Query query, String collectionName) {
		WriteResult result = super.remove(query, collectionName);
		return result;
	}

	/**
	 * 根据查询对象生成对应的查询条件的对象,查询的规则如下：<br/>
	 * 1、所有的查询key（即数据集中的列）均为传入的查询的json对象key。<br/>
	 * 2、根据key所对应的值的不同类型，来区分具体的查询方式，具体为：<br/>
	 * <p style="padding:10px">
	 *①：当字段对应的值为int或者long类型时，直接采用等于（equality）的条件<br/>
	 *②：当字段对应的值为Array类型时，采用in（$in）的查询条件<br/>
	 *③：当字段对应的值为String类型时，采用like的查询条件<br/>
	 *④：当字段对应的值为Object类型时，分为如下两中情况：<br/>
	 *		<tt style="padding:20px">
	 *			1:当对象包含2个字段并且分别是start、end，此时作为区间段进行查询，如：price 在 100 和300 直接就可写成<code>{price:{start:100,end:300}}</code><br/>
	 *		</tt>
	 *		<tt style="padding:20px">
	 *			2:当为其它内嵌对象时，则直接产生其内置对象的查询语句，生产的语句key为：父key.子key的格式，如：user对象{name:sa,role:{name:'w'}},生成的子对象查询语句key为role.name;
	 *		</tt>
	 * </p>
	 * @param querys JSONObject生成查询的json对象
	 * @return Query查询对象，用于MongoDB的查询。
	 */
	public Criteria generateQuery(Criteria pc,JSONObject qo){
		Criteria cri=null;
			for(String qk:qo.keySet()){
				Object qv=qo.get(qk);
				if(qo.get(qk).toString().isEmpty())continue;
				if(pc!=null){
					cri=pc.and(pc.getKey()+"."+qk);
				}else if(cri!=null){
					if(qv instanceof JSONObject){
						cri=cri.and(qk).exists(true);
					}else{
						cri=cri.and(qk);
					}
					
				}else{
					if(qv instanceof JSONObject){
						cri=Criteria.where(qk).exists(true);
					}else{
						cri=Criteria.where(qk);
					}
					
				}
				if(qv instanceof Integer || qv instanceof Long){
					cri.is(qv);
				}else if(qv instanceof String&&!qv.toString().isEmpty()){
					if(qk.equals(MongoConfig._ID)){
						cri.is(qv);
					}else{
						cri.regex(".*"+qv+".*");
					}
				}else if(qv instanceof JSONObject){
					JSONObject tv=(JSONObject) qv;
					//判断是否是查询区间段
					if(tv.has(MongoConfig.CRITERIA_START)&&tv.has(MongoConfig.CRITERIA_END)){
						cri.gte(tv.get(MongoConfig.CRITERIA_START)).lte(tv.get(MongoConfig.CRITERIA_END));
					}else if(tv.has(MongoConfig.CRITERIA_START)){
						cri.gte(tv.get(MongoConfig.CRITERIA_START));
					}else if(tv.has(MongoConfig.CRITERIA_END)){
						cri.lte(tv.get(MongoConfig.CRITERIA_END));
					}else{
						//否则则是内嵌对象的查询
						generateQuery(cri,tv);
					}
				}else if (qv instanceof JSONArray){	
					JSONArray tv=(JSONArray) qv;
					List<Object> vs=new ArrayList<Object>();
					for(int i=0;i<tv.length();i++){
						vs.add(tv.get(i));
					}
					cri.in(vs);
				}
			}
		return cri;
	}
	public static void main(String[] args) {
		
		try {
			//testValidate();
			 MongoDao dao = new MongoDao(new Mongo("192.168.1.7", 27017));
			 JSONObject jsonobj = new JSONObject("{name:'最新的测试数据'}");
			 DBObject obj=BSONObjectUtil.getDBObject(jsonobj);
			 obj.put("guotu", new DBRef("guotu", "589ae83bf8242547bcf1cb95"));
			// Object key=dao.save(obj, "project_extra");
			for (String k : jsonobj.keySet()) {
				System.out.println(k + "<--->" + jsonobj.get(k).getClass());
			}
			 
			 DBObject obj1=dao.get("project_zhaoshang","58a26f89c7fd100f64b74666");
			// System.out.println("- 保存后的--"+BSONObjectUtil.getJsonSimple(obj)); 
			 //obj1.put("name", "修改后的新对象111");
			 	//DBObject dbObject=new BasicDBObject();
			 	obj1.put("name", "这是测试的名词101293123 ");
			 	obj1.put("update_time", "这是测试的shij  ");
			 	obj1.removeField("_id");
			 	dao.update(new Query(Criteria.where("_id").is("58a26f89c7fd100f64b74666")),"project_zhaoshang",obj1, "");
			 //int i= dao.update(null, "project_extra", obj1,"");
			  //System.out.println("-修改后的--"+BSONObjectUtil.getJsonSimple(dao.get("user",key)));
			  //Query query=new Query();
			  //query.addCriteria(Criteria.where("_id").is(new ObjectId("58995fcb7accdd4a44294340")));
			  //List list=dao.find(query, "project_extra");
			  ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			  //System.out.println("---size--->"+list.size());
			  // WriteResult wr=dao.remove(new
				// Query(Criteria.where("_id").is(obj.get("_id"))), "user");
				// System.out.println("---删除影响的行数--"+wr.getN()+"--id--"+wr.getUpsertedId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void testValidate(){
		 List<IValidateRuleColMapper> cols=new ArrayList<IValidateRuleColMapper>();
		 cols.add(new IValidateRuleColMapper() {
			
			public String getRules() {
				// TODO Auto-generated method stub
				return "notNull,min(16)";
			}
			
			public String getColCode() {
				// TODO Auto-generated method stub
				return "name";
			}

			public String getColCn() {
				// TODO Auto-generated method stub
				return "cnName";
			}
		});
		 JSONObject jsonobj = new JSONObject("{'name':'','age':80}");
		 List<String> errors= Validate.validate(jsonobj, cols,null);
		 System.out.println(errors.size()+"----"+Validate.getErrorString(errors));
	}
}
