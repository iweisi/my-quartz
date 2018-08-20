package com.github.my.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author liumin
 * @Description DisallowConcurrentExecution 串行执行job
 * @date 2018/8/20
 * @copyright www.qguanzi.com Inc. All rights reserved.
 */
@DisallowConcurrentExecution
public class DCJob extends BaseJob {


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String id = runId();
        System.out.println("******************************DCJob Start " + id + "******************************");
        boolean s = true;
        while (s) {
            System.out.println("******************************DCJob Runing " + id + "   " + now() + "******************************");
            try {
                Thread.sleep(1000L * 30);
            } catch (Exception e) {
            }
        }
    }
}

