package com.fintech.insurance.components.web.feign;

import com.alibaba.fastjson.JSON;
import com.fintech.insurance.commons.constants.GatewayFintechHeaders;
import com.fintech.insurance.commons.constants.JWTConstant;
import com.fintech.insurance.commons.enums.RequestSourceType;
import com.fintech.insurance.components.web.context.FInsuranceApplicationContext;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/12/4 15:47
 */
public class FeignRequestInterceptor implements RequestInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(FeignRequestInterceptor.class);

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(GatewayFintechHeaders.CLIENT_INVOKE, RequestSourceType.FEIGN.getCode());

        //拷贝
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (null != servletRequestAttributes) { // 对于从定时器来的Feign请求，该属性可能为空
            String principal = servletRequestAttributes.getRequest().getHeader(GatewayFintechHeaders.PRINCIPAL);
            String principalId = servletRequestAttributes.getRequest().getHeader(GatewayFintechHeaders.PRINCIPAL_ID);

            if (StringUtils.isNotEmpty(principal) && StringUtils.isNotEmpty(principalId)) { //来自运营管理平台的请求
                Map<String, Object> context = FInsuranceApplicationContext.currentContext();
                if (context == null) {
                    context = new HashMap<>();
                }
                context.put(JWTConstant.USER_TYPE, ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader(GatewayFintechHeaders.PRICIPAL_USERTYPE));
                context.put(JWTConstant.USER_ID, principalId);
                context.put(JWTConstant.USER_NAME, principal);
                FInsuranceApplicationContext.JWT_CONTEXT.set(context);
            }
        }

        Map<String, Object> context = FInsuranceApplicationContext.currentContext();
        if (context != null) { // 从定时器或@Async方法里面来的都会把Context拷贝往Feign端进行传播
            requestTemplate.header(GatewayFintechHeaders.H5_APP_CONTEXT, JSON.toJSONString(context));
        }
    }
}
