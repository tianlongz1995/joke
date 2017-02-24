package com.oupeng.joke.front.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;
import com.oupeng.joke.domain.EzineJoke;
import com.oupeng.joke.domain.Joke;
import com.oupeng.joke.domain.Topic;
import com.oupeng.joke.front.util.Constants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.*;


@Service
public class ClientService {

    private static final String PNG = ".png";
    private static final String JPG = ".jpg";
    private static final String SHARE_URL_PREFIX = "http://joke.oupeng.com/?did=12#";
    /** 开始浏览位置    */
    private static final String START_POS = "0";
    /** 浏览位置头    */
    private static final String POS_PREFIX = "pos_";
    /** 历史浏览记录    */
    private static final String HISTORY_PREFIX = "history_";
    /** 推荐频道    */
    private static final String CHANNEL_RECOMMEND = "joke.channel.3";
    /** 段子频道    */
    private static final String CHANNEL_JOKE = "joke.channel.2";
    /** 图片前缀    */
    private static String IMG_PREFIX = "http://joke2-img.oupeng.com/";

    private static List<String> addLikeIds = Lists.newCopyOnWriteArrayList();
    /**
     * 赞信息列表
     */
    private static List<String> addStepIds = Lists.newCopyOnWriteArrayList();
    /**
     * 踩信息列表
     */
    private static Logger logger = LoggerFactory.getLogger(ClientService.class);
    @Autowired
    private JedisCache jedisCache;
    @Autowired
    private Environment env;

    /**
     * 修改图片后缀
     *
     * @param img
     * @return
     */
    private static String png2jpg(String img) {
        return img.replace(PNG, JPG);
    }

    /**
     * 初始化时获取图片前缀
     */
    @PostConstruct
    public void initConstants() {
        String imgPrefix = env.getProperty("img.real.server.url");
        if(StringUtils.isNotBlank(imgPrefix)){
            IMG_PREFIX = imgPrefix;
        }
    }

//    /**
//     * 文字
//     *
//     * @param cid        频道id
//     * @param uid        用户id
//     * @param actionType 0:首次请求; 1:上拉刷新; 2:下拉刷新
//     * @param sort       排序值，记录上拉请求，用户看到的段子编号
//     * @return
//     */
//    public List<Joke> getTextList(int cid, String uid, int actionType, int sort) {
//        String key = JedisKey.SORTEDSET_COMMON_CHANNEL + cid;
//        if (actionType == 1) {
//            return getOldJokeCacheList(key, sort, uid, 0);
//        } else {
//            return getNewJokeCacheList(key, uid, 0);
//        }
//    }
//
//    /**
//     * 动图
//     *
//     * @param cid        频道id
//     * @param uid        用户id
//     * @param actionType 0:首次请求; 1:上拉刷新; 2:下拉刷新
//     * @param sort       排序值，记录上拉请求，用户看到的段子编号
//     * @return
//     */
//    public List<Joke> getGiftList(int cid, String uid, int actionType, int sort) {
//        String key = JedisKey.SORTEDSET_COMMON_CHANNEL + cid;
//        if (actionType == 1) {
//            return getOldJokeCacheList(key, sort, uid, 1);
//        } else {
//            return getNewJokeCacheList(key, uid, 1);
//        }
//    }
//
//    /**
//     * 图片
//     *
//     * @param cid        频道id
//     * @param uid        用户id
//     * @param actionType 0:首次请求; 1:上拉刷新; 2:下拉刷新
//     * @param sort       排序值，记录上拉请求，用户看到的段子编号
//     * @return
//     */
//    public List<Joke> getImageList(int cid, String uid, int actionType, int sort) {
//        String key = JedisKey.SORTEDSET_COMMON_CHANNEL + cid;
//        if (actionType == 1) {
//            //上拉，获取老数据
//            return getOldJokeCacheList(key, sort, uid, 2);
//        } else {
//            //下拉，获取新数据
//            return getNewJokeCacheList(key, uid, 2);
//
//        }
//    }
//
//    /**
//     * 获取专题
//     *
//     * @param uid        用户id
//     * @param actionType 0:首次请求; 1:上拉刷新; 2:下拉刷新
//     * @param sort       排序值，记录上拉请求，用户看到的专题编号
//     * @return
//     */
//    public List<Topic> getTopicList(String uid, int actionType, int sort) {
//        String key = JedisKey.SORTEDSET_TOPIC_LIST;
//        if (actionType == 1) {
//            return getOldTopicCacheList(key, sort, uid, 3);
//        } else {
//            return getNewTopicCacheList(key, uid, 3);
//        }
//    }
//
//    /**
//     * 专题详情
//     *
//     * @return
//     */
//    public List<Joke> getTopicDetailList(int tid) {
//        String key = JedisKey.SORTEDSET_TOPIC_JOKE_SET + tid;
//        Set<String> jokeIdSet = jedisCache.zrevrange(key, 0L, -1L);
//        if (CollectionUtils.isEmpty(jokeIdSet)) {
//            return null;
//        }
//        List<Joke> list = Lists.newArrayList();
//        Joke joke;
//        for (String jokeId : jokeIdSet) {
//            joke = JSON.parseObject(jedisCache.get(JedisKey.STRING_JOKE + jokeId), Joke.class);
//            if (joke != null) {
//                if (joke.getType() == Constants.JOKE_TYPE_IMG) {
//                    joke.setImg(IMG_REAL_SERVER_URL + joke.getImg());
//                } else if (joke.getType() == Constants.JOKE_TYPE_GIF) {
//                    joke.setImg(png2jpg(IMG_REAL_SERVER_URL + joke.getImg()));
//                    joke.setGif(IMG_REAL_SERVER_URL + joke.getGif());
//                }
//                list.add(joke);
//                //专题分享
//                joke.setShareUrl(SHAREURLPREFIX+"/cid/22/tid/"+tid);
//            }
//        }
//        return list;
//    }

