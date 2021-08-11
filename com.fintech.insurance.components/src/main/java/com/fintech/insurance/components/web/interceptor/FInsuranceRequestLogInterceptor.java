package com.fintech.insurance.components.web.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fintech.insurance.commons.enums.SystemProfile;
import com.fintech.insurance.components.web.BaseFintechController;
import com.fintech.insurance.components.web.context.FInsuranceApplicationContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * @Description: 用于拦截记录所有外部接口的请求和响应日志
 * @Author: Yong Li
 * @Date: 2018/1/18 18:38
 */
@Aspect
@Order(-100000000)
@Component
public class FInsuranceRequestLogInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(FInsuranceRequestLogInterceptor.class);

    @Autowired
    @Qualifier("jacksonObjectMapper")
    private ObjectMapper jacksonObjectMapper;

    @Pointcut("execution(* com.fintech.insurance.micro..*.controller..*.*(..))")
    public void executeService(){
    }

    @Around("executeService()")
    public Object doAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        if (LOG.isInfoEnabled() && !BaseFintechController.isRequestFromFeignInvoke()) {
            Method targetMethod = ((MethodSignature)proceedingJoinPoint.getSignature()).getMethod();
            LOG.info("==Request METHOD: {}.{}", targetMethod.getDeclaringClass(), targetMethod.getName());
            Type[] paramTypes = targetMethod.getGenericParameterTypes();
            if (paramTypes.length > 0) { //方法有参数
                LOG.info("request info as below:");
                for (int paramIndex = 0; paramIndex < paramTypes.length; paramIndex++) {
                    //打印请求参数
                    if (SystemProfile.PROD != FInsuranceApplicationContext.getSystemProfile()) {
                        if (null != proceedingJoinPoint.getArgs()[paramIndex]) {
                            try {
                                LOG.info("parameter: {}, \n{}", paramIndex, jacksonObjectMapper.writeValueAsString(proceedingJoinPoint.getArgs()[paramIndex]));
                            } catch (Exception e) {
                                LOG.error("Failed to parse object to string: " + e.getMessage(), e);
                            }
                        }
                    }
                }
            }
        }

        Object methodReturnValue = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());

        if (LOG.isInfoEnabled() && !BaseFintechController.isRequestFromFeignInvoke()) {
            if (null != methodReturnValue) {
                try {
                    LOG.info("==Response: \n{}", jacksonObjectMapper.writeValueAsString(methodReturnValue));
                } catch (Exception e) {
                    LOG.error("Failed to parse object to string: " + e.getMessage(), e);
                }
            } else {
                LOG.info("==Response: is NULL");
            }
        }

        return methodReturnValue;
    }

}
