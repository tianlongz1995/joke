package com.oupeng.joke.front.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;
import com.oupeng.joke.domain.*;
import com.oupeng.joke.front.util.Constants;
import com.oupeng.joke.front.util.FormatUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 首页服务接口
 */
@Service
public class IndexService {
    /**
     * 日志
     */
    private static final Logger log = LoggerFactory.getLogger(IndexService.class);
    /**
     * 默认did
     */
    private static final String DEFAULT_DID = "1";
    /**
     * 图片前缀
     */
    private static String IMG_PREFIX = "http://joke2-img.oupeng.com/";
    @Autowired
    private CommentService commentService;
    @Autowired
    private Environment env;
    @Autowired
    private JedisCache jedisCache;
    /**
     * 配置集合
     */
    private ConcurrentHashMap<String, String> configMap = new ConcurrentHashMap<>();
    /**
     * 资源集合
     */
    private ConcurrentHashMap<String, IndexResource> resourceMap = new ConcurrentHashMap<>();
//    @Autowired
//    private CacheManager cacheManager;

    private IndexResource backResource;


    @PostConstruct
    public void initConstants() {
//        获取CDN前缀
        String img = env.getProperty("img.real.server.url");
        if (StringUtils.isNotEmpty(img)) {
            IMG_PREFIX = img;
        }
//        获取首页资源文件配置缓存
        IndexResource indexResource = JSON.parseObject(jedisCache.get(JedisKey.JOKE_RESOURCE_CONFIG_INDEX), IndexResource.class);
        if (indexResource != null) {
            resourceMap.put(JedisKey.INDEX_CACHE_INDEX, indexResource);
        }
//        获取备用资源文件配置
        String backResourceStr = env.getProperty("back.resource");
        if (StringUtils.isNotBlank(backResourceStr)) {
            backResource = JSON.parseObject(backResourceStr, IndexResource.class);
            if (backResource == null) {
                log.error("渠道备用配置为空:{}", backResourceStr);
            } else {
                resourceMap.put(JedisKey.INDEX_CACHE_BACK, backResource);
            }
        }
//        获取测试资源文件配置
        IndexResource testResource = JSON.parseObject(jedisCache.get(JedisKey.JOKE_RESOURCE_CONFIG_TEST), IndexResource.class);
        if (testResource != null) {
            resourceMap.put(JedisKey.INDEX_CACHE_TEST, testResource);
        }
//        更新所有渠道菜单配置缓存
        Set<String> keys = jedisCache.hkeys(JedisKey.JOKE_DISTRIBUTOR_CONFIG);
        for (String key : keys) {
            String config = jedisCache.hget(JedisKey.JOKE_DISTRIBUTOR_CONFIG, key);
            if (StringUtils.isNotBlank(config)) {
                configMap.put(key, config);
            }
        }
    }

