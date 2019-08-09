package com.ijs.core.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.ijs.core.base.Config;

/**
 * 查询条件类，用于除公共查询外的复杂联合查询
 * @author Dustin
 *
 */
public class QueryParameters implements java.io.Serializable {
	private static final long serialVersionUID = -5231126651190370112L;
	//显示的数据条数
	private int count;
	private String startTime;
	private String endTime;
	private String[] vals;
	private String orderCol;
	private String orderDri;
	private List<Section> sections;
	/**
	 * 手机号或运单号查询条件
	 */
	private String phoneOrExpressNumber;
	public String getStartTime() {
		if(startTime==null&&sections!=null&&sections.size()>0) {
			return sections.get(0).getStart(); 
		}
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		if(endTime==null&&sections!=null&&sections.size()>0) {
			return sections.get(0).getEnd(); 
		}
		return endTime;
	}
	
	public List<Section> getSections() {
		if(sections==null) {
			sections=new ArrayList<>();
		}
		return sections;
	}
	public void setSections(List<Section> sections) {
		this.sections = sections;
	}
	public void setEndTime(String endTime) {
		
		if(endTime != null && endTime != ""){
				try {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					 Date date = sdf.parse(endTime);
					 Calendar   calendar   =   new   GregorianCalendar(); 
				     calendar.setTime(date); 
				     calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动 
				     endTime = sdf.format(calendar.getTime());   //这个时间就是日期往后推一天的结果
				} catch (Exception e) {
					// TODO: handle exception
				}
		}
		this.endTime = endTime;
	}
	/**
	 * 用于查询条件为数组的条件
	 * @return
	 */
	public String[] getVals() {
		return vals;
	}
	public void setVals(String[] vals) {
		this.vals = vals;
	}
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return Config.gson.toJson(this);
	}
	public String getOrderCol() {
		return orderCol;
	}
	public void setOrderCol(String orderCol) {
		this.orderCol = orderCol;
	}
	public String getOrderDri() {
		return orderDri==null?"desc":orderDri;
	}
	public void setOrderDri(String orderDri) {
		this.orderDri = orderDri;
	}
	public String getPhoneOrExpressNumber() {
		return phoneOrExpressNumber;
	}

	public void setPhoneOrExpressNumber(String phoneOrExpressNumber) {
		this.phoneOrExpressNumber = phoneOrExpressNumber;
	}
	/**
	 * 区间查询条件
	 * @author Dustin
	 *
	 */
	public static class Section{
		//列名
		private String colName;
		private String start;
		private String end;
		//类型，1固定值  2区间 
		private Integer type;
		public Section() {
			super();
		}
		public String getColName() {
			return colName;
		}
		public void setColName(String colName) {
			this.colName = colName;
		}
		public String getStart() {
			return start;
		}
		public void setStart(String start) {
			this.start = start;
		}
		public String getEnd() {
			return end;
		}
		public void setEnd(String end) {
			this.end = end;
		}
		public Integer getType() {
			return type;
		}
		public void setType(Integer type) {
			this.type = type;
		}
		public Section(String colName, String start) {
			super();
			this.colName = colName;
			this.start = start;
		}
		
		
	}
}
