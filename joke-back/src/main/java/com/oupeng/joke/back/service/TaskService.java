package com.oupeng.joke.back.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.oupeng.joke.back.task.Joke2PublishTask;
import com.oupeng.joke.back.util.Constants;
import com.oupeng.joke.back.util.FormatUtil;
import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;
import com.oupeng.joke.domain.JobInfo;
import com.oupeng.joke.domain.Joke;
import com.oupeng.joke.domain.Task;
import org.apache.commons.lang3.StringUtils;
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
import java.util.ArrayList;
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

    private static final Logger log = LoggerFactory.getLogger(TaskService.class);
    /**
     * 文字权重
     */
    private static Integer TEXT_WEIGHT = 2;
    /**
     * 静图权重
     */
    private static Integer IMG_WEIGHT = 2;
    /**
     * 动图权重
     */
    private static Integer GIF_WEIGHT = 1;
    /**
     * 发布文字数量
     */
    private static Integer PUBLISH_TEXT_SIZE = 200;
    /**
     * 发布静图数量
     */
    private static Integer PUBLISH_IMG_SIZE = 200;
    /**
     * 发布动图数量
     */
    private static Integer PUBLISH_GIF_SIZE = 100;

    /**
     * 发布推荐消息邮件
     */
    private static final String MAIL_SUBJECT = "段子发布通知";
    private static final String MAIL_PEOPLE = "各位:\n";
    private static final String PUBLISH = "发布";
    private static final String APPROVED = "，已审核剩余";
    private static final String UNAPPROVED = "，未审核剩余";
    private static final String TIAO = "条";
    private static final String CH = "。\n";
    private static final String[] PUBTYPE = {"\t趣图:", "\t动图:", "\t段子"};


    @Autowired
    private StdSchedulerFactory stdSchedulerFactory;
    @Autowired
    private JokeService jokeService;
    @Autowired
    private JedisCache jedisCache;
    @Autowired
    private MailService mailService;
    @Autowired
    private Environment env;

    //收件人
    private String recipient;
    //抄送
    private String cc;


    /**
     * 任务调度器
     */
    private Scheduler sched = null;

    @PostConstruct
    public void init() {
        try {
            String re = env.getProperty("data.publish.recipient");
            if (StringUtils.isNotBlank(re)) {
                recipient = re;
            }
            String co = env.getProperty("data.publish.cc");
            if (StringUtils.isNotBlank(co)) {
                cc = co;
            }

            sched = stdSchedulerFactory.getScheduler();
            sched.start();
//            初始化任务
            List<Task> tasks = jokeService.getJoke2PublishTask();
            if (!CollectionUtils.isEmpty(tasks)) {
                for (Task task : tasks) {
                    if (task.getPolicy() != null) {
                        JSONObject jsonObject = JSON.parseObject(task.getPolicy());
                        task.setObject(jsonObject);
                        task.setCron(jsonObject.getString("role"));
                        addJob(task);
                    }
                }
            }

        } catch (SchedulerException e) {
            log.error("任务服务启动异常:" + e.getMessage(), e);
        }
        log.info("-------TaskService init ---------");
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
            log.error("初始化定时任务， 解析任务规则异常:" + e.getMessage(), e);
        } catch (SchedulerException e) {
            log.error("初始化定时任务， 解析任务规则异常:" + e.getMessage(), e);
        }
    }


    /**
     * 更新任务
     *
     * @param task
     * @return
     */
    public boolean updateTask(Task task) {
        try {
            JobKey jobKey = new JobKey(task.getId());
            JobDetail jobDetail = sched.getJobDetail(jobKey);
            if (jobDetail != null) {
                boolean del = sched.deleteJob(jobKey);
                if (del) {
                    log.info("更新定时任务[{}]完成:[{}]", jobDetail.getKey(), task.getObject());
                } else {
                    log.error("更新定时任务[{}]异常:[{}]", jobDetail.getKey(), task.getObject());
                }
                addJob(task);
                return true;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * 发布段子(文字)
     * 段子2.0
     *
     * @param task
     */
    public void publishText(Task task) {
        try {
            long start = System.currentTimeMillis();
            int count = 0;
            Double baseScore = Double.parseDouble(new SimpleDateFormat("yyyyMMddHHmm00").format(new Date()));
            Map<String, Double> map = Maps.newHashMap();
            if (task.getObject().getInteger("textNum") != null) {
                PUBLISH_TEXT_SIZE = task.getObject().getInteger("textNum");
            }
            List<Joke> list = jokeService.getJoke2PublishList(Constants.AUD, 0, PUBLISH_TEXT_SIZE);
            StringBuffer ids = new StringBuffer();
            if (!CollectionUtils.isEmpty(list)) {
                int i = list.size();
                for (Joke j : list) {
                    int id = j.getId();
//                    int weight = j.getWeight();
                    map.put(Integer.toString(id), baseScore + Double.valueOf(i));
                    ids.append(id).append(",");
                    count++;
                    i--;
                }
                jedisCache.zadd(JedisKey.JOKE_CHANNEL + 2, map);
                // 更新已发布状态
                jokeService.updateJoke2PublishStatus(ids.deleteCharAt(ids.lastIndexOf(",")).toString(), 3);


            }
            long end = System.currentTimeMillis();

            log.info("2.0 - 发布段子[{}]条, 耗时[{}], cron:{}", count, FormatUtil.getTimeStr(end - start), task.getObject());


            //发送邮件
            sendEmailByPub(count);


        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 发布趣图
     * 段子2.0
     *
     * @param task
     */
    public void publishImage(Task task) {
        long start = System.currentTimeMillis();
        try {
            int imgCount = 0, gifCount = 0;
            Double baseScore = Double.parseDouble(new SimpleDateFormat("yyyyMMddHHmm00").format(new Date()));
            Map<String, Double> map = Maps.newHashMap();
            if (task.getObject().getInteger("gifNum") != null) {
                PUBLISH_GIF_SIZE = task.getObject().getInteger("gifNum");
            }
            if (task.getObject().getInteger("imageNum") != null) {
                PUBLISH_IMG_SIZE = task.getObject().getInteger("imageNum");
            }

            if (task.getObject().getInteger("gifWeight") != null) {
                GIF_WEIGHT = task.getObject().getInteger("gifWeight");
            }

            if (task.getObject().getInteger("imageWeight") != null) {
                IMG_WEIGHT = task.getObject().getInteger("imageWeight");
            }
            List<Joke> imgList = jokeService.getJoke2PublishList(Constants.AUD, 1, PUBLISH_IMG_SIZE);
            List<Joke> gifList = jokeService.getJoke2PublishList(Constants.AUD, 2, PUBLISH_GIF_SIZE);
            StringBuffer ids = new StringBuffer();
            int imgSize = 0;
            if (!CollectionUtils.isEmpty(imgList)) {
                imgSize = imgList.size();
            }
            int gifSize = 0;
            if (!CollectionUtils.isEmpty(gifList)) {
                gifSize = gifList.size();
            }
            int img = IMG_WEIGHT;
            int gif = GIF_WEIGHT;
            int total = imgSize + gifSize;
            log.info("准备发布趣图[{}]条(img:{}, gif:{})", total, imgSize, gifSize);
            for (int i = total; i > 0; i--) {
//			按照段子审核时间进行权重
                if (img > 0) {
                    if (imgSize > 0) {
                        String id = imgList.get(imgSize - 1).getId().toString();
//                        int weight = imgList.get(imgSize - 1).getWeight();
                        map.put(id, baseScore + Double.valueOf(i));
                        ids.append(id).append(",");
                        imgCount++;
                    }
                    imgSize--;
                    img--;
                }
                if (gif > 0) { // 频道权重是否用完
                    if (gifSize > 0) {
                        String id = gifList.get(gifSize - 1).getId().toString();
//                        int weight = gifList.get(gifSize - 1).getWeight();
                        map.put(id, baseScore + Double.valueOf(i));
                        ids.append(id).append(",");
                        gifCount++;
                    }
                    gifSize--;
                    gif--;
                }
                if (gif == 0 && img == 0) {
                    gif = GIF_WEIGHT;
                    img = IMG_WEIGHT;
                }
                //			map中数量与总数对应上说明数据记录完成;
                if (map.size() == total) {
                    break;
                }
            }
            log.debug(JSON.toJSONString(map));
            if (map != null && map.size() > 0) {
                jedisCache.zadd(JedisKey.JOKE_CHANNEL + 1, map);
                // 更新已发布状态
                jokeService.updateJoke2PublishStatus(ids.deleteCharAt(ids.lastIndexOf(",")).toString(), 3);
            }
            long end = System.currentTimeMillis();
            log.info("发布趣图[{}]条(img:{}, gif:{}), 耗时[{}], cron:{}", imgCount + gifCount, imgCount, gifCount, FormatUtil.getTimeStr(end - start), task.getObject());

            //发送邮件
            sendEmailByPub(imgCount, gifCount);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 发布推荐消息
     * 段子2.0
     *
     * @param task
     */
    public void publishRecommend(Task task) {
        try {
            long start = System.currentTimeMillis();
            int imgCount = 0, gifCount = 0, textCount = 0;
            Double baseScore = Double.parseDouble(new SimpleDateFormat("yyyyMMddHHmm00").format(new Date()));
            if (task.getObject().getInteger("gifNum") != null) {
                PUBLISH_GIF_SIZE = task.getObject().getInteger("gifNum");
            }
            if (task.getObject().getInteger("imageNum") != null) {
                PUBLISH_IMG_SIZE = task.getObject().getInteger("imageNum");
            }
            if (task.getObject().getInteger("textNum") != null) {
                PUBLISH_TEXT_SIZE = task.getObject().getInteger("textNum");
            }
            if (task.getObject().getInteger("textWeight") != null) {
                TEXT_WEIGHT = task.getObject().getInteger("textWeight");
            }
            if (task.getObject().getInteger("gifWeight") != null) {
                GIF_WEIGHT = task.getObject().getInteger("gifWeight");
            }
            if (task.getObject().getInteger("imageWeight") != null) {
                IMG_WEIGHT = task.getObject().getInteger("imageWeight");
            }
            Map<String, Double> map = Maps.newHashMap();
            List<Joke> textList = jokeService.getJoke2PublishList(Constants.PUB, 0, PUBLISH_TEXT_SIZE);
            List<Joke> imgList = jokeService.getJoke2PublishList(Constants.PUB, 1, PUBLISH_IMG_SIZE);
            List<Joke> gifList = jokeService.getJoke2PublishList(Constants.PUB, 2, PUBLISH_GIF_SIZE);
            StringBuffer ids = new StringBuffer();
            int imgSize = 0;
            if (!CollectionUtils.isEmpty(imgList)) {
                imgSize = imgList.size();
            }
            int gifSize = 0;
            if (!CollectionUtils.isEmpty(gifList)) {
                gifSize = gifList.size();
            }
            int textSize = 0;
            if (!CollectionUtils.isEmpty(textList)) {
                textSize = textList.size();
            }

            int img = IMG_WEIGHT;
            int gif = GIF_WEIGHT;
            int text = TEXT_WEIGHT;
            int total = imgSize + gifSize + textSize;
            log.info("准备发布推荐[{}]条(img:{}, gif:{}, text:{})", total, imgSize, gifSize, textSize);
            for (int i = total; i > 0; i--) {
//			按照段子审核时间进行权重
                if (text > 0) {
                    if (textSize > 0) {
                        String id = textList.get(textSize - 1).getId().toString();
//                        int weight = textList.get(textSize - 1).getWeight();
                        map.put(id, baseScore + Double.valueOf(i));
                        ids.append(id).append(",");
                        textCount++;
                    }
                    textSize--;
                    text--;
                }
                if (img > 0) {
                    if (imgSize > 0) {
                        String id = imgList.get(imgSize - 1).getId().toString();
//                        int weight = imgList.get(imgSize - 1).getWeight();
                        map.put(id, baseScore + Double.valueOf(i));
                        ids.append(id).append(",");
                        imgCount++;
                    }
                    imgSize--;
                    img--;
                }
                if (gif > 0) { // 频道权重是否用完
                    if (gifSize > 0) {
                        String id = gifList.get(gifSize - 1).getId().toString();
//                        int weight = gifList.get(gifSize - 1).getWeight();
                        map.put(id, baseScore + Double.valueOf(i));
                        ids.append(id).append(",");
                        gifCount++;
                    }
                    gifSize--;
                    gif--;
                }
                if (gif == 0 && img == 0 && text == 0) {
                    gif = GIF_WEIGHT;
                    img = IMG_WEIGHT;
                    text = TEXT_WEIGHT;
                }
                //			map中数量与总数对应上说明数据记录完成;
                if (map.size() == total) {
                    break;
                }
            }

            if (map != null && map.size() > 0) {
                jedisCache.zadd(JedisKey.JOKE_CHANNEL + 3, map);
//            // 更新已发布状态为已推荐
                jokeService.updateJoke2PublishStatus(ids.deleteCharAt(ids.lastIndexOf(",")).toString(), 4);


            }
            long end = System.currentTimeMillis();
            log.info("发布推荐[{}]条(img:{}, gif:{}, text:{}), 耗时[{}], cron:{}", imgCount + gifCount + textCount, imgCount, gifCount, textCount, FormatUtil.getTimeStr(end - start), task.getObject());

            //发送邮件
            sendEmailByPub(imgCount, gifCount, textCount);


        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 根据段子类型，获取段子数:
     * count[0]=未审核
     * count[1]=已审核
     *
     * @param type
     * @return
     */
    private int[] getCount(Integer type) {
        int[] count = new int[2];
        for (int i = 0; i < 2; i++) {
            count[i] = jokeService.getJokeListForCount(type, i);
        }
        return count;

    }

    /**
     * 发送邮件
     *
     * @param args
     */
    private void sendEmailByPub(Integer... args) {

        int[] textAudAndNoCount;
        int[] imgAudAndNoCount;
        int[] gifAudAndNoCount;

        int lth = args.length;

        //邮件内容
        StringBuffer stringBuffer = new StringBuffer();


        if (lth == 2 || lth == 3) {
            //趣图记录(0:未审核  1:审核）
            imgAudAndNoCount = getCount(Constants.JOKE_TYPE_IMG);
            //动图记录(0:未审核  1:审核）
            gifAudAndNoCount = getCount(Constants.JOKE_TYPE_GIF);

            if (lth != 3) {
                stringBuffer.append(MAIL_PEOPLE)
                        .append("\t段子【趣图】频道发布段子信息:\n");
            } else {
                stringBuffer.append(MAIL_PEOPLE)
                        .append("\t段子【推荐】频道发布段子信息:\n");
            }

            stringBuffer.append(appendString(0, args[0], imgAudAndNoCount))
                    .append(appendString(1, args[1], gifAudAndNoCount));
        }

        if (lth == 1 || lth == 3) {
            //段子(0:未审核  1:审核）
            textAudAndNoCount = getCount(Constants.JOKE_TYPE_TEXT);
            if (lth != 3) {
                stringBuffer.append(MAIL_PEOPLE)
                        .append("\t段子【段子】频道发布段子信息:\n");
            }
            stringBuffer.append(appendString(2, args[lth - 1], textAudAndNoCount));


        }

        mailService.sendMail(recipient, cc, MAIL_SUBJECT, stringBuffer.toString());


    }

    /**
     * 拼接字符串
     *
     * @param type     (0:趣图  1:动图 2:段子)
     * @param typeVale
     * @param counts
     * @return
     */
    private String appendString(Integer type, Integer typeVale, int[] counts) {
        StringBuffer sb = new StringBuffer();
        sb.append(PUBTYPE[type]).append(PUBLISH).append(typeVale).append(TIAO)
                .append(APPROVED).append(counts[1]).append(TIAO)
                .append(UNAPPROVED).append(counts[0]).append(TIAO).append(CH);
        return sb.toString();
    }

    @PreDestroy
    public void destroy() {
        try {
            sched.clear();
            sched.shutdown();
        } catch (SchedulerException e) {
            log.error("关闭任务调度器异常:" + e.getMessage(), e);
        }
    }
}