    /**
     * 获取首页配置信息
     *
     * @param did
     * @param model
     */
    public void getIndexConfig(String did, Model model) {
        long start = System.nanoTime();
        model.addAttribute("systemUtc", System.currentTimeMillis());
        if (StringUtils.isNumeric(did)) {
//            测试渠道
            if (did.equals(DEFAULT_DID)) {
                IndexResource testResource = resourceMap.get(JedisKey.INDEX_CACHE_TEST);
                if (testResource == null) {
                    testResource = JSON.parseObject(jedisCache.get(JedisKey.JOKE_RESOURCE_CONFIG_TEST), IndexResource.class);
                    if (testResource != null) {
                        resourceMap.put(JedisKey.INDEX_CACHE_TEST, testResource);
                    }
                }
                model.addAttribute(JedisKey.INDEX_CACHE_INDEX, testResource);
                String config = configMap.get(did);
                if (config == null) {
                    config = jedisCache.hget(JedisKey.JOKE_DISTRIBUTOR_CONFIG, did);
                    if (config != null) {
                        configMap.put(did, config);
                    }
                }
                model.addAttribute(JedisKey.INDEX_CACHE_CONFIG, config);
            } else {
                IndexResource indexResource = resourceMap.get(JedisKey.INDEX_CACHE_INDEX);
                if (indexResource == null) {
                    indexResource = JSON.parseObject(jedisCache.get(JedisKey.JOKE_RESOURCE_CONFIG_INDEX), IndexResource.class);
                    if (indexResource == null) {
                        IndexResource backResource = resourceMap.get(JedisKey.INDEX_CACHE_BACK);
                        if (backResource == null) {
                            log.error("{}的首页请求失效, 备用资源为空!", did);
                        }
                        indexResource = backResource;
                        if (log.isDebugEnabled()) {
                            log.error("{}的首页请求失效,正在使用备用内容!", did);
                        }
                    } else {
                        resourceMap.put(JedisKey.INDEX_CACHE_INDEX, indexResource);
                    }
                }
                String config = configMap.get(did);
                if (config == null) {
                    config = jedisCache.hget(JedisKey.JOKE_DISTRIBUTOR_CONFIG, did);
                    if (config != null) {
                        configMap.put(did, config);
                    }
                }
                model.addAttribute(JedisKey.INDEX_CACHE_INDEX, indexResource);
                model.addAttribute(JedisKey.INDEX_CACHE_CONFIG, config);
            }
        } else {
            IndexResource backResource = resourceMap.get(JedisKey.INDEX_CACHE_BACK);
            if (backResource == null) {
                log.error("{}的首页请求失效, 备用资源为空!", did);
            }
            model.addAttribute(JedisKey.INDEX_CACHE_INDEX, backResource);
            model.addAttribute(JedisKey.INDEX_CACHE_CONFIG, configMap.get(DEFAULT_DID));
            if (log.isDebugEnabled()) {
                log.error("{}的首页请求失效,正在使用备用内容!", did);
            }
        }

        if (log.isDebugEnabled()) {
            long end = System.nanoTime();
            log.debug("获取首页信息完成,耗时:{}", FormatUtil.getTimeStr(end - start));
        }
    }

    /**
     * 刷新配置缓存
     *
     * @param did
     */
    public void flushConfigCache(String did) {
        configMap.remove(did);
    }

    /**
     * 刷新资源缓存
     *
     * @param environment
     */
    public void flushResourceCache(String environment) {
        if (JedisKey.INDEX_CACHE_BACK.equals(environment)) {
            log.error("备用资源[{}]不能删除!", environment);
        } else {
            resourceMap.remove(environment);
        }
    }

