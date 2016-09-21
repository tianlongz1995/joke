package com.oupeng.joke.back.service;

import com.mchange.v1.db.sql.ConnectionUtils;
import com.oupeng.joke.back.util.FormatUtil;
import com.oupeng.joke.dao.mapper.SourceMapper;
import com.oupeng.joke.domain.JokeVerifyRate;
import com.oupeng.joke.domain.QueryParam;
import com.oupeng.joke.domain.Source;
import com.oupeng.joke.domain.SourceCrawl;
import com.oupeng.joke.domain.statistics.SourceCrawlExport;
import com.oupeng.joke.domain.statistics.SourceQualityExport;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 内容源服务
 * Created by hushuang on 16/7/6.
 */
@Service
public class SourceService {
    private static final Logger logger = LoggerFactory.getLogger(SourceService.class);
    @Autowired
    private SourceMapper sourceMapper;

    /**
     * 获取内容源列表记录总数
     * @param source
     * @return
     */
    public int getSourceListCount(Source source) {
        return sourceMapper.getSourceListCount(source);
    }
    /**
     * 获取内容源列表
     * @param source        内容源
     * @return
     */
    public List<Source> getSourceList(Source source){
        return sourceMapper.getSourceList(source);
    }
    /**
     * 获取内容源列表
     * @return
     */
    public List<Source> getAllSourceList(){
        return sourceMapper.getAllSourceList();
    }
    /**
     * 获取内容源信息
     * @param id 内容源编号
     * @return
     */
    public Source getSourceById(Integer id){
        return sourceMapper.getSourceById(id);
    }

    /**
     * 删除（逻辑删除）
     * @param id
     */
    public void del(Integer id){
        sourceMapper.del(id);
    }

    /**
     * 新增内容源
     * @param name
     * @param url
     * @param status
     */
    public boolean insertSource(String name, String url, Integer status){
        url = url.trim();
        if(url.length() < 12){}
        String urlSuffix = url;
        if(url.endsWith("/")){
            urlSuffix = url.substring(0, url.length() -1);
        }else{
            urlSuffix = url + "/";
        }
        int count = sourceMapper.getSourceUrlCount(url, urlSuffix);
        if(count > 0){
            return false;
        }else{
            Source source = new Source();
            source.setName(name);
            source.setUrl(url);
            source.setStatus(status);
            sourceMapper.insertSource(source);
            return true;
        }
    }

    /**i
     * 更新内容源信息
     * @param id
     * @param name
     * @param url
     * @param status
     */
    public void updateSource(Integer id, String name, String url, Integer status){
        Source source = new Source();
        source.setId(id);
        source.setName(name);
        source.setUrl(url);
        source.setStatus(status);
        sourceMapper.updateSource(source);
    }

    /**
     * 获取数据源抓取信息列表
     * @param monitorDate
     * @param status
     * @return
     */
    public List<SourceCrawl> getSourceMonitorList(String monitorDate, Integer status) {
        List<SourceCrawl> list = null;
        try{
            Integer date = null;
            if(monitorDate != null && monitorDate.length() >0){
                monitorDate = monitorDate.replaceAll("-", "");
            }
            if(StringUtils.isNumeric(monitorDate)){
                date = Integer.valueOf(monitorDate);
            } else {
                date = Integer.valueOf(new SimpleDateFormat("yyyyMMdd").format(new Date()));
            }
           list = sourceMapper.getSourceMonitorList(date, status);
        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }


        return list;
    }

    /**
     * 获取内容源编号列表
     * @param status
     * @return
     */
    public List<Integer> getSourceMonitorIds(Integer status) {
        return sourceMapper.getSourceMonitorIds(status);
    }

    /**
     * 插入内容源监控记录
     * @param ids
     * @param today
     */
    public void insertSourceMonitors(List<Integer> ids, Integer today) {
        try{
            sourceMapper.insertSourceMonitors(today, ids);
        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }
    }
    
    public void updateSourceByVerify(JokeVerifyRate jokeVerifyRate){
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(new Date());
    	calendar.add(Calendar.DATE, -1);
    	Integer day = Integer.valueOf(new SimpleDateFormat("yyyyMMdd").format(calendar.getTime()));
    	Double rate = FormatUtil.getRoundHalfUp4Double(jokeVerifyRate.getValidNum(), (jokeVerifyRate.getValidNum() + jokeVerifyRate.getInValidNum()));
    	sourceMapper.updateSourceByVerify(jokeVerifyRate.getSoureId(), day, rate);
    }

