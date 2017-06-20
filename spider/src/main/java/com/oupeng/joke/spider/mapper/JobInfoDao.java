package com.oupeng.joke.spider.mapper;


import com.oupeng.joke.spider.domain.JokeImg;
import org.apache.ibatis.annotations.*;


/**
 * Created by zongchao on 2017/3/8.
 */
public interface JobInfoDao {


    @Insert("insert into joke (`title`,`img`,`gif`,`width`,`height`,`comment`,`type`,`create_time`,`src`,`avata`,`nick`,`source_id`,`release_avata`,`release_nick`,`good`,`bad`,`comment_number`)values (#{title},#{img},#{gif},#{width},#{height},#{commentContent},#{type},now(),#{src},#{avata},#{nick},#{sourceId},#{releaseAvata},#{releaseNick},#{good},#{bad},#{commentNumber})")
    @SelectKey(statement="SELECT LAST_INSERT_ID() as id", keyProperty="id", before=false, resultType=Integer.class)
    void addJokeImg(JokeImg img);

    @Select("select id from joke where img=#{img}")
    int getLastId(String img);

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
