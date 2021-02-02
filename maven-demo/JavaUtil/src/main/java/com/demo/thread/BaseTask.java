package com.demo.thread;

import lombok.Data;

/**
 * 标签打标任务.
 *
 * 服务基础配置.
 * @version 1.0
 * @author xad
 * @date 2020/12/24 0024
 */
@Data
public abstract class BaseTask implements Runnable
{
    private long partStartId;

    private long partEndId;

    private String taskType;

    private String missionCode;

    private String parentCode;
    /**
     * constructor.
     */
    public BaseTask(long statId, long endId, String taskType, String missionCode, String parentCode)
    {
        this.partStartId = statId;
        this.partEndId = endId;
        this.taskType = taskType;
        this.missionCode = missionCode;
        this.parentCode = parentCode;
    }

}
