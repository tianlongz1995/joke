package com.oupeng.joke.domain;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;

/**
 * Created by hushuang on 2016/11/15.
 */
public class JobInfo {
    /** 任务明细    */
    private JobDetail jobDetail;
    /** 任务触发器   */
    private CronTrigger cronTrigger;

    /**
     * @return the 任务明细
     */
    public JobDetail getJobDetail() {
        return jobDetail;
    }
    /**
     * @param jobDetail 任务明细
     */
    public void setJobDetail(JobDetail jobDetail) {
        this.jobDetail = jobDetail;
    }
    /**
     * @return the 任务触发器
     */
    public CronTrigger getCronTrigger() {
        return cronTrigger;
    }
    /**
     * @param cronTrigger
     */
    public void setCronTrigger(CronTrigger cronTrigger) {
        this.cronTrigger = cronTrigger;
    }
}
