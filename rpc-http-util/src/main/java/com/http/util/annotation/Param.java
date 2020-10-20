package com.http.util.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Param
{
    String type() default "";

    String name() default "";

    String URI = "URI";

    String JSON = "JSON";
}