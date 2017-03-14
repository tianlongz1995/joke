package test;

import org.apache.commons.io.FilenameUtils;

/**
 * Created by Administrator on 2017/3/10.
 */
public class StringTest {
   private static   String sb= test1();
   private static String test1(){
       System.out.println("test1");
     return "abc" ;
   }

    public static void test(String s){
      System.out.println(sb);
       System.out.println("test:"+s);
    }

    public static void main(String[] args){
        String url="E:/url/1.jpg";
        System.out.println(FilenameUtils.getFullPath(url)+FilenameUtils.getBaseName(url)+".gif");
    }
}
