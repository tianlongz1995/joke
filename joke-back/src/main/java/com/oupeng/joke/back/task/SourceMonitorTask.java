package com.oupeng.joke.back.task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.oupeng.joke.back.service.JokeService;
import com.oupeng.joke.back.service.SourceService;
import com.oupeng.joke.domain.JokeVerifyRate;

@Component
public class SourceMonitorTask {
	
	private static final Logger logger = LoggerFactory.getLogger(JokeTask.class);
	
	@Autowired
	private JokeService jokeService;
	@Autowired
	private SourceService sourceService;
	
	/**
	 * 每天凌晨零点生成数据源抓取记录
	 * */
	@Scheduled(cron="0 0 0 * * ?")
	public void insertSourceMonitor(){
		logger.info("insertSourceMonitor starting...");
		List<Integer> ids = sourceService.getSourceMonitorIds(1);
		if(!CollectionUtils.isEmpty(ids)){
			String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
			sourceService.insertSourceMonitors(ids, Integer.valueOf(today));
			logger.info("insertSourceMonitor ids size:[{}] success!", ids.size());
		}else{
			logger.error("insertSourceMonitor ids size:[{}] error! ids is empty!", ids);
		}
	}
	
	/**
	 * 更新
	 * */
	@Scheduled(cron="0 20 0 * * ?")
	public void updateSourceMonitor(){
		List<JokeVerifyRate> jokeVerifyRateList = jokeService.getJokeVerifyRate();
		if(!CollectionUtils.isEmpty(jokeVerifyRateList)){
			for(JokeVerifyRate jokeVerifyRate : jokeVerifyRateList){
				sourceService.updateSourceByVerify(jokeVerifyRate);
			}
		}
	}
}
