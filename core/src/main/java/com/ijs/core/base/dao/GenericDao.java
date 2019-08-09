package com.ijs.core.base.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


/**
 *  @author Tairong Zou
 *
 */
public interface GenericDao <T, PK extends Serializable> {

	List<T> getAll();
    T get(PK id);

    void save(T entity);
    void refresh(T entity);
    void update(T entity);
    void remove(PK id);
    void flush();
    
    Object findOne(String ql);
    Object findOne(String ql, final Object... values);
    
    List<T> find(String ql);
    List<T> find(String ql, final Integer pageNo, final Integer objectPerPage);
        
    List<T> find(String ql, final Object... values);
    List<T> find(String ql, final Integer pageNo, final Integer objectPerPage, final Object... values);
    
    List<T> find(String ql, Map<String, ?> params);
    List<T> find(String ql, Map<String, ?> params, final Integer pageNo, final Integer objectPerPage);
        
    int count(String propertyName, String ql);
    int count(String propertyName, String ql, final Object... values);
    int count(String propertyName, String ql, Map<String, ?> params);
    
    double total(String propertyName, String ql);
    double total(String propertyName, String ql, final Object... values);
    double total(String propertyName, String ql, Map<String, ?> params);
    
    List<T> findByNamed(String named);
    List<T> findByNamed(String named, final Integer pageNo, final Integer objectPerPage);  
    
    List<T> findByNamed(String named, final Object... values);
    List<T> findByNamed(String named, final Integer pageNo, final Integer objectPerPage, final Object... values);
    
    List<T> findByNamed(String named, Map<String, ?> params);
    List<T> findByNamed(String named, Map<String, ?> params, final Integer pageNo, final Integer objectPerPage);
    
    int countByNamed(String named);
    int countByNamed(String named, final Object... values);
    int countByNamed(String named, Map<String, ?> params);
    
    
    //int countBySqlName(String propertyName, String ql,  Integer pageNo, Integer objectPerPage);
    //int countBySqlName(String propertyName, String ql,  Integer pageNo, Integer objectPerPage, final Object... values);
    //int countBySqlName(String propertyName, String ql, Map<String, ?> params, Integer pageNo, Integer objectPerPage);
        
    int executeJPQL(String updateOrDeleteql);
    int executeJPQL(String updateOrDeleteql, final Object... values);
    int executeJPQL(String updateOrDeleteql, Map<String, ?> params);
    
    int executeNamed(String named);
    int executeNamed(String named, final Object... values);
    int executeNamed(String named, Map<String, ?> params);
    
    List<T> executeSqlQuery(String sql,Class<T> clazz);
    List<?> executeSqlQuery(String sql);
    List<?> executeSqlQuery(String sql, final Object... values);
    List<?> executeSqlQuery(String sql, Map<String, ?> params);
    //int execSQL(Connection conn, String sql)throws SQLException;
    //int execSQL(Connection conn, String sql, final Object... values)throws SQLException; 
    //Object findOneBySql(Connection conn, String sql, final Object... values)throws SQLException;
    //List<Object> findBySql(Connection conn, String sql, final Object... values)throws SQLException;
    List<Map<String,Object>> executeSqlMapQuery(String sql);
}