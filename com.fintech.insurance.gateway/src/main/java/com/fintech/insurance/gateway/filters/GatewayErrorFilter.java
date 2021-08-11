package com.fintech.insurance.gateway.filters;

import com.fintech.insurance.commons.utils.ToolUtils;
import com.fintech.insurance.gateway.service.ExceptionPropertyService;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 集中处理zuul过滤器异常以及错误，并将错误以统一的结构返回
 */
@Component
public class GatewayErrorFilter extends ZuulFilter {

    private static final Logger logger = LoggerFactory.getLogger(GatewayErrorFilter.class);

    @Autowired
    private ExceptionPropertyService exceptionPropertyService;

    @Override
    public String filterType() {
        return FilterConstants.ERROR_TYPE;
    }

    @Override
    public int filterOrder() {
        return -1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        Throwable throwable = ctx.getThrowable();
        logger.debug("Error encountered during calling " + ctx.getRequest().getRequestURL(), throwable);
        for (Map.Entry<String, Object> entry : ctx.entrySet()) {
            if (entry != null) {
                logger.debug(entry.getKey() + " with value " + String.valueOf(entry.getValue()));
            }
        }
        Map<String, Object> finalResult = new HashMap<>();
        int errorCode = 100100;
        finalResult.put(GatewayResponseRestructureFilter.KEY_CODE, errorCode);
        finalResult.put(GatewayResponseRestructureFilter.KEY_MSG, this.exceptionPropertyService.getProperty(String.valueOf(errorCode)));
        ctx.setResponseStatusCode(HttpStatus.OK.value());
        ctx.setResponseBody(ToolUtils.toJsonString(finalResult));
        return null;
    }
}
