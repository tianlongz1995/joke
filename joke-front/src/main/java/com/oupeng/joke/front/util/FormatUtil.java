package com.oupeng.joke.front.util;

import java.math.BigDecimal;

public class FormatUtil {
	public static Integer getHeight(Integer realHeight,Integer realWidth,Integer width){
		if(realHeight == null || realWidth == null || realHeight.intValue() == 0 || realWidth.intValue() ==0
				|| width == null || width.intValue() ==0){
			return 0;
		}
		BigDecimal b = new BigDecimal(Double.valueOf(width)/Double.valueOf(realWidth));
		return (int)(realHeight * (b.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue()));
	}
}
