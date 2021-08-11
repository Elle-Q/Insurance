package com.fintech.insurance.commons.annotations;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MicroEnum {
    String valueField() default "code";
}
