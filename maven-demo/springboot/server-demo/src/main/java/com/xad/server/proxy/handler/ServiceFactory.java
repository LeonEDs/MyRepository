package com.xad.server.proxy.handler;

import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;

/**
 * 接口实例工厂，这里主要是用于提供接口的实例对象
 *
 * @param <T>
 */
public class ServiceFactory<T> implements FactoryBean<T>
{
    private Class<?> interfaceType;

    public ServiceFactory(Class<T> interfaceType)
    {
        this.interfaceType = interfaceType;
    }

    @Override
    public T getObject() throws Exception
    {
        //这里主要是创建接口对应的实例，便于注入到spring容器中
        ServiceProxy handler = ServiceProxy.getInstance();
        handler.setInterfaceType(interfaceType);
        Object instance = Proxy.newProxyInstance(interfaceType.getClassLoader(),
                new Class[]{interfaceType}, handler);
        return (T) instance;
    }

    @Override
    public Class<?> getObjectType()
    {
        return interfaceType;
    }

    @Override
    public boolean isSingleton()
    {
        return true;
    }
}