    /**
     * 获取推荐
     *
     * @param uid        用户id
     * @param actionType 0:首次请求; 1:上拉刷新; 2:下拉刷新
     * @param sort       排序值，记录上拉请求，用户看到的段子编号
     * @return
     */
    public List<Joke> getRecommendList(String uid, int actionType, int sort) {
//        String key = JedisKey.SORTEDSET_RECOMMEND_CHANNEL;
        if (actionType == 1) {
            return getOldJokeCacheList(CHANNEL_RECOMMEND, sort, uid);
        } else { //
            return getNewJokeCacheList(CHANNEL_RECOMMEND, uid);
        }
    }

    /**
     * 踩
     *
     * @param id joke id
     */
    public void addClick(int id) {
        addStepIds.add(String.valueOf(id));
    }


    //TODO 请求旧数据，保存排序值

    /**
     * 赞
     *
     * @param id joke id
     */
    public void addLike(int id) {
        addLikeIds.add(String.valueOf(id));
    }

    /**
     * 下拉操作：获取缓存最新数据
     *
     * @param key       缓存key
     * @param uid       用户id
     * @return
     */
    private List<Joke> getNewJokeCacheList(String key, String uid) {
        List<Joke> list = Lists.newArrayList();
        //请求最新的500条数据
        Set<String> jokeIdSet = jedisCache.zrevrange(key, 0L, 499L);
        if (CollectionUtils.isEmpty(jokeIdSet)) {
            logger.warn("欧12端化接口获取新数据请求数据为空:[{}], uid:[{}], 0-499", key, uid);
            return null;
        }
        Set<String> noRepeatedList = new HashSet<>();
        //用户浏览记录缓存key
        String historyVisit = HISTORY_PREFIX + uid;
        Set<String> viewedJokeIdSet = jedisCache.zrange(historyVisit, 0, -1);
        if (!CollectionUtils.isEmpty(viewedJokeIdSet)) {
            //去重处理
            int flag = 0;
            for (String jokeId : jokeIdSet) {
                if (!viewedJokeIdSet.contains(jokeId)) {
                    //获取10条
                    if (flag > 9)
                        break;
                    noRepeatedList.add(jokeId);
                    flag++;
                }
            }
        } else {
            //获取10条
            int flag = 0;
            for (String jokeId : jokeIdSet) {
                if (flag > 9)
                    break;
                noRepeatedList.add(jokeId);
                flag++;
            }
            //缓存这10条
        }
        //记录用户浏览记录
        if (!CollectionUtils.isEmpty(noRepeatedList)) {
            Map<String, Double> viewedMap = new HashMap<>();
            for (String jokeId : noRepeatedList) {
                viewedMap.put(jokeId, (double) System.currentTimeMillis());
            }
            jedisCache.zadd(historyVisit, viewedMap);
            //检查用户浏览记录数目
            checkViewedJokeNum(historyVisit);
            String[] idArray = noRepeatedList.toArray(new String[noRepeatedList.size()]);
            for(int i = 0; i < idArray.length; i++){
                idArray[i] = JedisKey.STRING_JOKE + idArray[i];
            }
            List<String> jokeList = jedisCache.mget(idArray);
            if(CollectionUtils.isEmpty(jokeList)){
                logger.warn("欧12端化接口请求新段子内容为空:[{}]", JSON.toJSONString(noRepeatedList));
                return null;
            }
            for (String jokeStr : jokeList) {
                Joke joke = JSON.parseObject(jokeStr, Joke.class);
                if (joke != null) {
                    processImg(joke);
                    //设置分享url
                    joke.setShareUrl(SHARE_URL_PREFIX + "/detail/" + joke.getId() + "/cid/3");
                    processText(joke);
                    list.add(joke);
                }
            }
        } else {
            logger.info("下拉：没有请求到新数据");
        }
        return list;
    }

