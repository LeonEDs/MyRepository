package com.demo.MainTest;

import com.demo.thread.pool.MonitorExecutorService;
import com.demo.thread.task.DemoTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main
{

    public static void main(String[] args) throws Exception
    {
        MonitorExecutorService<DemoTask> executorService = new MonitorExecutorService<>(2);
        List<DemoTask> taskList = new ArrayList<>();
        taskList.add(new DemoTask("AAA"));
        taskList.add(new DemoTask("BBB"));

        Map<String, Object> param = new HashMap<>();
        param.put("message", "ABAB");
        executorService.setLogParam(param).addTaskList(taskList);
    }
}
