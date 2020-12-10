package com.demo.thread.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@Slf4j
@AllArgsConstructor
@Data
public class BooleanTask implements Callable<Boolean>
{
    private String message;

    @Override
    public Boolean call() throws Exception
    {
        boolean result = false;
        try
        {
            System.out.println(message);
            TimeUnit.MILLISECONDS.sleep(100);
            result = true;
        }catch (Exception e)
        {
            log.error(">>>", e);
        }
        return result;
    }
}
