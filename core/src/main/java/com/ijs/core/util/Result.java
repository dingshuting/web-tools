package com.ijs.core.util;

import com.google.gson.annotations.Expose;
/**
 * REST Control返回的公共结果类
 * @author Dustin
 *
 * @param <T> data数据的泛型指定
 */
public class Result<T> {
	/**
	 * 处理成功
	 */
	public final static String CODE_SUCCESS="200";
	/**
	 * 系统异常
	 */
	public final static String CODE_ERROR="500";
	/**
	 * 资源没找见
	 */
	public final static String CODE_NOFOUND="404";
	/**
	 * 数据核验错误
	 */
	public final static String CODE_VALIDATE="400";
	/**
	 * 未知的错误
	 */
	public final static String CODE_NAME="999"; 
	/**
	 * 异步处理的代码<br/>
	 * 200-处理成功<br/>
	 * 500-系统异常<br/>
	 * 403-无权限访问<br/>
	 * 400-数据核验失败<br/>
	 * 404-访问的值或数据不存在<br/>
	 * 999-未知的错误
	 */
@Expose	
	private String code;
	// 异步处理的描述，描述具体失败及产生错误的原因
@Expose	
	private String desc;
	//处理成功后可放置对接返回到页面
@Expose	
	private T data;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	
}
