package test;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/3/10.
 */
public class TimeTest {

    public static void main(String[] args) {
//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date date = new Date();
//        String dates = dateFormat.format(date);
//        System.out.println(Timestamp.valueOf(dates).getTime() / 1000);
        String s="bnf";
        for (int i=0;i<2;i++) {
            StringTest.test(s);
        }
    }
}
