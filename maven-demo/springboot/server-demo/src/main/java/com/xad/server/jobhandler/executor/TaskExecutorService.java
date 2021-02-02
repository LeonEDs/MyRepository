package com.xad.server.jobhandler.executor;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.xad.server.jobhandler.core.BaseTask;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.*;

/**
 * @author xad
 * @version 1.0
 * @date 2021/1/26
 */
public class TaskExecutorService<T extends BaseTask> implements Runnable
{
    private final ThreadPoolExecutor executor;
    private final LinkedBlockingQueue<T> taskQueue = new LinkedBlockingQueue<>(50000);

    public TaskExecutorService()
    {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("tag-executor-service-pool-%d").build();
        this.executor = new ThreadPoolExecutor(100, 100
                , 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(100));
        executor.setRejectedExecutionHandler((task, executor) -> {
            BlockingQueue<Runnable> queue = executor.getQueue();
            try
            {
                queue.put(task);
            }catch (InterruptedException e)
            {
                throw new RejectedExecutionException(e);
            }
        });
        ThreadPoolExecutor consumer = new ThreadPoolExecutor(1, 1, 0
                , TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(1));
        consumer.submit(this);
    }

    @Override
    public void run()
    {
        boolean submitted = true;
        T task = null;
        while (true)
        {
            try
            {
                if (submitted)
                {
                    task = taskQueue.take();
                }

                if (task != null)
                {
                    this.executor.submit(task);
                    submitted = true;
                }
            }catch (Exception e)
            {
                submitted = false;
                try
                {
                    TimeUnit.MILLISECONDS.sleep(10000);
                } catch (Exception ignore) {}
            }
        }
    }

    public void putTask(T task) throws InterruptedException
    {
        this.taskQueue.put(task);
    }

    public void putTaskList(List<T> taskList) throws InterruptedException
    {
        for (T task : taskList)
        {
            this.putTask(task);
        }
    }

    public boolean isFinish()
    {
        return !(this.executor.getActiveCount() > 0);
    }
}
