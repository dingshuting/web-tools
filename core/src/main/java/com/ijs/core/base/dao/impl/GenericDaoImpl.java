/**
 * 
 */
package com.ijs.core.base.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.stereotype.Service;

import com.ijs.core.base.dao.GenericDao;

/**
 *  @author Tairong Zou
 *
 */
public abstract class GenericDaoImpl<T,  PK extends Serializable> implements GenericDao<T,  PK> {
	
	@PersistenceContext
	private EntityManager em;
	private EntityManagerFactory emf;
	private Class<T> entityClass;
	
	
	@SuppressWarnings("unchecked")
	public GenericDaoImpl() {
		entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	@PersistenceUnit
	public void setEmf(EntityManagerFactory emf) {
		this.emf = emf;
	}
	
	public EntityManagerFactory getEmf() {
		return emf;
	}

	/**
	 * get all entity
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> getAll() {
		return this.em.createQuery("from " + this.entityClass.getName()).getResultList();	
	}
	
	/**
	 * save entity
	 * @param id
	 * @return
	 */
	public T get(PK id){
		return this.em.find(this.entityClass, id);
	}
	
	/**
	 * save entity
	 * @param entity
	 * @return
	 */
	public void save(T entity) {
		this.em.persist(entity);
	}
	
	/**
	 * refresh entity
	 * @param entity
	 * @return
	 */
	public void refresh(T entity){
		this.em.refresh(entity);
	}
		
	/**
	 * save or update entity
	 * @param entity
	 * @return
	 */
	public void update(T entity){
		this.em.merge(entity);
	}
	
	/**
	 * remove entity
	 * @param id
	 */
	public void remove(PK id) {
		this.em.remove(this.get( id));
	}
	
	@Override
	public void flush() {
       this.em.flush();		
	}

	public List<T> find(String jpql){
		return this.find(jpql, (Object[])null);
	}
	
	public List<T> find(String jpql, Class<T> entity){
		return this.find(jpql, entity, (Object[])null);
	}
	
	public List<T> find(String jpql, Integer pageNo,Integer objectPerPage) {		
		return this.find(jpql, pageNo, objectPerPage, (Object[])null);
	}
	
	public List<T> find(String jpql, Class<T> entity, Integer pageNo,Integer objectPerPage) {		
		return this.find(jpql, entity, pageNo, objectPerPage, (Object[]) null);
	}
	
	@SuppressWarnings("unchecked")
	public List<T> find(String jpql, final Object... values) {
		Query queryObject = em.createQuery(jpql);
		
		prepareQuery(queryObject);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				queryObject.setParameter(i + 1, values[i]);
			}
		}
		return queryObject.getResultList();
	}
			
	@SuppressWarnings("unchecked")
	public List<T> find(String jpql, Integer pageNo,Integer objectPerPage
				, final Object... values) {
		
		Query queryObject = em.createQuery(jpql);
		
		prepareQuery(queryObject);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				queryObject.setParameter(i + 1, values[i]);
			}
		}
		
		queryObject.setFirstResult((pageNo-1)*objectPerPage);
		queryObject.setMaxResults(objectPerPage);
		List<T> list=queryObject.getResultList();
		if(list==null||list.size()==0){
			if(pageNo>1){
				pageNo=pageNo-1;
				queryObject.setFirstResult((pageNo-1)*objectPerPage);
			}
		}
		return queryObject.getResultList();
	}
		
	@SuppressWarnings("unchecked")
	public List<T> find(String jpql, Map<String, ?> params) {
		Query queryObject = em.createQuery(jpql);
		
		prepareQuery(queryObject);
		if (params != null) {
			for (Map.Entry<String, ?> entry : params.entrySet()) {
				queryObject.setParameter(entry.getKey(), entry.getValue());
			}
		}
		return queryObject.getResultList();
	}
		
	@SuppressWarnings("unchecked")
	public List<T> find(String jpql, Map<String, ?> params, Integer pageNo,
			Integer objectPerPage) {
		
		Query queryObject = em.createQuery(jpql);
		
		prepareQuery(queryObject);
		if (params != null) {
			for (Map.Entry<String, ?> entry : params.entrySet()) {
				queryObject.setParameter(entry.getKey(), entry.getValue());
			}
		}
		
		queryObject.setFirstResult((pageNo-1)*objectPerPage);
		queryObject.setMaxResults(objectPerPage);
		
		return queryObject.getResultList();
	}
	
	public int count(String propertyName, String jpql) {		
		return this.count(propertyName, jpql, (Object[]) null);
	}
	
	public int count(String propertyName, String jpql, final Object... values) {
		
		StringBuffer jpqlSB = new StringBuffer();	
		if(jpql.startsWith("select")){
			jpqlSB.append("select count(").append(propertyName).append(") ")
				.append(jpql.substring(jpql.indexOf("from")));
		}else{
			jpqlSB.append("select count(").append(propertyName).append(") ")
				.append(jpql);
		}
		
		int groupby_index = jpqlSB.indexOf("group by");
		int orderby_index = jpqlSB.indexOf("order by");
		
		if(groupby_index>0){
			orderby_index = groupby_index;
		}
		
		Query queryObject = null;
		if(orderby_index>0){
			queryObject = em.createQuery(jpqlSB.substring(0, orderby_index));
		}else{
			queryObject = em.createQuery(jpqlSB.toString());
		}
		
		prepareQuery(queryObject);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				queryObject.setParameter(i + 1, values[i]);
			}
		}
		
		return ((Long) queryObject.getSingleResult()).intValue();
	}
	
	public int count(String propertyName, String jpql, Map<String, ?> params) {
		
		StringBuffer jpqlSB = new StringBuffer();	
		if(jpql.startsWith("select")){
			jpqlSB.append("select count(").append(propertyName).append(") ")
				.append(jpql.substring(jpql.indexOf("from")));
		}else{
			jpqlSB.append("select count(").append(propertyName).append(") ")
				.append(jpql);
		}
		
		int groupby_index = jpqlSB.indexOf("group by");
		int orderby_index = jpqlSB.indexOf("order by");
		
		if(groupby_index>0){
			orderby_index = groupby_index;
		}
		
		Query queryObject = null;
		if(orderby_index>0){
			queryObject = em.createQuery(jpqlSB.substring(0, orderby_index));
		}else{
			queryObject = em.createQuery(jpqlSB.toString());
		}
		
		prepareQuery(queryObject);
		if (params != null) {
			for (Map.Entry<String, ?> entry : params.entrySet()) {
				queryObject.setParameter(entry.getKey(), entry.getValue());
			}
		}
		
		return ((Long) queryObject.getSingleResult()).intValue();
	}

	public int executeJPQL(String updateOrDeleteJPQL) {		
		return this.executeJPQL(updateOrDeleteJPQL,  (Object[]) null);
	}

	public int executeJPQL(String updateOrDeleteJPQL, final Object... values) {
		Query queryObject = em.createQuery(updateOrDeleteJPQL);
		
		prepareQuery(queryObject);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				queryObject.setParameter(i + 1, values[i]);
			}
		}
				
		return queryObject.executeUpdate();
	}
	
	public int executeJPQL(String updateOrDeleteJPQL, Map<String, ?> params) {
		Query queryObject = em.createQuery(updateOrDeleteJPQL);
		
		prepareQuery(queryObject);
		if (params != null) {
			for (Map.Entry<String, ?> entry : params.entrySet()) {
				queryObject.setParameter(entry.getKey(), entry.getValue());
			}
		}
				
		return queryObject.executeUpdate();
	}

	public List<T> findByNamed(String sqlName) {		
		return this.findByNamed(sqlName, (Object[]) null);
	}

	public List<T> findByNamed(String sqlName, Integer pageNo,
			Integer objectPerPage) {
		return this.findByNamed(sqlName, pageNo, objectPerPage, (Object[]) null);
	}

	@SuppressWarnings("unchecked")
	public List<T> findByNamed(String sqlName, Object... values) {
		Query queryObject = em.createNamedQuery(sqlName);
		
		prepareQuery(queryObject);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				queryObject.setParameter(i + 1, values[i]);
			}
		}
		
		return queryObject.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<T> findByNamed(String sqlName, Integer pageNo,
			Integer objectPerPage, Object... values) {
		Query queryObject = em.createNamedQuery(sqlName);
		
		prepareQuery(queryObject);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				queryObject.setParameter(i + 1, values[i]);
			}
		}
		
		queryObject.setFirstResult((pageNo-1)*objectPerPage);
		queryObject.setMaxResults(objectPerPage);
		
		return queryObject.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<T> findByNamed(String sqlName, Map<String, ?> params) {
		Query queryObject = em.createNamedQuery(sqlName);
		
		prepareQuery(queryObject);
		if (params != null) {
			for (Map.Entry<String, ?> entry : params.entrySet()) {
				queryObject.setParameter(entry.getKey(), entry.getValue());
			}
		}
		
		return queryObject.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<T> findByNamed(String sqlName, Map<String, ?> params,
			Integer pageNo, Integer objectPerPage) {
		Query queryObject = em.createNamedQuery(sqlName);
		
		prepareQuery(queryObject);
		if (params != null) {
			for (Map.Entry<String, ?> entry : params.entrySet()) {
				queryObject.setParameter(entry.getKey(), entry.getValue());
			}
		}
		
		queryObject.setFirstResult((pageNo-1)*objectPerPage);
		queryObject.setMaxResults(objectPerPage);
		
		return queryObject.getResultList();
	}
	
	public int executeNamed(String updateOrDeleteSql) {
		return this.executeNamed(updateOrDeleteSql,  (Object[]) null);
	}

	public int executeNamed(String updateOrDeleteSql, Object... values) {
		Query queryObject = em.createNamedQuery(updateOrDeleteSql);
		
		prepareQuery(queryObject);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				queryObject.setParameter(i + 1, values[i]);
			}
		}
				
		return queryObject.executeUpdate();
	}

	public int executeNamed(String updateOrDeleteSql, Map<String, ?> params) {
		Query queryObject = em.createNamedQuery((updateOrDeleteSql));
		
		prepareQuery(queryObject);
		if (params != null) {
			for (Map.Entry<String, ?> entry : params.entrySet()) {
				queryObject.setParameter(entry.getKey(), entry.getValue());
			}
		}
				
		return queryObject.executeUpdate();
	}
	
	/* (non-Javadoc)
	 * @see com.hi332.wms.base.dao.GenericDao#countByNamed(java.lang.String)
	 */
	public int countByNamed(String named) {
		return countByNamed(named, (Object[]) null);
	}

	/* (non-Javadoc)
	 * @see com.hi332.wms.base.dao.GenericDao#countByNamed(java.lang.String, java.lang.Object[])
	 */
	public int countByNamed(String named, Object... values) {
		
		Query queryObject = em.createNamedQuery(named);
		
		prepareQuery(queryObject);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				queryObject.setParameter(i + 1, values[i]);
			}
		}
				
		return ((Integer)queryObject.getSingleResult());
	}

	/* (non-Javadoc)
	 * @see com.hi332.wms.base.dao.GenericDao#countByNamed(java.lang.String, java.util.Map)
	 */
	public int countByNamed(String named, Map<String, ?> params) {
		Query queryObject = em.createNamedQuery(named);
		
		prepareQuery(queryObject);
		if (params != null) {
			for (Map.Entry<String, ?> entry : params.entrySet()) {
				queryObject.setParameter(entry.getKey(), entry.getValue());
			}
		}
		
		return ((Integer)queryObject.getSingleResult());
	}

	
	
	/* (non-Javadoc)
	 * @see com.hi332.wms.base.dao.GenericDao#total(java.lang.String, java.lang.String)
	 */
	public double total(String propertyName, String jpql) {
		return this.total(propertyName, jpql, (Object[]) null);
	}

	/* (non-Javadoc)
	 * @see com.hi332.wms.base.dao.GenericDao#total(java.lang.String, java.lang.String, java.lang.Object[])
	 */
	public double total(String propertyName, String jpql, Object... values) {
		StringBuffer jpqlSB = new StringBuffer();	
		if(jpql.startsWith("select")){
			jpqlSB.append("select sum(").append(propertyName).append(") ")
				.append(jpql.substring(jpql.indexOf("from")));
		}else{
			jpqlSB.append("select sum(").append(propertyName).append(") ")
				.append(jpql);
		}
		
		int groupby_index = jpqlSB.indexOf("group by");
		int orderby_index = jpqlSB.indexOf("order by");
		
		if(groupby_index>0){
			orderby_index = groupby_index;
		}
		
		Query queryObject = null;
		if(orderby_index>0){
			queryObject = em.createQuery(jpqlSB.substring(0, orderby_index));
		}else{
			queryObject = em.createQuery(jpqlSB.toString());
		}
		
		prepareQuery(queryObject);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				queryObject.setParameter(i + 1, values[i]);
			}
		}
		
		Object obj = queryObject.getSingleResult();
		
		if(obj instanceof Double){
			return (Double)obj;
		}else{
			return ((BigDecimal)obj).doubleValue();
		}
	}

	/* (non-Javadoc)
	 * @see com.hi332.wms.base.dao.GenericDao#total(java.lang.String, java.lang.String, java.util.Map)
	 */
	public double total(String propertyName, String jpql, Map<String, ?> params) {
		StringBuffer jpqlSB = new StringBuffer();	
		if(jpql.startsWith("select")){
			jpqlSB.append("select sum(").append(propertyName).append(") ")
				.append(jpql.substring(jpql.indexOf("from")));
		}else{
			jpqlSB.append("select sum(").append(propertyName).append(") ")
				.append(jpql);
		}
		
		int groupby_index = jpqlSB.indexOf("group by");
		int orderby_index = jpqlSB.indexOf("order by");
		
		if(groupby_index>0){
			orderby_index = groupby_index;
		}
		
		Query queryObject = null;
		if(orderby_index>0){
			queryObject = em.createQuery(jpqlSB.substring(0, orderby_index));
		}else{
			queryObject = em.createQuery(jpqlSB.toString());
		}
		
		prepareQuery(queryObject);
		if (params != null) {
			for (Map.Entry<String, ?> entry : params.entrySet()) {
				queryObject.setParameter(entry.getKey(), entry.getValue());
			}
		}
		
		Object obj = queryObject.getSingleResult();
		
		if(obj instanceof Double){
			return (Double)obj;
		}else{
			return ((BigDecimal)obj).doubleValue();
		}
	}

	public Object findOne(String jpql) {
		return em.createQuery(jpql).getSingleResult();
	}
	
	public Object findOne(String jpql, Object... values) {
		
		Query queryObject = em.createQuery(jpql);
		
		prepareQuery(queryObject);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				queryObject.setParameter(i + 1, values[i]);
			}
		}
		
		return queryObject.getSingleResult();
	}
	
	private void prepareQuery(Query query) {
		if (this.emf != null) {
			EntityManagerFactoryUtils.applyTransactionTimeout(query, this.emf);
		}
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> executeSqlQuery(String sql,Class<T> clazz) {
		// TODO Auto-generated method stub
		Query query=em.createNativeQuery(sql, clazz);
		List<T> list=query.getResultList();
		return list;
	}
	@Override
	public List<?> executeSqlQuery(String sql){
		Query query=em.createNativeQuery(sql);
		List<?> list=query.getResultList();
		return list;
	}
	@Override
	public List<?> executeSqlQuery(String sql,final Object...objects){
		Query query=em.createNativeQuery(sql);
		if (objects != null) {
			for (int i = 0; i < objects.length; i++) {
				query.setParameter(i + 1, objects[i]);
			}
		}
		List<?> list=query.getResultList();
		return list;
	}
	@Override
	public List<?> executeSqlQuery(String sql,Map<String,?> maps){
		Query query=em.createNativeQuery(sql);
		if (maps != null) {
			for (Map.Entry<String, ?> entry : maps.entrySet()) {
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}
		
		List<?> list=query.getResultList();
		return list;
	}
	/**
	 * sql查询，并且以List<Map<columnName,columnValue>>的形式返回数据
	 */
	public	List<Map<String,Object>> executeSqlMapQuery(String sql){
		Session session=(Session) em.getDelegate();
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> mapList=session
	     .createSQLQuery(sql)
	     .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
		return mapList;
	}
}
