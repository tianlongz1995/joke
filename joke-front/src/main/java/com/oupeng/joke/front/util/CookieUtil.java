package com.oupeng.joke.front.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

public class CookieUtil {

	private static final String COOKIE_NAME = "uid";
	
	public static String getCookie(HttpServletRequest request){
		Cookie[] cookies = request.getCookies();
		if(cookies != null && cookies.length > 0){
			for(Cookie cookie : cookies){
				if(COOKIE_NAME.equalsIgnoreCase(cookie.getName())){
					return StringUtils.rightPad(cookie.getValue(), 24, "=");
				}
			}
		}
		return null;
	}
}
