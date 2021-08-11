package com.fintech.insurance.gateway.filters;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fintech.insurance.commons.constants.GatewayFintechHeaders;
import com.fintech.insurance.micro.dto.system.UserVO;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Component;

/**
 * 前置过滤器，用于设置RequestContext中的数据，用于下游的过滤器。
 */
@Component
public class GatewayPreFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.PRE_DECORATION_FILTER_ORDER - 1; // run before PreDeocration
    }

    /**
     * 用于检测是否执行当前过滤器，在测场景下为：如果RequestContext中已经设置了该过滤器处理之后的结果数据，则不需要执行，否则需要执行
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        //设置请求的UUID
        ctx.addZuulRequestHeader(GatewayFintechHeaders.REQUEST_ID, Md5Crypt.md5Crypt(RandomStringUtils.random(64).getBytes()));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //如果当前为已鉴权的请求，则设置已登录用户的登录名，在服务中使用((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest.getHeader(GatewayFintechHeaders.PRINCIPAL)可以获取当前已登录用户的登录名
        if (authentication != null && authentication.isAuthenticated()) {
            ctx.addZuulRequestHeader(GatewayFintechHeaders.PRINCIPAL, authentication.getName());
            //判断当前是否为Oauth2Authentication
            if (authentication instanceof OAuth2Authentication && authentication.getDetails() != null) {
                String tokenValue = ((OAuth2AuthenticationDetails)((OAuth2Authentication) authentication).getDetails()).getTokenValue();
                Jwt jwt = JwtHelper.decode(tokenValue);//
                if (!StringUtils.isEmpty(jwt.getClaims())) {
                    JSONObject claims = JSON.parseObject(jwt.getClaims());
                    if (claims != null) {
                        if (claims.containsKey(UserVO.ID) && claims.getInteger(UserVO.ID) != null) {
                            ctx.addZuulRequestHeader(GatewayFintechHeaders.PRINCIPAL_ID, claims.getString(UserVO.ID));
                        }
                        if (claims.containsKey(UserVO.USER_TYPE) && claims.getString(UserVO.USER_TYPE) != null) {
                            ctx.addZuulRequestHeader(GatewayFintechHeaders.PRICIPAL_USERTYPE, claims.getString(UserVO.USER_TYPE));
                        }
                    }
                }
            }
        }
        return null;
    }
}
