package com.oupeng.joke.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
/**
 * Created by rainy on 2017/2/8.
 */
public class EzineJoke {
    @JsonInclude(Include.NON_NULL)
    private String context;
    @JsonInclude(Include.NON_NULL)
    private int objectId;
    @JsonInclude(Include.NON_NULL)
    private String url;

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public int getObjectId() {
        return objectId;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
