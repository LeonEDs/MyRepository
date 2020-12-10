package com.demo.thread.task;

import com.demo.thread.pool.MonitorExecutorService;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

@AllArgsConstructor
@Data
public class DemoTask implements Callable<Boolean>
{
    String message;

    @Override
    public Boolean call() throws Exception
    {
        MonitorExecutorService<BooleanTask> executorService = new MonitorExecutorService<>(1);
        List<BooleanTask> taskList = new ArrayList<>();
        for (int i = 1; i < 100; i ++)
        {
            taskList.add(new BooleanTask(message + " NO." + i));
        }

        Map<String, Object> param = new HashMap<>();
        param.put("message", message);
        executorService.setLogParam(param).addTaskList(taskList);
        return true;
    }
}
