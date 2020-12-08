package MyUtil.MainTest;


import MyUtil.NumericUtil.NumericCheckUtils;
import com.alibaba.dubbo.common.utils.StringUtils;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Main
{

    public static void main(String[] args) throws Exception
    {
        Long maxCustId = 37L;
        Long minCustId = 12L;
        String missionCode = "EX202012031442#382";


        int parallelNum = 1;
        long partLength = maxCustId - minCustId;


        long split = partLength / 10;
        for (int i = 0; i < 10; i++)
        {
            long startId;
            long endId;
            String taskMissionCode = missionCode + "#" + parallelNum++;

            if (i == (10 - 1))
            {
                startId = minCustId + split * i;
                endId = maxCustId;
            } else
            {
                startId = minCustId + split * i;
                endId = minCustId + split * (i + 1) - 1;
            }

            System.out.println("|| " + startId +" || " + endId +" || " + taskMissionCode);
        }

    }
}
