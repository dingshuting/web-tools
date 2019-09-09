package com.ijs.core.base;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;


/**全局的配置信息，当服务器启动时会自动加载
 * 
 * @author Dustin
 */
public final class Config {
	public static final String CTX="ctx";
	//数据库配置信息key
	public static final String IS_ADAPTER="is_adapter";
	public static final String PER_PAGE_OBJS="per_page_objs";
	public static final String PER_PAGE_OBJS_TITLE="per_page_objs_title";
	public static final String UPLOAD_ROOT_PATH = "upload_root_path";
	public static final String FILE_TEMP_PATH = "upload_temp_path";
	public static final String IMAGE_PATH = "image_path";
	public static final String IMAGE_DOMAIN = "image_domain";
	public static final String FILE_PATH = "file_path";
	public static final String IS_SEACH_ECHO="is_seach_echo";
	public static final String IS_RECORD_LOGIN_INFO="is_record_login_info";
	public static final String MALL_DOMAIN="mall_domain";
	public static final String XMS_DOMAIN = "xms_domain";
	public static final String KUAIDI_URL = "kuaidi100_url";//快递100接口地址
	public static final String KUAIDI_KEY = "kuaidi100_key";//100key
	public static final String RESET_PASSWORD="reset_password";
	public static final String INDEX_SELLERITEM_PAGES="index_selleritem_pages";
	public static final String INDEX_SELLERITEM_NUMBER="index_selleritem_number";
	public static final String ORDER_AUTO_RECEIVING_TIME="order_auto_receiving_time";
	public static final String SOLR_URL = "solr_url";//全文检索访问链接
	public static final String ON = "1";//开关开
	public static final String OF = "0";//开关关
	public static final String IS_VERIFI_COMPANY="is_verifi_company";
	public static final String IS_VERIFI_ARTICLE="is_verifi_article";
	public static final String WAYBILL_HISTORY_TIME_INTERVAL="waybill_history_time_interval";

	//配置信息列表
	public static Map<String, String> SYS_PARAMETER_MAP = new HashMap<String, String>();
	public static final String ROLE_ADMIN = "ROLE_ADMIN";
	public static final String SA_FUNCS = null;
	public static final String SEND_SMS_INTERVAL="send_sms_interval";
	public static final Gson gson=new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	public static final Object ROLE_SA = "ROLE_SA";
}
