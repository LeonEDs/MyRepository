package com.xad.demo.beans;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Aspect
@Component
@Lazy(false)
public class MyAspect
{
    /**
     * 定义切入点：对要拦截的方法进行定义与限制，如包、类
     * <p>
     * 1、execution(public * *(..)) 任意的公共方法
     * 2、execution（* set*（..）） 以set开头的所有的方法
     * 3、execution（* com.LogRecord.annotation.LoggerApply.*（..））com.LogRecord.annotation.LoggerApply这个类里的所有的方法
     * 4、execution（* com.LogRecord.annotation.*.*（..））com.LogRecord.annotation包下的所有的类的所有的方法
     * 5、execution（* com.LogRecord.annotation..*.*（..））com.LogRecord.annotation包及子包下所有的类的所有的方法
     * 6、execution(* com.LogRecord.annotation..*.*(String,?,Long)) com.LogRecord.annotation包及子包下所有的类的有三个参数
     * 第一个参数为String类型，第二个参数为任意类型，第三个参数为Long类型的方法
     * 7、execution(@annotation(com.LogRecord.annotation.LogRecord)) 注解
     */
    @Pointcut("execution (* com.xad.demo.beans.AspectDemo.* (float , float ))")
    private void logPoint()
    {
    }

    /**
     * 前置通知：在目标方法执行前调用
     */
    @Before("logPoint()")
    public void begin()
    {
        System.out.println("start log point...");
    }

    /**
     * 后置通知：无论目标方法在执行过程中出现一场都会在它之后调用
     */
    @After("logPoint()")
    public void after()
    {
        System.out.println("end log point...");
    }

    /**
     * 返回通知：在目标方法执行后调用，若目标方法出现异常，则不执行
     */
    @AfterReturning(value = "logPoint()", returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result)
    {
        System.out.println("return "+ joinPoint.getSignature().getName() +" log point..." + result);
    }

    /**
     * 异常通知：目标方法抛出异常时执行
     * joinPoint 切入点参数必须是第一位参数
     * throwing 必须指定异常参数名
     */
    @AfterThrowing(value = "logPoint()", throwing = "exception")
    public void afterThrowing(JoinPoint joinPoint, Exception exception)
    {
        System.out.println("throw "+ joinPoint.getSignature().getName() +" log point..." + exception);
    }

    /**
     * 环绕通知：灵活自由的在目标方法中切入代码
     */
//    @Around("logPoint()")
//    public void around(ProceedingJoinPoint joinPoint) throws Throwable
//    {
//        // 获取方法名
//        String methodName = joinPoint.getSignature().getName();
//        // 反射获取目标类
//        Class<?> targetClass = joinPoint.getTarget().getClass();
//        // 拿到方法对应的参数类型
//        Class<?>[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getParameterTypes();
//        // 获取反射方法
//        Method method = targetClass.getMethod(methodName, parameterTypes);
//        // 获取方法传入参数
//        Object[] params = joinPoint.getArgs();
//
//        // 拿到方法定义的注解信息
//        method.getDeclaredAnnotation(Annotation.class);
//
//        StringBuilder builder = new StringBuilder();
//
//        if (params != null && params.length > 0)
//        {
//            for (Object param : params)
//            {
//                builder.append(param.toString());
//                builder.append("  ");
//            }
//        }
//        System.out.println(builder.toString());
//        // 执行源方法
//        joinPoint.proceed();
//    }
}
