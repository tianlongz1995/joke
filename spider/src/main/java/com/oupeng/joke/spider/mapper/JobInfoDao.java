package com.oupeng.joke.spider.mapper;


import com.oupeng.joke.spider.domain.JokeImg;

import org.apache.ibatis.annotations.*;


/**
 * Created by zongchao on 2017/3/8.
 */
public interface JobInfoDao {


    @Insert("insert into joke (`title`,`img`,`gif`,`width`,`height`,`comment`,`type`,`create_time`,`src`,`avata`,`nick`,`source_id`,`release_avata`,`release_nick`)values (#{title},#{img},#{gif},#{width},#{height},#{commentContent},#{type},now(),#{src},#{avata},#{nick},#{sourceId},#{releaseAvata},#{releaseNick})")
    void addImg(JokeImg img);

    @Select("select max(id) from joke")
    int getLastId();

    @Select("select count(id) from  joke where src=#{src}")
    int isExist(String src);
}
