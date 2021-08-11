package com.fintech.insurance.micro.finance.service.yjf;

import java.lang.annotation.*;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/12/8 17:24
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface YjfService {
    String name();
}
