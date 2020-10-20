package com.http.util.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestHeadConfig
{
    String[] header() default {"Content-Type: application/json"};
}
