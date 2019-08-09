package com.ijs.core.util;

import java.util.UUID;
/**
 * UUID的生成工具，所有的入库id应使用此类生成uuid
 * @author Dustin
 *
 */
public class IdGen{

	/**
	 * 封装JDK自带的UUID, 通过Random数字生成, 中间无-分割.
	 */
	public static String uuid() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	public static String guid() {
		return UUID.randomUUID().toString().toUpperCase();
	}
}
