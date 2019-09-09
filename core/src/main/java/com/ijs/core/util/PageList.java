package com.ijs.core.util;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.ijs.core.base.Config;
/**
 * 分页工具类
 * @author Dustin
 *
 */
public class PageList {
	@Expose
	private int currentPage; //当前页
	@Expose
	private int perPageSize; //每页大小
	@Expose
	private List<?> list;  //列表中的值
	@Expose
	private int totalSize; //总条数
	
	public PageList() {
		super();
		this.currentPage=1;
		this.perPageSize=Integer.parseInt(Config.SYS_PARAMETER_MAP.get("per_page_objs"));
		// TODO Auto-generated constructor stub
		
	}
	/**
	 * 获取当前页码
	 * @return 当前页
	 */
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		
		this.currentPage = currentPage;
	}
	/**
	 * 获取每页显示多少条数据
	 * @return int数字
	 */
	public int getPerPageSize() {
		return perPageSize;
	}
	public void setPerPageSize(int perPageSize) {
		if(perPageSize<1){
			this.perPageSize=Integer.parseInt(Config.SYS_PARAMETER_MAP.get("per_page_objs"));
		}else{
			this.perPageSize = perPageSize;
		}
	
	}
	/**
	 * 获取当前的list集合数据
	 * @return
	 */
	public List<?> getList() {
		return list;
	}
	public void setList(List<?> list) {
		this.list = list;
	}
	/**
	 * 当前总数据的条数
	 * @return
	 */
	public int getTotalSize() {
		return totalSize;
	}
	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}
}