    /**
     * 上拉操作：获取缓存数据
     * joke :加上sort 值
     *
     * @param key  缓存key
     * @param sort 请求编号
     * @return
     */
    private List<Joke> getOldJokeCacheList(String key, int sort, String uid) {
        //用户排序值key  TODO userSoreCacheKey 可优化存储到一个hash中
        String uidPosKey = POS_PREFIX + uid;
        List<Joke> list = Lists.newArrayList();
        //缓存记录总数
        Integer pos;
        if (0 == sort) {
            pos = getUidPos(uidPosKey); //读取缓存排序值
            if (pos < 9) {
                long totalNum = jedisCache.zcard(key);
                if (totalNum > 509) {
                    sort = (int) totalNum - 501;
                } else {
                    logger.warn("欧12端化接口上拉请求旧数据为空:[{}-{}-{}]", key, sort, uid);
                    return list;
                }
            } else {
                sort = pos;
            }
        }
        addUidPos(uidPosKey, sort);
        if (sort > 8) {
            // 请求sort前面的10条数据
            Set<String> jokeIdSet = jedisCache.zrevrange(key, (long) (sort - 9), (long) sort);
            if (CollectionUtils.isEmpty(jokeIdSet)) {
                logger.warn("欧12端化接口上拉请求段子ID为空:[{}] start:[{}], end:[{}]，没有请求到旧数据!", key, sort - 9, sort);
                return null;
            }
            String[] idArray = jokeIdSet.toArray(new String[jokeIdSet.size()]);
            for(int i = 0; i < idArray.length; i++){
                idArray[i] = JedisKey.STRING_JOKE + idArray[i];
            }
            Joke joke;
            List<String> jokeList = jedisCache.mget(idArray);
            if(CollectionUtils.isEmpty(jokeList)){
                logger.warn("欧12端化接口请求段子内容为空:[{}]", JSON.toJSONString(jokeIdSet));
                return null;
            }
            for (String jokeStr : jokeList) {
                joke = JSON.parseObject(jokeStr, Joke.class);
                if (joke != null) {
                    processImg(joke);
                    //设置排序值，便于下次请求，请求旧数据
                    joke.setSort(sort--);
                    //设置分享url
                    joke.setShareUrl(SHARE_URL_PREFIX + "/detail/" + joke.getId() + "/cid/3");
//                    setShareUrl(joke);
                    //文字joke的title
                    processText(joke);
                    list.add(joke);
                }
            }
        } else {
            logger.info("上拉请求，数据不足10条");
        }
        return list;
    }

