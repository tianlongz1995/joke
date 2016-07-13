package com.oupeng.joke.domain;

import java.util.List;

/**
 * 广告配置
 * Created by hushuang on 16/7/13.
 */
public class AdConfig {
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

    public AdConfig() {
    }

    public AdConfig(List<Ad> ads) {
        if(ads == null){
            return;
        }
        for(Ad ad : ads){
            if(ad.getPos() == 1){
                s = ad.getSlide();
                lc = ad.getSlotId();
            } else if(ad.getPos() == 2){
                lb = ad.getSlotId();
            } else if(ad.getPos() == 3){
                dt = ad.getSlotId();
            } else if(ad.getPos() == 4){
                dc = ad.getSlotId();
            } else if(ad.getPos() == 5){
                db = ad.getSlotId();
            }
        }
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
}
