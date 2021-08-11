package com.fintech.insurance.gateway.filters;

import com.fintech.insurance.commons.enums.SystemProfile;
import com.fintech.insurance.commons.utils.ToolUtils;
import com.fintech.insurance.gateway.service.ExceptionPropertyService;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.poi.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * IP黑名单过滤器，仅针对生产环境有效
 */
@Component
@Configuration
public class IPBlackListFilter extends AbstractIPFilter implements EnvironmentAware {

    private static final Logger logger = LoggerFactory.getLogger(IPBlackListFilter.class);

    private SystemProfile profile;

    @Value("${fintech.insurance.security.ip-black-list}")
    private List<String> ipBlackList;

    @Autowired
    private ExceptionPropertyService exceptionPropertyService;

    @Override
    public void setEnvironment(Environment environment) {
        String[] activeProfiles = environment.getActiveProfiles();
        if (activeProfiles.length == 0) {
            this.profile = SystemProfile.DEFAULT;
        } else {
            this.profile = SystemProfile.codeOf(activeProfiles[0]);
        }
    }

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return this.profile != null && this.profile.equals(SystemProfile.PROD);
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        String ip = this.getIpAddr(ctx.getRequest());
        if (!StringUtils.isEmpty(ip) && this.ipBlackList != null && this.ipBlackList.size() > 0) {
            boolean isBlock = false;
            if (this.ipBlackList.contains(ip)) {
                logger.info("Current request ip " + ip + " is included in the black ip list");
                isBlock = true;
            } else {
                for (String blackIp : ipBlackList) {
                    if (StringUtils.isNotEmpty(blackIp) && Pattern.matches(blackIp, ip)) {
                        logger.info("Current request ip " + ip + " matches the block ip pattern " + blackIp);
                        isBlock = true;
                        break;
                    }
                }
            }
            if (isBlock) {
                ctx.setResponseStatusCode(HttpStatus.SC_OK);
                ctx.setSendZuulResponse(false);
                //发送错误信息到客户端
                Map<String, Object> finalResult = new HashMap<>();
                int errorCode = 100106;
                finalResult.put(GatewayResponseRestructureFilter.KEY_CODE, errorCode);
                finalResult.put(GatewayResponseRestructureFilter.KEY_MSG, this.exceptionPropertyService.getProperty(String.valueOf(errorCode)));
                ctx.setResponseBody(ToolUtils.toJsonString(finalResult));
                ctx.getResponse().setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            }
        }
        return null;
    }
}
