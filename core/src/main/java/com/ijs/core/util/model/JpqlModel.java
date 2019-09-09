package com.ijs.core.util.model;
/**
 * 用于生成JPQL的model类
 * @author Dustin
 *
 */
public class JpqlModel {
	//默认创建对象并实例化几个stringBuffer
	public JpqlModel() {
		this.queryCols=new StringBuffer();
		this.fromTables=new StringBuffer(" from ");
		this.where=new StringBuffer(" where 1=1 ");
		this.orderBy=new StringBuffer(" order by ");
	}
	/**
	 * 查询列字段，其应包含从select开始到from前结束
	 */
	private StringBuffer queryCols;
	/**
	 * 从from开始到where结束之间的语句
	 */
	private StringBuffer fromTables;
	/**
	 * where开始到order by结束，可以包含group by字段
	 */
	private StringBuffer where;
	/**
	 * order by 及其之后的排序字段
	 */
	private StringBuffer orderBy;
	public StringBuffer addQueryCols(String queryCols) {
		return this.queryCols.append(queryCols);
	}
	public StringBuffer addFromTables(String fromTables) {
		return this.fromTables.append(fromTables);
	}
	public StringBuffer addWhere(String where) {
		return this.where.append(where);
	}
	
	public StringBuffer addOrderBy(String orderBy) {
		if(this.orderBy.length()>10) {
			this.orderBy.append(",");
		}
		return this.orderBy.append(orderBy);
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer fjpql=new StringBuffer(this.queryCols);
		fjpql.append(fromTables).append(where);
		if(orderBy.length()==10)
			return fjpql.toString();
		else {
			return fjpql.append(orderBy).toString();
		}
	}
}
