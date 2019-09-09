package com.ijs.util;

import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class DateFormat {
    /**
     * 将秒转换为 x天零y小时 格式
     * 如果小于24小时则不显示
     * @param mss 秒数
     * @return
     */
    public static String formatDateTime(long mss) {
        String DateTimes = null;
        long days = mss / ( 60 * 60 * 24);
        long hours = (mss % ( 60 * 60 * 24)) / (60 * 60);
        long minutes = (mss % ( 60 * 60)) /60;
        long seconds = mss % 60;
        if(days>0){
            DateTimes = days + "天零" + hours + "小时";
        }else{
            DateTimes = "--";
        }
        return DateTimes;
    }
    /**
     * 将秒转换为 x天零y小时 格式
     * 如果小于24小时则显示x小时
     * @param mss 秒数
     * @return
     */
    public static String formatDateTimeWithHour(long mss) {
        String DateTimes = null;
        long days = mss / ( 60 * 60 * 24);
        long hours = (mss % ( 60 * 60 * 24)) / (60 * 60);
        long minutes = (mss % ( 60 * 60)) /60;
        long seconds = mss % 60;
        if(days>0){
            DateTimes= days + "天零" + hours + "小时";
        }else if(hours>0){
            DateTimes=hours + "小时";
        }else{
            DateTimes = "--";
        }
        return DateTimes;
    }
    /**
     * 将秒转换xx.xx年
     * @return
     */
    public static String formatDateTimeWithYear(Timestamp beginTime,Timestamp entTime) {
        Long DateTimes = entTime.getTime()-beginTime.getTime();
        DecimalFormat formater = new DecimalFormat();
        formater.setMaximumFractionDigits(2);
		formater.setGroupingSize(0);
		formater.setRoundingMode(RoundingMode.HALF_UP);
		String result = formater.format((double)DateTimes/(31536000000.00));

        return result;
    }
    public static String formatDateSimple(Timestamp time,String p) {
    	return new SimpleDateFormat(p).format(time).toString();
    }
}
