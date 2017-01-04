package com.oupeng.joke.domain;

/**
 * 链接资源
 * Created by hushuang on 2016/12/30.
 */
public class LinkSource {
    /** css    */
    private String css;
    /** 公共JS    */
    private String common;
    /** 编译JS    */
    private String build;

    public String getCss() {
        return css;
    }

    public void setCss(String css) {
        this.css = css;
    }

    public String getCommon() {
        return common;
    }

    public void setCommon(String common) {
        this.common = common;
    }

    public String getBuild() {
        return build;
    }

    public void setBuild(String build) {
        this.build = build;
    }
}
