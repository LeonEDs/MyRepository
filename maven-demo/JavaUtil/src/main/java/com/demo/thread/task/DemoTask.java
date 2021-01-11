package com.demo.thread.task;

import com.demo.MainTest.Main;
import com.demo.thread.pool.MonitorExecutorService;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@AllArgsConstructor
@Data
public class DemoTask implements Callable<Boolean>
{
    long key;

    @Override
    public Boolean call() throws Exception
    {
        MonitorExecutorService<BooleanTask> executorService = new MonitorExecutorService<>(1);
        List<BooleanTask> taskList = new ArrayList<>();

        for (int j = 1; j <= 10; j++)
        {
            taskList.add(new BooleanTask(key));
        }

        Main.addCount(key, taskList.size());
        executorService.addTaskList(taskList);
        return true;
    }
}
