package com.oupeng.joke.domain.response;

import com.oupeng.joke.domain.AdConfig;
import com.oupeng.joke.domain.ChannelMenu;

import java.util.List;

/**
 * 渠道配置结果
 * Created by hushuang on 16/7/1.
 */
public class DistributorConfigResult extends Result {

    private List<ChannelMenu> channel;

    private AdConfig adConfig;

    public DistributorConfigResult() {
        super(200, null, null);
    }

    public DistributorConfigResult(int status, String info, Object data) {
        super(status, info, data);
    }

    public List<ChannelMenu> getChannel() {
        return channel;
    }

    public void setChannel(List<ChannelMenu> channel) {
        this.channel = channel;
    }

    public AdConfig getAdConfig() {
        return adConfig;
    }

    public void setAdConfig(AdConfig adConfig) {
        this.adConfig = adConfig;
    }
}
