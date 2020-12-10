package com.demo.listener.event;

import org.springframework.context.ApplicationEvent;

public class BaseEvent<T> extends ApplicationEvent
{
    private T data;

    public BaseEvent(Object source, T data)
    {
        super(source);
        this.data = data;
    }

    public T getData()
    {
        return this.data;
    }

    public void setData(T data)
    {
        this.data = data;
    }
}
