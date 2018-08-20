package com.github.my.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author liumin
 * @Description jdbc job 串行
 * @date 2018/8/20
 * @copyright www.qguanzi.com Inc. All rights reserved.
 */
@DisallowConcurrentExecution
public class TXDCJob extends BaseJob {


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String serverId = String.valueOf(context.get("serverId"));
        String id = runId();
        System.out.println("******************************TXDCJob Start " + serverId + "  " + id + "  " + now() + "******************************");
        boolean s = true;
        while (s) {
            System.out.println("******************************TXDCJob Runing " + serverId + "  " + id + "  " + now() + "******************************");
            try {
                Thread.sleep(1000L * 30);
            } catch (Exception e) {
            }
        }
    }
}