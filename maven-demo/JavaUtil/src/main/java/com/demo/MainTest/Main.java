package com.demo.MainTest;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class Main
{

    public static void main(String[] args) throws Exception
    {
        String value = "\n";
        String patter = "[\\s\\S]*\\S+[\\s\\S]*";
        Pattern p = Pattern.compile(patter);
        Matcher m = p.matcher(value);

        System.out.println(patter);
        if (!m.matches())
        {
            System.out.println("error Data");
        }
        System.out.println(System.currentTimeMillis());

    }

    private static final Map<Long, Map<String, Object>> missionCount = new HashMap<>();
    private static final String TIMESTAMP_KEY = "TIMESTAMP_KEY";
    private static final String COUNT_KEY = "COUNT_KEY";

    public static synchronized void addCount(Long key, int count)
    {
        Map<String, Object> mapParam = missionCount.get(key);

        if (mapParam == null)
        {
            mapParam = initMap();
        }

        int num = (int) mapParam.get(COUNT_KEY) + count;
        mapParam.put(COUNT_KEY, num);
        missionCount.put(key, mapParam);

    }

    public static synchronized void minusCount(Long key)
    {
        Map<String, Object> mapParam = missionCount.get(key);

        if (mapParam == null)
        {
            return;
        } else
        {
            int count = (int) mapParam.get(COUNT_KEY) - 1;
            mapParam.put(COUNT_KEY, count);
            missionCount.put(key, mapParam);
        }

        clearUnLock();
    }

    public static synchronized boolean isFinished(Long key)
    {
        Map<String, Object> mapParam = missionCount.get(key);

        if (mapParam == null)
        {
            return true;
        } else
        {
            int count = (int) mapParam.get(COUNT_KEY);
            return count <= 0;
        }
    }

    public static synchronized int getCount(Long key)
    {
        Map<String, Object> mapParam = missionCount.get(key);

        if (mapParam == null)
        {
            return 0;
        } else
        {
            return (int) mapParam.get(COUNT_KEY);
        }
    }

    private static synchronized Map<String, Object> initMap()
    {
        Map<String, Object> init = new HashMap<>();
        init.put(TIMESTAMP_KEY, LocalDateTime.now(ZoneId.systemDefault()));
        init.put(COUNT_KEY, 0);
        return init;
    }

    private static synchronized void clearUnLock()
    {
        if (CollectionUtils.isNotEmpty(missionCount.entrySet()))
        {
            List<Map.Entry<Long, Map<String, Object>>> tempList = new LinkedList<>(missionCount.entrySet());
            Iterator<Map.Entry<Long, Map<String, Object>>> iterator = tempList.listIterator();
            Map.Entry<Long, Map<String, Object>> entry;
            while (iterator.hasNext())
            {
                entry = iterator.next();
                Long key = entry.getKey();
                Map<String, Object> value = entry.getValue();
                if (isTimeout(value.get(TIMESTAMP_KEY)))
                {
                    System.out.println("isTimeout");
                    System.out.println("TaskCountLock ID is cleared: " + key);
                    missionCount.remove(key);
                }

                if (value == null || value.get(COUNT_KEY) == null || ((int) value.get(COUNT_KEY) <= 0))
                {
                    if (isTimeout(value.get(TIMESTAMP_KEY)))
                    {
                        System.out.println("isTimeout");
                    }
                    System.out.println("TaskCountLock ID is cleared: " + key);
                    missionCount.remove(key);
                } else
                {
                    System.out.println("TaskCountLock ID is exist: " + key);
                }
            }
        }
    }

    /**
     * 超时
     */
    private static synchronized boolean isTimeout(Object paramTime)
    {
        if (paramTime != null)
        {
            try
            {
                LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
                Duration duration = Duration.between((LocalDateTime) paramTime, now);
                long hoursDiff = duration.toMillis();
                return hoursDiff >= 50000;
            } catch (Exception ignore)
            {
            }
        }
        return true;
    }

}
