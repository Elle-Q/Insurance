package com.fintech.insurance.components.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于标注该请求的执行是否需要微信授权登录，注解于Controller类上，则该控制器的所有入口都必须微信授权后才能调用，注解于Controller的方法上，则该方法需要微信授权登录后调用，否则通知调用者当前调用未微信授权
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE, ElementType.METHOD})
public @interface RequireWechatLogin {

}
