package MyUtil.cacheUtil;

import java.util.function.Function;

public interface ICache
{
    void expire(String key, int timeOutSecond);

    void leftPush(String key, Object value);

    void rightPush(String key, Object value);

    void rightPush(String key, Object value, int timeOutSecond);

    <T> T rightPop(String key);

    <T> T leftPop(String key);

    public <T> T computeIfAbsent(String key, int outSecond, Function<String, Object> mappingFunction);

    void put(String key, Object value);

    void put(String key, Object value, int timeOutSecond);

    boolean putIfAbsent(String key, Object value);

    boolean putIfAbsent(String key, Object value, int timeOutSecond);

    <T> T get(String key);

    boolean hasKey(String key);

    void remove(String key);

    <T> T removeAndGet(String key);
}
