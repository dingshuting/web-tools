package com.ijs.core.util;

import java.sql.Timestamp;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;


/**
 * 各种日期操作
 * 
 * @author shibc 2009-3-26 下午01:36:29
 * 
 */
public class DateUtil {
	private static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";
	private static Logger logger = Logger.getLogger(DateUtil.class);

	/**
	 * 日期的算术操作，可以增加或者减少，可以某一部分进行操作 year--年 month-月 1-12 day-天 1-31 hour -小时 0-23
	 * minute 分钟 0-59 second 秒 0-59 millisecond 毫秒 显示格式，可以任意组合
	 * 
	 * G Era designator Text AD y Year Year 1996; 96 M Month in year Month July;
	 * Jul; 07 w Week in year Number 27 W Week in month Number 2 D Day in year
	 * Number 189 d Day in month Number 10 F Day of week in month Number 2 E Day
	 * in week Text Tuesday; Tue a Am/pm marker Text PM H Hour in day (0-23)
	 * Number 0 k Hour in day (1-24) Number 24 K Hour in am/pm (0-11) Number 0 h
	 * Hour in am/pm (1-12) Number 12 m Minute in hour Number 30 s Second in
	 * minute Number 55 S Millisecond Number 978 z Time zone General time zone
	 * Pacific Standard Time; PST; GMT-08:00 Z Time zone RFC 822 time zone -0800
	 * 
	 * @param srcDate
	 * @param srcFormat
	 * @param destFormat
	 * @param operType
	 * @param operValue
	 * @return
	 */
	public static String evalTime(Date srcDate, String destFormat, String operType, int operValue) {
		if (srcDate == null || srcDate.equals(""))
			return "";
		if (destFormat == null || destFormat.equals(""))
			destFormat = "yyyy-MM-dd";
		if (operType == null || operType.equals(""))
			operType = "day";

		// Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("AST"));
		Calendar cal = Calendar.getInstance();
		cal.setTime(srcDate);
		if (operType.equals("year")) {
			cal.add(Calendar.YEAR, operValue);
		} else if (operType.equals("month")) {
			cal.add(Calendar.MONTH, operValue);
		} else if (operType.equals("day")) {
			cal.add(Calendar.DAY_OF_MONTH, operValue);
		} else if (operType.equals("hour")) {
			cal.add(Calendar.HOUR_OF_DAY, operValue);
		} else if (operType.equals("minute")) {
			cal.add(Calendar.MINUTE, operValue);
		} else if (operType.equals("second")) {
			cal.add(Calendar.SECOND, operValue);
		} else if (operType.equals("millisecond")) {
			cal.add(Calendar.MILLISECOND, operValue);
		}
		String curDay = new java.text.SimpleDateFormat(destFormat).format(cal.getTime());
		return curDay;
	}

	/**
	 * 日期格式转化 转换为时间戳类型
	 * 
	 * @param sdate
	 * @return
	 * @throws ParseException
	 */
	/*public static Timestamp string2Time(String sdate) throws ParseException {
		java.sql.Timestamp d = null;
		java.util.Date d1 = new SimpleDateFormat(Constant.TIMEFORMAT, Locale.getDefault()).parse(sdate);
		d = new java.sql.Timestamp(d1.getTime());
		String d2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(d);
		Timestamp.valueOf(d2);
		return d;
	}*/

	/**
	 * 获得当前时间 转换为时间戳类型
	 * 
	 * @param
	 * @return
	 * @throws ParseException
	 */
	public static Timestamp getCurTime2Timestamp() throws ParseException {
		Timestamp t = new Timestamp(System.currentTimeMillis());
		String d2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(t);
		Timestamp.valueOf(d2);
		return t;
	}

