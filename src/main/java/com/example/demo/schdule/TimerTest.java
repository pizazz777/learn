package com.example.demo.schdule;

import com.example.demo.component.exception.ServiceException;
import com.example.demo.configuration.TimerConfig;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 * @date 2020-07-16 09:11
 */
@Component
public class TimerTest {

    private static final String TEST_JOB = "TEST_JOB";
    private static final String TEST_TRIGGER = "TEST_TRIGGER";

    private TimerConfig timerConfig;

    @Autowired
    public TimerTest(TimerConfig timerConfig) {
        this.timerConfig = timerConfig;
    }

    public void createJob() throws ServiceException {
        try {
            timerConfig.createJob(JobTest.class, null, "0/5 * * * * ?", TEST_TRIGGER, TEST_JOB);
        } catch (SchedulerException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public void pauseJob() throws ServiceException {
        try {
            timerConfig.pauseJob(TEST_JOB, JobTest.class.getName());
        } catch (SchedulerException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public void resumeJob() throws ServiceException {
        try {
            timerConfig.resumeJob(TEST_JOB, JobTest.class.getName());
        } catch (SchedulerException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public void removeTrigger() throws ServiceException {
        try {
            timerConfig.removeTrigger(TEST_TRIGGER, JobTest.class.getName());
        } catch (SchedulerException e) {
            throw new ServiceException(e.getMessage());
        }
    }


}
