package com.oupeng.joke.dao.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Created by hushuang on 2017/3/23.
 */
public interface ManagerMapper {

    @Select("select img from choice")
    List<String> choiceList();

    @Select("select id from joke where release_avata is null or release_nick is null")
    List<Integer> getNotAvatarJoke();

    @Update("update joke set release_avata = #{avatar}, release_nick = #{nick} where id = #{id}")
    void updateAvatarAndNick(@Param("id") Integer id, @Param("avatar")String avatar, @Param("nick")String nick);
}
