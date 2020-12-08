package com.demo.dubbo.consumer;

import com.demo.dubbo.api.IDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.annotation.Resource;
import java.util.Arrays;

public class ConsumerTest
{
    public static void main(String [] args)
    {
        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext("application-consumer.xml");
        System.err.println("consumer start");

        IDemoService demoService = (IDemoService)context.getBean(IDemoService.class);
        System.out.println(">>> \n\n\n"+
                Arrays.asList(demoService.testMethod()).toString());
    }
}
