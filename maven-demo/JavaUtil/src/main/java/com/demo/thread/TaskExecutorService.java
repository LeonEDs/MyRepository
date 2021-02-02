package com.demo.thread;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @author xad
 * @version 1.0
 * @date 2021/1/19
 */
@Slf4j
public class TaskExecutorService<T extends Runnable> implements Runnable
{
    private final ThreadPoolExecutor executor;

    private final ThreadPoolExecutor consumer;

    private final LinkedBlockingQueue<T> blockingQueue = new LinkedBlockingQueue<>(10000);

    /**
     * constructor.
     */
    public TaskExecutorService()
    {
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("tag-executor-pool-%d").build();

        this.executor = new ThreadPoolExecutor(100, 100,
                10, TimeUnit.SECONDS, new LinkedBlockingQueue<>(1000), threadFactory);
        this.consumer = new ThreadPoolExecutor(1, 1,
                0, TimeUnit.SECONDS, new LinkedBlockingQueue<>(1), threadFactory);

        this.consumer.submit(() -> this);
    }

    @Override
    public void run()
    {
        boolean isSubmit = true;
        T task = null;

        while (true)
        {
            try
            {
                if (isSubmit)
                {
                    task = this.blockingQueue.take();
                }

                if (task != null)
                {
                    this.executor.submit(task);
                    isSubmit = true;
                }
            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
            catch (RejectedExecutionException e)
            {
                isSubmit = false;
            }
        }
    }

    public void putTask(T task) throws InterruptedException
    {
        this.blockingQueue.put(task);
    }
}
