package MyUtil.cache.LRU;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Function;

public class LRU<E>
{
    private ConcurrentLinkedDeque<E> CACHE_LIST;
    private Long maxCapacity;
    private Long defaultMaxCapacity = 100L;

    public LRU()
    {
        CACHE_LIST = new ConcurrentLinkedDeque<E>();
        maxCapacity = defaultMaxCapacity;
    }

    public LRU(Collection<? extends E> collection)
    {
        CACHE_LIST = new ConcurrentLinkedDeque<E>(collection);
        maxCapacity = defaultMaxCapacity;
    }

    public LRU(Collection<? extends E> collection, Long capacity)
    {
        CACHE_LIST = new ConcurrentLinkedDeque<E>(collection);
        maxCapacity = capacity;
    }

    public void put(E e)
    {
        CACHE_LIST.add(e);
        if (CACHE_LIST.size() >= maxCapacity)
        {
            CACHE_LIST.removeLast();
        }
    }
    public E get(E e)
    {
        if(null == e)
        {
            return null;
        }
        Object result = null;
        Iterator iterable = CACHE_LIST.iterator();
        for(;iterable.hasNext();)
        {
            Object temp = iterable.next();
            if (e.equals(temp))
            {
                result = temp;
            }
        }
        if (null != result)
        {
            CACHE_LIST.remove(result);
            CACHE_LIST.addFirst((E)result);
        }
        return (E)result;
    }
}
