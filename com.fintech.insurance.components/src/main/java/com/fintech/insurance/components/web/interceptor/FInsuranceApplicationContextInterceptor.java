package com.fintech.insurance.components.web.interceptor;

import com.alibaba.fastjson.JSON;
import com.fintech.insurance.commons.constants.GatewayFintechHeaders;
import com.fintech.insurance.components.web.BaseFintechManagementController;
import com.fintech.insurance.components.web.BaseFintechWechatController;
import com.fintech.insurance.components.web.context.FInsuranceApplicationContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 处理请求中的context信息
 */
public class FInsuranceApplicationContextInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(FInsuranceApplicationContextInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler != null && (handler instanceof HandlerMethod) && (!(((HandlerMethod) handler).getBean() instanceof BaseFintechWechatController) || !(((HandlerMethod) handler).getBean() instanceof BaseFintechManagementController))) {
            String h5Context = request.getHeader(GatewayFintechHeaders.H5_APP_CONTEXT);
            if (StringUtils.isNotEmpty(h5Context)) {
                try {
                    Map<String, Object> context = (Map<String, Object>) JSON.parseObject(h5Context, Map.class);
                    if (context != null) {
                        FInsuranceApplicationContext.JWT_CONTEXT.set(context);
                    }
                } catch (Exception e) {
                    logger.error("Fail to handle the h5 application context");
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
