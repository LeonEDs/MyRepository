package com.xad.server.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * Spring上下文获取Bean.
 * @version 1.0
 * @author xad
 * @date 2020/12/24 0024
 */
@Component
public class ContextUtils implements ApplicationContextAware
{
    private static ApplicationContext applicationContext = null;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext context) throws BeansException
    {
        if (applicationContext == null)
        {
            applicationContext = context;
        }
    }

    /**
     * 获取上下文.
     */
    public static ApplicationContext getApplicationContext()
    {
        return applicationContext;
    }

    /**
     * 获取Bean.
     */
    public static <T> T getBean(Class<T> clazz)
    {
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 获取Bean.
     */
    public static <T> T getBean(String name, Class<T> clazz)
    {
        return getApplicationContext().getBean(name, clazz);
    }
}
