package com.fintech.insurance.components.web.interceptor;

import com.fintech.insurance.commons.constants.GatewayFintechHeaders;
import com.fintech.insurance.commons.constants.JWTConstant;
import com.fintech.insurance.components.web.context.FInsuranceApplicationContext;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 定时器任务的AOP
 * @Author: Yong Li
 * @Date: 2017/12/19 19:55
 */
@Aspect
@Order(100000)
@Component
public class FInsuranceTimerInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(FInsuranceTimerInterceptor.class);

    private static final String TIMER_USER_ID = "1";
    private static final String TIMER_USER_TYPE = "staff";
    private static final String TIMER_USER_NAME = "admin";

    /**
     * 在每个定时任务执行前先设置用户上下文， 以便在Feign在调用内部服务的时候能通过FInsuranceApplicationContext拿到用户信息
     *
     * 定时器配置用户名为: admin
     */
    @Before("execution(* com.fintech.insurance.micro.timer.controller..*.*(..))")
    public void injectUserContext() {
        Map<String, Object> context = new HashMap<String, Object>();
        context.put(JWTConstant.USER_ID, TIMER_USER_ID);
        context.put(JWTConstant.USER_TYPE, TIMER_USER_TYPE);
        context.put(JWTConstant.USER_NAME, TIMER_USER_NAME);

        FInsuranceApplicationContext.JWT_CONTEXT.set(context);
    }
}


