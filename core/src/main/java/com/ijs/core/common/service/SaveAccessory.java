package com.ijs.core.common.service;

import com.ijs.core.exception.ServiceException;

public interface SaveAccessory {
	/**
	 * 保存上传成功后的附件信息
	 * @param uid 主键id，附件所属的对象id及将该附件关联到的对象id
	 * @param url 上传成功后的url地址
	 * @param type 附件类型  可为null
	 * @throws ServiceException 
	 */
	public void saveAccessory(String uid,String url,String type) throws ServiceException;
	/**
	 * 删除指定资源id的附件信息
	 * @param rid
	 * @throws ServiceException
	 */
	public void removeAccessory(String rid,String type) throws ServiceException;
}
