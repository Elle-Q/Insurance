package com.fintech.insurance.commons.annotations;


import java.lang.annotation.*;

/**
 * @Description: (some words)
 * @Author: qxy
 * @Date: 2017/12/1 11:03
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FInsuranceTimer {

    String name() default "";

    String desc() default "";

}
