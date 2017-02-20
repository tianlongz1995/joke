//package com.oupeng.joke.back.task;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//
//import com.oupeng.joke.domain.SourceCrawl;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.util.CollectionUtils;
//
//import com.oupeng.joke.back.service.JokeService;
//import com.oupeng.joke.back.service.SourceService;
//import com.oupeng.joke.domain.JokeVerifyRate;
//
////@Component
//public class SourceMonitorTask {
//
//	private static final Logger logger = LoggerFactory.getLogger(JokeTask.class);
//	/**	一天的毫秒数	*/
//	private static final long ONE_DAY_MILLISECOND = 1000L * 60 * 60 * 24;
//
//	@Autowired
//	private JokeService jokeService;
//	@Autowired
//	private SourceService sourceService;
//
//	/**
//	 * <pre>
//	 * 任务(1)
//	 * 每天凌晨零点生成数据源抓取记录
//	 * </pre>
//	 * */
//	@Scheduled(cron="0 0 0 * * ?")
//	public void insertSourceMonitor(){
//		logger.info("insertSourceMonitor starting...");
//		List<Integer> ids = sourceService.getSourceMonitorIds(1);
//		if(!CollectionUtils.isEmpty(ids)){
//			String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
//			sourceService.insertSourceMonitors(ids, Integer.valueOf(today));
//			logger.info("insertSourceMonitor ids size:[{}] success!", ids.size());
//		}else{
//			logger.error("insertSourceMonitor ids size:[{}] error! ids is empty!", ids);
//		}
//	}
//
//	/**
//	 * <pre>
//	 * 任务(2)
//	 * 更新前一天审核通过率
//	 * </pre>
//	 **/
//	@Scheduled(cron="0 20 0 * * ?")
//	public void updateSourceMonitor(){
//		List<JokeVerifyRate> jokeVerifyRateList = jokeService.getJokeVerifyRate();
//		if(!CollectionUtils.isEmpty(jokeVerifyRateList)){
//			for(JokeVerifyRate jokeVerifyRate : jokeVerifyRateList){
//				sourceService.updateSourceByVerify(jokeVerifyRate);
//			}
//		}
//	}
//
//	/**
//	 * <pre>
//	 * 任务(3)
//	 * 数据源抓取质量统计
//	 * 每天凌晨2点定时统计前一天的数据
//	 * </pre>
//	 */
//	@Scheduled(cron="0 0 2 * * ?")
////	@Scheduled(cron="0 0/1 * * * ?")
//	public void sourceCrawlQualityStat(){
//		logger.debug("SourceCrawlQualityStat start...");
//		long start = System.currentTimeMillis();
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		// 一天前的字符串表示
//		String dayStr = sdf.format(new Date(new Date().getTime() - ONE_DAY_MILLISECOND));
//		String day = dayStr.replaceAll("-", "");
//		List<SourceCrawl> list = sourceService.getSourceMonitorCrawlList(day);
////		抓取统计
//		crawlStat(day, dayStr, list);
//		long s1 = System.currentTimeMillis();
////		审核质量统计
//		qualityStat(day, dayStr, list);
//		long end = System.currentTimeMillis();
//		logger.debug("SourceCrawlQualityStat over time:{}ms, crawlStat:{}ms, qualityStat:{}ms", end - start, s1-start, end-s1);
//	}
//
//	/**
//	 * <pre>
//	 * 抓取统计
//	 * 统计前一天的抓取数据及类型
//	 * </pre>
//	 * @param day
//	 * @param dayStr
//	 */
//	private void crawlStat(String day, String dayStr, List<SourceCrawl> list) {
//		try {
////		存储前一天抓取数据记录
//			int size = sourceService.insertSourceCrawlStat(day, dayStr);
////		更新数据源的最后抓取时间与抓取次数
//			if(!CollectionUtils.isEmpty(list)){
//				for(SourceCrawl sourceCrawl : list){
//					if(sourceCrawl.getLastGrabTime() != null || sourceCrawl.getGrabCount() != null) {
//						sourceService.updateSourceCrawlLastGrabTimeAndGrabCount(sourceCrawl);
//					}
//				}
//			}
//			logger.info("抓取统计存储前一天{}条记录", size);
//		}catch (Exception e){
//			logger.error("crawlStat error:" + e.getMessage(), e);
//		}
//	}
//
//	/**
//	 * 审核质量统计
//	 */
//	private void qualityStat(String day, String dayStr, List<SourceCrawl> list) {
//		try {
////		存储前一天抓取数据记录
//			int size = sourceService.insertSourceQualityStat(day, dayStr);
////		更新数据源审核质量的最后抓取时间
//			if(!CollectionUtils.isEmpty(list)){
//				for(SourceCrawl sourceCrawl : list){
//					if(sourceCrawl.getLastGrabTime() != null){
//						sourceService.updateSourceQualityLastGrabTime(sourceCrawl);
//					}
//				}
//			}
//			logger.info("审核质量统计存储前一天{}条记录", size);
//		}catch (Exception e){
//			logger.error("crawlStat error:" + e.getMessage(), e);
//		}
//	}
//}
