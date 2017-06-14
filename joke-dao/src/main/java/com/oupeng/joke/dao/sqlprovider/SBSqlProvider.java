package com.oupeng.joke.dao.sqlprovider;


import com.oupeng.joke.domain.user.BlackMan;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Jane on 17-6-14.
 */
public class SBSqlProvider {
    /***
     *   看那些SB被拉黑
     */
    /***
     * 查看被拉黑
     * @return
     */
    public static String getASBStoBeLaHei(String id)
    {
        StringBuffer sql = new StringBuffer();
        sql.append("select count(1) from BlackMan where id=");
        sql.append(id).append(" limit 1");
        return sql.toString();
    }

    public static String getSBStoBeLaHei()
    {
        StringBuffer sql = new StringBuffer();
        sql.append("select id,create_time from BlackMan where 1=1");
        return sql.toString();
    }

    public static String insertSBtoBeLaHei(BlackMan sb)
    {
        StringBuffer sql = new StringBuffer();
        sql.append("insert into BlackMan(id,create_time) value(");
        if(StringUtils.isNoneBlank(sb.getId()))
            sql.append("'").append(sb.getId()).append("',");
        else
            sql.append("null,");

        sql.append("now() )");
        return sql.toString();
    }

    public  static  String deleteSBtoBeLaHei(BlackMan sb)
    {
        StringBuffer sql = new StringBuffer();
        sql.append("delete from BlackMan where id=");
        sql.append(sb.getId());
        return sql.toString();
    }
}
