package com.fintech.insurance.components.web.interceptor;

import com.fintech.insurance.commons.annotations.FinanceDuplicateSubmitDisable;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.BaseFintechController;
import com.fintech.insurance.components.web.context.FInsuranceApplicationContext;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

import java.util.concurrent.TimeUnit;

/**
 * @Description: 控制层拦截器，防止重复提交
 * @Author: Yong Li
 * @Date: 2017/12/1 10:01
 */
@Aspect
@Order(-100000)
@Component
public class FinanceDuplicateSubmitInterceptor {
    private static final Logger LOG = LoggerFactory.getLogger(FinanceDuplicateSubmitInterceptor.class);

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    @Pointcut("execution(* com.fintech.insurance.micro..*.controller..*.*(..))")
    public void executeService(){

    }

    private Integer currentUserId = 0;

    @Around("executeService()")
    public Object doAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Method targetMethod = ((MethodSignature)proceedingJoinPoint.getSignature()).getMethod();

        //缓存的key
        String cacheKey = null;
        if (!BaseFintechController.isRequestFromFeignInvoke()) {
            //获取重复提交的注解bean
            FinanceDuplicateSubmitDisable duplicateSubmitDisable = targetMethod.getAnnotation(FinanceDuplicateSubmitDisable.class);
            if (duplicateSubmitDisable != null ) {
                //当前登录用户id
                currentUserId = FInsuranceApplicationContext.getCurrentUserId() == null ? 0 : FInsuranceApplicationContext.getCurrentUserId();
                synchronized (currentUserId) {
                    //获取缓存的key
                    cacheKey = StringUtils.join(targetMethod.getDeclaringClass(), "_" , targetMethod.getName(), "_", currentUserId);
                    LOG.info("Current Object:" + this.toString());
                    LOG.info("check duplicated method key: {}", cacheKey);
                    if(redisTemplate.hasKey(cacheKey)){
                        LOG.info("check duplicated method key: {}, does not exist.", cacheKey);
                        throw new FInsuranceBaseException(duplicateSubmitDisable.msg());
                    }
                    redisTemplate.opsForValue().set(cacheKey, String.valueOf(System.currentTimeMillis()), duplicateSubmitDisable.value(), TimeUnit.SECONDS);
                    LOG.info("put duplicated method key: {} in cache: {} seconds", cacheKey, duplicateSubmitDisable.value());
                }
            }
        }

        try {
            return proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
        } finally {
            if (!BaseFintechController.isRequestFromFeignInvoke() && cacheKey != null) {
                boolean flag = redisTemplate.hasKey(cacheKey);
                if (flag) {
                    redisTemplate.delete(cacheKey);
                }
            }
        }
    }
}
