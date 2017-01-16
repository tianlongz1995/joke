package com.oupeng.joke.domain;

import java.util.Date;

/**
 * 广告配置
 * Created by hushuang on 16/7/13.
 */
public class Ads {
    /**	广告编号	*/
    private Integer id;
    /**	渠道编号	*/
    private Integer did;
    /**	广告插入间隔（位置pos=1时，）	*/
    private Integer s;
    /**	列表页中间 listCenter	*/
    private Integer lc;
    /**	列表页底部 listBottom	*/
    private Integer lb;
    /**	详情页上方 detailsTop	*/
    private Integer dt;
    /**	详情页中部 detailsCentor	*/
    private Integer dc;
    /**	详情页底部 detailsBottom	*/
    private Integer db;
    /**	详情页插屏 detailsInterstitial	*/
    private Integer di;
    /**详情页推荐广告 detailsRecommendAd*/
    private Integer dr;
    /**	创建时间	*/
    private Date createTime;
    /**	创建人	*/
    private String createBy;
    /**	更新时间	*/
    private Date updateTime;
    /**	更新人	*/
    private String updateBy;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getS() {
        return s;
    }

    public void setS(Integer s) {
        this.s = s;
    }

    public Integer getLc() {
        return lc;
    }

    public void setLc(Integer lc) {
        this.lc = lc;
    }

    public Integer getLb() {
        return lb;
    }

    public void setLb(Integer lb) {
        this.lb = lb;
    }

    public Integer getDt() {
        return dt;
    }

    public void setDt(Integer dt) {
        this.dt = dt;
    }

    public Integer getDc() {
        return dc;
    }

    public void setDc(Integer dc) {
        this.dc = dc;
    }

    public Integer getDb() {
        return db;
    }

    public void setDb(Integer db) {
        this.db = db;
    }

    public Integer getDi() {
        return di;
    }

    public void setDi(Integer di) {
        this.di = di;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getDid() {
        return did;
    }

    public void setDid(Integer did) {
        this.did = did;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Integer getDr() {
        return dr;
    }

    public void setDr(Integer dr) {
        this.dr = dr;
    }
}
