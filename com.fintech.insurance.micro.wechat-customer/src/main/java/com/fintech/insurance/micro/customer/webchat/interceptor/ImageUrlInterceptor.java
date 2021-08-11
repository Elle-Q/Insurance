package com.fintech.insurance.micro.customer.webchat.interceptor;

import com.fintech.insurance.components.web.BaseFintechController;
import com.fintech.insurance.components.web.interceptor.FinanceDataInterceptor;
import com.fintech.insurance.service.agg.ImageUtilService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


/**
 * @Description: 控制层拦截器，对图片类型的字段转换
 * @Author: qxy
 * @Date: 2017/12/1 10:01
 */
@Aspect
@Order(-1000)
@Component
public class ImageUrlInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(FinanceDataInterceptor.class);

    @Autowired
    ImageUtilService imageUtilService;

    @AfterReturning(pointcut = "execution(* com.fintech.insurance.micro..*.controller..*.*(..))", returning = "returnValue")
    public void doAfterAdvice(JoinPoint joinPoint, Object returnValue ) throws Throwable {
        try {
            if (!BaseFintechController.isRequestFromFeignInvoke()) {
                imageUtilService.transferImageUrlOnObject(returnValue, 0);
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
    }

}

