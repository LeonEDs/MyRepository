package com.xad.server.proxy.annotation;

import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RestPost
{
    String path() default "";

    RequestMethod method() default RequestMethod.POST;
}
