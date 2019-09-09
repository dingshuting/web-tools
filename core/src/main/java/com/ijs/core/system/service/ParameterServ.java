package com.ijs.core.system.service;

import com.ijs.core.base.model.SysParameters;
import com.ijs.core.base.service.GenericServ;


public interface ParameterServ extends GenericServ{
	/**
	 * put parameters to memories from a database.
	 * which you want to use the parameters, you can get it by accessing variable 'Config.SYS_PARAMETER_MAP' 
	 * 将数据库的配置信息缓存到内存中，当需要使用时可以通过变量Config.SYS_PARAMETER_MAP直接调用。
	 */
	public void cache();
	/**
	 * 将指定id的参数进行解锁操,解锁操作后可以对系统参数进行修改操作
	 * @param id 功能的id标识
	 * @param status 1、锁 0、解
	 */
	public void saveLock(String id,String status);
	/**
	 * 修改参数信息,修改完成后立即在系统配置中进行生效
	 * */
	public void updatesys(SysParameters sysyParameters);
	/**
	 * 保存系统参数信息，保存完毕后再系统配置中立即生效
	 * */
	public void save(SysParameters sysyParameters);
	
	/**
	 * 根据id 获取当前参数
	 * @return
	 */
	public SysParameters getDetail(String sysParameterId);
}
