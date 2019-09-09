package com.ijs.core.common.dao;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.MappedSuperclass;
import javax.persistence.OrderBy;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.persistence.Transient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.stereotype.Repository;

import com.ijs.core.base.model.FieldInterface;
import com.ijs.core.base.model.User;
import com.ijs.core.component.anno.DefaultConstructorForList;
import com.ijs.core.util.BeanUtils;
import com.ijs.core.util.model.JpqlModel;
/**
 * 提供对数据库基本的CURD操作
 * @author Dustin
 *
 */
@Repository("genericDao")
public class GenericDao {
	@PersistenceContext
	private EntityManager em;
	private EntityManagerFactory emf;
	private static final String STATUS_NAME="status";
	protected final static transient Log log = LogFactory.getLog(GenericDao.class);
	LocalVariableTableParameterNameDiscoverer localVariableTableParameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
	@PersistenceUnit
	public void setEmf(EntityManagerFactory emf) {
		this.emf = emf;
	}

	public EntityManagerFactory getEmf() {
		return emf;
	}

	/**
	 * get all entity
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> getAll(Class<T> t) {
		return this.em.createQuery("from " + t.getName()).getResultList();
	}

	/**
	 * get entity
	 * 获取指定主键id的实体，同时根据其字段名字规则，如果名字以Id结尾则代表其为外键关联，如果该名字去除Id后的名字变量存在，则根据查找该变量的id为带id字段值的实体
	 * @param id 主键id
	 * @return 单实体对象
	 */
	public <T> T get(Class<T> t, Serializable id) {
		T obj = this.em.find(t, id);
		try {
			if(obj==null){
				log.error("there is no emtity["+t.getName()+"] was found by the id["+id+"] you specified");
				return null;
			}
			PropertyDescriptor[] pds=BeanUtils.getPropertyDescriptors(t);
			for(PropertyDescriptor pd:pds) {
				if(null!=pd.getReadMethod().getAnnotation(Transient.class)&&pd.getReadMethod().getReturnType()!=null) {
					if(pd.getReadMethod().getReturnType().getAnnotation(Entity.class)!=null||(pd.getReadMethod().getReturnType().getSuperclass()!=null&&pd.getReadMethod().getReturnType().getSuperclass().getAnnotation(Entity.class)!=null)) {
						PropertyDescriptor fpd=BeanUtils.getPropertyDescriptor(t, pd.getName()+"Id");
						if(fpd!=null&& fpd.getReadMethod().invoke(obj)!=null) {
							pd.getWriteMethod().invoke(obj, em.find(pd.getReadMethod().getReturnType(), fpd.getReadMethod().invoke(obj)));
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}

	/**
	 * save entity
	 * 
	 * @param entity
	 * @return
	 */
	public <T> void save(T entity) {
		this.em.persist(entity);
	}

	/**
	 * refresh entity
	 * 
	 * @param entity
	 * @return
	 */
	public <T> void refresh(T entity) {
		this.em.refresh(entity);
	}

	/**
	 * save or update entity
	 * 
	 * @param entity
	 * @return
	 */
	public <T> void update(T entity) {
		this.em.merge(entity);
	}

	/**
	 * remove entity
	 * 
	 * @param id
	 */
	public <T> void remove(Class<T> t, Serializable id) {

		this.em.remove(this.get(t, id));
	}

	public <T> List<T> find(String jpql) {
		return find(jpql, (Object[]) null);
	}

	public <T> List<T> find(String jpql, Integer pageNo, Integer objectPerPage) {
		return this.find(jpql, pageNo, objectPerPage, (Object[]) null);
	}

	public <T> List<T> find(String jpql, final Object... values) {
		Query queryObject = em.createQuery(jpql);
		/*if (queryObject instanceof Object) {
			String className = jpql.substring(jpql.indexOf("form") + 5, jpql.indexOf("where"));
			try {
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
		prepareQuery(queryObject);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				queryObject.setParameter(i + 1, values[i]);
			}
		}

		List<T> list = queryObject.getResultList();
		return list;
	}

	public <T> List<T> find(String jpql, Integer pageNo, Integer objectPerPage, final Object... values) {

		Query queryObject = em.createQuery(jpql);

		prepareQuery(queryObject);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				queryObject.setParameter(i + 1, values[i]);
			}
		}

		queryObject.setFirstResult((pageNo - 1) * objectPerPage);
		queryObject.setMaxResults(objectPerPage);
		List<T> list = queryObject.getResultList();
		if (list == null || list.size() == 0) {
			if (pageNo > 1) {
				pageNo = pageNo - 1;
				queryObject.setFirstResult((pageNo - 1) * objectPerPage);
			}
		}
		return list;
	}

	public <T> List<T> find(String jpql, Map<String, ?> params) {
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
	public <T> List<T> find(String jpql, Map<String, ?> params, Integer pageNo, Integer objectPerPage) {

		Query queryObject = em.createQuery(jpql);

		prepareQuery(queryObject);
		if (params != null) {
			for (Map.Entry<String, ?> entry : params.entrySet()) {
				queryObject.setParameter(entry.getKey(), entry.getValue());
			}
		}

		queryObject.setFirstResult((pageNo - 1) * objectPerPage);
		queryObject.setMaxResults(objectPerPage);

		return queryObject.getResultList();
	}

	public int count(String propertyName, String jpql) {
		return this.count(propertyName, jpql, (Object[]) null);
	}

	public int count(String propertyName, String jpql, final Object... values) {

		StringBuffer jpqlSB = new StringBuffer();
		if (jpql.startsWith("select")) {
			jpqlSB.append("select count(").append(propertyName).append(") ")
					.append(jpql.substring(jpql.lastIndexOf("from")));
		} else {
			jpqlSB.append("select count(").append(propertyName).append(") ").append(jpql);
		}

		int groupby_index = -1;//jpqlSB.indexOf("group by");
		int orderby_index = -1;//jpqlSB.indexOf("order by");

		if (groupby_index > 0) {
			orderby_index = groupby_index;
		}

		Query queryObject = null;
		if (orderby_index > 0) {
			queryObject = em.createQuery(jpqlSB.substring(0, orderby_index));
		} else {
			queryObject = em.createQuery(jpqlSB.toString());
		}

		prepareQuery(queryObject);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				queryObject.setParameter(i + 1, values[i]);
			}
		}

		try {
			return ((Long) queryObject.getSingleResult()).intValue();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}

	public int count(String propertyName, String jpql, Map<String, ?> params) {

		StringBuffer jpqlSB = new StringBuffer();
		if (jpql.startsWith("select")) {
			jpqlSB.append("select count(").append(propertyName).append(") ")
					.append(jpql.substring(jpql.indexOf("from")));
		} else {
			jpqlSB.append("select count(").append(propertyName).append(") ").append(jpql);
		}

		int groupby_index = jpqlSB.indexOf("group by");
		int orderby_index = jpqlSB.indexOf("order by");

		if (groupby_index > 0) {
			orderby_index = groupby_index;
		}

		Query queryObject = null;
		if (orderby_index > 0) {
			queryObject = em.createQuery(jpqlSB.substring(0, orderby_index));
		} else {
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
		return this.executeJPQL(updateOrDeleteJPQL, (Object[]) null);
	}
	/**
	 * 执行一条非查询的语句
	 * @param updateOrDeleteJPQL
	 * @param values 以顺序占位形式的参数
	 * @return 受影响的行数
	 */
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
	/**
	 * 执行一条非查询的语句,使用键站位参数
	 * @param updateOrDeleteJPQL 语句
	 * @param params 
	 * @return
	 */
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

	public int executeNamed(String updateOrDeleteSql) {
		return this.executeNamed(updateOrDeleteSql, (Object[]) null);
	}

	public int executeNamed(String updateOrDeleteSql, Object... values) {
		Query queryObject = em.createNativeQuery(updateOrDeleteSql);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hi332.wms.base.dao.GenericDao#countByNamed(java.lang.String)
	 */
	public int countByNamed(String named) {
		return countByNamed(named, (Object[]) null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hi332.wms.base.dao.GenericDao#countByNamed(java.lang.String,
	 * java.lang.Object[])
	 */
	public int countByNamed(String named, Object... values) {

		Query queryObject = em.createNamedQuery(named);

		prepareQuery(queryObject);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				queryObject.setParameter(i + 1, values[i]);
			}
		}

		return ((Integer) queryObject.getSingleResult());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hi332.wms.base.dao.GenericDao#countByNamed(java.lang.String,
	 * java.util.Map)
	 */
	public int countByNamed(String named, Map<String, ?> params) {
		Query queryObject = em.createNamedQuery(named);

		prepareQuery(queryObject);
		if (params != null) {
			for (Map.Entry<String, ?> entry : params.entrySet()) {
				queryObject.setParameter(entry.getKey(), entry.getValue());
			}
		}

		return ((Integer) queryObject.getSingleResult());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hi332.wms.base.dao.GenericDao#total(java.lang.String,
	 * java.lang.String)
	 */
	public double total(String propertyName, String jpql) {
		return this.total(propertyName, jpql, (Object[]) null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hi332.wms.base.dao.GenericDao#total(java.lang.String,
	 * java.lang.String, java.lang.Object[])
	 */
	public double total(String propertyName, String jpql, Object... values) {
		StringBuffer jpqlSB = new StringBuffer();
		if (jpql.startsWith("select")) {
			jpqlSB.append("select sum(").append(propertyName).append(") ").append(jpql.substring(jpql.indexOf("from")));
		} else {
			jpqlSB.append("select sum(").append(propertyName).append(") ").append(jpql);
		}

		int groupby_index = jpqlSB.indexOf("group by");
		int orderby_index = jpqlSB.indexOf("order by");

		if (groupby_index > 0) {
			orderby_index = groupby_index;
		}

		Query queryObject = null;
		if (orderby_index > 0) {
			queryObject = em.createQuery(jpqlSB.substring(0, orderby_index));
		} else {
			queryObject = em.createQuery(jpqlSB.toString());
		}

		prepareQuery(queryObject);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				queryObject.setParameter(i + 1, values[i]);
			}
		}

		Object obj = queryObject.getSingleResult();

		if (obj instanceof Double) {
			return (Double) obj;
		} else {
			return ((BigDecimal) obj).doubleValue();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hi332.wms.base.dao.GenericDao#total(java.lang.String,
	 * java.lang.String, java.util.Map)
	 */
	public double total(String propertyName, String jpql, Map<String, ?> params) {
		StringBuffer jpqlSB = new StringBuffer();
		if (jpql.startsWith("select")) {
			jpqlSB.append("select sum(").append(propertyName).append(") ").append(jpql.substring(jpql.indexOf("from")));
		} else {
			jpqlSB.append("select sum(").append(propertyName).append(") ").append(jpql);
		}

		int groupby_index = jpqlSB.indexOf("group by");
		int orderby_index = jpqlSB.indexOf("order by");

		if (groupby_index > 0) {
			orderby_index = groupby_index;
		}

		Query queryObject = null;
		if (orderby_index > 0) {
			queryObject = em.createQuery(jpqlSB.substring(0, orderby_index));
		} else {
			queryObject = em.createQuery(jpqlSB.toString());
		}

		prepareQuery(queryObject);
		if (params != null) {
			for (Map.Entry<String, ?> entry : params.entrySet()) {
				queryObject.setParameter(entry.getKey(), entry.getValue());
			}
		}

		Object obj = queryObject.getSingleResult();

		if (obj instanceof Double) {
			return (Double) obj;
		} else {
			return ((BigDecimal) obj).doubleValue();
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

	/**
	 * 生成jpql语句，根据传入的T实例对象，生成查询语句，其规则如下：</br>
	 * 	1、根据t的class类型的构造函数，通过获取是否有注解{@link DefaultConstructorForList}}判断是否需要生成构造；<br/>
	 * 	2、获取所有的get方法，然后获取实例值，当值不为空时，则附加条件，当值为字符串时不包含（status、id、type等字眼）时为like条件，number时为等于<br/>
	 * 	3、当获取的到值为对象类型，并且对应的对象字段名字+Id存在于当前对象的字段集合中时，则进行联表关联查询，通过当前字段实例值的id=当前对象名字+Id为关联条件<br/>
	 * 	5、最后通过{@link OrderBy}来附加整个条件的排序，完成语句的生成
	 * @param t 实例对象
	 * @param sb jpql语句，初始可以为null，通过返回的String获取jpql语句
	 * @param previous 当前生成语句的字段，为null则为主对象，否则则为关联对象
	 * @return jpql语句
	 * @throws Exception
	 */
	public <T> JpqlModel generateJpql(T t,JpqlModel sb,PropertyDescriptor previous,Boolean noLike) throws Exception {
		if(noLike==null)noLike=false;
		if(t==null)return null;
		PropertyDescriptor[] pds=BeanUtils.getPropertyDescriptors(t.getClass());
		String alias=t.getClass().getSimpleName().substring(0, 1).toLowerCase()+t.getClass().getSimpleName().substring(1);
		//according the main query object to generate the constructor of JPQL.
		if(sb==null) {
			sb=new JpqlModel();
			Constructor<?>[] conses=t.getClass().getConstructors();
			for(Constructor<?> cons:conses) {
				DefaultConstructorForList dcfl=(DefaultConstructorForList) cons.getAnnotation(DefaultConstructorForList.class);
				if(dcfl!=null) {
					String[] paras=localVariableTableParameterNameDiscoverer.getParameterNames(cons);
					if(paras.length>0) {
						sb.addQueryCols("select ");
						sb.addQueryCols((" new ")).append(t.getClass().getName()).append("(");
						for(int i=0;i<paras.length;i++) {
							String[] colName=paras[i].split("_");
							if(colName.length<2) {
								sb.addQueryCols(alias).append(".").append(colName[0]);
							}else {
								PropertyDescriptor tPd=BeanUtils.getPropertyDescriptor(t.getClass(), colName[0]);
								if(tPd!=null&&tPd.getReadMethod().invoke(t)==null) {  
									tPd.getWriteMethod().invoke(t, tPd.getPropertyType().newInstance());
								}
								sb.addQueryCols(colName[0]).append(".").append(colName[1]);
							}
							if(i==(paras.length-1)) {
								sb.addQueryCols("");
							}else {
								sb.addQueryCols(",");
							}
						}
						sb.addQueryCols(") "); 
					}
				}
			}
			sb.addFromTables(t.getClass().getSimpleName()).append(" ").append(alias);
		}else {
			sb.addFromTables(","+t.getClass().getSimpleName()+" "+alias);
			sb.addWhere(" and ").append(alias).append(".id").append("=").append(previous.getValue("alias")).append(".").append(previous.getName());
		}
		PropertyDescriptor statusPd=BeanUtils.getPropertyDescriptor(t.getClass(),STATUS_NAME);
		if(statusPd!=null) {
			sb.addWhere(" and ").append(alias != null && !alias.isEmpty() ? (alias + ".") : "").append("status>0");
		}
		Field orderField=null;
		for(PropertyDescriptor pd:pds) {
			Method getMethod = pd.getReadMethod();// 获得get方法
			Transient ts = getMethod.getAnnotation(Transient.class);
			Object o = getMethod.invoke(t);// 执行get方法返回一个Object
			if (ts == null) {
				try {
					MappedSuperclass ms = t.getClass().getSuperclass().getAnnotation(MappedSuperclass.class);
					Field field=null;
					if(ms!=null) {
						field=t.getClass().getSuperclass().getDeclaredField(pd.getName());
					}else {
						field=t.getClass().getDeclaredField(pd.getName());
					}
					
					if(field.getAnnotation(OrderBy.class)!=null) {
						orderField=field;
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					continue;
				}
				if (o != null && !o.equals("")) {
					if (o instanceof String) {
						sb.addWhere(" and ");
						if (alias != null && !alias.isEmpty()) {
							sb.addWhere(alias).append(".");
						} else {
							alias = "";
						}
						sb.addWhere(pd.getName());
						if (noLike||(pd.getName().contains("Id")&&!pd.getName().toLowerCase().contains("full")) || pd.getName().contains("owner")|| pd.getName().contains("Number")
								|| pd.getName().contains("status")||pd.getName().equals("id")||pd.getName().toLowerCase().contains("type")||pd.getName().equals("pid")||pd.getName().contains("Code")) {
							sb.addWhere(" = '").append(o.toString()).append("'");
						} else {
							sb.addWhere(" like '%").append(o.toString()).append("%'");
						}
					} else if (o instanceof Date) {
						sb.addWhere(" and ");
						if (alias != null) {
							sb.addWhere(alias).append(".");
						} else {
							alias = "";
						}
						sb.addWhere(pd.getName());
						sb.addWhere(" >= '").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date) o))
								.append("'");
					} else if (o instanceof Integer || o instanceof Long) {
						sb.addWhere(" and ");
						if (alias != null && !alias.isEmpty()) {
							sb.addWhere(alias).append(".");
						} else {
							alias = "";
						}
						sb.addWhere(pd.getName());
						sb.addWhere(" = ").append(o.toString());
					}
				}
			}else {
				PropertyDescriptor previousPd=BeanUtils.getPropertyDescriptor(t.getClass(),pd.getName()+"Id");
				if(previousPd!=null&&o!=null) {
					previousPd.setValue("alias", alias);
					generateJpql(o,sb,previousPd,null);
				}
			}
		}
		//append the 'order by' to the end of JPQL on the main query object. The parameter 'previous' is for avoiding to inert multi 'order by', make sure the 'order' of JPQL is the main query object.
		if(orderField!=null&&previous==null) {
			sb.addOrderBy(alias).append(".").append(orderField.getName()).append(" ").append(orderField.getAnnotation(OrderBy.class).value());
		}
		return sb;
	}
	/**
	 * 根据实体类属性生成JPQL，其中属性为integer或Long时为等于，属性为String为like条件，为Date时为大于条件,
	 * 此方法只会执行与数据库关联的字段作为条件进行查询
	 * 
	 * @param t
	 *            实例对象
	 * @param isOnlyWhere
	 *            是否只生产where之后的语句
	 * @param alias
	 *            对象在jpql中的别名
	 * @return
	 *//*
	public <T> String generateJpql(T t, boolean isOnlyWhere, String alias, boolean noLike) {
		try {
			if(1==1)
				return generateJpql(t,null,null);
			StringBuffer sb = new StringBuffer();
			if (!isOnlyWhere) {
				sb.append("from " + t.getClass().getName());
				if (alias != null) {
					sb.append(" ").append(alias);
				}
				sb.append(" where 1=1 ");
			}
			Field[] fields = null;
			Entity entity = t.getClass().getAnnotation(Entity.class);
			if (null != entity){
				MappedSuperclass ms = t.getClass().getSuperclass().getAnnotation(MappedSuperclass.class);
				if (null != ms){
					fields = t.getClass().getSuperclass().getDeclaredFields();
				}else{
					fields = t.getClass().getDeclaredFields();
				}
			}
			if (null != fields && fields.length == 0){
				fields = t.getClass().getDeclaredFields();
			}
			Field.setAccessible(fields, true);
			try {
				t.getClass().getSuperclass().getDeclaredField("status");
				sb.append(" and ").append(alias != null && !alias.isEmpty() ? (alias + ".") : "").append("status>0");
			} catch (Exception e) {
				// TODO Auto-generated catch block
			}
			Field orderField = null;
			for (Field f : fields) {
				try {
					PropertyDescriptor pd = new PropertyDescriptor(f.getName(), t.getClass());
					Method getMethod = pd.getReadMethod();// 获得get方法
					Transient ts = getMethod.getAnnotation(Transient.class);
					if (f.getAnnotation(OrderBy.class) != null) {
						orderField = f;
					}
					if (ts == null) {
						Object o = getMethod.invoke(t);// 执行get方法返回一个Object
						if (o != null && !o.equals("")) {

							if (o instanceof String) {
								sb.append(" and ");
								if (alias != null && !alias.isEmpty()) {
									sb.append(alias).append(".");
								} else {
									alias = "";
								}
								sb.append(f.getName());
								if ((f.getName().contains("Id")&&!f.getName().toLowerCase().contains("full")) || f.getName().contains("owner")|| f.getName().contains("Number")
										|| f.getName().contains("status") || noLike) {
									sb.append(" = '").append(o.toString()).append("'");
								} else {
									sb.append(" like '%").append(o.toString()).append("%'");
								}
							} else if (o instanceof Date) {
								sb.append(" and ");
								if (alias != null) {
									sb.append(alias).append(".");
								} else {
									alias = "";
								}
								sb.append(f.getName());
								sb.append(" >= '").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date) o))
										.append("'");
							} else if (o instanceof Integer || o instanceof Long) {
								sb.append(" and ");
								if (alias != null && !alias.isEmpty()) {
									sb.append(alias).append(".");
								} else {
									alias = "";
								}
								sb.append(f.getName());
								sb.append(" = ").append(o.toString());
							} else {
								Field[] cfields = o.getClass().getDeclaredFields();
								for (Field tf : cfields) {
									PropertyDescriptor tpd;
									try {
										tpd = new PropertyDescriptor(tf.getName(), o.getClass());
									} catch (Exception e) {
										// TODO Auto-generated catch block
										continue;
									}
									Method tgetMethod = tpd.getReadMethod();// 获得get方法
									Object to = tgetMethod.invoke(o);

									if (to instanceof String) {
										sb.append(" and ");
										if (alias != null && !alias.isEmpty()) {
											sb.append(alias).append(".");
										} else {
											alias = "";
										}
										sb.append(f.getName());
										sb.append(".").append(tf.getName());
										if (tf.getName().contains("Id") || tf.getName().contains("owner") || noLike) {
											sb.append(" = '").append(to.toString()).append("'");
										} else {
											sb.append(" like '%").append(to.toString()).append("%'");
										}

									} else if (to instanceof Date) {
										sb.append(" and ");
										if (alias != null && !alias.isEmpty()) {
											sb.append(alias).append(".");
										} else {
											alias = "";
										}
										sb.append(f.getName());
										sb.append(".").append(tf.getName());
										sb.append(" >= '")
												.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date) o))
												.append("'");
									} else if (to instanceof Integer || to instanceof Long) {
										sb.append(" and ");
										if (alias != null && !alias.isEmpty()) {
											sb.append(alias).append(".");
										} else {
											alias = "";
										}
										sb.append(f.getName());
										sb.append(".").append(tf.getName());
										sb.append(" = ").append(to.toString());
									} else {
										System.out.println("-----生成jpql时超过2级无法正常生成了----");
									}
								}
								// sb.delete(sb.length()-f.getName().length()-6-alias.length(),
								// sb.length());
							}
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
				}
			}

			if (orderField != null) {
				OrderBy by = orderField.getAnnotation(OrderBy.class);
				sb.append(" order by ").append(orderField.getName()).append(" ").append(by.value());
			}
			return sb.toString() != null && !sb.toString().equals("null") ? sb.toString() : "";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// System.out.println("--->注意生成jpql有错误，不会影响程序但其不会生成jpql语句-》");
		}
		return "";
	}*/

	public <T> List<T> findByEntity(T t) {
		try {
			return (List<T>) find(generateJpql(t, null,null,null).toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public <T> List<T> findByEntity(T t, boolean noLike) {
		try {
			return (List<T>) find(generateJpql(t, null, null,noLike).toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public <T> List<T> executeSqlQuery(String sql, Class<T> clazz) {
		// TODO Auto-generated method stub
		Query query = em.createNativeQuery(sql, clazz);
		List<T> list = query.getResultList();
		return list;
	}

	public List<?> executeSqlQuery(String sql) {
		Query query = em.createNativeQuery(sql);
		List<?> list = query.getResultList();
		return list;
	}
	public int executeSql(String sql,final Object... objects) {
		Query query = em.createNativeQuery(sql);
		if (objects != null) {
			for (int i = 0; i < objects.length; i++) {
				query.setParameter(i + 1, objects[i]);
			}
		}
		return query.executeUpdate();
	}
	public List<?> executeSqlQuery(String sql, final Object... objects) {
		Query query = em.createNativeQuery(sql);
		if (objects != null) {
			for (int i = 0; i < objects.length; i++) {
				query.setParameter(i + 1, objects[i]);
			}
		}
		List<?> list = query.getResultList();
		return list;
	}

	public List<?> executeSqlQuery(String sql, Map<String, ?> maps) {
		Query query = em.createNativeQuery(sql);
		if (maps != null) {
			for (Map.Entry<String, ?> entry : maps.entrySet()) {
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}

		List<?> list = query.getResultList();
		return list;
	}

	public List<?> executeSqlQuery(String sql, Integer pageNo, Integer objectPerPage) {
		Query query = em.createNativeQuery(sql);
		query.setFirstResult((pageNo - 1) * objectPerPage);
		query.setMaxResults(objectPerPage);
		return query.getResultList();
	}

	public int countBySql(String propertyName, String sql) {
		sql = sql.replaceAll("FROM","FROM".toLowerCase());
		sql = sql.replaceAll("GROUP BY","GROUP BY".toLowerCase());
		sql = sql.replaceAll("ORDER BY","ORDER BY".toLowerCase());
		StringBuffer jpqlSB = new StringBuffer();
			jpqlSB.append("select count(").append(propertyName).append(") ")
					.append(sql.substring(sql.indexOf("from"),sql.length()));
		int groupby_index = jpqlSB.indexOf("group by");
		int orderby_index = jpqlSB.indexOf("order by");
		if (groupby_index > 0) {
			orderby_index = groupby_index;
		}
		Query queryObject = null;
		if (orderby_index > 0) {
			queryObject = em.createNativeQuery(jpqlSB.substring(0, orderby_index));
		} else {
			queryObject = em.createNativeQuery(jpqlSB.toString());
		}
		prepareQuery(queryObject);
		return Integer.valueOf(queryObject.getSingleResult().toString());
	}

}
