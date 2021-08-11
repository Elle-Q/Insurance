package com.fintech.insurance.commons.annotations;

import java.lang.annotation.*;

/**
 * 用于注解生命周期处理器方法
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface LifeCycleHandler {

    String value() default "";
}
