package com.github.my.job;

import org.quartz.Job;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author liumin
 * @Description
 * @date 2018/8/20
 * @copyright www.qguanzi.com Inc. All rights reserved.
 */
public abstract class BaseJob implements Job {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

    /**
     * 执行时间
     * @return
     */
    public String now() {
        try {
            return sdf.format(new Date());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 当前执行的id
     * @return
     */
    public String runId() {
        return UUID.randomUUID().toString();
    }
}
