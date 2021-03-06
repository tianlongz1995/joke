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

    //中文标点符号unicode码
    public static final String[] chinesePunctuation = {"3002","FF1F","FF01","FF0C","3001","FF1B","FF1A","2018","2019","201C","201D","FF08",
            "FF09","3014","3015","3010","3011","2014","2026","2013","FF0E","300A","300B","3008","3009"};

    //是否为中文标点
    public static boolean ispunc(char t){
        String uncoide_t = Integer.toHexString(t).toUpperCase();
        for(int i =0;i<chinesePunctuation.length;i++){
            if(chinesePunctuation[i].equals(uncoide_t)){
                return true;
            }
        }
        return false;
    }

    /**
     * 去掉字符串中的特殊字符
     * 键盘普通字符:[32,126] 汉字字符:[19968,40869] 中文标点符号:chinesePunctuation[]
     *
     * 三个特殊英文字符：用空格代替
     * \ 反斜线(ASCII 92)。用‘\\’表示该字符。
     * ' 单引号(ASCII 39)。用‘\'’表示该字符。
     * " 双引号(ASCII 34)。用‘\"’表示该字符。
     *
     */
    public static String removeSpecial(String str) {
        char[] chars = str.toCharArray();
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == 92 || chars[i] == 39 || chars[i] == 34) {
                buf.append(" ");
                continue;
            }
            if ((chars[i] >= 32 && chars[i] <= 126) || (chars[i] >= 19968 && chars[i] <= 40869) || ispunc(chars[i])) {
                buf.append(chars[i]);
            }
        }
        return buf.toString();
    }
}
