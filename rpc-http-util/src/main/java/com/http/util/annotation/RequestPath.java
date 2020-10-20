package com.http.util.annotation;

import com.http.util.handler.DefaultRequestHandler;
import com.http.util.handler.IRequestHandler;
import org.springframework.core.annotation.AliasFor;
import org.springframework.http.HttpMethod;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestPath
{
    String url() default "";

    HttpMethod method() default HttpMethod.GET;

    Class<? extends IRequestHandler> requestHandler() default DefaultRequestHandler.class;
}
