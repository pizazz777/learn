package com.example.demo.configuration;

import com.example.demo.util.time.DateUtil;
import lombok.NonNull;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author Administrator
 * @date 2020-07-10 17:30
 * @description: quartz定时器配置类
 * 单个触发器只能单个任务，单个任务可以多个触发器触du发
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
        // 移除触发器和对应的任务
        scheduler.unscheduleJob(triggerKey);
    }

    /**
     * 创建任务
     *
     * @param clz         job类
     * @param jobDataMap  定时器附带数据,没有传null
     * @param cron        定时任务表达式
     * @param triggerName 触发器名称
     * @param jobName     任务名称
     * @throws SchedulerException e
     */
    public void createJob(Class<? extends QuartzJobBean> clz, JobDataMap jobDataMap, @NonNull String cron, String triggerName, String jobName) throws SchedulerException {
        // 任务类
        JobDetail jobDetail = JobBuilder.newJob(clz)
                .setJobData(Objects.nonNull(jobDataMap) ? jobDataMap : new JobDataMap())
                .withIdentity(jobName, clz.getName())
                .build();
        /*
         * .withMisfireHandlingInstructionDoNothing(): 忽略调度暂停过程中没有执行的调度,以正常Cron表达式执行
         * .withMisfireHandlingInstructionFireAndProceed(): 以当前时间为触发时间立刻触发一次执行,然后按照Cron频率执行
         * .withMisfireHandlingInstructionIgnoreMisfires(): 以停止的第一个触发时间开始,弥补所有调度,当下一次触发时间大于当前时间,恢复按照正常Cron频率执行
         */
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cron).withMisfireHandlingInstructionDoNothing();
        Trigger trigger = TriggerBuilder.newTrigger()
                // 触发器名称 + 组名称
                .withIdentity(triggerName, clz.getName())
                // 开始于
                .startAt(DateUtil.localDateTimeToDate(LocalDateTime.now()))
                // 表达式
                .withSchedule(cronScheduleBuilder)
                .build();
        // 将触发器和任务绑定
        scheduler.scheduleJob(jobDetail, trigger);
        // 如果没有设置开始于时间,可以在这儿设置马上执行
        // scheduler.start();
    }

    /**
     * 停止任务
     *
     * @param jobName      任务名称
     * @param jobGroupName 任务组
     * @throws SchedulerException e
     */
    public void pauseJob(String jobName, String jobGroupName) throws SchedulerException {
        scheduler.pauseJob(JobKey.jobKey(jobName, jobGroupName));
    }

    /**
     * 恢复任务
     *
     * @param jobName      任务名称
     * @param jobGroupName 任务组
     * @throws SchedulerException e
     */
    public void resumeJob(String jobName, String jobGroupName) throws SchedulerException {
        scheduler.resumeJob(JobKey.jobKey(jobName, jobGroupName));
    }

    /**
     * 删除任务
     *
     * @param jobName      任务名称
     * @param jobGroupName 任务组
     * @throws SchedulerException e
     */
    public void deleteJob(String jobName, String jobGroupName) throws SchedulerException {
        scheduler.deleteJob(JobKey.jobKey(jobName, jobGroupName));
    }

}
