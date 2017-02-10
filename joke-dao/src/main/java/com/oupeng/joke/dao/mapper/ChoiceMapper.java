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
                   @Param(value = "content") String content,
                   @Param(value = "image") String image,
                   @Param(value = "width") Integer width,
                   @Param(value = "height") Integer height,
                   @Param(value = "publishTime") String publishTime);

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
    @Select(value = "select id, title, img, status, content, width, height, good, bad,publish_time as publishTime from choice where id = #{id}")
    Choice getChoiceById(Integer id);


    @UpdateProvider(method = "updateChoice",type = ChoiceSqlProvider.class)
    void updateChoice(@Param(value = "id") Integer id,
                      @Param(value = "title") String title,
                      @Param(value = "content")String content,
                      @Param(value = "image")String image,
                      @Param(value = "width") Integer width,
                      @Param(value = "height") Integer height,
                      @Param(value = "publishTime") String publishTime) ;

    /**
     * 更新状态
     * @param id
     * @param status
     */
    @Update(value = "update choice set update_time=now(),status = #{status} where id = #{id}")
    void updateChoiceStatus(@Param(value = "id") Integer id,
                            @Param(value = "status") Integer status);

    /**
     * 查询待发布的精选
     * @return
     */
    @Select(value = "select id, title, img, status, content, width, height, good, bad," +
            "create_time as createTime,update_time as updateTime,publish_time as publishTime from choice where `status` = 2 and DATE_FORMAT(publish_time,'%Y-%m-%d %H') = DATE_FORMAT(now(),'%Y-%m-%d %H') ")
    List<Choice> getBannerForPublish();
}
