package com.oupeng.joke.cache;

public class JedisKey {
    /** 点赞队列    */
    public static final String JOKE_LIST_LIKE = "list.joke.like";
    /** 踩队列    */
    public static final String JOKE_LIST_STEP = "list.joke.step";
    /** 反馈队列    */
    public static final String JOKE_LIST_FEEDBACK = "list.joke.feedback";
    /** 渠道配置信息Hash表key    */
    public static final String JOKE_HASH_DISTRIBUTOR_CONFIG = "hash.joke.distributor.config";
}
