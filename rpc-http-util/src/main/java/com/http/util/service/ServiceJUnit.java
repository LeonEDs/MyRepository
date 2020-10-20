package com.http.util.service;

import com.http.util.KeyValue;
import com.http.util.handler.ServiceProxy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

@RunWith(SpringJUnit4ClassRunner.class)
public class ServiceJUnit
{
//    @Autowired
    private TestService testService;

    @Test
    public void testService1()
    {
        InvocationHandler handler = new ServiceProxy<>(TestService.class);
        Object instance = Proxy.newProxyInstance(TestService.class.getClassLoader(), new Class[] {TestService.class}, handler);
        testService = (TestService) instance;

        KeyValue getResult = testService.getInfo("AAABBBXXX");
        System.out.println(">>> GET   :   " + getResult.toString());
    }

    @Test
    public void testService2()
    {
        InvocationHandler handler = new ServiceProxy<>(TestService.class);
        Object instance = Proxy.newProxyInstance(TestService.class.getClassLoader(), new Class[] {TestService.class}, handler);
        testService = (TestService) instance;

        KeyValue postResult = testService.postInfo(new KeyValue("AAAA","BBBB"));
        System.out.println(">>> POST   :   " + postResult.toString());
    }
}
