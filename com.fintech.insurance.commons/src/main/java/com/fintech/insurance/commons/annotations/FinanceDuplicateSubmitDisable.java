package com.fintech.insurance.commons.annotations;

import java.lang.annotation.*;

/**
 * @Description: (不可重复提交的注解)
 * @Author: Administrator
 * @Date: 2018/1/9 0009 9:17
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FinanceDuplicateSubmitDisable {
    long value() default 5;// 默认5秒
    int msg() default 105930;// 默认不可重复提交
}
