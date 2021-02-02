package com.demo.thread;

import lombok.extern.slf4j.Slf4j;

/**
 * 标签打标任务，串行，小任务.
 * 小任务最大容量1k.
 *
 * @version 1.0
 * @author xad
 * @date 2020/12/24 0024
 */
@Slf4j
public class SerialTask extends BaseTask
{
    public SerialTask(Long startId, Long endId, String missionCode, String parentCode)
    {
        super(startId, endId, "SERIAL", missionCode, parentCode);
    }


    @Override
    public void run()
    {
        log.info("Thread - SerialTask - {} - >>> starting...", this.getMissionCode());
        boolean executeResult = false;

        try
        {
            executeResult = true;

            log.info("Thread - SerialTask - {} - >>> success", this.getMissionCode());
        }catch (Exception e)
        {

        }
    }
}
