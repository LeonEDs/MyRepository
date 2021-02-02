package com.xad.server.jobhandler.core;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 标签打标任务.
 *
 * 服务基础配置.
 * @version 1.0
 * @author xad
 * @date 2020/12/24 0024
 */
@Data
@NoArgsConstructor
public abstract class BaseTask implements Runnable
{
    private long partStartId;

    private long partEndId;

    private String taskType;

    private String missionCode;

    private String parentCode;

    public BaseTask(long statId, long endId, String taskType, String missionCode, String parentCode)
    {
        this.partStartId = statId;
        this.partEndId = endId;
        this.taskType = taskType;
        this.missionCode = missionCode;
        this.parentCode = parentCode;
    }
}