    private void processImg(Joke joke) {
        if (joke.getType() == Constants.JOKE_TYPE_IMG) {
            joke.setImg(IMG_PREFIX + joke.getImg());
        } else if (joke.getType() == Constants.JOKE_TYPE_GIF) {
            joke.setImg(png2jpg(IMG_PREFIX + joke.getImg()));
            joke.setGif(IMG_PREFIX + joke.getGif());
        }
    }


//    /**
//     * 下拉操作：获取缓存最新数据
//     *
//     * @param key       缓存key
//     * @param uid       用户id
//     * @param cacheType 请求缓存类型，生成记录用户浏览记录key，去重处理,
//     * @return
//     */
//    private List<Topic> getNewTopicCacheList(String key, String uid, Integer cacheType) {
//        //请求最新的500条数据
//        Set<String> topicIdSet = jedisCache.zrevrange(key, 0L, 0L);
//
//        if (CollectionUtils.isEmpty(topicIdSet)) {
//            return null;
//        }
//        //去重后的joke
//        Set<String> noRepeatedList = new HashSet<>();
//        //用户浏览记录缓存key
//        String userViewRecordKey = getUserViewedCacheKey(uid, cacheType);
//        Set<String> viewedJokeIdSet = jedisCache.zrange(userViewRecordKey, 0, -1);
//        if (!CollectionUtils.isEmpty(viewedJokeIdSet)) {
//            //去重处理
//            int flag = 0;
//            for (String topicId : topicIdSet) {
//                //未浏览过
//                if (!viewedJokeIdSet.contains(topicId)) {
//                    //获取10条
//                    if (flag > 0)
//                        break;
//                    noRepeatedList.add(topicId);
//                    flag++;
//                }
//            }
//        } else {
//            int flag = 0;
//            for (String topicId : topicIdSet) {
//                //获取10条
//                if (flag > 0)
//                    break;
//                noRepeatedList.add(topicId);
//                flag++;
//            }
//        }
//
//        //记录用户浏览记录
//        Map<String, Double> viewedMap = new HashMap<>();
//        for (String jokeId : noRepeatedList) {
//            viewedMap.put(jokeId, (double) System.currentTimeMillis());
//        }
//        if (!CollectionUtils.isEmpty(viewedMap)) {
//            jedisCache.zadd(userViewRecordKey, viewedMap);
//        }
//        //检查用户浏览记录数目，保持500以内
//        checkViewedJokeNum(userViewRecordKey);
//        List<Topic> list = Lists.newArrayList();
//        Topic topic;
////        System.out.println(CollectionUtils.isEmpty(noRepeatedList));
//        if (!CollectionUtils.isEmpty(noRepeatedList)) {
//            for (String topicId : noRepeatedList) {
//                topic = JSON.parseObject(jedisCache.get(JedisKey.STRING_TOPIC + topicId), Topic.class);
//                if (topic != null) {
//                    topic.setType(Constants.JOKE_TYPE_TOPIC_LIST);
//                    topic.setImg(IMG_REAL_SERVER_URL + topic.getImg());
//                    list.add(topic);
//                }
//            }
//        } else {
//            logger.info("专题下拉：没有请求到新数据");
//        }
//        return list;
//    }
//
//    /**
//     * 上拉操作：获取缓存数据
//     * Topic :加上sort 值
//     *
//     * @param key  缓存key
//     * @param sort 请求编号
//     * @return
//     */
//    private List<Topic> getOldTopicCacheList(String key, int sort, String uid, int cacheType) {
//        String userSoreCacheKey = getUserScoreCacheKey(uid, cacheType);
//        List<Topic> list = Lists.newArrayList();
//        //专题列表总数目
//        long totalNum = jedisCache.zcard(key);
//        //缓存排序值
//        Integer cacheSort;
//        //sort为零 读取缓存排序值
//        if (0 == sort) {//处理未传递sort值
//            //读取缓存排序值
//            cacheSort = getCacheScore(userSoreCacheKey);
//            if (cacheSort < 9) {
//                //循环
//                if (totalNum > 509) {
//                    sort = (int) totalNum - 501;
//                } else {
//                    logger.info("专题上拉请求，没有请求到旧数据");
//                    return list;
//                }
//            } else {
//                sort = cacheSort;
//            }
//        }
//        //记录排序值到缓存
//        addCacheSort(userSoreCacheKey, sort);
//        if (sort > 8) {
//            Set<String> topicIdSet = jedisCache.zrevrange(key, (long) (sort - 9), (long) sort);
//            if (CollectionUtils.isEmpty(topicIdSet)) {
//                return null;
//            }
//            Topic topic;
//            for (String topicId : topicIdSet) {
//                topic = JSON.parseObject(jedisCache.get(JedisKey.STRING_TOPIC + topicId), Topic.class);
//                if (topic != null) {
//                    topic.setType(Constants.JOKE_TYPE_TOPIC_LIST);
//                    topic.setImg(IMG_REAL_SERVER_URL + topic.getImg());
//                    //设置请求排序值
//                    topic.setType(sort--);
//                    list.add(topic);
//                }
//            }
//        } else {
//            logger.info("专题：上拉，请求到数据不足十条");
//        }
//
//        return list;
//    }

