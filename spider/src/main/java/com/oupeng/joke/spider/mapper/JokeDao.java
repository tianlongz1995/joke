package com.oupeng.joke.spider.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * Created by xiongyingl on 2017/6/19.
 */
public interface JokeDao {

    /**
     * 神评中点赞数最大的一条神评，插入到joke中
     *
     * @param id
     * @param comment
     * @param avata
     * @param nick
     */
    @Update("update joke set comment = #{comment}, avata = #{avata}, nick = #{nick} where id = #{id}")
    void updateJokeOfGod(@Param("id") Integer id, @Param("comment") String comment, @Param("avata") String avata, @Param("nick") String nick);
}
