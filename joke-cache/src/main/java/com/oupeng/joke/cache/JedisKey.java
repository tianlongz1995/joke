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
//    /** 专题频道封面首页    */
//    public static final String SORTEDSET_TOPIC_COVER_LIST = "sortedset.topic.channel";
    /** 专题列表    */
    public static final String SORTEDSET_TOPIC_LIST = "sortedset.topic.list";
    /** 专题段子集合    */
    public static final String SORTEDSET_TOPIC_JOKE_SET = "sortedset.topic.joke.set.";
    /** 普通频道段子编号列表   */
    public static final String SORTEDSET_COMMON_CHANNEL = "sortedset.common.channel.";
    /** 推荐频道段子ID列表    */
    public static final String SORTEDSET_RECOMMEND_CHANNEL = "sortedset.recommend.channel";
    /** 段子缓存    */
    public static final String STRING_JOKE = "string.joke.";
    /** 专题封面缓存    */
    public static final String STRING_TOPIC_COVER = "string.topic.cover.";
    /** 专题缓存    */
    public static final String STRING_TOPIC = "string.topic.";

    public static final String SORTEDSET_ALL = "sortedset.*";
    public static final String SORTEDSET_TOPIC_ALL = "sortedset*topic*";
    /** 相关文字段子    */
    public static final String SET_RELATED_JOKE_TEXT = "set.related.joke.text";
    /** 相关图片段子    */
    public static final String SET_RELATED_JOKE_IMG = "set.related.joke.img";
    /** 推荐文字段子    */
    public static final String SET_RECOMMEDN_JOKE_TEXT = "set.recommend.joke.text";
    /** 段子最后发布时间    */
    public static final String JOKE_LAST_PUBLISH_TIME = "joke.last.publish.time";
}
