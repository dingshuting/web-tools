package com.ijs.core.util;

import java.sql.Date;

import com.ijs.core.base.Config;
/**
 * 单号的生成工具，用于生成如：订单号，运单号等系统内部的数据单号
 * @author Dustin
 *
 */
public final class FormIDGenerator {
	
	public static int SALE_ORDER_ID_SEQ = 0;
	public static int PURCHASE_ORDER_ID_SEQ = 0;
	public static int WAREHOUSE_OUT_ID_SEQ = 0;	
	public static int WAREHOUSINT_ID_SEQ = 0;	
	
	private static String TODAY = new java.text.SimpleDateFormat("yyyyMMdd").format(new Date(System.currentTimeMillis()));
	 
	/**
	 * 获得订单号
	 * @return
	 */
	public synchronized static String getOrderId(){
		init();
		
		FormIDGenerator.SALE_ORDER_ID_SEQ = FormIDGenerator.SALE_ORDER_ID_SEQ + 1;
		return Config.SYS_PARAMETER_MAP.get("sale_order_id_prefix") 
				+ TODAY 
				+ fill(FormIDGenerator.SALE_ORDER_ID_SEQ, Integer.parseInt(Config.SYS_PARAMETER_MAP.get("form_sn_digit")));
	}
	
	/**
	 * 采购单号
	 * @return
	 */
	public synchronized static String getPurchaseOrderId(){
		init();
		
		FormIDGenerator.PURCHASE_ORDER_ID_SEQ = FormIDGenerator.PURCHASE_ORDER_ID_SEQ + 1;
		return Config.SYS_PARAMETER_MAP.get("purchase_order_id_prefix") 
				+ TODAY 
				+ fill(FormIDGenerator.PURCHASE_ORDER_ID_SEQ, Integer.parseInt(Config.SYS_PARAMETER_MAP.get("form_sn_digit")));
	}
	
	/**
	 * 获得出库单号
	 * @return
	 */
	public synchronized static String getWarehouseOutId(){
		init();
		
		FormIDGenerator.WAREHOUSE_OUT_ID_SEQ = FormIDGenerator.WAREHOUSE_OUT_ID_SEQ + 1;
		return Config.SYS_PARAMETER_MAP.get("warehouse_out_id_prefix") 
				+ TODAY 
				+ fill(FormIDGenerator.WAREHOUSE_OUT_ID_SEQ, Integer.parseInt(Config.SYS_PARAMETER_MAP.get("form_sn_digit")));
	}
	
	/**
	 * 获得入库单号
	 * @return
	 */
	public synchronized static String getWarehouingId(){
		init();
		
		FormIDGenerator.WAREHOUSINT_ID_SEQ = FormIDGenerator.WAREHOUSINT_ID_SEQ + 1;
		return Config.SYS_PARAMETER_MAP.get("warehousing_id_prefix") 
				+ TODAY 
				+ fill(FormIDGenerator.WAREHOUSINT_ID_SEQ, Integer.parseInt(Config.SYS_PARAMETER_MAP.get("form_sn_digit")));
	}
	/**
	 * 生成唯一标识
	 * @param num 当前轮询到的数
	 * @param bits 签名标识符合，用于确保生成唯一的数字
	 * @return
	 */
	private static String fill(int num, int bits){
		return String.valueOf(new Double(Math.pow(10, bits)).intValue() + num).substring(1);
	}
	/**
	 * 初始化当前的生成环境，每天以0开始
	 */
	private synchronized static void init(){
		String now = new java.text.SimpleDateFormat("yyyyMMdd").format(new Date(System.currentTimeMillis()));
		
		if(TODAY.equals(now)){
			return;
		}else{
			SALE_ORDER_ID_SEQ = 0;
			PURCHASE_ORDER_ID_SEQ = 0;
			WAREHOUSE_OUT_ID_SEQ = 0;	
			WAREHOUSINT_ID_SEQ = 0;	
			TODAY = now;
		}
	}
	
}
