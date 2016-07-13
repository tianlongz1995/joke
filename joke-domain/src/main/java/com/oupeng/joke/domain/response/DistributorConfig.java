package com.oupeng.joke.domain.response;

import com.oupeng.joke.domain.AdConfig;
import com.oupeng.joke.domain.ChannelMenu;

import java.util.List;

/**
 * 渠道配置结果
 * Created by hushuang on 16/7/1.
 */
public class DistributorConfig {

    private List<ChannelMenu> channels;

    private AdConfig adConfig;

    public List<ChannelMenu> getChannels() {
        return channels;
    }

    public void setChannels(List<ChannelMenu> channels) {
        this.channels = channels;
    }

    public AdConfig getAdConfig() {
        return adConfig;
    }

    public void setAdConfig(AdConfig adConfig) {
        this.adConfig = adConfig;
    }
}
