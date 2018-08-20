package com.github.my.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author liumin
 * @Description jdbc job 并发
 * @date 2018/8/20
 * @copyright www.qguanzi.com Inc. All rights reserved.
 */
public class TXJob extends BaseJob {


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String id = runId();
        System.out.println("******************************TXJob Start " + id + "  " + now() + "******************************");
    }
}