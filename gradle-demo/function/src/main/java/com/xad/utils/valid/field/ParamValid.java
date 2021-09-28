package com.xad.utils.valid.field;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface ParamValid {
    boolean notNull() default false;

    String pattern() default "";

    int maxLength() default 0;

    int minLength() default -1;

    double maxValue() default Integer.MIN_VALUE;

    double minValue() default Integer.MAX_VALUE;

    ValidEnum inEnum() default ValidEnum.EMPTY;
}
