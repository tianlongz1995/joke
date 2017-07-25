package com.oupeng.joke.back.util;

/**
 * Created by java_zong on 2017/4/12.
 */
public class StringUtil {

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
     * 键盘有效字符:[32,126] 汉字字符:[19968,40869] 中文标点符号:chinesePunctuation[]
     */
    public static String removeSpecial(String str) {
        char[] chars = str.toCharArray();
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            if ((chars[i] >= 32 && chars[i] <= 126) || (chars[i] >= 19968 && chars[i] <= 40869) || ispunc(chars[i])) {
                buf.append(chars[i]);
            }
        }
        return buf.toString();
    }

}
