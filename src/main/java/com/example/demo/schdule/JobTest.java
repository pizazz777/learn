package com.example.demo.schdule;

import com.example.demo.util.time.DateConst;
import com.example.demo.util.time.DateUtil;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.time.LocalDateTime;

/**
 * @author Administrator
 * @date 2020-07-16 09:11
 */
public class JobTest extends QuartzJobBean {

    /**
     * Execute the actual job. The job data map will already have been
     * applied as bean property values by execute. The contract is
     * exactly the same as for the standard Quartz execute method.
     *
     * @param context context
     * @see #execute
     */
    @Override
    protected void executeInternal(JobExecutionContext context) {
        // 如果在设置定时任务的时候,设置了数据,可以从这儿取出来
//        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
//        Object object = jobDataMap.get("");
        System.out.println("执行quartz定时任务!  ==== " + DateUtil.localDateTimeToString(DateConst.DEFAULT_DATE_TIME, LocalDateTime.now()));
    }


}
