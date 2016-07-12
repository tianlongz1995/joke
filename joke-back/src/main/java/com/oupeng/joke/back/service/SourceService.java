package com.oupeng.joke.back.service;

import com.oupeng.joke.dao.mapper.SourceMapper;
import com.oupeng.joke.domain.Source;
import com.oupeng.joke.domain.SourceCrawl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
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
     * @param source
     */
    public void insertSource(Source source){
        sourceMapper.insertSource(source);
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
        Integer date = null;
        if(monitorDate != null && monitorDate.length() >0){
            monitorDate = monitorDate.replaceAll("-", "");
        }
        if(StringUtils.isNumeric(monitorDate)){
            date = Integer.valueOf(monitorDate);
        } else {
            date = Integer.valueOf(new SimpleDateFormat("yyyyMMdd").format(new Date()));
        }
        return sourceMapper.getSourceMonitorList(date, status);
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
}