    /**
     * 获取用户浏览记录缓存key
     *
     * @param cacheType 0 文字，1 动图，2 图片 3 专题， 4 推荐
     * @return
     */
//    private String getUserViewedCacheKey(String uid, Integer cacheType) {
//        String key;
//        if (0 == cacheType) {
//            key = "text_" + uid;
//        } else if (1 == cacheType) {
//            key = "gif_" + uid;
//        } else if (2 == cacheType) {
//            key = "image_" + uid;
//        } else if (3 == cacheType) {
//            key = "topic_" + uid;
//        } else {
//            key = "recommend_" + uid;
//        }
//        return key;
//    }

    private void processText(Joke joke) {
        if (joke.getType() == Constants.JOKE_TYPE_TEXT) {
            if (StringUtils.isEmpty(joke.getTitle()) || joke.getTitle().trim().length() < 2) {
                if (StringUtils.isNotEmpty(joke.getContent())) {
                    int length = joke.getContent().length();
                    int end = length > 25 ? 25 : (length / 2);
                    String title = joke.getContent().substring(0, end);
                    title = title + "...";
                    joke.setTitle(title);
                }
            }
        }
    }

    /**
     * 定时将赞列表中数据存储到缓存中
     */
    @Scheduled(fixedRate = 1000 * 60 * 1, initialDelay = 5000)
    public void addLikeQueue() {
        try {
            if (!CollectionUtils.isEmpty(addLikeIds)) {
                jedisCache.lpush(JedisKey.JOKE_LIST_LIKE, addLikeIds.toArray(new String[]{}));
                logger.info("增加点赞数记录{}条", addLikeIds.size());
                addLikeIds.clear();
            }
        } catch (Exception e) {
            logger.error("增加点赞数失败", e);
        }
    }

    /**
     * 定时将踩列表中数据存储到缓存中
     */
    @Scheduled(fixedRate = 1000 * 60 * 1, initialDelay = 8000)
    public void addStepQueue() {
        try {
            if (!CollectionUtils.isEmpty(addStepIds)) {
                jedisCache.lpush(JedisKey.JOKE_LIST_STEP, addStepIds.toArray(new String[]{}));
                logger.info("增加踩数记录{}条", addStepIds.size());
                addStepIds.clear();
            }
        } catch (Exception e) {
            logger.error("增加踩数失败", e);
        }
    }

//    /**
//     * 是否下拉过，判断频道入口还是列表入口
//     *
//     * @param uid         用户id
//     * @return true ：请求过 false ：为请求过
//     */
//    public boolean haveDropedDown(String uid) {
//        String key = HISTORY_PREFIX + uid;
//        Long count = jedisCache.zcard(key);
//        if (count > 0) {
//            return true;
//        } else {
//            return false;
//        }
//    }

    /**
     * 检查用户浏览记录
     * 保存500条以内
     *
     * @param key
     */
    private void checkViewedJokeNum(String key) {
        long total = jedisCache.zcard(key);
        if (total > 500) {
            jedisCache.zremrangebyrank(key, 0L, 9L);
        }
    }

