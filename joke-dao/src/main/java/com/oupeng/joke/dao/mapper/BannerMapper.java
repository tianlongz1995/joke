package com.oupeng.joke.dao.mapper;

import com.oupeng.joke.dao.sqlprovider.BannerSqlProvider;
import com.oupeng.joke.domain.Banner;
import com.oupeng.joke.domain.Distributor;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.List;


public interface BannerMapper {

    /**
     * 新增Banner
     *
     * @param banner
     */
    @InsertProvider(method = "addBanner", type = BannerSqlProvider.class)
    @SelectKey(statement="SELECT LAST_INSERT_ID() as id", keyProperty="id", before=false, resultType=Integer.class)
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
                               @Param(value = "did")   Integer did);


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
     * 根据dbid获取banner
     * @param id banner id
     * @return
     */
    @Select(value="select b.id,b.title,b.jid,b.type,b.img,db.sort,b.cid,b.did,b.slot,b.status,b.content,b.width,b.height,b.create_time as createTime,b.update_time as updateTime,b.publish_time as publishTime from banner b left join distributors_banner db on b.id = db.b_id where db.id = #{id}")
    Banner getBannerByDbId(Integer id);

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
    @Delete(value = "update banner set status = 4 where id = #{id}")
    void delBanner(Integer id);

    /**
     * 更新banner状态
     * @param id
     * @param status
     */
    @Update("update banner set update_time = now(), status = #{status} where id = #{id}")
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
    @Select(value = "select b.id from banner b where b.status = 2 and DATE_FORMAT(b.publish_time,'%Y-%m-%d %H') = DATE_FORMAT(now(),'%Y-%m-%d %H')")
    List<Integer> getBannerForPublish();

    /**
     * 发布上线banner数量
     * @param cid
     * @return
     */
    @Select("select count(1) from banner where cid = #{cid} and did=#{did} and status in (2, 3)")
    Integer getBannerCount(@Param("did")Integer did, @Param("cid")Integer cid);

    /**
     * 添加渠道横幅关联
     * @param id
     * @param did
     */
    @InsertProvider(method="addDistributorBanner",type=BannerSqlProvider.class)
    void addDistributorBanner(@Param("id")Integer id, @Param("did")Integer[] did);

    /**
     * 获取已配置横幅的渠道编号列表
     * @param id
     * @return
     */
    @Select("select d_id from distributors_banner where b_id = #{id}")
    List<Integer> getDistributorsBanners(@Param("id")Integer id);

    /**
     * 删除渠道横幅关联关系
     * @param id
     */
    @Delete("delete from distributors_banner where b_id = #{id}")
    void delDistributorsBanners(@Param("id")Integer id);

    /**
     * 获取渠道列表
     * @param id
     * @return
     */
    @Select("select d.id, d.name from distributors d left join distributors_banner db on d.id = db.d_id where db.b_id = #{id}")
    List<Distributor> distributorList(@Param("id")Integer id);

    /**
     * 修改排序值
     * @param id
     * @param sort
     * @param username
     * @return
     */
    @Update("update distributors_banner set update_time = now(), update_by= #{username}, sort = #{sort} where id = #{id}")
    int editSort(@Param("id")Integer id,
                 @Param("sort")Integer sort,
                 @Param("username")String username);

    /**
     * 获取渠道横幅列表
     * @param id
     * @return
     */
    @Select("select b.id, b.title, b.jid, b.cid, b.img, b.type, b.slot, db.sort, db.d_id as did from banner b left join distributors_banner db on b.id = db.b_id where db.b_id = #{id} order by db.sort asc, b.update_time desc")
    List<Banner> getDistributorsBannersList(@Param("id")Integer id);
}
