package com.oupeng.joke.back.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FormatUtil {
	public static Double getRoundHalfUp4Double(Integer i1,Integer i2){
		if(i1 == null || i2 == null || i1.intValue() == 0 || i2.intValue() ==0){
			return 0d;
		}
		BigDecimal b = new BigDecimal(Double.valueOf(i1)/Double.valueOf(i2));
		return b.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	public static String getRoundHalfUp4String(Integer i1,Integer i2){
		if(i1 == null || i2 == null || i1.intValue() == 0 || i2.intValue() ==0){
			return "0";
		}
		Double b1 = Double.valueOf(i1.intValue());
		Double b2 = Double.valueOf(i2.intValue());
		BigDecimal b = new BigDecimal(b1/b2);
		return String.valueOf(b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
	}
	
	public static String getFormat100(Integer i1,Integer i2){
		if(i1 == null || i2 == null || i1.intValue() == 0 || i2.intValue() ==0){
			return "0%";
		}
		Double b1 = Double.valueOf(i1.intValue());
		Double b2 = Double.valueOf(i2.intValue());
		NumberFormat nt = NumberFormat.getPercentInstance();
		nt.setMinimumFractionDigits(2);
		return nt.format(b1/b2);
		
	}

	/**
	 * 获取昨天日期
	 * @return  yyyyMMdd
	 */
    public static Integer getYesterday() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		return Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(cal.getTime()));

	}

//	public static void main(String[] args){
//		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(1476442401000L)));
//	}

	/**
	 * 获取昨天日期字符串
	 * @param format  格式(例如:yyyy-MM-dd HH:mm:ss)
	 * @return
	 */
	public static String getYesterdayStr(String format) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		return new SimpleDateFormat(format).format(cal.getTime());
	}
}
