package com.oupeng.joke.dao.mapper;


import com.oupeng.joke.dao.sqlprovider.ChoiceSqlProvider;
import com.oupeng.joke.domain.Choice;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface ChoiceMapper {

    /**
     * 统计精选列表条数
     * @param status
     * @return
     */
    @SelectProvider(method = "getChoiceListCount",type = ChoiceSqlProvider.class)
    Integer getChoiceListCount(@Param(value = "status")Integer status);


    /**
     * 获取精选列表
     * @param status
     * @param offset
     * @param pageSize
     * @return
     */
    @SelectProvider(method = "getBannerList",type = ChoiceSqlProvider.class)
    List<Choice> getBannerList(@Param(value = "status") Integer status,
                               @Param(value = "offset") Integer offset,
                               @Param(value = "pageSize") Integer pageSize);

    /**
     * 添加精选
     * @param title
     * @param content
     */
   @InsertProvider(method = "addChoice",type = ChoiceSqlProvider.class)
    void addChoice(@Param(value = "title") String title,
                   @Param(value = "content") String content);

    /**
     * 删除
     * @param id
     */
    @Delete("delete from choice where id = #{id}")
    void delChoice(Integer id);

    /**
     * 根据id获取精选
     * @param id
     * @return
     */
    @Select(value = "select id,title,status,content from choice where id = #{id}")
    Choice getChoiceById(Integer id);


    @UpdateProvider(method = "updateChoice",type = ChoiceSqlProvider.class)
    void updateChoice(@Param(value = "id") Integer id,
                      @Param(value = "title") String title,
                      @Param(value = "content")String content) ;
}
