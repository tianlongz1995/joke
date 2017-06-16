package com.oupeng.joke.dao.sqlprovider;


import com.oupeng.joke.domain.user.BlackMan;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Jane on 17-6-14.
 */
public class BlackManSqlProvider {
    /***
     * 查看是否被拉黑
     * @return
     */
    public static String countBlackMantoBeLaHei(String id) {
        StringBuffer sql = new StringBuffer();
        sql.append("select count(1) from black_man where id=");
        sql.append(id).append(" limit 1");
        return sql.toString();
    }

    public static String countBLackMantoBeLaHei(){
        StringBuffer sql=new StringBuffer();
        sql.append("select count(1) from black_man where 1=1");
        return sql.toString();
    }

    /**
     * 查询一个黑人
     * @param id
     * @return
     */
    public static String getBlackMantoBeLaHei(String id){
        StringBuffer sql=new StringBuffer();
        sql.append("select id,nick,create_time,create_by from black_man where id='").append(id).append("'");
        return sql.toString();
    }

    public static String listBlackMantoBeLaHei(){
        StringBuffer sql=new StringBuffer();
        sql.append("select id,nick,create_time,create_by from black_man where 1=1");
        return sql.toString();
    }
    /**
     * 插入一个黑人
     * @param sb
     * @return
     */
    public static String insertABlackMantoBeLaHei(BlackMan sb) {
        StringBuffer sql = new StringBuffer();
        sql.append("insert into black_man(id,nick,create_time,create_by) value(");
        sql.append("'").append(sb.getId()).append("',");
        if(StringUtils.isNotBlank(sb.getNick())) {
            sql.append("'").append(sb.getNick()).append("',");
        }
        else {
            sql.append("null,");
        }
        sql.append("now(),");
        if(StringUtils.isNotBlank(sb.getCreate_by()))
            sql.append("'").append(sb.getCreate_by()).append("')");
        else
            sql.append("null )");
        return sql.toString();
    }

    /**
     * 删除一个黑人
     * @param
     * @return
     */
    public  static  String deleteBlackMantoBeLaHei(String uid) {
        StringBuffer sql = new StringBuffer();
        sql.append("delete from black_man where id='");
        sql.append(uid).append("'");
        return sql.toString();
    }
}
