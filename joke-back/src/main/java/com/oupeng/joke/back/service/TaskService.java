package com.oupeng.joke.back.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.oupeng.joke.back.task.Joke2PublishTask;
import com.oupeng.joke.domain.JobInfo;
import com.oupeng.joke.domain.Task;
import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;
import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 定时任务服务
 * 1:趣图、2:段子、3:推荐、4:精选
 * Created by hushuang on 2016/11/15.
 */
@Service
public class TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);
    /**	文字权重	*/
    private static Integer TEXT_WEIGHT = 2;
    /**	静图权重	*/
    private static Integer IMG_WEIGHT = 2;
    /**	动图权重	*/
    private static Integer GIF_WEIGHT = 1;
    /**	发布文字数量	*/
    private static Integer PUBLISH_TEXT_SIZE = 200;
    /**	发布静图数量	*/
    private static Integer PUBLISH_IMG_SIZE = 200;
    /**	发布动图数量	*/
    private static Integer PUBLISH_GIF_SIZE = 100;
    @Autowired
    private StdSchedulerFactory stdSchedulerFactory;
    @Autowired
    private JokeService jokeService;
    @Autowired
    private JedisCache jedisCache;
    @Autowired
    private Environment env;
    @Autowired
    private MailService mailService;

    private int channnelMinSize = 10;
    /**
     * 任务调度器
     */
    private Scheduler sched = null;

    @PostConstruct
    public void init() {
        try {
            sched = stdSchedulerFactory.getScheduler();
            sched.start();
//            初始化任务
            List<Task> tasks = jokeService.getJoke2PublishTask();
            if(!CollectionUtils.isEmpty(tasks)){
                for(Task task : tasks){
                    if(task.getPolicy() != null && task.getType() == 1){
                        JSONObject jsonObject = JSON.parseObject(task.getPolicy());
                        task.setObject(jsonObject);
                        task.setCron(jsonObject.getString("role"));
                        addJob(task);
                    }
                }
            }


        } catch (SchedulerException e) {
            logger.error("任务服务启动异常:" + e.getMessage(), e);
        }
        logger.info("-------TaskService init ---------");
    }

    /**
     * 创建新发布任务
     * void
     *
     * @param task
     */
    private void addJob(Task task) {
        try {
            JobKey jobKey = new JobKey(task.getId());
            JobInfo jobInfo = new JobInfo();
            JobDetailImpl jobDetail = new JobDetailImpl();
            jobDetail.setKey(jobKey);
            jobDetail.setName(task.getId());
            jobDetail.setJobClass(Joke2PublishTask.class);
            JobDataMap jobDataMap = new JobDataMap();
			jobDataMap.put("task", task);
            jobDataMap.put("taskService", this);
            jobDetail.setJobDataMap(jobDataMap);
            jobInfo.setJobDetail(jobDetail);
            CronTriggerImpl cronTrigger = new CronTriggerImpl();
            cronTrigger.setJobKey(jobKey);
            cronTrigger.setName(task.getId());
            cronTrigger.setCronExpression(task.getCron());
            jobInfo.setCronTrigger(cronTrigger);
            sched.scheduleJob(jobDetail, cronTrigger);
        } catch (ParseException e) {
            logger.error("初始化定时任务， 解析任务规则异常:" + e.getMessage(), e);
        } catch (SchedulerException e) {
            logger.error("初始化定时任务， 解析任务规则异常:" + e.getMessage(), e);
        }
    }


    /**
     * 是否存在
     *
     * @param id
     */
    public boolean containsKey(String id) {
        try {
            JobKey jobKey = new JobKey(id);
            return sched.getJobDetail(jobKey) != null;
        } catch (Exception e) {
            logger.error("判断任务是否存在异常:" + e.getMessage(), e);
            return false;
        }
    }

    /**
     * 删除任务
     * @param taskId
     * @return
     */
    public boolean deleteTask(String taskId){
        try {
            JobKey jobKey = new JobKey(taskId);
            JobDetail jobDetail = sched.getJobDetail(jobKey);
            if (jobDetail != null) {
                return sched.deleteJob(jobKey);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * 段子2.0 发布段子(文字)
     * @param task
     */
    public void publishText(Task task){
        Double baseScore = Double.parseDouble(new SimpleDateFormat("yyyyMMdd000000").format(new Date()));
        Map<String,Double> map = Maps.newHashMap();
        if(task.getObject().getInteger("textNum") != null){
            PUBLISH_TEXT_SIZE = task.getObject().getInteger("textNum");
        }
        List<String> list = jokeService.getJoke2PublishList(0, PUBLISH_TEXT_SIZE);
        StringBuffer ids = new StringBuffer();
        if(!CollectionUtils.isEmpty(list)){
            int size = list.size();
            for(int i = size; i > 0; i--){
                map.put(list.get(i - 1), baseScore + Double.valueOf(i));
                ids.append(list.get(i - 1)).append(",");
            }
            jedisCache.zadd(JedisKey.JOKE_CHANNEL + 2, map);
            // 更新已发布状态
            jokeService.updateJoke2PublishTextStatus(ids.deleteCharAt(ids.lastIndexOf(",")).toString());
        }

    }
    public void publishImage(Task task){
        Double baseScore = Double.parseDouble(new SimpleDateFormat("yyyyMMdd000000").format(new Date()));
        Map<String,Double> map = Maps.newHashMap();
        if(task.getObject().getInteger("gifNum") != null){
            PUBLISH_GIF_SIZE = task.getObject().getInteger("gifNum");
        }
        if(task.getObject().getInteger("imageNum") != null){
            PUBLISH_IMG_SIZE = task.getObject().getInteger("imageNum");
        }

        if(task.getObject().getInteger("gifWeight") != null){
            GIF_WEIGHT = task.getObject().getInteger("gifWeight");
        }

        if(task.getObject().getInteger("imageWeight") != null){
            IMG_WEIGHT = task.getObject().getInteger("imageWeight");
        }
        List<String> imgList = jokeService.getJoke2PublishList(1, PUBLISH_IMG_SIZE);
        List<String> gifList = jokeService.getJoke2PublishList(2, PUBLISH_GIF_SIZE);
        StringBuffer ids = new StringBuffer();
//        int max = imgList.size() > gifList.size() ? imgList.size() : gifList.size();
        int imgSize = imgList.size();
        int gifSize = gifList.size();
        int img = IMG_WEIGHT;
        int gif = GIF_WEIGHT;
        int total = img + gif;
        for(int i = total; i > 0; i--){
//			按照段子审核时间进行权重
            if(img > 0){
                if(imgSize > 0){
                    String id = imgList.get(imgSize - 1);
                    map.put(id, baseScore + Double.valueOf(i));
                    ids.append(id).append(",");
                }
                imgSize--;
                img--;
            }
            if(gif > 0){ // 频道权重是否用完
                if(gifSize > 0){
                    String id = gifList.get(gifSize - 1);
                    map.put(id, baseScore + Double.valueOf(i));
                    ids.append(id).append(",");
                }
                gifSize--;
                gif--;
            }
            if(gif == 0 && img == 0){
                gif = GIF_WEIGHT;
                img = IMG_WEIGHT;
            }
            //			map中数量与总数对应上说明数据记录完成;
            if(map.size() == total ){
                break;
            }
        }
        jedisCache.zadd(JedisKey.JOKE_CHANNEL + 1, map);
        // 更新已发布状态
        jokeService.updateJoke2PublishTextStatus(ids.deleteCharAt(ids.lastIndexOf(",")).toString());


    }
    public void publishRecommend(Task task){
        Double baseScore = Double.parseDouble(new SimpleDateFormat("yyyyMMdd000000").format(new Date()));
        if(task.getObject().getInteger("gifNum") != null){
            PUBLISH_GIF_SIZE = task.getObject().getInteger("gifNum");
        }
        if(task.getObject().getInteger("imageNum") != null){
            PUBLISH_IMG_SIZE = task.getObject().getInteger("imageNum");
        }
        if(task.getObject().getInteger("textNum") != null){
            PUBLISH_TEXT_SIZE = task.getObject().getInteger("textNum");
        }
        if(task.getObject().getInteger("textWeight") != null){
            TEXT_WEIGHT = task.getObject().getInteger("textWeight");
        }
        if(task.getObject().getInteger("gifWeight") != null){
            GIF_WEIGHT = task.getObject().getInteger("gifWeight");
        }
        if(task.getObject().getInteger("imageWeight") != null){
            IMG_WEIGHT = task.getObject().getInteger("imageWeight");
        }
        Map<String,Double> map = Maps.newHashMap();
        List<String> textList = jokeService.getJoke2PublishList(0, PUBLISH_TEXT_SIZE);
        List<String> imgList = jokeService.getJoke2PublishList(1, PUBLISH_IMG_SIZE);
        List<String> gifList = jokeService.getJoke2PublishList(2, PUBLISH_GIF_SIZE);
        StringBuffer ids = new StringBuffer();
//        int max = imgList.size() > gifList.size() ? imgList.size() : gifList.size();
        int textSize = textList.size();
        int imgSize = imgList.size();
        int gifSize = gifList.size();
        int img = IMG_WEIGHT;
        int gif = GIF_WEIGHT;
        int text = TEXT_WEIGHT;
        int total = img + gif + text;
        for(int i = total; i > 0; i--){
//			按照段子审核时间进行权重
            if(text > 0){
                if(imgSize > 0){
                    String id = textList.get(textSize - 1);
                    map.put(id, baseScore + Double.valueOf(i));
                    ids.append(id).append(",");
                }
                textSize--;
                text--;
            }
            if(img > 0){
                if(imgSize > 0){
                    String id = imgList.get(imgSize - 1);
                    map.put(id, baseScore + Double.valueOf(i));
                    ids.append(id).append(",");
                }
                imgSize--;
                img--;
            }
            if(gif > 0){ // 频道权重是否用完
                if(gifSize > 0){
                    String id = gifList.get(gifSize - 1);
                    map.put(id, baseScore + Double.valueOf(i));
                    ids.append(id).append(",");
                }
                gifSize--;
                gif--;
            }
            if(gif == 0 && img == 0 && text == 0){
                gif = GIF_WEIGHT;
                img = IMG_WEIGHT;
                text = TEXT_WEIGHT;
            }
            //			map中数量与总数对应上说明数据记录完成;
            if(map.size() == total ){
                break;
            }
        }
        jedisCache.zadd(JedisKey.JOKE_CHANNEL + 3, map);
    }

    @PreDestroy
    public void destroy() {
        try {
            sched.clear();
            sched.shutdown();
        } catch (SchedulerException e) {
           logger.error("关闭任务调度器异常:" + e.getMessage(), e);
        }
    }
}