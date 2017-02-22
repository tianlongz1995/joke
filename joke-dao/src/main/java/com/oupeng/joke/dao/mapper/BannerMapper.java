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
                               @Param(value = "cid")   Integer cid,
                               @Param(value = "did") Integer did);

    /**
     * 获取banner列表
     * @param status    状态 0新建 1下线 2上线 3已发布
     * @param cid       频道id
     * @param offset    偏移
     * @param pageSize  页面显示条数
     * @return
     */
    @SelectProvider(method = "getBannerList",type = BannerSqlProvider.class)
    List<Banner> getBannerList(@Param(value= "status")Integer status,
                               @Param(value = "cid") Integer cid,
                               @Param(value = "did") Integer did,
                               @Param(value= "offset")Integer offset,
                               @Param(value ="pageSize")Integer pageSize);

    /**
     * 根据id获取banner
     * @param id banner id
     * @return
     */
    @Select(value="select id,title,jid,type,img,sort,cid,did,slot,status,content,width,height,"
            + "create_time as createTime,update_time as updateTime,publish_time as publishTime from banner where id = #{id}")
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
    @Select(value = "SELECT id,title,jid,type,sort,img,cid,slot,status,content,"
            + "create_time as createTime,update_time as updateTime FROM banner WHERE cid = #{cid} and did = #{did} and status = 2 ORDER BY sort ASC")
    List<Banner> getBannerMoveList(@Param(value = "cid") Integer cid,
                                   @Param(value = "did") Integer did);
    /**
     * 更新banner排序值
     * @param id
     * @param sort
     */
    @Update("update banner set sort = #{sort} where id = #{id}")
    void updateBannerSort(@Param(value = "id")Integer id,
                          @Param(value = "sort")Integer sort);

    /**
     * 获取频道下已发布的banner最大的排序值
     * @param cid
     * @return
     */
    @Select("select sort from banner  WHERE cid = #{cid}  and did = #{did} and status = 3 ORDER BY sort desc LIMIT 1")
    Integer getMaxSortByCidAndDid(@Param(value = "cid") Integer cid,
                            @Param(value = "did") Integer did);


    /**
     * 获取带发布的banner
     * @return
     */
    @Select(value = "select id,title,jid,type,img,sort,cid,did,slot,status,content,width,height," +
            "create_time as createTime,update_time as updateTime,publish_time as publishTime from banner where `status` = 2 and DATE_FORMAT(publish_time,'%Y-%m-%d %H') = DATE_FORMAT(now(),'%Y-%m-%d %H')")
    List<Banner> getBannerForPublish();

    /**
     * 发布上线banner数量
     * @param cid
     * @return
     */
    @Select("select count(1) from banner where cid = #{cid} and did=#{did} and status in (2, 3)")
    Integer getBannerCount(Integer did,Integer cid);
}
