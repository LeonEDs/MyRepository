package com.demo.dubbo;

import com.alibaba.dubbo.config.spring.ServiceBean;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class ProviderStart
{
    public static void main(String [] args) throws IOException
    {
        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext("classpath:application-*.xml");

        //ServiceBean bean = (ServiceBean)context.getBean("demoServiceImpl");
        //System.out.println("RefName: "+bean.getRef()+" Intf: "+bean.getInterface());

        context.start();
        System.err.println("服务已经启动...");

        com.alibaba.dubbo.container.Main.main(args);
    }

}
