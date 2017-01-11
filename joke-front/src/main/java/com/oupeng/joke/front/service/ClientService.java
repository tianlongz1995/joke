package com.oupeng.joke.front.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;
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
    private static final String SHAREURLPREFIX = "http://joke.oupeng.com/#!";
    private static String IMG_REAL_SERVER_URL = null;
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

    @PostConstruct
    public void initConstants() {
        IMG_REAL_SERVER_URL = env.getProperty("img.real.server.url");
    }

    /**
     * 推荐
     *
     * @param uid        用户id
     * @param actionType 0:首次请求; 1:上拉刷新; 2:下拉刷新
     * @param sort       排序值，记录上拉请求，用户看到的段子编号
     * @return
     */
    public List<Joke> getRecommendList(String uid, int actionType, int sort) {
        String key = JedisKey.SORTEDSET_RECOMMEND_CHANNEL;
        if (actionType == 1) {
            return getOldJokeCacheList(key, sort, uid, 4);
        } else { //
            return getNewJokeCacheList(key, uid, 4);

        }
    }

    /**
     * 文字
     *
     * @param cid        频道id
     * @param uid        用户id
     * @param actionType 0:首次请求; 1:上拉刷新; 2:下拉刷新
     * @param sort       排序值，记录上拉请求，用户看到的段子编号
     * @return
     */
    public List<Joke> getTextList(int cid, String uid, int actionType, int sort) {
        String key = JedisKey.SORTEDSET_COMMON_CHANNEL + cid;
        if (actionType == 1) {
            return getOldJokeCacheList(key, sort, uid, 0);
        } else {
            return getNewJokeCacheList(key, uid, 0);
        }
    }

    /**
     * 动图
     *
     * @param cid        频道id
     * @param uid        用户id
     * @param actionType 0:首次请求; 1:上拉刷新; 2:下拉刷新
     * @param sort       排序值，记录上拉请求，用户看到的段子编号
     * @return
     */
    public List<Joke> getGiftList(int cid, String uid, int actionType, int sort) {
        String key = JedisKey.SORTEDSET_COMMON_CHANNEL + cid;
        if (actionType == 1) {
            return getOldJokeCacheList(key, sort, uid, 1);
        } else {
            return getNewJokeCacheList(key, uid, 1);
        }
    }

    /**
     * 图片
     *
     * @param cid        频道id
     * @param uid        用户id
     * @param actionType 0:首次请求; 1:上拉刷新; 2:下拉刷新
     * @param sort       排序值，记录上拉请求，用户看到的段子编号
     * @return
     */
    public List<Joke> getImageList(int cid, String uid, int actionType, int sort) {
        String key = JedisKey.SORTEDSET_COMMON_CHANNEL + cid;
        if (actionType == 1) {
            //上拉，获取老数据
            return getOldJokeCacheList(key, sort, uid, 2);
        } else {
            //下拉，获取新数据
            return getNewJokeCacheList(key, uid, 2);

        }
    }

    /**
     * 获取专题
     *
     * @param uid        用户id
     * @param actionType 0:首次请求; 1:上拉刷新; 2:下拉刷新
     * @param sort       排序值，记录上拉请求，用户看到的专题编号
     * @return
     */
    public List<Topic> getTopicList(String uid, int actionType, int sort) {
        String key = JedisKey.SORTEDSET_TOPIC_LIST;
        if (actionType == 1) {
            return getOldTopicCacheList(key, sort, uid, 3);
        } else {
            return getNewTopicCacheList(key, uid, 3);
        }
    }

    /**
     * 专题详情
     *
     * @return
     */
    public List<Joke> getTopicDetailList(int tid) {
        String key = JedisKey.SORTEDSET_TOPIC_JOKE_SET + tid;
        Set<String> jokeIdSet = jedisCache.zrevrange(key, 0L, -1L);
        if (CollectionUtils.isEmpty(jokeIdSet)) {
            return null;
        }
        List<Joke> list = Lists.newArrayList();
        Joke joke;
        for (String jokeId : jokeIdSet) {
            joke = JSON.parseObject(jedisCache.get(JedisKey.STRING_JOKE + jokeId), Joke.class);
            if (joke != null) {
                if (joke.getType() == Constants.JOKE_TYPE_IMG) {
                    joke.setImg(IMG_REAL_SERVER_URL + joke.getImg());
                } else if (joke.getType() == Constants.JOKE_TYPE_GIF) {
                    joke.setImg(png2jpg(IMG_REAL_SERVER_URL + joke.getImg()));
                    joke.setGif(IMG_REAL_SERVER_URL + joke.getGif());
                }
                list.add(joke);
                //专题分享
                joke.setShareUrl(SHAREURLPREFIX+"/cid/22/tid/"+tid);
            }
        }
        return list;
    }

    /**
     * 踩
     *
     * @param id joke id
     */
    public void addClick(int id) {
        addStepIds.add(String.valueOf(id));
    }

    /**
     * 赞
     *
     * @param id joke id
     */
    public void addLike(int id) {
        addLikeIds.add(String.valueOf(id));
    }


    //TODO 请求旧数据，保存排序值

    /**
     * 下拉操作：获取缓存最新数据
     *
     * @param key       缓存key
     * @param uid       用户id
     * @param cacheType 请求缓存类型，生成记录用户浏览记录key，去重处理,
     * @return
     */
    private List<Joke> getNewJokeCacheList(String key, String uid, Integer cacheType) {
        List<Joke> list = Lists.newArrayList();
        //请求最新的500条数据
        Set<String> jokeIdSet = jedisCache.zrevrange(key, 0L, 499L);
        if (CollectionUtils.isEmpty(jokeIdSet)) {
            logger.info("没有请求到数据");
            return null;
        }
        Set<String> noRepeatedList = new HashSet<>();
        //用户浏览记录缓存key
        String userViewRecordKey = getUserViewedCacheKey(uid, cacheType);
        Set<String> viewedJokeIdSet = jedisCache.zrange(userViewRecordKey, 0, -1);
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
            jedisCache.zadd(userViewRecordKey, viewedMap);
            //检查用户浏览记录数目
            checkViewedJokeNum(userViewRecordKey);
            Joke joke;
            for (String jokeId : noRepeatedList) {
                joke = JSON.parseObject(jedisCache.get(JedisKey.STRING_JOKE + jokeId), Joke.class);
                if (joke != null) {
                    if (joke.getType() == Constants.JOKE_TYPE_IMG) {
                        joke.setImg(IMG_REAL_SERVER_URL + joke.getImg());
                    } else if (joke.getType() == Constants.JOKE_TYPE_GIF) {
                        joke.setImg(png2jpg(IMG_REAL_SERVER_URL + joke.getImg()));
                        joke.setGif(IMG_REAL_SERVER_URL + joke.getGif());
                    }
                    //设置分享url
                    setShareUrl(joke,cacheType);
                    //文字joke的title
                    if (cacheType == 0) {
                        if (StringUtils.isEmpty(joke.getTitle().trim())) {
                            if (StringUtils.isNotEmpty(joke.getContent())) {
                                int length = joke.getContent().length();
                                int end = length > 25 ? 25 : (length / 2);
                                String title = joke.getContent().substring(0, end);
                                title = title + "...";
                                joke.setTitle(title);
                            }
                        }
                    }
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
    private List<Joke> getOldJokeCacheList(String key, int sort, String uid, int cacheType) {
        //用户排序值key
        String userSoreCacheKey = getUserScoreCacheKey(uid, cacheType);
        List<Joke> list = Lists.newArrayList();
        //缓存记录总数
        long totalNum = jedisCache.zcard(key);
        Integer cacheSort;
        //sort为零 读取缓存排序值
        if (0 == sort) {
            //处理未传递sort值
            //读取缓存排序值
            cacheSort = getCacheScore(userSoreCacheKey);
            if (cacheSort < 9) {
                if (totalNum > 509) {
                    sort = (int) totalNum - 501;
                } else {
                    logger.info("上拉请求，没有请求到旧数据");
                    return list;
                }
            } else {
                sort = cacheSort;
            }
        }
        addCacheSort(userSoreCacheKey, sort);
        //足够请求到10条数据
        if (sort > 8) {
            //请求sort前面的10条数据
            Set<String> jokeIdSet = jedisCache.zrevrange(key, (long) (sort - 9), (long) sort);
            if (CollectionUtils.isEmpty(jokeIdSet)) {
                return null;
            }
            Joke joke;
            for (String jokeId : jokeIdSet) {
                joke = JSON.parseObject(jedisCache.get(JedisKey.STRING_JOKE + jokeId), Joke.class);
                if (joke != null) {
                    if (joke.getType() == Constants.JOKE_TYPE_IMG) {
                        joke.setImg(IMG_REAL_SERVER_URL + joke.getImg());
                    } else if (joke.getType() == Constants.JOKE_TYPE_GIF) {
                        joke.setImg(png2jpg(IMG_REAL_SERVER_URL + joke.getImg()));
                        joke.setGif(IMG_REAL_SERVER_URL + joke.getGif());
                    }
                    //设置排序值，便于下次请求，请求旧数据
                    joke.setSort(sort--);
                    //设置分享url
                    setShareUrl(joke,cacheType);
                    //文字joke的title
                    if (cacheType == 0) {
                        if (StringUtils.isEmpty(joke.getTitle().trim())) {
                            if (StringUtils.isNotEmpty(joke.getContent())) {
                                int length = joke.getContent().length();
                                int end = length > 25 ? 25 : (length / 2);
                                String title = joke.getContent().substring(0, end);
                                title = title + "...";
                                joke.setTitle(title);
                            }
                        }
                    }
                    list.add(joke);
                }
            }
        } else {
            logger.info("上拉请求，数据不足10条");
        }
        return list;
    }

    /**
     * 下拉操作：获取缓存最新数据
     *
     * @param key       缓存key
     * @param uid       用户id
     * @param cacheType 请求缓存类型，生成记录用户浏览记录key，去重处理,
     * @return
     */
    private List<Topic> getNewTopicCacheList(String key, String uid, Integer cacheType) {
        //请求最新的500条数据
        Set<String> topicIdSet = jedisCache.zrevrange(key, 0L, 0L);

        if (CollectionUtils.isEmpty(topicIdSet)) {
            return null;
        }
        //去重后的joke
        Set<String> noRepeatedList = new HashSet<>();
        //用户浏览记录缓存key
        String userViewRecordKey = getUserViewedCacheKey(uid, cacheType);
        Set<String> viewedJokeIdSet = jedisCache.zrange(userViewRecordKey, 0, -1);
        if (!CollectionUtils.isEmpty(viewedJokeIdSet)) {
            //去重处理
            int flag = 0;
            for (String topicId : topicIdSet) {
                //未浏览过
                if (!viewedJokeIdSet.contains(topicId)) {
                    //获取10条
                    if (flag > 0)
                        break;
                    noRepeatedList.add(topicId);
                    flag++;
                }
            }
        } else {
            int flag = 0;
            for (String topicId : topicIdSet) {
                //获取10条
                if (flag > 0)
                    break;
                noRepeatedList.add(topicId);
                flag++;
            }
        }

        //记录用户浏览记录
        Map<String, Double> viewedMap = new HashMap<>();
        for (String jokeId : noRepeatedList) {
            viewedMap.put(jokeId, (double) System.currentTimeMillis());
        }
        if (!CollectionUtils.isEmpty(viewedMap)) {
            jedisCache.zadd(userViewRecordKey, viewedMap);
        }
        //检查用户浏览记录数目，保持500以内
        checkViewedJokeNum(userViewRecordKey);
        List<Topic> list = Lists.newArrayList();
        Topic topic;
//        System.out.println(CollectionUtils.isEmpty(noRepeatedList));
        if (!CollectionUtils.isEmpty(noRepeatedList)) {
            for (String topicId : noRepeatedList) {
                topic = JSON.parseObject(jedisCache.get(JedisKey.STRING_TOPIC + topicId), Topic.class);
                if (topic != null) {
                    topic.setType(Constants.JOKE_TYPE_TOPIC_LIST);
                    topic.setImg(IMG_REAL_SERVER_URL + topic.getImg());
                    list.add(topic);
                }
            }
        } else {
            logger.info("专题下拉：没有请求到新数据");
        }
        return list;
    }

    /**
     * 上拉操作：获取缓存数据
     * Topic :加上sort 值
     *
     * @param key  缓存key
     * @param sort 请求编号
     * @return
     */
    private List<Topic> getOldTopicCacheList(String key, int sort, String uid, int cacheType) {
        String userSoreCacheKey = getUserScoreCacheKey(uid, cacheType);
        List<Topic> list = Lists.newArrayList();
        //专题列表总数目
        long totalNum = jedisCache.zcard(key);
        //缓存排序值
        Integer cacheSort;
        //sort为零 读取缓存排序值
        if (0 == sort) {//处理未传递sort值
            //读取缓存排序值
            cacheSort = getCacheScore(userSoreCacheKey);
            if (cacheSort < 9) {
                //循环
                if (totalNum > 509) {
                    sort = (int) totalNum - 501;
                } else {
                    logger.info("专题上拉请求，没有请求到旧数据");
                    return list;
                }
            } else {
                sort = cacheSort;
            }
        }
        //记录排序值到缓存
        addCacheSort(userSoreCacheKey, sort);
        if (sort > 8) {
            Set<String> topicIdSet = jedisCache.zrevrange(key, (long) (sort - 9), (long) sort);
            if (CollectionUtils.isEmpty(topicIdSet)) {
                return null;
            }
            Topic topic;
            for (String topicId : topicIdSet) {
                topic = JSON.parseObject(jedisCache.get(JedisKey.STRING_TOPIC + topicId), Topic.class);
                if (topic != null) {
                    topic.setType(Constants.JOKE_TYPE_TOPIC_LIST);
                    topic.setImg(IMG_REAL_SERVER_URL + topic.getImg());
                    //设置请求排序值
                    topic.setType(sort--);
                    list.add(topic);
                }
            }
        } else {
            logger.info("专题：上拉，请求到数据不足十条");
        }

        return list;
    }

    /**
     * 获取用户浏览记录缓存key
     *
     * @param cacheType 0 文字，1 动图，2 图片 3 专题， 4 推荐
     * @return
     */
    private String getUserViewedCacheKey(String uid, Integer cacheType) {
        String key;
        if (0 == cacheType) {
            key = "text_" + uid;
        } else if (1 == cacheType) {
            key = "gif_" + uid;
        } else if (2 == cacheType) {
            key = "image_" + uid;
        } else if (3 == cacheType) {
            key = "topic_" + uid;
        } else {
            key = "recommend_" + uid;
        }
        return key;
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
     * 是否下拉过，判断频道入口还是列表入口
     *
     * @param uid         用户id
     * @param requestType 请求类型,0 文字 1 动图 2 图片 3 专题 4 推荐
     * @return true ：请求过 false ：为请求过
     */
    public boolean haveDropedDown(String uid, int requestType) {
        String key = getUserViewedCacheKey(uid, requestType);
        Long count = jedisCache.zcard(key);
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取缓存中的排序值
     *
     * @param key
     * @return
     */
    private int getCacheScore(String key) {
        String cacheSort = jedisCache.get(key);
        if (StringUtils.isNumeric(cacheSort)) {
            return Integer.parseInt(cacheSort);
        }
        return 0;
    }

    /**
     * 记录缓存排序值
     */
    private void addCacheSort(String key, Integer cacheSort) {
        //记录用户下次浏览位置
        if (cacheSort > 10) {
            cacheSort = cacheSort - 10;
            jedisCache.set(key, cacheSort.toString());
        } else {
            cacheSort = 0;
            jedisCache.set(key, cacheSort.toString());
        }
    }

    /**
     * 获取用户排序值缓存key
     *
     * @param uid
     * @param cacheType
     * @return
     */
    private String getUserScoreCacheKey(String uid, Integer cacheType) {
        String key;
        if (0 == cacheType) {
            key = "score_text_" + uid;
        } else if (1 == cacheType) {
            key = "score_gif_" + uid;
        } else if (2 == cacheType) {
            key = "score_image_" + uid;
        } else if (3 == cacheType) {
            key = "score_topic_" + uid;
        } else {
            key = "score_recommend_" + uid;
        }
        return key;
    }

    /**
     * 设置分享url
     * @param joke
     * @param cacheType
     */
    public void setShareUrl(Joke joke,Integer cacheType){
        if(0 == cacheType) {
            //文字分享
            joke.setShareUrl(SHAREURLPREFIX + "/detail/" + joke.getId() + "/cid/" + 14 + "/tid/-1");
        }else if(1 == cacheType){
            //动图分享
            joke.setShareUrl(SHAREURLPREFIX + "/detail/" + joke.getId() + "/cid/" + 16 + "/tid/-1");
        }else if(2 == cacheType){
            //静图分享
            joke.setShareUrl(SHAREURLPREFIX + "/detail/" + joke.getId() + "/cid/" + 18 + "/tid/-1");
        }else{
            //推荐分享
            joke.setShareUrl(SHAREURLPREFIX + "/detail/" + joke.getId() + "/cid/" + 20 + "/tid/-1");
        }
    }
}
