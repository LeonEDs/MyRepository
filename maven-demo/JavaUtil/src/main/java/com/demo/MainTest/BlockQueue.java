package com.demo.MainTest;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class BlockQueue
{
    private static final LinkedBlockingQueue<Integer> tagInstanceQueue = new LinkedBlockingQueue<>(3);

    public static void putTagInstance(final List<Integer> instances) throws Exception
    {
        if (CollectionUtils.isNotEmpty(instances))
        {

            System.out.println("!!!!");

            System.out.println("2222");
        }
    }

    public static Integer pollTagInstance()
    {
        return tagInstanceQueue.poll();
    }
}
