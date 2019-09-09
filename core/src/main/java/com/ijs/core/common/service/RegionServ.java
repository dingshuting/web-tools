/**
 * 
 */
package com.ijs.core.common.service;

import java.util.List;

import com.ijs.core.base.model.Region;
import com.ijs.core.base.service.GenericServ;

/**
 * @author Dustin
 *
 */
public interface RegionServ extends GenericServ{
	/**
	 * 根据父id查询子区域的列表
	 * @param pid 父id编号
	 * @param rname 查询的关键字,可为空
	 * @return 当rname为空时则查询出全部的子信息列表
	 */
	public List<Region> findRegByPid(String pid,String rname);
	/**
	 * 根据主键id查询地区的详情信息，包含所有的父级区域，此id应为最子级区域信息
	 * @param id 地区的唯一标示码
	 * @return 返回一个区域对象，其parentReg不为空
	 */
	public Region getDetail(String id);
	/**
	 * 获取区域的详情并包含其一级的子区域信息
	 * @param id 主键标示id
	 * @return 包含childRegs的Region对象
	 */
	public Region getDetailContainChild(String id);
	
	/**
	 * 区域详情  liuyanyan 
	 * @param id
	 * @return
	 */
	public Region getRegionDetail(String id);
}
