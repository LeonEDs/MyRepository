package com.xad.common.utils;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ShareVariateLock
{
    private static final Lock lock = new ReentrantLock();
    private static final Condition empty = lock.newCondition();
    private static final Condition full = lock.newCondition();
    private static final List<Integer> list = new LinkedList<>();
    private static int size = 0;
    private static int maxSize = 5;
    private static int product = 1;

    public static void put()
    {
        lock.lock();
        try
        {
            while (size == maxSize)
            {
                empty.await();
            }
            list.add(product);
            size++;
            System.out.println("put " + product);
            product++;
            full.signal();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        } finally
        {
            lock.unlock();
        }
    }

    public static void take()
    {
        lock.lock();
        try
        {
            while (size == 0)
            {
                full.await();
            }
            int tep = list.remove(0);
            size--;
            System.out.println("take " + tep);
            empty.signal();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        } finally
        {
            lock.unlock();
        }
    }
}
