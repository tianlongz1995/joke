package com.oupeng.joke.domain;

/**
 * 广告配置
 * Created by hushuang on 16/7/1.
 */
public class AdConfig {
    /** 列表页广告间隔数 listInterval（对应列表页中间，其他位置没有广告间隔）    */
    private Integer i;
    /**  广告位置（列表页中间 listCenter）   */
    private String lc;
    /**  广告位置（列表页底部listBottom）   */
    private String lb;
    /**  广告位置（详情页顶部detailTop）   */
    private String dt;
    /**  广告位置（detailCenter）   */
    private String dc;
    /**  广告位置（详情页底部detailBottom）   */
    private String db;

    public Integer getI() {
        return i;
    }

    public void setI(Integer i) {
        this.i = i;
    }

    public String getLc() {
        return lc;
    }

    public void setLc(String lc) {
        this.lc = lc;
    }

    public String getLb() {
        return lb;
    }

    public void setLb(String lb) {
        this.lb = lb;
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public String getDc() {
        return dc;
    }

    public void setDc(String dc) {
        this.dc = dc;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }
}
