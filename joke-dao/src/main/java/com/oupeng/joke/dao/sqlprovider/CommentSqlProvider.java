package com.oupeng.joke.dao.sqlprovider;

import com.oupeng.joke.domain.Comment;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by java_zong on 2017/4/27.
 */
public class CommentSqlProvider {

    /**
     * 获取评论总数sql
     *
     * @param map
     * @return
     */
    public static String getListForVerifyCount(Map<String, Object> map) {
        String keyWord = (String) map.get("keyWord");
        Integer state = (Integer) map.get("state");
        StringBuffer sql = new StringBuffer();
        sql.append(" select count(id) from `comment` as c where 1=1 ");
        if (StringUtils.isNotBlank(keyWord)) {
            sql.append(" and c.content like ").append("'%").append(keyWord).append("%'");
        }
        if (state != null) {
            sql.append(" and c.state = ").append(state);
        }
        return sql.toString();
    }

    /**
     * 获取评论列表sql
     *
     * @param map
     * @return
     */
    public static String getListForVerify(Map<String, Object> map) {
        String keyWord = (String) map.get("keyWord");
        Integer state = (Integer) map.get("state");
        Integer offset = (Integer) map.get("offset");
        Integer pageSize = (Integer) map.get("pageSize");
        StringBuffer sql = new StringBuffer();
        sql.append(" select c.id, c.state, c.uid, c.nickname as nick, c.content as bc, c.createtime as time, c.createtime as createTime from `comment` as c where 1=1");
        if (StringUtils.isNotBlank(keyWord)) {
            sql.append(" and c.content like ").append("'%").append(keyWord).append("%'");
        }
        if (state != null) {
            sql.append(" and c.state = ").append(state);
        }
        sql.append(" order by c.createtime desc limit ");
        sql.append(offset).append(" , ").append(pageSize);
        return sql.toString();
    }

    /**
     * 待发布评论
     *
     * @param map
     * @return
     */
    public static String getCommentForPublish(Map<String, Object> map) {
        String ids = (String) map.get("ids");
        Integer state = (Integer) map.get("state");
        Integer pubState = (Integer) map.get("pubState");
        StringBuffer sql = new StringBuffer();
        sql.append(" select id, content as bc, avata, nickname as nick,sid as jokeId, createtime as time, good from `comment` as c where 1 = 1 ");
        if (state != null) {
            sql.append(" and c.state = ").append(state);
        }
        if (pubState != null) {
            sql.append(" and c.publish_state =  ").append(pubState);
        }
        if (StringUtils.isNotBlank(ids)) {
            sql.append(" and id in (").append(ids).append(")");
        }
        return sql.toString();
    }

    /**
     * 批量插入
     *
     * @param map
     * @return
     */
    public static String insertBatchComment(Map<String, Object> map) {

        List<Comment> list = (List<Comment>)map.get("list");

        StringBuffer buf = new StringBuffer();
        buf.append("insert into comment (`state`,`sid`,`uid`,`nickname`,`content`,`avata`,`good`,`createtime`,`publish_state`) values");
        for (Comment comment : list) {
            buf.append(" (1,'").append(comment.getJokeId()).append("','")
                    .append(comment.getUid()).append("','")
                    .append(comment.getNick()).append("','")
                    .append(comment.getBc()).append("','")
                    .append(comment.getAvata()).append("',")
                    .append(comment.getGood()).append(",")
                    .append(comment.getTime()).append(",1),");
        }
        String sql = buf.toString();
        String sql0 = sql.substring(0,sql.length() - 1);
        return sql0;
    }

}