    /**
     * 获取列表
     *
     * @param did
     * @param cid   1:趣图、2:段子、3:推荐、4:精选
     * @param page
     * @param limit
     * @return
     */
    public Result list(Integer did, Integer cid, Integer page, Integer limit) {
        long start = System.currentTimeMillis();
        long s = (page - 1) * limit;
        long e = page * limit - 1;
        Long size = jedisCache.zcard(JedisKey.JOKE_CHANNEL + cid);
        Set<String> keys = jedisCache.zrevrange(JedisKey.JOKE_CHANNEL + cid, s, e);
        List<Joke> list = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(keys)) {
            for (String id : keys) {
                Joke joke = getJoke(id, cid);
                if (joke != null) {
                    List<Comment> listHotComment = commentService.getComment(joke.getId(), null, null, true);
                    int count = commentService.getCommentCount(joke.getId());
                    if (listHotComment == null || listHotComment.size() <= 0) {
//                        log.debug("不存在神评");
                        joke.setComment(null);
                    } else {
//                        log.debug("存在神评，取点赞数最多的一条");
                        joke.setCommentNumber(count);
                        joke.setComment(listHotComment.get(0));
                    }
                    Comment comment = joke.getComment();
                    if (comment == null) {
//                        log.debug("没有神评，返回空");
                        comment = new Comment();
                        comment.setBc(null);
                        comment.setNick(joke.getNick());
                        joke.setComment(comment);
                    } else {
                        String bc = comment.getBc();
                        if (bc != null && bc.length() > 38) {
                            bc = bc.substring(0, 35) + "...";
                            comment.setBc(bc);
                        }
                    }
                    comment.setTotal(count);
                    joke.setSrc(null);
                    list.add(joke);
                }
            }
            if (CollectionUtils.isEmpty(list)) {
                log.debug("获取数据为空");
                return new Result("获取数据为空!", 2);
            }
        } else {
            log.debug("获取数据为空");
            return new Result("获取数据为空!", 1);
        }
        long end = System.currentTimeMillis();
        log.debug("获取列表页数据 - 总耗时:{}, did:{}, cid:{}, page:{}, limit:{}", FormatUtil.getTimeStr(end - start), did, cid, page, limit);
        return new Result(size.intValue(), list);

    }

    /**
     * 获取趣图
     *
     * @param id
     * @return
     */
    public Joke getJoke(String id, Integer cid) {
        Joke joke;
        joke = JSON.parseObject(jedisCache.get(JedisKey.STRING_JOKE + id), Joke.class);
        if (joke != null) {
            if (joke.getImg() != null) {
                if (cid.equals(4)) {
                    joke.setImg(joke.getImg().replace(".jpg", "_w600.jpg"));
                }
                joke.setImg(IMG_PREFIX + joke.getImg());
            }
            if (joke.getType() != null && joke.getType().equals(Constants.JOKE_TYPE_GIF)) {
                joke.setGif(IMG_PREFIX + joke.getGif());
            }
            if (joke.getRa() != null && !joke.getRa().startsWith("http")) {
                joke.setRa(IMG_PREFIX + joke.getRa());
            }
            if (cid != null && cid == 4) {
                joke.setContent(null);
            }
            if (joke.getComment() != null && joke.getComment().getAvata() != null) {
                Comment comment = joke.getComment();
                comment.setAvata(comment.getAvata());
            }
        }
        return joke;
    }

    /**
     * 获取段子详情页
     *
     * @param did
     * @param cid
     * @param jid
     * @return
     */
    public JokeDetail getJokeDetail(Integer did, Integer cid, Integer jid) {
        JokeDetail joke = JSON.parseObject(jedisCache.get(JedisKey.STRING_JOKE + jid), JokeDetail.class);
        if (joke != null) {
            if (joke.getImg() != null) {
                joke.setImg(IMG_PREFIX + joke.getImg());
            }
            if (joke.getType().equals(Constants.JOKE_TYPE_GIF)) {
                joke.setGif(IMG_PREFIX + joke.getGif());
            }
            if (joke.getType().equals(0)) {
                joke.setSrc(null);
            }
            if (joke.getRa() != null && !joke.getRa().startsWith("http")) {
                joke.setRa(IMG_PREFIX + joke.getRa());
            }
            String key = JedisKey.JOKE_CHANNEL + cid;
            Long index = jedisCache.zrevrank(key, String.valueOf(jid));
            if (index != null) {
//    			获取上一条段子和下一条编号
                if (index > 0) {
                    Set<String> jokeLastIds = jedisCache.zrevrange(key, index - 1, index + 1);
                    if (!CollectionUtils.isEmpty(jokeLastIds)) {
                        int seq = 0;
                        for (String jokeLastId : jokeLastIds) {
                            if (seq == 0) {
                                joke.setLastId(Integer.valueOf(jokeLastId));
                            }
                            if (seq == 2) {
                                joke.setNextId(Integer.valueOf(jokeLastId));
                            }
                            seq++;
                        }
                    }
                } else {
//        		    获取下一条段子编号
                    Set<String> jokeNextIds = jedisCache.zrevrange(key, index + 1, index + 1);
                    if (!CollectionUtils.isEmpty(jokeNextIds)) {
                        for (String jokeNextId : jokeNextIds) {
                            joke.setNextId(Integer.valueOf(jokeNextId));
                        }
                    }
                }
            } else {
                List<String> randoms = jedisCache.srandmember(JedisKey.SET_RELATED_JOKE_IMG, 2);
                if (!CollectionUtils.isEmpty(randoms) && randoms.size() == 2) {
                    if (!randoms.get(0).equals(String.valueOf(jid))) {
                        joke.setLastId(Integer.valueOf(randoms.get(0)));
                    }
                    if (!randoms.get(1).equals(String.valueOf(jid))) {
                        joke.setNextId(Integer.valueOf(randoms.get(1)));
                    }
                } else {
                    log.error("渠道[{}]随机获取[{}]频道2条数据异常!", did, key);
                }
            }
        }
        return joke;
    }

    /**
     * 精选推荐
     *
     * @param did
     * @param cid
     * @return
     */
    public List<Relate> getChoiceRelate(Integer did, Integer cid, Integer id) {
        List<Relate> relatedList = Lists.newArrayList();
        List<String> relatedImgIdList = jedisCache.srandmember(JedisKey.SET_RELATED_JOKE_IMG, 5);
        if (!CollectionUtils.isEmpty(relatedImgIdList)) {
            Joke joke;
            int index = 0;
            for (String jokeId : relatedImgIdList) {
                joke = JSON.parseObject(jedisCache.get(JedisKey.STRING_JOKE + jokeId), Joke.class);
                if (joke != null) {
                    if (id != null && joke.getId().equals(id)) {
                        log.info("精选随机到自己不进行推荐:[{}]", jokeId);
                        continue;
                    }
                    Relate relate = new Relate();
                    relate.setId(joke.getId());
                    relate.setCid(cid);
                    relate.setType(joke.getType());
                    relate.setTxt(joke.getTitle());
                    if (joke.getImg() != null) {
                        relate.setImg(IMG_PREFIX + joke.getImg().replace(".jpg", "_w200.jpg"));
                    }
                    relatedList.add(relate);
                    index++;
                    if (index == 4) {
                        break;
                    }
                } else {
                    log.error("精选内容为空:[{}]", jokeId);
                }
            }
        }
        return relatedList;
    }

    public List<Relate> getJokeRelate(Integer did, Integer cid, Integer id) {
        List<Relate> relatedList = Lists.newArrayList();
        List<String> relatedImgIdList = jedisCache.srandmember(JedisKey.JOKE_CHANNEL + 1, 5);
        if (!CollectionUtils.isEmpty(relatedImgIdList)) {
            Joke joke;
            int index = 0;
            for (String jokeId : relatedImgIdList) {
                joke = JSON.parseObject(jedisCache.get(JedisKey.STRING_JOKE + jokeId), Joke.class);
                if (joke != null) {
                    if (id != null && joke.getId().equals(id)) {
                        log.info("段子随机到自己不进行推荐:[{}]", jokeId);
                        continue;
                    }
                    Relate relate = new Relate();
                    relate.setId(joke.getId());
                    relate.setCid(cid);
                    relate.setType(joke.getType());
                    relate.setTxt(joke.getTitle());
                    if (joke.getImg() != null) {
                        relate.setImg(IMG_PREFIX + joke.getImg().replace("_600x_", "_200x_"));
                    }
                    relatedList.add(relate);
                    index++;
                    if (index == 4) {
                        break;
                    }
                } else {
                    log.error("段子内容为空:[{}]", jokeId);
                }
            }
        }
        return relatedList;
    }

    /**
     * 获取banner
     *
     * @param cid
     * @return
     */
    public Result getBannerList(Integer did, Integer cid) {
        String key = JedisKey.JOKE_BANNER + did + "_" + cid;
        //        Long size = jedisCache.zcard(key);
        int size = 0;
        Set<String> bannerSet = jedisCache.zrevrange(key, 0L, -1L);
        List<Banner> bannerList = new ArrayList<>();
        Banner banner;
        if (!CollectionUtils.isEmpty(bannerSet)) {
            size = bannerSet.size();
            for (String bannerId : bannerSet) {
                String bannerKey = JedisKey.STRING_BANNER + did + "_" + cid + "_" + bannerId;
                banner = JSON.parseObject(jedisCache.get(bannerKey), Banner.class);
                if (null != banner) {
                    banner.setImg(IMG_PREFIX + banner.getImg());
                    bannerList.add(banner);
                } else {
                    log.error("获取banner缓存异常:{}", bannerKey);
                }
            }
            if (CollectionUtils.isEmpty(bannerList)) {
                return new Result("获取数据为空!", 2);
            }
        } else {
            return new Result("获取数据为空!", 1);
        }
        return new Result(size, bannerList);
    }


}
