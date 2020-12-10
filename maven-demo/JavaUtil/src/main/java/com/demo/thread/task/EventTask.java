package com.demo.thread.task;

import com.demo.listener.event.BaseEvent;
import org.springframework.context.ApplicationEventPublisher;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class EventTask<T> implements Callable<Boolean>
{
    ApplicationEventPublisher publisher;

    BaseEvent<T> event;

    public EventTask(ApplicationEventPublisher publisher, BaseEvent<T> event)
    {
        this.publisher = publisher;
        this.event = event;
    }

    @Override
    public Boolean call() throws Exception
    {
        TimeUnit.SECONDS.sleep(1);
        publisher.publishEvent(event);
        TimeUnit.SECONDS.sleep(1);
        return true;
    }
}