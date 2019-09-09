/**
 * File Name: com.officedepot.cn.runnable.EveryDayRunnable.java
 * Copyright(C) 2007 Office Depot China
 * Create Time: 2007-3-20 ����05:26:26
 */
package com.ijs.core.util.rundelay;

import java.util.Calendar;

/**
 * Explain: 
 * @author Kevin Zou
 * @version v1.0.0
 * @since 1.6.0 (Sun Microsystems Inc.)
 */
public abstract class EveryDayRunnable 	 
	extends AutoRunnable {

	public void towait(long runtime) throws InterruptedException {
		/* �õ���ǰʱ�� */
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		
		/* �õ�ִ��ʱ�� */
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTimeInMillis(runtime);
		int hour = calendar2.get(Calendar.HOUR_OF_DAY);
		int minute = calendar2.get(Calendar.MINUTE);
		int second = calendar2.get(Calendar.SECOND);		
			
		calendar2.set(year,month,day,hour,minute,second);
		Calendar calendar3 = Calendar.getInstance();
		
		if(calendar2.getTimeInMillis()-300 > calendar.getTimeInMillis()){
			calendar3 = calendar2;			
		}else{
			calendar3.set(year,month,day+1,hour,minute,second);
		}
				
		logger.info("waitting:" + (calendar3.getTimeInMillis()-calendar.getTimeInMillis()-300));
		
		EveryDayRunnable.sleep(calendar3.getTimeInMillis()-calendar.getTimeInMillis()-300);

	}
	
	public static void main(String[] args){
		/* �õ���ǰʱ�� */
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		
		/* �õ�ִ��ʱ�� */
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTimeInMillis(50400000);
		int hour = calendar2.get(Calendar.HOUR_OF_DAY);
		int minute = calendar2.get(Calendar.MINUTE);
		int second = calendar2.get(Calendar.SECOND);		
			
		calendar2.set(year,month,day,hour,minute,second);
		Calendar calendar3 = Calendar.getInstance();
		
		System.out.println(calendar.getTime());
		System.out.println(calendar2.getTime());
		System.out.println(calendar.getTimeInMillis());
		System.out.println(calendar2.getTimeInMillis());
		
		
		if(calendar2.getTimeInMillis()-300 > calendar.getTimeInMillis()){
			calendar3 = calendar2;			
		}else{
			calendar3.set(year,month,day+1,hour,minute,second);
		}
		
		calendar.set(1970, 0, 2, 
				16,22,0);
		
		System.out.println(calendar.getTime());
		System.out.println(calendar.getTimeInMillis());
		//System.out.println(calendar3.getTimeInMillis()-calendar.getTimeInMillis()-300);		
		//log.info("waitting:" + (calendar3.getTimeInMillis()-calendar.getTimeInMillis()-300));		
	}
}
