package com.github.my;

import com.github.my.job.DCJob;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;

/**
 * 内存中存储job
 */
public class RAMMain {
    public static void main(String[] args) throws Exception {

        SchedulerFactory schedulerFactory = new StdSchedulerFactory();

        Scheduler scheduler = schedulerFactory.getScheduler();

        // 创建任务
        JobDetail jobDetail = JobBuilder.newJob(DCJob.class).withIdentity("dcJob", "jobGroup").build();

        // 创建触发器
        TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger().withIdentity("myTrigger", "triggerGroup");
        Trigger trigger = triggerBuilder.startAt(new Date())
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0/1 * * * ?"))
                .build();
     /*   scheduler.scheduleJob(jobDetail, TriggerBuilder.newTrigger().withIdentity("myTrigger2", "triggerGroup").startAt(new Date())
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0/5 * * * ?"))
                .build());*/
        // 将任务及其触发器放入调度器
        scheduler.scheduleJob(jobDetail, trigger);
        // 调度器开始调度任务
        scheduler.start();


    }
}
