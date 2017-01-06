package com.oupeng.joke.dao.mapper;

import com.oupeng.joke.dao.sqlprovider.BannerSqlProvider;
import com.oupeng.joke.domain.Banner;
import org.apache.ibatis.annotations.*;

import java.util.List;


public interface BannerMapper {

    /**
     * 新增Banner
     *
     * @param banner
     */
    @InsertProvider(method = "addBanner", type = BannerSqlProvider.class)
    void addBanner(Banner banner);

    /**
     * 统计 banner列表总数
     * @param status 状态
     * @param cid    频道id
     * @return
     */
    @SelectProvider(method = "getBannerListCount", type = BannerSqlProvider.class)
    Integer getBannerListCount(@Param(value = "status")Integer status,
                               @Param(value = "cid")   Integer cid);

    /**
     * 获取banner列表
     * @param status    状态
     * @param cid       频道id
     * @param offset    偏移
     * @param pageSize  页面显示条数
     * @return
     */
    @SelectProvider(method = "getBannerList",type = BannerSqlProvider.class)
    List<Banner> getBannerList(@Param(value= "status")Integer status,
                               @Param(value = "cid") Integer cid,
                               @Param(value= "offset")Integer offset,
                               @Param(value ="pageSize")Integer pageSize);

    /**
     * 根据id获取banner
     * @param id banner id
     * @return
     */
    @Select(value="select id,title,jid,type,img,cid,adid,status,content,"
            + "create_time as createTime,update_time as updateTime from banner where id = #{id}")
    Banner getBannerById(Integer id);

    /**
     * 更新banner
     * @param banner
     */
    @UpdateProvider(method="updateBanner",type=BannerSqlProvider.class)
    void updateBanner(Banner banner);

    /**
     * 删除Banner
     * @param id
     */
    @Delete(value = "delete from banner where id = #{id}")
    void delBanner(Integer id);

    /**
     * 更新banner状态
     * @param id
     * @param status
     */
    @Update("update banner set update_time=now(),status = #{status} where id = #{id}")
    void updateBannerStatus(@Param(value = "id") Integer id,
                            @Param(value = "status") Integer status);


    /**
     * 获取banner sort值排序列表
     * @param cid
     * @return
     */
    @Select(value = "SELECT id ,cid, sort FROM banner WHERE cid = #{cid} ORDER BY sort ASC")
    List<Banner> getBannerMoveList(Integer cid);
    /**
     * 更新banner排序值
     * @param id
     * @param sort
     */
    @Update("update banner set sort = #{sort} where id = #{id}")
    void updateBannerSort(@Param(value = "id")Integer id,
                          @Param(value = "sort")Integer sort);

    /**
     * 获取频道下banner最大的排序值
     * @param cid
     * @return
     */
    @Select("select sort from banner  WHERE cid = #{cid}  ORDER BY sort desc LIMIT 1")
    Integer getMaxSortByCid(Integer cid);

}