package MyUtil.NumericUtil;

import MyUtil.MainTest.C;

import java.util.LinkedList;
import java.util.List;

public class SplitNum
{
    private final static int splitGroupNum = 10;

    public static List<C> splitNum(long startId, long endId)
    {
        long partLength = endId - startId;
        long newStart = startId;
        long newEnd = startId;
        List<C> cList = new LinkedList<>();

        if (partLength >= 0 && endId > 0 && startId > 0)
        {
            do
            {
                newEnd += 9L;
                if (newEnd > endId)
                {
                    C c = new C();
                    c.start = newStart;
                    c.end = endId;
                    cList.add(c);
                    break;
                }else
                {
                    C c = new C();
                    c.start = newStart;
                    c.end  = newEnd;
                    cList.add(c);

                    newStart = newEnd + 1;
                    newEnd = newStart;
                }
            }while (newEnd <= endId);
        }

        cList.forEach(c ->
        {
            System.out.print("#"+c.start+":"+c.end+"#");
            for (long i = c.start; i <= c.end; i++)
            {
                System.out.print(i + ", ");
            }
            System.out.print("\n");

        });

        return cList;
    }

    public static List<C> splitNum()
    {
        long maxCustId = 3002L;
        long minCustId = 3001L;

        List<C> cList = new LinkedList<>();

        if (maxCustId > 0 && minCustId > 0)
        {
            long partLength = maxCustId - minCustId;
            if (partLength < 10)
            {
                for (int i = 0; i <= partLength; i++)
                {
                    C task = new C();
                    task.start = (minCustId + i);
                    task.end = (minCustId + i);
                    cList.add(task);
                }
            }else
            {
                long split = partLength / splitGroupNum;
                for (int i = 0; i < splitGroupNum; i++)
                {
                    C task = new C();
                    if (i == (splitGroupNum - 1))
                    {
                        task.start = (minCustId + split*i);
                        task.end = (maxCustId);
                        cList.add(task);
                    }else
                    {
                        task.start = (minCustId + split*i);
                        task.end = (minCustId + split*(i+1)-1);
                        cList.add(task);
                    }
                }
            }
        }


        cList.forEach(c ->
        {
            System.out.print("#"+c.start+":"+c.end+"#");
            for (long i = c.start; i <= c.end; i++)
            {
                System.out.print(i + ", ");
            }
            System.out.print("\n");

        });
        return cList;
    }
}
