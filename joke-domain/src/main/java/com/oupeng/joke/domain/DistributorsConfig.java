package com.oupeng.joke.domain;

import java.util.List;

/**
 * 渠道菜单配置
 * Created by hushuang on 16/7/13.
 */
public class DistributorsConfig {
    /**
     * 频道列表
     */
    private List<Channels> channels;
    /**
     * 广告信息
     */
    private Ads ads;

    public DistributorsConfig() {
    }

    public List<Channels> getChannels() {
        return channels;
    }

    public void setChannels(List<Channels> channels) {
        this.channels = channels;
    }

    public Ads getAds() {
        return ads;
    }

    public void setAds(Ads ads) {
        this.ads = ads;
    }
}
