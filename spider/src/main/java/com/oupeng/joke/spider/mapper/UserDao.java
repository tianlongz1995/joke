package com.oupeng.joke.spider.mapper;

import com.oupeng.joke.spider.domain.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * Created by Administrator on 2017/3/9.
 */
public interface UserDao {

    @Select("select * from nickname where id=#{id}")
    User select(Integer id);

    @Update("update nickname set last=#{last},updatetime=now() where id=#{id}")
    void update(@Param("last") Integer last,@Param("id") Integer id);
}
