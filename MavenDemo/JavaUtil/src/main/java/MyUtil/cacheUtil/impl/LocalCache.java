package MyUtil.cacheUtil.impl;

import MyUtil.cacheUtil.ICache;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.googlecode.concurrentlinkedhashmap.Weighers;
import sun.misc.Unsafe;
import sun.misc.VM;
import sun.reflect.CallerSensitive;
import sun.reflect.Reflection;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class LocalCache implements ICache
{
    private static LocalCache staticInstance = new LocalCache();

    public static LocalCache getInstance()
    {
        if (staticInstance != null)
        {
            return staticInstance;
        } else
        {
            synchronized (LocalCache.class)
            {
                staticInstance = new LocalCache();
                return staticInstance;
            }
        }
    }

    private LocalCache() {
    }

    /**
     * 存储最大数据数量，超出该数据量时，删除最新存储的数据
     */
    private static final int MAXCOUNT = 2000;

    /**
     * 缓存实例
     */
    private static final Map<String, Object> INSTANCE =new ConcurrentLinkedHashMap.Builder<String, Object>()
            .maximumWeightedCapacity(MAXCOUNT).weigher(Weighers.singleton()).initialCapacity(100).build();

    /**
     * 缓存KEY 存储时间记录
     */
    private static final Map<String, Long> KEY_TIME_INSTANCE = new ConcurrentLinkedHashMap.Builder<String, Long>()
            .maximumWeightedCapacity(MAXCOUNT). weigher(Weighers.singleton()).initialCapacity(100).build();

    /**
     * 时间格式化对象
     */
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 清理缓存 线程,防止频繁的缓存清理 创建线程消耗性能
     */
    private static final ExecutorService THREAD_POOL = Executors.newCachedThreadPool();

    /**
     * 清理缓存 线程 的锁
     */
    private static final AtomicInteger TREAM_CACHE_LOCK = new AtomicInteger(0);

    /**
     * 缓存清理 轮询一圈等待时长
     */
    private static final int TRIM_INTERIM = 2000;

    /**
     * 队列存储，在末尾添加元素
     *
     * @param key key
     * @param value value
     * @param outSecond 保存时间(秒)，超出时间，被清除
     */
    @SuppressWarnings("unchecked")
    @Override
    public void rightPush(String key, Object value, int outSecond)
    {
        ConcurrentLinkedDeque<Object> linkList = (ConcurrentLinkedDeque<Object>) INSTANCE.get(key);
        if (linkList == null) {
            linkList = new ConcurrentLinkedDeque<>();
            INSTANCE.put(key, linkList);
        }
        KEY_TIME_INSTANCE.put(key,
                Long.parseLong(LocalDateTime.now().plusSeconds(outSecond).format(DATE_TIME_FORMAT)));
        linkList.offer(value);
        LocalCache.streamInstance();
    }

    /**
     * 队列存储，在末尾添加元素
     *
     * @param key key
     * @param value value
     */
    @SuppressWarnings("unchecked")
    @Override
    public void rightPush(String key, Object value) {
        ConcurrentLinkedDeque<Object> linkList = (ConcurrentLinkedDeque<Object>) INSTANCE.get(key);
        if (linkList == null) {
            linkList = new ConcurrentLinkedDeque<>();
            INSTANCE.putIfAbsent(key, linkList);
        }
        linkList.offer(value);
        LocalCache.streamInstance();
    }

    /**
     * 队列存储，在开头添加元素
     *
     * @param key key
     * @param value value
     */
    @SuppressWarnings("unchecked")
    @Override
    public void leftPush(String key, Object value) {
        ConcurrentLinkedDeque<Object> linkList = (ConcurrentLinkedDeque<Object>) INSTANCE.get(key);
        if (linkList == null) {
            linkList = new ConcurrentLinkedDeque<>();
            INSTANCE.putIfAbsent(key, linkList);
        }
        linkList.offerFirst(value);
        LocalCache.streamInstance();
    }

    /**
     * 删除队列的最后一个元素
     *
     * @param key key
     * @return T
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T rightPop(String key) {
        ConcurrentLinkedDeque<Object> linkList = (ConcurrentLinkedDeque<Object>) INSTANCE.get(key);
        if (linkList == null) {
            return null;
        }
        return (T) linkList.pollLast();
    }

    /**
     * 删除队列的第一个元素
     *
     * @param key key
     * @return T
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T leftPop(String key) {
        ConcurrentLinkedDeque<Object> linkList = (ConcurrentLinkedDeque<Object>) INSTANCE.get(key);
        if (linkList == null) {
            return null;
        }
        return (T) linkList.pollFirst();
    }

    /**
     *
     * @param key key
     * @param outSecond 保存时间(秒)，超出时间，被清除
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T>T computeIfAbsent(String key, int outSecond, Function<String, Object> mappingFunction) {
        T t=(T) INSTANCE.computeIfAbsent(key, mappingFunction);
        KEY_TIME_INSTANCE.putIfAbsent(key,
                Long.parseLong(LocalDateTime.now().plusSeconds(outSecond).format(DATE_TIME_FORMAT)));
        LocalCache.streamInstance();
        return t;
    }

    /**
     *
     * @param key key
     * @param value value
     */
    @Override
    public void put(String key, Object value) {
        INSTANCE.put(key, value);
    }

    /**
     *
     * @param key key
     * @param value value
     * @param outSecond 保存时间(秒)，超出时间，被清除
     */
    @Override
    public void put(String key, Object value, int outSecond) {
        INSTANCE.put(key, value);
        KEY_TIME_INSTANCE.put(key,
                Long.parseLong(LocalDateTime.now().plusSeconds(outSecond).format(DATE_TIME_FORMAT)));
        LocalCache.streamInstance();
    }

    /**
     *
     * @param key key
     * @param value value
     * @return boolean
     */
    @Override
    public boolean putIfAbsent(String key, Object value) {
        Object result = null;
        result = INSTANCE.putIfAbsent(key, value);
        LocalCache.streamInstance();
        return result == null;
    }

    /**
     *
     * @param key key
     * @param value value
     * @param outTimeSecond 保存时间(秒)，超出时间，被清除
     * @return boolean
     */
    @Override
    public boolean putIfAbsent(String key, Object value, int outTimeSecond) {
        Object result = null;
        result = INSTANCE.putIfAbsent(key, value);
        KEY_TIME_INSTANCE.putIfAbsent(key,
                Long.parseLong(LocalDateTime.now().plusSeconds(outTimeSecond).format(DATE_TIME_FORMAT)));
        LocalCache.streamInstance();
        return result == null;
    }

    /**
     * 获取缓存
     *
     * @param key key
     * @return T
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(String key) {
        T value = (T) INSTANCE.get(key);
        if (value == null) {
            return null;
        }
        if (LocalCache.isTimeOut(key)) {
            INSTANCE.remove(key);
            KEY_TIME_INSTANCE.remove(key);
            return null;
        } else {
            return value;
        }
    }

    @Override
    public void expire(String key, int timeOutSecond) {
        KEY_TIME_INSTANCE.put(key,
                Long.parseLong(LocalDateTime.now().plusSeconds(timeOutSecond).format(DATE_TIME_FORMAT)));
    }

    /**
     * 是否含有
     *
     * @param key key
     * @return boolean
     */
    @Override
    public boolean hasKey(String key) {
        return INSTANCE.containsKey(key);
    }

    /**
     * 删除
     */
    @Override
    public void remove(String key) {
        INSTANCE.remove(key);
    }

    /**
     * 删除并返回
     *
     * @return T
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T removeAndGet(String key) {
        return (T) INSTANCE.remove(key);
    }

    /**
     * 整理缓存：<br>
     * 整理的缓存的线程只能一个，节约资源开销<br>
     * TRIM_INTERIM<br>
     */
    private static void streamInstance()
    {

        if (TREAM_CACHE_LOCK.incrementAndGet() > 1) {
            return;
        }
        THREAD_POOL.execute(() -> {
            long now = Long.parseLong(LocalDateTime.now().format(DATE_TIME_FORMAT));
            do {
                /*
                 * 1、超时缓存清除
                 */
                Iterator<Entry<String, Object>> instanceIt = INSTANCE.entrySet().iterator();
                while (instanceIt.hasNext()) {
                    String key = instanceIt.next().getKey();
                    if (LocalCache.isTimeOut(key, now)) {
                        instanceIt.remove();
                        KEY_TIME_INSTANCE.remove(key);
                    }
                }

//                /*
//                 * 2、 超容量,从首位开始清除
//                 */
//                if (INSTANCE.size() > MAXCOUNT) {
//                    List<String> tempList = new ArrayList<>();
//                    for (Entry<String, Object> en : INSTANCE.entrySet()) {
//                        tempList.add(en.getKey());
//                        if (INSTANCE.size() - tempList.size() <= MAXCOUNT) {
//                            tempList.forEach(e -> {
//                                INSTANCE.remove(e);
//                                KEY_TIME_INSTANCE.remove(e);
//                            });
//                            break;
//                        }
//                    }
//                }
                try
                {
                    TimeUnit.SECONDS.sleep(TRIM_INTERIM);
                }catch (InterruptedException e)
                {
                    System.out.println(e);
                }

                now = Long.valueOf(LocalDateTime.now().format(DATE_TIME_FORMAT));
            } while (!INSTANCE.isEmpty());
            TREAM_CACHE_LOCK.set(0);
        });
    }

    /**
     * 判断key对比当前时间是否超时
     *
     * @param key key
     * @return boolean
     */
    private static boolean isTimeOut(String key) {
        long now = Long.parseLong(LocalDateTime.now().format(DATE_TIME_FORMAT));
        return LocalCache.isTimeOut(key, now);
    }

    /**
     *
     * 判断key对比now是否超时
     *
     * @param key key
     * @param now now
     * @return boolean
     */
    private static boolean isTimeOut(String key, long now) {
        Long saveTime = KEY_TIME_INSTANCE.get(key);
        return saveTime == null || saveTime < now;
    }
}
