package com.xad.server.jobhandler.listener.event;

/**
 * 成功任务事件.
 *
 * @version 1.0
 * @author xad
 * @date 2020/12/24 0024
 */
public class SuccessTaskEvent<T> extends BaseEvent<T>
{
    /**
     * constructor.
     */
    public SuccessTaskEvent(Object source, T logDto)
    {
        super(source, logDto);
    }
}
