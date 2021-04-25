package com.xad.demo.beans;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author xad
 * @version 1.0
 * @date 2021/3/29
 */
@Component
public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor
{
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException
    {
        Arrays.stream(beanFactory.getBeanDefinitionNames()).forEach(System.out::println);
    }
}
