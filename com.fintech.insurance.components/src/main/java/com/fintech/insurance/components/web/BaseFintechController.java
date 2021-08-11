package com.fintech.insurance.components.web;

import com.fintech.insurance.commons.constants.GatewayFintechHeaders;
import com.fintech.insurance.commons.enums.RequestSourceType;
import com.fintech.insurance.components.web.context.FInsuranceApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 微服务通用抽象类
 * @author Yong Li
 * @since 2017/11/16 11:59
 */
public abstract class BaseFintechController {

    /**
     * 获取当前用户的登录名
     * @return 当前用户的登录名，如果为非登录接口则返回为null
     */
    public String getCurrentLoginUser() {
        return FInsuranceApplicationContext.getCurrentUserName();
    }

    /**
     * 获取当前登录用户的id
     * @return 当前用户的id，如果为非登录接口则返回为0
     */
    public Integer getCurrentLoginUserId() {
        return FInsuranceApplicationContext.getCurrentUserId();
    }

    /**
     * 当前请求是否由Feign客户端调用，即为内部请求
     * @return
     */
    public static boolean isRequestFromFeignInvoke() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (null != requestAttributes) {
            String requestSource = requestAttributes.getRequest().getHeader(GatewayFintechHeaders.CLIENT_INVOKE);
            return RequestSourceType.FEIGN.getCode().equals(requestSource);
        }
        return false;
    }
}
