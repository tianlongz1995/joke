package com.oupeng.joke.back.util;

import java.math.BigDecimal;

public class FormatUtil {
	public static Double getRoundHalfUp(Integer i1,Integer i2){
		if(i1 == null || i2 == null || i1.intValue() == 0 || i2.intValue() ==0){
			return 0d;
		}
		BigDecimal b = new BigDecimal(Double.valueOf(i1)/Double.valueOf(i2));
		return b.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
}
