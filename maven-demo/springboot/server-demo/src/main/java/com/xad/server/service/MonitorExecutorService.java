package com.xad.server.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Slf4j
public class MonitorExecutorService<T extends Callable<Boolean>>
{
    private final ExecutorService monitor;

    private final ExecutorService executor;

    private final Map<Future<Boolean>, T> mapTaskResult;

    public MonitorExecutorService(int executeTaskNum)
    {
        this.monitor = Executors.newSingleThreadExecutor();
        this.executor = Executors.newFixedThreadPool(executeTaskNum);
        this.mapTaskResult = new HashMap<>();
    }

    public void addTaskList(List<T> taskList) throws InterruptedException
    {
        if (CollectionUtils.isNotEmpty(taskList))
        {
            taskList.forEach(task -> {
                if (task != null)
                {
                    Future<Boolean> result = executor.submit(task);
                    mapTaskResult.put(result, task);
                }
            });
        }

        // 检测线程池中所有线程是否正常结束
        if (CollectionUtils.isNotEmpty(mapTaskResult.keySet()))
        {
            monitor.submit(() -> {
                boolean isOver;
                boolean isSuccess = true;

                do
                {
                    try
                    {
                        TimeUnit.SECONDS.sleep(3); // 每隔一段时间检查一次执行结果
                    }catch (Exception ignore){}

                    isOver = true; //任务全部结束后退出检查
                    for (Map.Entry<Future<Boolean>, T> map : mapTaskResult.entrySet())
                    {
                        if (map != null && map.getKey() != null)
                        {
                            Future<Boolean> future = map.getKey();
                            if (future.isDone())
                            {
                                try
                                {
                                    Boolean result = future.get();
                                    if (result == null || !result)
                                        isSuccess = false;

                                } catch (Exception e)
                                {
                                    isSuccess = false;
                                }
                            } else
                            {
                                isOver = false;
                            }
                        }
                    }
                }while (!isOver);

                executor.shutdown();
                monitor.shutdown();
            });

            shutdown();
        }
    }

    public void shutdown()
    {

        System.out.println("##################\n"+executor.isShutdown() +" "+monitor.isShutdown());
        try
        {
            if (!executor.awaitTermination(20, TimeUnit.SECONDS))
            {
                System.out.println("######shutdownNow*******");
                executor.shutdownNow();
                monitor.shutdownNow();
            }
        }catch (Exception e)
        {
            executor.shutdownNow();
            monitor.shutdownNow();
        }
        System.out.println("******************\n"+executor.isShutdown() +" "+ monitor.isShutdown());
    }
}
