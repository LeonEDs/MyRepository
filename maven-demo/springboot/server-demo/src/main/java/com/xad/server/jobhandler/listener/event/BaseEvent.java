package com.xad.server.jobhandler.listener.event;

import org.springframework.context.ApplicationEvent;

/**
 * 基础事件定义.
 *
 * @version 1.0
 * @author xad
 * @date 2020/12/24 0024
 */
public class BaseEvent<T> extends ApplicationEvent
{
    T data;

    /**
     * constructor.
     */
    public BaseEvent(Object source, T data)
    {
        super(source);
        this.data = data;
    }

    public T getData()
    {
        return data;
    }

    public void setData(T data)
    {
        this.data = data;
    }
}
