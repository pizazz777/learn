package com.example.demo.configuration;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @author Administrator
 * @date 2020-07-10 17:30
 * @description: quartz定时器配置类
 */
@Configuration
public class TimerConfig {

    private Scheduler scheduler;

    @Autowired
    public TimerConfig(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    /**
     * 调度器绑定触发器及任务
     *
     * @param jobDetail 任务
     * @param trigger   触发器
     * @throws SchedulerException e
     */
    public void buildTimer(JobDetail jobDetail, Trigger trigger) throws SchedulerException {
        scheduler.scheduleJob(jobDetail, trigger);
    }

    /**
     * 删除定时器中的触发器
     *
     * @param triggerName      触发器名称
     * @param triggerGroupName 触发器组
     * @throws SchedulerException e
     */
    public void removeTrigger(String triggerName, String triggerGroupName) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
        // 停止触发器
        scheduler.pauseTrigger(triggerKey);
        // 移除触发器
        scheduler.unscheduleJob(triggerKey);
    }

    public void  createJob(Class<? extends QuartzJobBean> jobClass, CronScheduleBuilder cronScheduleBuilder) {

    }

    /**
     * 删除定时器中的任务
     *
     * @param jobName      任务名称
     * @param jobGroupName 任务组
     * @throws SchedulerException e
     */
    public void removeJob(String jobName, String jobGroupName) throws SchedulerException {
        // 删除任务
        scheduler.deleteJob(JobKey.jobKey(jobName, jobGroupName));
    }

}
