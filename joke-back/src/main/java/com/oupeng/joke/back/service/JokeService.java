package com.oupeng.joke.back.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.oupeng.joke.back.util.Constants;
import com.oupeng.joke.back.util.HttpUtil;
import com.oupeng.joke.back.util.ImgRespDto;
import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;
import com.oupeng.joke.dao.mapper.JokeMapper;
import com.oupeng.joke.domain.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class JokeService {
    private static final String ADMIN_PASS = "admin@joke.com";
    private static Logger logger = LoggerFactory.getLogger(JokeService.class);
    @Autowired
    private JokeMapper jokeMapper;
    @Autowired
    private JedisCache jedisCache;
    @Autowired
    private TaskService taskService;
    @Autowired
    private Environment env;

    /**
     * 获取段子列表
     *
     * @param type
     * @param status
     * @param source
     * @param startDay
     * @param endDay
     * @return
     */
    public List<Joke> getJokeListForVerify(Integer type, Integer status, Integer source, String startDay, String endDay, Integer offset, Integer pageSize) {
        List<Joke> jokeList = jokeMapper.getJokeListForVerify(type, status, source, startDay, endDay, offset, pageSize);
        handleJokesUrl(jokeList);
        return jokeList;
    }

    /**
     * 获取待审核的段子列表总记录数
     *
     * @param type
     * @param status
     * @param source
     * @param startDay
     * @param endDay
     * @return
     */
    public int getJokeListForVerifyCount(Integer type, Integer status, Integer source, String startDay, String endDay) {
        return jokeMapper.getJokeListForVerifyCount(type, status, source, startDay, endDay);
    }

    /**
     * 处理段子列表记录URL
     *
     * @param jokeList
     */
    private void handleJokesUrl(List<Joke> jokeList) {
        if (!CollectionUtils.isEmpty(jokeList)) {
            for (Joke joke : jokeList) {
                handleJokeUrl(joke);
            }
        }
    }

    private void handleJokeUrl(Joke joke) {
        if (joke.getType() == Constants.JOKE_TYPE_IMG) {
            joke.setImg(env.getProperty("img.real.server.url") + joke.getImg());
        } else if (joke.getType() == Constants.JOKE_TYPE_GIF) {
            joke.setImg(env.getProperty("img.real.server.url") + joke.getImg());
            joke.setGif(env.getProperty("img.real.server.url") + joke.getGif());
        }
    }

    /**
     * 修改审核状态
     *
     * @param status
     * @param ids
     * @param user
     */
    public void verifyJoke(Integer status, String ids, String user) {
        String[] jokeIds = ids.split(",");
        if (status == Constants.JOKE_STATUS_VALID) { //通过
            // 更改状态
            jokeMapper.updateJokeStatus(status, ids, user);
            //加到缓存
            for (String id : jokeIds) {
                Joke joke = jokeMapper.getJokeById(Integer.parseInt(id));
                if (joke != null) {
                    joke.setCreateTime(null);
                    joke.setSourceId(null);
                    joke.setStatus(null);
                    joke.setUpdateTime(null);
                    joke.setVerifyTime(null);
                    joke.setVerifyUser(null);
                    if (joke.getCommentNumber() != null
                            && joke.getCommentContent() != null
                            && joke.getAvata() != null
                            && joke.getNick() != null) {
                        joke.setComment(new Comment(joke.getCommentNumber(), joke.getCommentContent(), joke.getAvata(), joke.getNick()));
                        joke.setCommentNumber(null);
                        joke.setCommentContent(null);
                        joke.setAvata(null);
                        joke.setNick(null);
                    }
                    jedisCache.set(JedisKey.STRING_JOKE + joke.getId(), JSON.toJSONString(joke));
                }
            }
            logger.info("内容审核：段子id[{}]：通过", ids);
        } else if (status == Constants.JOKE_STATUS_NOVALID) {//不通过
            jokeMapper.updateJokeStatus(status, ids, user);
            logger.info("内容审核：段子id[{}]:不通过", ids);
        } else {
            Set<String> keys = jedisCache.keys(JedisKey.SORTEDSET_ALL);
            if (!CollectionUtils.isEmpty(keys)) {
                for (String key : keys) {
                    jedisCache.zrem(key, jokeIds);
                }
            }
            //删除段子
            for (String id : jokeIds) {
                //  type 0:纯文本 1:图片 2:动图 3 精选
                //段子缓存(1:趣图、2:段子、3:推荐、4:精选)
                jedisCache.zrem(JedisKey.JOKE_CHANNEL + 1, id);
                jedisCache.zrem(JedisKey.JOKE_CHANNEL + 2, id);
                jedisCache.zrem(JedisKey.JOKE_CHANNEL + 3, id);
                jedisCache.zrem(JedisKey.JOKE_CHANNEL + 4, id);
                //删除段子
                jedisCache.del(JedisKey.STRING_JOKE + id);
            }
            jokeMapper.updateJokeStatus(Constants.JOKE_STATUS_NOVALID, ids, user); //修改为通过
            logger.info("内容审核：段子id[{}]:下线", ids);
        }
    }

    public Joke getJokeById(Integer id) {
        Joke joke = jokeMapper.getJokeById(id);
        if (joke != null) {
            handleJokeUrl(joke);
        }
        return joke;
    }

    /**
     * 更新段子点赞数
     */
    @PostConstruct
    public void jokeLikeCountUpdate() {
        logger.debug("jokeLikeCountUpdate init...");
        new Thread() {
            public void run() {
                while (true) {
                    try {
                        List<String> likeIdList = jedisCache.brpop(JedisKey.JOKE_LIST_LIKE, 60 * 5);
                        logger.info("jokeLikeCountUpdate receved size:[{}]", likeIdList == null ? 0 : likeIdList.size());
                        if (!CollectionUtils.isEmpty(likeIdList)) {
                            String likeId = likeIdList.get(1);
                            logger.debug("update joke Like Count id:" + likeId);
                            if (Integer.valueOf(likeId) > 20000000) { //更新精选的点踩数
                                jokeMapper.updateChoiceLikeCount(Integer.valueOf(likeId));
                            } else {
                                jokeMapper.updatejokeLikeCount(Integer.valueOf(likeId));
                            }
                            updateJokeLikeCache(likeId);
                        }
                    } catch (Exception e) {
                        logger.error("update joke Like Count error!", e);
                    }
                }
            }

            ;
        }.start();
    }


    /**
     * 更新段子被踩数
     */
    @PostConstruct
    public void jokeStepCountUpdate() {
        logger.debug("jokeStepCountUpdate init...");
        new Thread() {
            public void run() {
                while (true) {
                    try {
                        List<String> stepIdList = jedisCache.brpop(JedisKey.JOKE_LIST_STEP, 60 * 5);
                        logger.debug("jokeStepCountUpdate receved size:[{}]", stepIdList == null ? 0 : stepIdList.size());
                        if (!CollectionUtils.isEmpty(stepIdList)) {
                            String stepId = stepIdList.get(1);
                            logger.debug("update joke step Count id:" + stepId);
                            if (Integer.valueOf(stepId) > 20000000) { //更新精选的点踩数
                                jokeMapper.updateChoiceStepCount(Integer.valueOf(stepId));
                            } else {
                                jokeMapper.updateJokeStepCount(Integer.valueOf(stepId));
                            }
                            updateJokeStepCache(stepId);
                        }
                    } catch (Exception e) {
                        logger.error("update joke step Count error!" + e.getMessage(), e);
                    }
                }
            }

            ;
        }.start();
    }

    /**
     * 存储段子反馈信息
     */
    @PostConstruct
    public void insertJokeFeedback() {
        logger.debug("insertJokeFeedback init...");
        new Thread() {
            public void run() {
                while (true) {
                    try {
                        List<String> feedbackList = jedisCache.brpop(JedisKey.JOKE_LIST_FEEDBACK, 60 * 5);
                        logger.debug("insertJokeFeedback receved size:[{}]", feedbackList == null ? 0 : feedbackList.size());
                        if (!CollectionUtils.isEmpty(feedbackList)) {
                            String feedbackStr = feedbackList.get(1);
                            logger.debug("update joke feedback String:" + feedbackStr);
                            Feedback feedback = JSON.parseObject(feedbackStr, Feedback.class);
                            if (feedback != null) {
                                jokeMapper.insertJokeFeedback(feedback);
                            }
                        }
                    } catch (Exception e) {
                        logger.error("update joke feedback Count error!", e);
                    }
                }
            }

            ;
        }.start();
    }

    /**
     * 更新段子点赞缓存数
     *
     * @param jokeId
     */
    private void updateJokeLikeCache(String jokeId) {
        String jokeStr = jedisCache.get(JedisKey.STRING_JOKE + jokeId);
        if (jokeStr != null) {
            Joke joke = JSON.parseObject(jokeStr, Joke.class);
            if (joke != null && joke.getGood() != null) {
                joke.setGood(joke.getGood() + 1);
            } else {
                joke.setGood(1);
            }
            jedisCache.set(JedisKey.STRING_JOKE + jokeId, JSON.toJSONString(joke));
        }
    }

    /**
     * 更新段子被踩缓存数
     *
     * @param jokeId
     */
    private void updateJokeStepCache(String jokeId) {
        String jokeStr = jedisCache.get(JedisKey.STRING_JOKE + jokeId);
        if (jokeStr != null) {
            Joke joke = JSON.parseObject(jokeStr, Joke.class);
            if (joke != null && joke.getBad() != null) {
                joke.setBad(joke.getBad() + 1);
            } else {
                joke.setBad(1);
            }
            jedisCache.set(JedisKey.STRING_JOKE + jokeId, JSON.toJSONString(joke));
        }
    }

    /**
     * 更新段子信息
     *
     * @param id
     * @param title
     * @param img
     * @param gif
     * @param width
     * @param height
     * @param content
     * @param user
     * @return
     */
    public boolean updateJoke(Integer id, String title, String img, String gif, Integer width, Integer height, String content, Integer weight, String user) {
        Joke joke = new Joke();
        joke.setId(id);
        joke.setContent(content);
        joke.setTitle(title);
        joke.setVerifyUser(user);
        joke.setWeight(weight);
        boolean result = handleJokeImg(img, gif, width, height, joke);
        if (result) {
            jokeMapper.updateJoke(joke);
        }
        return result;
    }

    public Map<String, Integer> getJokeVerifyInfoByUser(String user) {
        Map<String, Integer> map = Maps.newHashMap();
        List<JokeVerifyInfo> list = jokeMapper.getJokeVerifyInfoByUser(user);
        String textKey = "type0";
        String imgKey = "type1";
        String gifKey = "type2";
        if (!CollectionUtils.isEmpty(list)) {
            for (JokeVerifyInfo jokeVerifyInfo : list) {
                if (Constants.JOKE_TYPE_GIF == jokeVerifyInfo.getType()) {
                    map.put(gifKey, jokeVerifyInfo.getNum());
                } else if (Constants.JOKE_TYPE_IMG == jokeVerifyInfo.getType()) {
                    map.put(imgKey, jokeVerifyInfo.getNum());
                } else if (Constants.JOKE_TYPE_TEXT == jokeVerifyInfo.getType()) {
                    map.put(textKey, jokeVerifyInfo.getNum());
                }
            }
        }
        if (!map.containsKey(textKey)) map.put(textKey, 0);
        if (!map.containsKey(imgKey)) map.put(imgKey, 0);
        if (!map.containsKey(gifKey)) map.put(gifKey, 0);
        return map;
    }

    public List<Joke> getJokeListForSearch(Integer id, String content) {
        List<Joke> jokeList = jokeMapper.getJokeList(null, null, id, content, false);
        handleJokesUrl(jokeList);
        return jokeList;
    }

    public int getJokeCountForChannel(String contentType) {
        return jokeMapper.getJokeCountForChannel(contentType);
    }

    public List<Joke> getJokeListForChannel(String contentType, Integer start, Integer size) {
        List<Joke> jokeList = jokeMapper.getJokeListForChannel(contentType, start, size);
        handleJokesUrl(jokeList);
        return jokeList;
    }

    /**
     * 获取已审核的段子列表记录总数
     *
     * @param type
     * @param status
     * @return
     */
    public int getJokeListForTopicCount(Integer type, Integer status) {
        return jokeMapper.getJokeListForTopicCount(type, status);
    }

    /**
     *
     * @param type
     * @param status
     * @return
     */
    public int getJokeListForCount(Integer type, Integer status) {
        return jokeMapper.getJokeListForCount(type, status);
    }


    /**
     * 获取已审核的段子列表  TODO
     *
     * @param type
     * @param status
     * @return
     */
    public List<Joke> getJokeListForTopic(Integer type, Integer status, Integer offset, Integer pageSize) {
        List<Joke> jokeList = jokeMapper.getJokeListForTopic(type, status, offset, pageSize);
        handleJokesUrl(jokeList);
        return jokeList;
    }

    /**
     * 获取专题对应的段子编号
     *
     * @param topicId
     * @return
     */
    public List<Integer> getJokeForPublishTopic(Integer topicId) {
        return jokeMapper.getJokeForPublishTopic(topicId);
    }

    /**
     * 查询最近审核通过的数据
     *
     * @param contentType 发布类型
     * @param size        数量
     * @return
     */
    public List<Joke> getJokeForPublishChannel(String contentType, Integer size) {
        return jokeMapper.getJokeForPublishChannel(contentType, size);
    }

    public int getJokeCountForPublishChannel(String contentType, Integer status) {
        return jokeMapper.getJokeCountForPublishChannel(contentType, status);
    }

    public void updateJokeForPublishChannel(String jokeIds) {
        jokeMapper.updateJokeStatus(Constants.JOKE_STATUS_PUBLISH, jokeIds, null);
    }

    /**
     * 获取待发布的段子列表
     *
     * @param lastUpdateTime
     * @param currentUpdateTime
     * @return
     */
    public List<Joke> getJokeListForPublish(String lastUpdateTime, String currentUpdateTime) {
        return jokeMapper.getJokeListForPublish(lastUpdateTime, currentUpdateTime);
    }

    public List<String> getJokeListForPublishRecommend(Integer type, Integer num) {
        return jokeMapper.getJokeListForPublishRecommend(type, num);
    }

    public List<JokeVerifyRate> getJokeVerifyRate() {
        return jokeMapper.getJokeVerifyRate();
    }

    private boolean handleJokeImg(String imgUrl, String gifUrl, Integer width, Integer height, Joke joke) {
        if (StringUtils.isNotBlank(gifUrl)) {//动图
            joke.setType(Constants.JOKE_TYPE_GIF);
            ImgRespDto imgRespDto = HttpUtil.handleImg(env.getProperty("remote.crop.img.server.url"), gifUrl, true);
            if (imgRespDto != null && imgRespDto.getErrorCode() == 0) {
                joke.setGif(imgRespDto.getGifUrl());
                joke.setImg(imgRespDto.getImgUrl());
                joke.setWidth(imgRespDto.getWidth());
                joke.setHeight(imgRespDto.getHeight());
                return true;
            }
        } else if (StringUtils.isNotBlank(imgUrl)) {//静图
            joke.setType(Constants.JOKE_TYPE_IMG);
            ImgRespDto imgRespDto = HttpUtil.handleImg(env.getProperty("remote.crop.img.server.url"), imgUrl, true);
            if (imgRespDto != null && imgRespDto.getErrorCode() == 0) {
                joke.setGif(null);
                joke.setImg(imgRespDto.getImgUrl());
                joke.setWidth(imgRespDto.getWidth());
                joke.setHeight(imgRespDto.getHeight());
                return true;
            }
        } else {
            joke.setType(Constants.JOKE_TYPE_TEXT);
            joke.setGif(null);
            joke.setImg(null);
            joke.setWidth(null);
            joke.setHeight(null);
            return true;
        }
        return false;
    }

    /**
     * 获取字典记录总条数
     *
     * @param code
     * @return
     */
    public int getDictionaryRecordCount(String code) {
        return jokeMapper.getDictionaryRecordCount(code);
    }

    /**
     * 获取字典记录列表
     *
     * @param code
     * @param offset
     * @param pageSize
     * @return
     */
    public List<Dictionary> getDictionaryRecordList(String code, int offset, Integer pageSize) {
        return jokeMapper.getDictionaryRecordList(code, offset, pageSize);
    }

    /**
     * 添加一条字典记录
     *
     * @param dict
     * @return
     */
    public int addDictionary(Dictionary dict) {
        return jokeMapper.addDictionary(dict);
    }

    /**
     * 修改权重信息
     *
     * @param dict
     * @return
     */
    public int weightEdit(Dictionary dict) {
        return jokeMapper.weightEdit(dict);
    }

    /**
     * 删除权限信息
     *
     * @param id
     * @return
     */
    public int weightDel(Integer id) {
        return jokeMapper.weightDel(id);
    }

    /**
     * 获取字典信息
     *
     * @param id
     * @return
     */
    public Object weightGet(String id) {
        return jokeMapper.weightGet(id);
    }

    /**
     * 修改发布数量
     *
     * @param id
     * @param size
     * @return
     */
    public int editPublishSize(String id, Integer size) {
        return jokeMapper.editPublishSize(id, size);
    }

    /**
     * 刷新频道缓存
     *
     * @param id
     * @param pass
     * @return
     */
    public boolean flushCache(Integer id, String pass) {
        if (pass.equals(ADMIN_PASS)) {
            jedisCache.lpush(JedisKey.JOKE_LIST_FLUSH_CACHE, String.valueOf(id));
            return true;
        }
        return false;
    }

    /**
     * 获取段子数据
     *
     * @param type  段子类型: 0:文字、1:图片、2:动图
     * @param count 获取段子数量
     * @return
     */
    public List<Joke> getPublishJokeListByType(Integer type, Integer count) {
        return jokeMapper.getPublishJokeListByType(type, count);
    }

    /**
     * 自动发布段子
     *
     * @param type
     * @param limit
     */
    public void autoAuditJoke(int type, int limit) {
        jokeMapper.autoAuditJoke(type, limit);
    }


    /**
     * 添加发布规则
     *
     * @param type        1:趣图、2:段子、3:推荐、4:精选
     * @param textNum
     * @param imageNum
     * @param gifNum
     * @param textWeight
     * @param imageWeight
     * @param gifWeight
     */
    public void addPublishRole(Integer type, String role, Integer textNum, Integer imageNum, Integer gifNum, Integer textWeight, Integer imageWeight, Integer gifWeight) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("role", role);
        jsonObject.put("textNum", textNum);
        jsonObject.put("imageNum", imageNum);
        jsonObject.put("gifNum", gifNum);
        jsonObject.put("textWeight", textWeight);
        jsonObject.put("imageWeight", imageWeight);
        jsonObject.put("gifWeight", gifWeight);
        if (type == 1) { //纯文
//			更新数据库
            jokeMapper.addPublishRole(10041, jsonObject.toString());
//			更新定时任务
            updateTask(10041, "14", role, 2, jsonObject);
        } else if (type == 2) { //趣图
//			更新数据库
            jokeMapper.addPublishRole(10042, jsonObject.toString());
//			更新定时任务
            updateTask(10042, "15", role, 1, jsonObject);
        } else if (type == 3) { //推荐
//			更新数据库
            jokeMapper.addPublishRole(10043, jsonObject.toString());
//			更新定时任务
            updateTask(10043, "16", role, 3, jsonObject);
        }
    }

    /**
     * 更新定时任务
     *
     * @param code
     * @param id
     * @param role
     * @param type
     * @param jsonObject
     */
    private void updateTask(int code, String id, String role, int type, JSONObject jsonObject) {
        Task task = new Task();
        task.setCron(role);
        task.setId(id);
        task.setType(type);
        task.setObject(jsonObject);
        taskService.updateTask(task);
    }

    /**
     * 获取发布规则
     *
     * @param code
     * @return
     */
    public String getPublishRole(int code) {
        return jokeMapper.getPublishRole(code);
    }

    /**
     * 新增评论数量记录
     *
     * @param jid
     * @return TODO
     */
    public boolean incrementComment(Integer[] jid) {
//    	更新数据库中段子评论数
        for (Integer id : jid) {
            if (id != null) {
                if (id > 20000000) {
                    jokeMapper.incrementChoiceComment(id);
                } else {
                    jokeMapper.incrementComment(id);
                }
                //		更新缓存中的段子评论数
                Joke joke = JSON.parseObject(jedisCache.get(JedisKey.STRING_JOKE + id), Joke.class);
                if (joke != null && joke.getComment() != null) {
                    Comment comment = joke.getComment();
                    comment.setTotal(comment.getTotal() + 1);
                    jedisCache.set(JedisKey.STRING_JOKE + id, JSON.toJSONString(joke));
                } else {
                    logger.error("更新段子[{}]评论数失败!缓存中没有此段子!", id);
                }
            }
        }
        return true;
    }

    /**
     * 减少评论数量记录
     *
     * @param jid
     * @return TODO
     */
    public boolean decrementComment(Integer[] jid) {
//    	更新数据库中段子评论数
        for (Integer id : jid) {
            if (id != null) {
                if (id > 20000000) {
                    jokeMapper.decrementChoiceComment(id);
                } else {
                    jokeMapper.decrementComment(id);
                }
                //		更新缓存中的段子评论数
                Joke joke = JSON.parseObject(jedisCache.get(JedisKey.STRING_JOKE + id), Joke.class);
                if (joke != null && joke.getComment() != null) {
                    Comment comment = joke.getComment();
                    comment.setTotal(comment.getTotal() - 1);
                    jedisCache.set(JedisKey.STRING_JOKE + id, JSON.toJSONString(joke));
                } else {
                    logger.error("更新段子[{}]评论数失败!缓存中没有此段子!", id);
                }
            }
        }
        return true;
    }


    /**
     * 获取段子2.0文字段子发布列表
     *
     * @param limit
     * @return
     */
    public List<Joke> getJoke2PublishList(int status, int type, int limit) {
        return jokeMapper.getJoke2PublishList(status, type, limit);
    }

    /**
     * 更新段子2.0文字段子状态
     *
     * @param idsStr
     */
    public void updateJoke2PublishStatus(String idsStr, int status) {
        jokeMapper.updateJoke2PublishStatus(idsStr, status);
    }

    /**
     * 获取段子2.0发布任务
     *
     * @return
     */
    public List<Task> getJoke2PublishTask() {
        return jokeMapper.getJoke2PublishTask();
    }
}