    /**
     * 获取内容源审核质量统计总数
     * @param queryParam
     * @return
     */
    public int getQualityListCount(QueryParam queryParam) {
        if(queryParam.getSourceType() == 0){
            return sourceMapper.getQualityListCount(queryParam);
        }else{
            return sourceMapper.getQualityListTotalCount(queryParam);
        }
    }

    /**
     * 获取内容源审核质量统计记录
     * @param queryParam
     * @return
     */
    public List<SourceCrawl> getQualityList(QueryParam queryParam) {
        if(queryParam.getSourceType() == 0){
            return sourceMapper.getQualityList(queryParam);
        }else{
            return sourceMapper.getQualityTotalList(queryParam);
        }
    }

    /**
     * 获取内容源抓取统计记录总数
     * @param queryParam
     * @return
     */
    public int getSourceCrawlListCount(QueryParam queryParam) {
        if(queryParam.getSourceType() == 0){
            return sourceMapper.getSourceCrawlListCount(queryParam);
        }else{
            return sourceMapper.getSourceCrawlTotalListCount(queryParam);
        }
    }
    /**
     * 获取内容源抓取统计记录
     * @param queryParam
     * @return
     */
    public List<SourceCrawl> getSourceCrawlList(QueryParam queryParam) {
        if(queryParam.getSourceType() == 0){
            return sourceMapper.getSourceCrawlList(queryParam);
        }else{
            return sourceMapper.getSourceCrawlTotalList(queryParam);
        }
    }

    /**
     * 写入前一天数据源抓取统计
     * @param day
     * @param dayStr
     */
    public int insertSourceCrawlStat(String day, String dayStr) {
        return sourceMapper.insertSourceCrawlStat(day, dayStr);
    }

    /**
     * 获取数据源抓取记录列表
     * @param day
     * @return
     */
    public List<SourceCrawl> getSourceMonitorCrawlList(String day) {
        return sourceMapper.getSourceMonitorCrawlList(day);
    }

    /**
     * 更新数据源的最后抓取时间与抓取次数
     * @param sourceCrawl
     */
    public void updateSourceCrawlLastGrabTimeAndGrabCount(SourceCrawl sourceCrawl) {
        sourceMapper.updateSourceCrawlLastGrabTimeAndGrabCount(sourceCrawl);
    }

    /**
     *  写入前一天数据源审核统计
     * @param day
     * @param dayStr
     * @return
     */
    public int insertSourceQualityStat(String day, String dayStr) {
        return sourceMapper.insertSourceQualityStat(day, dayStr);
    }

    /**
     * 更新数据源审核质量的最后抓取时间
     * @param sourceCrawl
     */
    public void updateSourceQualityLastGrabTime(SourceCrawl sourceCrawl) {
        sourceMapper.updateSourceQualityLastGrabTime(sourceCrawl);
    }

    /**
     * 获取数据源抓取报告记录列表
     * @param  queryParam
     * @return
     */
    public List<SourceCrawlExport> getSourceCrawlExport(QueryParam queryParam) {
        if(queryParam.getSourceType() == 0){
//            按格式分类查询
            return sourceMapper.getSourceCrawlExport(queryParam);
        } else {
//            按数据源汇总查询 - 默认方式
            return sourceMapper.getSourceCrawlTotalExport(queryParam);
        }
    }

    /**
     * 数据源审核质量统计报告列表
     * @param queryParam
     * @return
     */
    public List<SourceQualityExport> getSourceQualityExport(QueryParam queryParam) {
        List<SourceQualityExport> list;
        if(queryParam.getSourceType() == 0){
//            按格式分类查询
            list = sourceMapper.getSourceQualityExport(queryParam);
        } else {
//            按数据源汇总查询 - 默认方式
            list = sourceMapper.getSourceQualityTotalExport(queryParam);
        }
        if(!CollectionUtils.isEmpty(list)){
            for(SourceQualityExport s : list){
                s.setVerifyRate(FormatUtil.getFormat100(Integer.valueOf(s.getPassed()), Integer.valueOf(s.getTotal())));
            }
        }
        return list;
    }

}
