package com.xad.server.controller;

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

        return "success";
    }
}
