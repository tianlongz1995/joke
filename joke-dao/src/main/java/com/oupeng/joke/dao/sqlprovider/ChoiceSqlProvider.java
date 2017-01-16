package com.oupeng.joke.dao.sqlprovider;

import java.util.Map;


public class ChoiceSqlProvider {

    /**
     * 统计精选总条数
     *
     * @param map
     * @return
     */
    public static String getChoiceListCount(Map<String, Object> map) {
        Object status = map.get("status");
        StringBuffer sql = new StringBuffer();
        sql.append("select count(1) from choice where 1 = 1 ");
        if (status != null) {
            sql.append(" and status = ").append(status);
        }
        return sql.toString();
    }

    /**
     * 获取精选列表
     *
     * @param map
     * @return
     */
    public static String getBannerList(Map<String, Object> map) {
        Object status = map.get("status");
        Object offset = map.get("offset");
        Object pageSize = map.get("pageSize");
        StringBuffer sql = new StringBuffer();
        sql.append(" select id,title,img,content,status,");
        sql.append(" create_time as createTime,update_time as updateTime from choice where 1 = 1 ");
        if (null != status) {
            sql.append(" and status = ").append(status);
        } else {
            sql.append(" and status != 2 ");
        }
        sql.append(" order by create_time desc ");
        if (offset != null && pageSize != null) {
            sql.append(" limit ").append(offset).append(" , ").append(pageSize);
        }
        return sql.toString();
    }

    /**
     * 添加精选
     *
     * @param map
     * @return
     */
    public static String addChoice(Map<String, Object> map) {
        Object title = map.get("title");
        Object content = map.get("content");
        Object image = map.get("image");
        Object width = map.get("width");
        Object height = map.get("height");
        StringBuffer sql = new StringBuffer();
        sql.append(" insert into choice(title,img,status,content,width,height,create_time, update_time) value (");
        if (null != title) {
            sql.append("'").append(title).append("',");
        } else {
            sql.append("null,");
        }
        if(null != image){
            sql.append("'").append(image).append("',");
        } else{
            sql.append("null,");
        }
        //status
        sql.append("0,");
        //content
        if (null != content) {
            sql.append("'").append(content).append("',");
        } else {
            sql.append("null,");
        }
        if(null != width){
            sql.append("'").append(width).append("',");
        }else {
            sql.append("0 ,");
        }

        if(null != height){
            sql.append("'").append(height).append("',");
        }else {
            sql.append("0 ,");
        }
        sql.append("now(),now())");
        return sql.toString();
    }

    /**
     * 更新精选内容
     *
     * @param map
     * @return
     */
    public static String updateChoice(Map<String, Object> map) {
        Object id = map.get("id");
        Object title = map.get("title");
        Object content = map.get("content");
        Object image = map.get("image");
        Object width = map.get("width");
        Object height = map.get("height");
        StringBuffer sql = new StringBuffer();
        sql.append(" update choice set update_time=now(),title = ");
        if (null != title) {
            sql.append("'").append(title).append("',");
        } else {
            sql.append("null,");
        }
        sql.append(" content=");
        if (null != content) {
            sql.append("'").append(content).append("',");
        } else {
            sql.append("null,");
        }
        sql.append(" img = ");
        if (null != image) {
            sql.append("'").append(image).append("',");
        } else {
            sql.append("null,");
        }
        sql.append(" width = ");
        if (null != width) {
            sql.append(width).append(",");
        } else {
            sql.append("0,");
        }
        sql.append(" height = ");
        if (null != width) {
            sql.append(height);
        } else {
            sql.append("0");
        }
        sql.append(" where id = ").append(id);
        return sql.toString();
    }
}
