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
    
    public static final String SORTEDSET_DISTRIBUTOR_TOPIC = "sortedset.distributor.topic.";
    public static final String SORTEDSET_TOPIC_CHANNEL = "sortedset.topic.channel.";
    public static final String SORTEDSET_COMMON_CHANNEL = "sortedset.common.channel.";
    public static final String SORTEDSET_RECOMMEND_CHANNEL = "sortedset.recommend.channel";
    public static final String STRING_JOKE = "string.joke.";
    public static final String SORTEDSET_ALL = "sortedset.*";
    public static final String SORTEDSET_TOPIC_ALL = "sortedset*topic*";
    
    public static final String SET_RELATED_JOKE_TEXT = "set.related.joke.text";
    public static final String SET_RELATED_JOKE_IMG = "set.related.joke.img";
    public static final String SET_RECOMMEDN_JOKE_TEXT = "set.recommend.joke.text";
}
