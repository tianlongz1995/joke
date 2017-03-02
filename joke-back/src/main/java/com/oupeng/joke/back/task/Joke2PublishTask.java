package com.oupeng.joke.back.task;

import com.oupeng.joke.back.service.TaskService;
import com.oupeng.joke.back.util.FormatUtil;
import com.oupeng.joke.domain.Task;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 段子2.0发布任务
 */
public class Joke2PublishTask implements Job{

	private static final Logger log = LoggerFactory.getLogger(Joke2PublishTask.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		topHandle(context);

	}

	/**
	 * 处理任务 void
	 */
	private void topHandle(JobExecutionContext context) {
        long start = System.currentTimeMillis();
	    try {
	        String type = "";
			Task task = (Task) context.getJobDetail().getJobDataMap().get("task");
			TaskService taskService = (TaskService) context.getJobDetail().getJobDataMap().get("taskService");
			switch (task.getType()){
				case 1: // 发布趣图
					taskService.publishImage(task);
                    type = "趣图";
					break;
				case 2: // 发布文字段子
					taskService.publishText(task);
                    type = "段子";
					break;
				case 3: // 发布推荐数据
					taskService.publishRecommend(task);
                    type = "推荐";
					break;
				default:
					log.error("段子2.0发布任务执行异常, 任务类型无效:" + task.getType());
					break;
			}
            long end = System.currentTimeMillis();
            log.info("段子2.0发布[{}]任务[{}]执行完成, 耗时[{}]:", type, context.getJobDetail().getKey(), FormatUtil.getTimeStr(end - start));
		} catch (Exception e) {
			log.error("段子2.0发布任务执行异常:" + e.getMessage(), e);
		}
	}
}
