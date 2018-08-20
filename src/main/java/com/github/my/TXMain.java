package com.github.my;

import com.github.my.job.TXJob;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;

/**
 * @author liumin
 * @Description 集群实例，多次运行main方法查看效果
 * @date 2018/8/20
 * @copyright www.qguanzi.com Inc. All rights reserved.
 */
public class TXMain {

    public static void main(String[] args) throws Exception {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        // 创建任务
        JobDetail jobDetail = JobBuilder.newJob(TXJob.class).withIdentity("TXJob", "jobGroup").build();
        // 将任务及其触发器放入调度器
        if (!scheduler.checkExists(jobDetail.getKey())) {
            // 创建触发器
            TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger().withIdentity("TXTrigger", "triggerGroup");
            Trigger trigger = triggerBuilder.startAt(new Date())
                    .withSchedule(CronScheduleBuilder.cronSchedule("0 0/1 * * * ?"))
                    .build();
            scheduler.scheduleJob(jobDetail, trigger);
        }
        scheduler.start();
    }

}
