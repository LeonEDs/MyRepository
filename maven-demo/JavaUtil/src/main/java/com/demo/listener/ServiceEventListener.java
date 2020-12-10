package com.demo.listener;

import com.demo.listener.event.BaseEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class ServiceEventListener
{
    @Async
    @EventListener
    public void listener(BaseEvent<?> event) throws Exception
    {
        System.out.println("listener start...." + event.getData());
        TimeUnit.SECONDS.sleep(2);
        System.out.println(event.getData());
        TimeUnit.SECONDS.sleep(2);
        System.out.println("listener start...." + event.getData());
    }
}
