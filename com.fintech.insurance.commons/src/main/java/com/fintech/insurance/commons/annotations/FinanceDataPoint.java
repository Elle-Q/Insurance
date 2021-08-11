package com.fintech.insurance.commons.annotations;

import java.lang.annotation.*;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/12/1 11:03
 */
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FinanceDataPoint {
}
