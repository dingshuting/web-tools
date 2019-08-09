package com.ijs.core.util;

/**
 * 返回对象工具类，用于快速返回Control结果
 * @author 张磊
 *
 */
public class ResultUtil {

	/**
	 * 成功 
	 * @param data 返回数据
	 * @return
	 */
	public static Result<?> success(Object data){
		Result<Object> result=new Result<Object>();
		result.setCode(Result.CODE_SUCCESS);
		result.setData(data);
		return result;
	}
	/**
	 * 成功 无返回数据
	 * @return
	 */
	public static Result<?> success(){
		return success(null);
	}
	/**
	 * 失败
	 * @param errorCode 错误码
	 * @param errorDesc 错误描述
	 * @return
	 */
	public static Result<?> error(String errorCode, String errorDesc){
		Result<Object> result=new Result<Object>();
		result.setCode(errorCode);
		result.setDesc(errorDesc);
		return result;
	}
}