	/**
	 * 将时间格式化
	 * 
	 * @return
	 */
	public static Date DatePattern(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_PATTERN);
		try {
			String dd = sdf.format(date);
			date = stringToDate(dd);
		} catch (Exception e) {
			logger.error("", e);
		}
		return date;
	}

	/**
	 * 日期格式转化
	 * 
	 * @param srcDate
	 * @param srcFormat
	 * @param destFormat
	 * @return
	 */
	public static String dateFormat(String srcDate, String srcFormat, String destFormat) {
		if (srcFormat == null || srcFormat.equals(""))
			srcFormat = "yyyy-MM-dd";
		if (destFormat == null || destFormat.equals(""))
			destFormat = "yyyy-MM-dd";
		if (srcDate == null || srcDate.equals(""))
			return "";

		Calendar cal = Calendar.getInstance();
		Date currentTime = null;
		try {
			currentTime = (Date) new java.text.SimpleDateFormat(srcFormat).parse(srcDate);
		} catch (ParseException e) {
			logger.error("", e);
			currentTime = new Date();
		}
		cal.setTime(currentTime);
		String curDay = new java.text.SimpleDateFormat(destFormat).format(cal.getTime());
		return curDay;
	}

	/**
	 * 日期格式转化
	 * 
	 * @param srcDate
	 * @param srcFormat
	 * @param destFormat
	 * @return
	 */
	public static String dateFormat(Date srcDate, String destFormat) {
		if (srcDate == null)
			return "";
		if (destFormat == null || destFormat.equals(""))
			destFormat = getFmtOfDateFromString(srcDate);
		String curDay = new java.text.SimpleDateFormat(destFormat).format(srcDate);
		return curDay;
	}
	public static String getFmtOfDateFromString(Date date) {
		String fmt=DEFAULT_PATTERN;
		if(date!=null) {
			Calendar calen=Calendar.getInstance();
			calen.setTime(date);
			int hour=calen.get(Calendar.HOUR_OF_DAY);
			int min=calen.get(Calendar.MINUTE);
			int sec=calen.get(Calendar.SECOND);
			if(hour==0&&min==0&&sec==0) {
				return "yyyy-MM-dd";
			}else {
				
			}
		}
		return fmt;
	}
	/**
	 * 根据给定的时间字符串获取其格式，时间均为24小时制
	 * @param dataVal 时间的字符串值
	 * @return
	 */
	public static String getFmtOfDateFromString(String dataVal) {
		String fmt=DEFAULT_PATTERN;
		if(dataVal.matches("[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])")) {
			fmt="yyyy-MM-dd";
		}else if(dataVal.matches("[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])\\s+(20|21|22|23|[0-1]\\d):[0-5]\\d:[0-5]\\d")) {
			fmt="yyyy-MM-dd HH:mm:ss";
		}else if(dataVal.matches("[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])\\s+(20|21|22|23|[0-1]\\d):[0-5]\\d")) {
			fmt="yyyy-MM-dd HH:mm";
		}else if(dataVal.matches("[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])\\s+(20|21|22|23|[0-1]\\d)")) {
			fmt="yyyy-MM-dd HH";
		}else if(dataVal.matches("[1-9]\\d{3}-(0[1-9]|1[0-2])")) {
			fmt="yyyy-MM";
		}else if(dataVal.matches("[1-9]\\d{3}")) {
			fmt="yyyy";
		}
		return fmt;
	}
	/**
	 * 字符串日期转化为java.util.Date
	 * 
	 * @param srcDate
	 * @param srcFormat
	 * @return
	 */
	public static Date stringToDate(String srcDate, String srcFormat) {
		if (srcDate == null || srcDate.equals(""))
			return null;
		if (srcFormat == null || srcFormat.equals(""))
			srcFormat = getFmtOfDateFromString(srcDate);
		Calendar cal = Calendar.getInstance();
		Date currentTime = null;
		try {
			currentTime = (Date) new java.text.SimpleDateFormat(srcFormat).parse(srcDate);
		} catch (ParseException e) {
			logger.error("", e);
			currentTime = new Date();
		}
		cal.setTime(currentTime);
		return cal.getTime();
	}
	/**
	 * 将字符串格式化为‘yyyy-MM-dd HH:mm:ss’格式的java.util.Date
	 */
	public static Date stringToDate(String srcDate) {
		if (srcDate == null || srcDate.equals(""))
			return null;
		Date d = null;
		SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_PATTERN);
		try {
			d = sdf.parse(srcDate);
		} catch (Exception e) {
			logger.error("", e);
		}
		return d;
	}

	/**
	 * 比较两个日期的大小
	 */
	public static boolean compare(Date d1, Date d2) {
		if (d1.after(d2)) {
			return true;
		} else {
			return false;
		}
	}

	/** 
	 * 名称：dateToStr
	 * 功能：将指定的日期转换成字符串
	 * 输入：aDteValue: 要转换的日期;
	 * aFmtDate: 转换日期的格式, 默认为:"yyyy/MM/dd"
	 * 输出：
	 * 返回：转换之后的字符串
	 */
	public static String dateToStr(java.util.Date aDteValue, String aFmtDate) {
		String strRtn = null;

		if (aFmtDate.length() == 0) {
			aFmtDate = "yyyy/MM/dd";
		}
		Format fmtDate = new SimpleDateFormat(aFmtDate);
		try {
			strRtn = fmtDate.format(aDteValue);
		} catch (Exception e) {
			logger.error("", e);
		}

		return strRtn;
	}

	/**
	 * 名称：strToDate
	 * 功能：将指定的字符串转换成日期
	 * 输入：aStrValue: 要转换的字符串;
	 * aFmtDate: 转换日期的格式, 默认为:"yyyy/MM/dd"
	 * aDteRtn: 转换后的日期
	 * 输出：
	 * 返回：TRUE: 是正确的日期格式; FALSE: 是错误的日期格式
	 */
	public static boolean strToDate(String aStrValue, String aFmtDate, java.util.Date aDteRtn) {
		if (aFmtDate.length() == 0) {
			aFmtDate = "yyyy/MM/dd";
		}
		SimpleDateFormat fmtDate = new SimpleDateFormat(aFmtDate);
		try {
			aDteRtn.setTime(fmtDate.parse(aStrValue).getTime());
		} catch (Exception e) {
			logger.error("", e);
			return false;
		}

		return true;
	}

	/**
	 * 格式化时间（字符串格式：yyyy-MM-dd HH:mm:ss）
	 * 
	 * @param date
	 * @return
	 */
	public static String date2String(Date date) {
		SimpleDateFormat sf = new SimpleDateFormat(DEFAULT_PATTERN);
		return sf.format(date);
	}

	/**
	 * 当前时间（字符串格式：yyyy-MM-dd HH:mm:ss）
	 * 
	 * @return
	 */
	public static String currentTime() {
		SimpleDateFormat sf = new SimpleDateFormat(DEFAULT_PATTERN);
		return sf.format(new Date());
	}
	
	
	
	/**
	 * 当前时间（字符串格式：yyyyMMdd ）
	 *  add by suyang
	 * @return
	 */
	public static String getTime() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
		return sf.format(new Date());
	}

	/**
	 * java.util.Date转化为java.sql.Date
	 */
	public static java.sql.Date parseSqlDate(java.util.Date date) {
		if (date == null)
			return null;
		return new java.sql.Date(date.getTime());
	}

	/**
	 * java.util.Date转化为java.sql.Timestamp
	 */
	public static java.sql.Timestamp parseTimestamp(Date date, String format) {
		if (date == null)
			return null;
		long t = date.getTime();
		return new java.sql.Timestamp(t);
	}

	/**
	 * 日期相减
	 * 
	 * @param date
	 *            日期
	 * @param date1
	 *            日期
	 * @return 返回相减后的日期
	 */
	public static long diffDate(java.util.Date date, java.util.Date date1) {
		if (date == null)
			return 0;
		if (date1 == null)
			return 0;
		return date.getTime() - date1.getTime();
	}

	/**
	 * 针对年月取出最大的天数 
	 */
	/*public static String getMaxDay(String yearmonth) {
		String tmp = evalTime(yearmonth, "yyyyMM", "yyyyMM", "month", 1);
		String tmp2 = tmp + "01";
		String tmp3 = evalTime(tmp2, "yyyyMMdd", "dd", "day", -1);
		return tmp3;
	}*/

	/**
	 * 针对下月月初日期 modify by jiangmm 20091204
	 */
	public static String getNextBeginDay() {
		Calendar cal = Calendar.getInstance();
		@SuppressWarnings("unused")
		String endDate_year = String.valueOf(cal.get(Calendar.YEAR));
		@SuppressWarnings("unused")
		String endDate_month = "";
		if (cal.get(Calendar.MONTH) + 1 < 10) {
			endDate_month = "0" + (cal.get(Calendar.MONTH) + 1);
		} else {
			endDate_month = String.valueOf(cal.get(Calendar.MONTH) + 1);
		}
		@SuppressWarnings("unused")
		String endDate_day = String.valueOf(cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		String effDate_year = "";
		String effDate_month = "";
		// 当月为12月时，下月应该为下一年的1月，下个月的年份应该加1
		if (cal.get(Calendar.MONTH) + 1 == 12) {
			effDate_year = String.valueOf(cal.get(Calendar.YEAR) + 1);
			effDate_month = "01";
		} else {
			effDate_year = String.valueOf(cal.get(Calendar.YEAR));
			// 当下月为小于10月时，应该为下月的前面加0，如02到09
			if (cal.get(Calendar.MONTH) + 2 < 10) {
				effDate_month = "0" + (cal.get(Calendar.MONTH) + 2);
			} else {
				effDate_month = String.valueOf(cal.get(Calendar.MONTH) + 2);
			}
		}

		String effDate = effDate_year + effDate_month + "01";// 更改后套餐生效日期
		return effDate;
	}

	/**
	 * 判断是否是闰年
	 */
	public boolean isLeapyear(int year) {

		if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获得当前月份 转换为Long类型
	 * @throws ParseException
	 */
	public static Long getCurMonthLong() throws ParseException {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM");
		// t_log_echn_servinvoke.setTime_range(Long.valueOf(sdf.format(date)));
		return Long.parseLong(sdf.format(date));
	}

	public static String date2Str(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	/**
	 * 比较时间是否在这两个时间点之间
	 * 
	 * @param time1
	 * @param time2
	 */
	public static boolean checkTime(String time1, String time2) {
		Calendar calendar = Calendar.getInstance();
		Date date1 = calendar.getTime();
		Date date11 = DateUtil.stringToDate(DateUtil.dateToStr(date1, "yyyy-MM-dd") + " " + time1, DEFAULT_PATTERN);// 起始时间
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, 1);
		Date date2 = c.getTime();
		Date date22 = DateUtil.stringToDate(DateUtil.dateToStr(date2, "yyyy-MM-dd") + " " + time2, DEFAULT_PATTERN);// 终止时间
		Calendar scalendar = Calendar.getInstance();
		scalendar.setTime(date11);// 起始时间
		Calendar ecalendar = Calendar.getInstance();
		ecalendar.setTime(date22);// 终止时间
		Calendar calendarnow = Calendar.getInstance();
		if (calendarnow.after(scalendar) && calendarnow.before(ecalendar)) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 判断日期 date1-当前时间 date2-失效时间 delay-时间阀值（天数） return true-即将失效，提醒 false-未失效
	 */
	public static boolean toRemind(Date date1, Date date2, int delay) {

		Long time = DateUtil.diffDate(date1, date2);
		if (delay > time / (60 * 60 * 24 * 1000)) {
			return false;// 未失效
		} else {
			return true;// 即将失效，提醒
		}
	}

	/**
	 * 获得上一个月的月份
	 * 
	 */
	public static Calendar getPreviousMonth(Calendar cal) {
		//String str = "";   
		//SimpleDateFormat sdf=new SimpleDateFormat("yyyyMM");       

		if (cal == null) {
			cal = Calendar.getInstance();
		}
		cal.add(Calendar.MONTH, -1);//减一个月，变为下月的1号   
		return cal;
	}

	/**
	 * 根据日期类型更改时间
	 * 
	 * @param date
	 *            日期
	 * @param type
	 *            类型.month|date|year
	 * @param value
	 *            更改值大小
	 * @return 返回变更后的日期
	 */
	public static Date changeDate(Date date, String type, int value) {
		// Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		// Calendar calendar = Calendar.
		if (type.equals("month")) {
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + value);
		} else if (type.equals("date")) {
			calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + value);
		} else if (type.endsWith("year")) {
			calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + value);
		}
		return calendar.getTime();
	}
	

	/**
	 * @author zhaoweia
	 * 月份相减 
	 * @param startDate 
	 * @param enDate
	 * @return startDate - enDate
	 */
	public static int divMonth(Date startDate, Date enDate) {
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(startDate);
		
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(enDate);
		
		int yearDiv = startCalendar.get(Calendar.YEAR) - endCalendar.get(Calendar.YEAR);
		
		int monthDiv = startCalendar.get(Calendar.MONTH) - endCalendar.get(Calendar.MONTH);
		
		return monthDiv + yearDiv * 12;
	}
	/**
	 * @author jiangmm
	 * 获取当前时间的前几个月的月份  不包括本月
	 * @param count 指要取多少个月份 
	 * @return datelist List<String>
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List getPreMonths(int count){
		//Calendar 与 Date 的转换
		Calendar calendar = Calendar.getInstance();
		// 从一个 Calendar 对象中获取 Date 对象
		Date date = calendar.getTime();
		// 将 Date 对象反应到一个 Calendar 对象中，
		// Calendar/GregorianCalendar 没有构造函数可以接受 Date 对象
		// 所以我们必需先获得一个实例，然后设置 Date 对象
		calendar.setTime(date);	
		
		List datelist=new ArrayList();
		for(int i=0;i<count;i++){
			calendar=DateUtil.getPreviousMonth(calendar);
			Date date2 = calendar.getTime();
			String date3 =DateUtil.date2Str(date2, "yyyyMM");
			//logger.debug("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^"+date3);
			datelist.add(date3);
		}
		return datelist;
	}
	/**
	 * @author jiangmm
	 * 获取当前时间的前几个月的月份  包括本月
	 * @param count 指要取多少个月份 
	 * @return datelist List<String>
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List getFormNowMonths(int count){
		//Calendar 与 Date 的转换
		Calendar calendar = Calendar.getInstance();
		// 从一个 Calendar 对象中获取 Date 对象
		Date date = calendar.getTime();
		// 将 Date 对象反应到一个 Calendar 对象中，
		// Calendar/GregorianCalendar 没有构造函数可以接受 Date 对象
		// 所以我们必需先获得一个实例，然后设置 Date 对象
		calendar.setTime(date);	
		
		List datelist=new ArrayList();
		for(int i=0;i<count;i++){			
			Date date2 = calendar.getTime();
			String date3 =DateUtil.date2Str(date2, "yyyyMM");
			//logger.debug("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^"+date3);
			datelist.add(date3);
			calendar=DateUtil.getPreviousMonth(calendar);
		}
		return datelist;
	}

	/**
	 * 当前年月（字符串格式：yyyyMM）
	 * @return
	 */
	public  String getCurrentYearMonth()
	{
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMM");
		return sf.format(new Date());
	}
	/**
	 * 
	  * 方法功能：当前年的上个月 <BR>
	  * @return
	  * @see com.iss.common.util.DateUtil
	  * @since
	 */
	public  String getLastYearMonth()
	{
		 Calendar cal = Calendar.getInstance();
		 int month = cal.get(Calendar.MONTH);
		 String m = String.valueOf(month);
		 if (month < 10) {
				m = "0"+m;
		 }
		 int year = cal.get(Calendar.YEAR);
		 return year+""+m;
	}
	
	
	/**
	 * 
	 * 功能描述:得到当前月份
	 * @param p_date
	 * @return
	 */
	public static int getMonthOfDate(java.util.Date p_date){
        Calendar c = Calendar.getInstance();
        c.setTime(p_date);
        return c.get(2) + 1;
    }
	
	/**
	 * 
	 * 功能描述:得到日期
	 * @param p_date
	 * @return
	 */
	public static int getDayOfMonth(java.util.Date p_date){
        Calendar c = Calendar.getInstance();
        c.setTime(p_date);
        return c.get(Calendar.DAY_OF_MONTH);
    }
	/**
	 * 获取延迟多少毫秒后的时间
	 * @param delay 毫秒数
	 * @param destFormat 时间格式可空默认为yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getTimeAfterDelay(long delay,String destFormat){
	     Calendar c = Calendar.getInstance();
	     c.setTimeInMillis(c.getTimeInMillis()+delay);
	     return dateFormat(new Date(c.getTimeInMillis()), destFormat);
	}
	
	/**
	 * 时间计算一
	 * @param numData
	 * @return
	 * @throws ParseException
	 */
	public static String dateAdd(String numData) throws ParseException{
		SimpleDateFormat format = new SimpleDateFormat(DEFAULT_PATTERN);
		Date date = format.parse(format.format(new Date()));
		Date date2 = new Date(date.getTime() + Long.parseLong(numData) * 24L * 60L * 60L * 1000L);
		String str = format.format(date2);
		return str;
		
	}
	
	/**
	 * 时间计算二
	 * @param pat1
	 * @param pat2
	 * @return
	 * @throws ParseException
	 */
	public static String dateAdd(String pat1,String pat2) throws ParseException{
		SimpleDateFormat dateFormat=new SimpleDateFormat(DEFAULT_PATTERN);
		Date date2 = new Date(dateFormat.parse(pat1).getTime() + Long.parseLong(pat2));
		String str = dateFormat.format(date2);
		return str;
	}
	public static void main(String[] args) {
		System.out.println(evalTime(new Date(), "yyyy-MM-dd", "month", -1));
	}
	
}
