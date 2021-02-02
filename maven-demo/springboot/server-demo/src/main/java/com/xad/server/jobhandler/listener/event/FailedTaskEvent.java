package com.xad.server.jobhandler.listener.event;

/**
 * 失败任务事件.
 *
 * @version 1.0
 * @author xad
 * @date 2020/12/24 0024
 */
public class FailedTaskEvent<T> extends BaseEvent<T>
{
    /**
     * constructor.
     */
    public FailedTaskEvent(Object source, T logDto)
    {
        super(source, logDto);
    }
}
