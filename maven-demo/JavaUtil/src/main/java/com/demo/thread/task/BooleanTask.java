package com.demo.thread.task;

import com.demo.MainTest.Main;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@Slf4j
@AllArgsConstructor
@Data
public class BooleanTask implements Callable<Boolean>
{
    private Long key;

    @Override
    public Boolean call() throws Exception
    {
        boolean result = false;
        try
        {
            TimeUnit.MILLISECONDS.sleep(1000);
            Random random = new Random();
            if (random.nextInt(3) == 1)
            {
                throw new RuntimeException("ERRRRRRRRRRRRROR!");
            }
            result = true;
        }catch (Exception e)
        {
//            log.error(">>>", e);
        }
        System.out.println(key + " count: "+ Main.getCount(key));
        Main.minusCount(key);
        return result;
    }
}