    /**
     * 获取缓存中的排序值
     *
     * @param key
     * @return
     */
    private int getUidPos(String key) {
        String pos = jedisCache.hget(JedisKey.UID_POS, key);
        if (StringUtils.isNumeric(pos)) {
            return Integer.parseInt(pos);
        }
        return 0;
    }

//    /**
//     * 获取用户排序值缓存key
//     *
//     * @param uid
//     * @param cacheType
//     * @return
//     */
//    private String getUidPosKey(String uid, Integer cacheType) {
//        String key;
//        if (0 == cacheType) {
//            key = "score_text_" + uid;
//        } else if (1 == cacheType) {
//            key = "score_gif_" + uid;
//        } else if (2 == cacheType) {
//            key = "score_image_" + uid;
//        } else if (3 == cacheType) {
//            key = "score_topic_" + uid;
//        } else {
//            key = "score_recommend_" + uid;
//        }
//        return key;
//    }

//    /**
//     * 设置分享url
//     * @param joke
//     * @param cacheType
//     */
//    public void setShareUrl(Joke joke,Integer cacheType){
//        if(0 == cacheType) {
//            //文字分享
//            joke.setShareUrl(SHAREURLPREFIX + "/detail/" + joke.getId() + "/cid/" + 14 + "/tid/-1");
//        }else if(1 == cacheType){
//            //动图分享
//            joke.setShareUrl(SHAREURLPREFIX + "/detail/" + joke.getId() + "/cid/" + 16 + "/tid/-1");
//        }else if(2 == cacheType){
//            //静图分享
//            joke.setShareUrl(SHAREURLPREFIX + "/detail/" + joke.getId() + "/cid/" + 18 + "/tid/-1");
//        }else{
//            //推荐分享
//            joke.setShareUrl(SHAREURLPREFIX + "/detail/" + joke.getId() + "/cid/" + 20 + "/tid/-1");
//        }
//    }

    /**
     * 记录缓存排序值
     */
    private void addUidPos(String key, Integer pos) {
        //记录用户下次浏览位置
        if (pos > 10) {
            pos = pos - 10;
            jedisCache.hset(JedisKey.UID_POS, key, pos.toString());
        } else {
            jedisCache.hset(JedisKey.UID_POS, key, START_POS);
        }
    }

    /**
     * 欧朋浏览器开心一刻接口
     * ezine
     * @return
     */
    public List<EzineJoke> ezineJoke(){
        List<EzineJoke> ezineJokeList = new ArrayList<>();
//        Set<String> ezineJokeSet = jedisCache.zrevrange(JedisKey.EZINE_JOKE, (long) 0, (long) 19);
        Set<String> ezineJokeSet = jedisCache.zrevrange(CHANNEL_JOKE, (long) 0, (long) 19);
        if(CollectionUtils.isEmpty(ezineJokeSet)){
            logger.warn("开心一刻接口请求段子ID为空:[{}]", CHANNEL_JOKE);
            return null;
        }
        String[] idArray = ezineJokeSet.toArray(new String[ezineJokeList.size()]);
        for(int i = 0; i < idArray.length; i++){
            idArray[i] = JedisKey.STRING_JOKE + idArray[i];
        }
        List<String> jokeList = jedisCache.mget(idArray);
        if(CollectionUtils.isEmpty(jokeList)){
            logger.warn("开心一刻接口请求段子内容为空:[{}]", CHANNEL_JOKE);
            return null;
        }
        for (String jokeStr : jokeList){
            Joke joke = JSON.parseObject(jokeStr, Joke.class);
            if(null != joke){
                EzineJoke ezineJoke = new EzineJoke();
                String content = joke.getContent();
                if(StringUtils.isNotEmpty(content)&&content.length() > 120){
                    content = content.substring(0,120)+"...";
                }
                ezineJoke.setContext(content);
                ezineJoke.setObjectId(joke.getId());
                ezineJoke.setUrl(SHARE_URL_PREFIX + "/detail/" + joke.getId() + "/cid/2");
                ezineJokeList.add(ezineJoke);
            }
        }
        return ezineJokeList;
    }
}
