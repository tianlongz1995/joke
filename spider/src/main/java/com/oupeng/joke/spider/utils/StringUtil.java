package com.oupeng.joke.spider.utils;

/**
 * Created by java_zong on 2017/4/12.
 */
public class StringUtil {
    /**
     * 切字符串
     * @param str
     * @param separator
     * @return
     */
    public static String substringAfter(String str, String separator) {
        if (str == null || str.length() <= 0) {
            return str;
        } else if (separator == null) {
            return "";
        } else {
            int pos = str.indexOf(separator);
            return pos == -1 ? str : str.substring(pos + separator.length());
        }
    }
}
