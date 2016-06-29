package com.oupeng.joke.front.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

public class IpUtil {
	
	private final static long ipBegin_10 = getIpNum("10.0.0.0");//10.0.0.0
	private final static long ipEnd_10 = getIpNum("10.255.255.255");//10.255.255.255
	private final static long ipBegin_172 = getIpNum("172.16.0.0");//172.16.0.0 
	private final static long ipEnd_172 = getIpNum("172.31.255.255");;//172.31.255.255
	private final static long ipBegin_192 = getIpNum("192.168.0.0");//192.168.0.0
	private final static long ipEnd_192 = getIpNum("192.168.255.255");//192.168.255.255
	
	public static String getRemoteAddr(HttpServletRequest request){
    	String ip = request.getHeader("X-Forwarded-For");
    	if(StringUtils.isNotEmpty(ip) && !StringUtils.equalsIgnoreCase("unknown", ip)){
    		int index = ip.indexOf(",");
            if (index != -1) {
            	ip = ip.substring(0, index);
            }
    	}
    	
    	if(!isValidIp(ip)){
    		ip = request.getHeader("x-real-ip");
    	}
    	
    	if(!isValidIp(ip)){
    		ip = request.getRemoteAddr();
    	}
    	
    	return ip;
    }
	
	private static boolean isValidIp(String ip){
    	if(StringUtils.isBlank(ip) || isInnerIp(ip)){
    		return false;
    	}
    	return true;
    }
    
	private static boolean isInnerIp(String ipAddress){   
        long ipNum = getIpNum(ipAddress);   
        return ipAddress.equals("127.0.0.1") || isInner(ipNum,ipBegin_10,ipEnd_10) || isInner(ipNum,ipBegin_172,ipEnd_172) || isInner(ipNum,ipBegin_192,ipEnd_192);   
    }  
    
    private static long getIpNum(String ipAddress) {   
        String [] ip = ipAddress.split("\\.");   
        long a = Integer.parseInt(ip[0]);   
        long b = Integer.parseInt(ip[1]);   
        long c = Integer.parseInt(ip[2]);   
        long d = Integer.parseInt(ip[3]);   
        long ipNum = a * 256 * 256 * 256 + b * 256 * 256 + c * 256 + d;   
        return ipNum;   
    }
    
    private static boolean isInner(long userIp,long begin,long end){   
        return (userIp>=begin) && (userIp<=end);   
    } 
}
