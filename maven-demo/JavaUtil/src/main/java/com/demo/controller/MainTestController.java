package com.demo.controller;

import com.demo.listener.event.BaseEvent;
import com.demo.thread.task.EventTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/main")
public class MainTestController
{
    @Autowired
    ApplicationEventPublisher publisher;

    @GetMapping("/taskSendEvent")
    public String taskSendEvent()
    {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        EventTask<String> task1 = new EventTask<>(publisher, new BaseEvent<>(this, "task_1"));
        EventTask<String> task2 = new EventTask<>(publisher, new BaseEvent<>(this, "task_2"));
        EventTask<String> task3 = new EventTask<>(publisher, new BaseEvent<>(this, "task_3"));
        executorService.submit(task1);
        executorService.submit(task2);
        executorService.submit(task3);
        return "success";
    }
}
