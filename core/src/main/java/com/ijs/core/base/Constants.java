package com.ijs.core.base;
/**
 * 常量信息保存系统中常用或者固定的字符串信息，与Config不同，Config中保存的为SysParameter中的key信息
 * @author Administrator
 *
 */
public class Constants {
	
	//当前登录的用户
	public static final String CURRENT_USER_KEY = "CURRENT_USER";
	//错误的session信息
	public static final String SESSION_ERROR = "sessionError";
	//提示的session信息
	public static final String SESSION_MESSAGE = "sessionMessage";
	//当前功能对象
	public static final String FUNC_CURRENT = "funcCurrent";
	//验证码常量
	public static String VERIFY_CODE_SESSION_NAME="verifyCode";
	public static String VERIFY_SMS_CODE_SESSION_NAME="verifySmsCode";
	public static final String SA_ID="100000";
	public static final Integer ROLE_ADMIN=1003;
	public static final Integer ROLE_SA=1001;
	
	//所有涉及到固定开关的值，TURN_ON为开启 TURN_OFF为关闭
	public static final String TURN_ON="1";
	public static final String TURN_OFF="0";
	
	//主键的id标识，用于根据该标识反射出model主键的值
	public static final String MODEL_ID_KEY = "id";
	public static final String VERIFY_SMS_CODE_SESSION_TIME_NAME = "smsCodeSendTime";
	
	public final static String COL_VAL_TYPE_FV="fv";
	public final static String COL_VAL_TYPE_STRING="s";
	public final static String COL_VAL_TYPE_TIME="t";
	public final static String COL_VAL_TYPE_OBJECT="o";
	public final static String COL_VAL_TYPE_OBJECT_LIST="ol";
	public final static String COL_VAL_TYPE_REF="ref";
	public final static String COL_VAL_TYPE_INTEGER="i";
	public final static String COL_VAL_TYPE_MAP="m";
	public final static String COL_VAL_TYPE_DOUBLE="d";
	public final static String COL_VAL_TYPE_BOOLEAN="b";
	public static final String IS_VERIFY_CODE = "is_verify_code";
	
}
