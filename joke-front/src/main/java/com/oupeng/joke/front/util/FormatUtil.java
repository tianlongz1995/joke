package com.oupeng.joke.front.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FormatUtil {
	public static Integer getHeight(Integer realHeight,Integer realWidth,Integer width){
		if(realHeight == null || realWidth == null || realHeight.intValue() == 0 || realWidth.intValue() ==0
				|| width == null || width.intValue() ==0){
			return 0;
		}
		BigDecimal b = new BigDecimal(Double.valueOf(width)/Double.valueOf(realWidth));
		return (int)(realHeight * (b.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue()));
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

	/**
	 * 获取时间字符串
	 * @param time millisecond (毫秒)
	 * @return
	 */
	public static String getTimeStr(long time) {
		StringBuffer sb = new StringBuffer();
		if(time < 1000){
			sb.append(time).append("毫秒");
		}else if(time < 1000 * 60){
			long sec = (time / 1000);
			long m_mod = (time % 1000);
			sb.append(sec).append("秒");
			if(m_mod > 0){
				sb.append(m_mod).append("毫秒");
			}
		}else if(time < 1000 * 60 * 60){
			long min = (time / 60000);
			long m_mod = (time % 60000);
			long sec = m_mod /1000;
			sb.append(min).append("分钟");
			if(sec > 0){
				sb.append(sec).append("秒");
			}
		} else {
			long hour = time / 3600000;
			long m_time = time % 3600000;
			long min = (m_time / 60000);

			sb.append(hour).append("小时");
			if(min > 0){
				long m_mod = (m_time % 60000);
				long sec = m_mod /1000;
				sb.append(min).append("分钟");
				if(sec > 0){
					sb.append(sec).append("秒");
				}
			}
		}
		return sb.toString();
	}
}
