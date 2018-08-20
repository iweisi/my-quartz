package com.github.my;

import com.github.my.job.TXJob;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;

/**
 * @author liumin
 * @Description 集群实例，多次运行main方法查看效果
 * 1.数据库悲观锁实现原理
 * 每次任务执行只有一个实例取得执行权限,基于qrtz_locks表，联合主键SCHED_NAME（实例名称），LOCK_NAME(锁名称)
 * 基于DB for update实现 核心类StdRowLockSemaphore
 * 需要设置事务手动提交,获取锁的时候先for update查询
 * 有记录则返回说明已经获取到锁，没有则新增然后返回
 * 然后把锁名称(TRIGGER_ACCESS)绑定到ThreadLocal，然后执行回调(获取并更新触发器状态)
 * 然后提交,最后关闭连接，把锁名称从ThreadLocal移除
 * @date 2018/8/20
 * @copyright www.qguanzi.com Inc. All rights reserved.
 */
public class TXMain {

    public static void main(String[] args) throws Exception {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        JobDetail jobDetail = JobBuilder.newJob(TXJob.class).withIdentity("TXJob", "jobGroup").build();
        if (!scheduler.checkExists(jobDetail.getKey())) {
            TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger().withIdentity("TXTrigger", "triggerGroup");
            Trigger trigger = triggerBuilder.startAt(new Date())
                    .withSchedule(CronScheduleBuilder.cronSchedule("0 0/1 * * * ?"))
                    .build();
            scheduler.scheduleJob(jobDetail, trigger);
        }
        scheduler.start();
    }

}
