package com.fintech.insurance.gateway.filters;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fintech.insurance.commons.utils.ToolUtils;
import com.fintech.insurance.commons.utils.URLUtils;
import com.fintech.insurance.gateway.service.ExceptionPropertyService;
import com.fintech.insurance.gateway.service.HttpStatusConverter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 后置过滤器用于操纵响应。该过滤器接收代理微服务的处理之后的结果并将结果转化为指定的结构
 */
@Component
@Configuration
public class GatewayResponseRestructureFilter extends AbstractIPFilter {

    public static final String KEY_CODE = "code";
    public static final String KEY_MSG = "msg";
    public static final String KEY_DATA = "data";

    private static final Logger logger = LoggerFactory.getLogger(GatewayResponseRestructureFilter.class);

    @Value("${fintech.insurance.security.authz-service-id:authz-server}")
    private String authzServiceId ;

    private AntPathRequestMatcher wxIntRequestMatcher = new AntPathRequestMatcher(URLUtils.WECHAT_INTEGRATION_URL_PATTERN);//微信集成以及第三方外部请求的响应无需转成统一的结构
    private AntPathRequestMatcher yjfIntRequestMatcher = new AntPathRequestMatcher(URLUtils.MANAGEMENT_CALLBACK_URL_PATTERN);

    @Autowired
    private ExceptionPropertyService exceptionPropertyService;

    @Autowired
    private HttpStatusConverter httpStatusConverter;

    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.SEND_RESPONSE_FILTER_ORDER - 1; //在发送结果之前执行
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        return !this.wxIntRequestMatcher.matches(ctx.getRequest()) && !this.yjfIntRequestMatcher.matches(ctx.getRequest());
    }

    @Override
    public Object run() {
        try {
            RequestContext ctx = RequestContext.getCurrentContext();
            boolean isAuthzRequestProxy = this.authzServiceId.equalsIgnoreCase(ctx.get(FilterConstants.SERVICE_ID_KEY) != null ? String.valueOf(ctx.get(FilterConstants.SERVICE_ID_KEY)) : "");
            HttpServletResponse response = ctx.getResponse();
            Map<String, Object> finalResult = new HashMap<>();
            String originalBody = null;
            if (ctx.sendZuulResponse()) {
                originalBody = new String(StreamUtils.copyToByteArray(ctx.getResponseDataStream()));
            } else {
                originalBody = ctx.getResponseBody();
            }
            JSON originalResult = null;
            try {
                if (!StringUtils.isEmpty(originalBody)) {
                    originalResult = JSON.parseObject(originalBody);
                }
            } catch (Exception e) {
                logger.error("Fail to convert " + originalBody + " to json object");
            }
            if (originalResult == null) {
                try {
                    originalResult = JSON.parseArray(originalBody);
                } catch (Exception e) {
                    logger.error("Fail to convert " + originalBody + " to json array");
                }
            }
            if (isAuthzRequestProxy) { //如果请求转发的目标地址为授权服务器则修改响应的数据
                if (response.getStatus() >= HttpStatus.OK.value() && response.getStatus() < HttpStatus.MULTIPLE_CHOICES.value()) { //20X响应结果
                    if (originalResult != null && originalResult instanceof JSONObject && ((JSONObject)originalResult).containsKey(KEY_CODE)) {
                        ctx.setResponseBody(ToolUtils.toJsonString(originalResult));
                        return null;
                    } else {
                        finalResult.put(KEY_CODE, 0);
                        finalResult.put(KEY_MSG, "ok");
                        //如果当前的结果中包含access_token、refresh_token、token_type，则认为用户登录，获取用户最近的ip更新到数据库
                        if (originalResult != null && originalResult instanceof JSONObject
                                && ((JSONObject)originalResult).containsKey(OAuth2AccessToken.ACCESS_TOKEN)
                                && ((JSONObject)originalResult).containsKey(OAuth2AccessToken.REFRESH_TOKEN)
                                && ((JSONObject)originalResult).containsKey(OAuth2AccessToken.TOKEN_TYPE)) {
                            String ip = this.getIpAddr(ctx.getRequest());
                            Integer userId = ((JSONObject)originalResult).getInteger("id");
                            if (!StringUtils.isEmpty(ip) && userId != null) {
                                //更新用户的最近登录时间和ip
                                logger.info("user with id " + userId + " has been login at " + DateFormatUtils.ISO_DATETIME_FORMAT.format(new Date()));
                            }
                        }

                    }
                } else { //其他响应结果则认为是错误
                    if (originalResult != null && originalResult instanceof JSONObject && ((JSONObject)originalResult).containsKey(OAuth2Exception.ERROR) && ((JSONObject)originalResult).containsKey(OAuth2Exception.DESCRIPTION)) { //转化错误码
                        String gatewayErrorCode = this.exceptionPropertyService.getProperty("fintech.insurance.authz-error-mapping." + ((JSONObject)originalResult).get(OAuth2Exception.ERROR));
                        if (gatewayErrorCode == null) {
                            gatewayErrorCode = "100202";
                        }
                        finalResult.put(GatewayResponseRestructureFilter.KEY_CODE, Integer.valueOf(gatewayErrorCode));
                        finalResult.put(GatewayResponseRestructureFilter.KEY_MSG, this.exceptionPropertyService.getProperty(gatewayErrorCode));
                    } else {
                        this.httpStatusConverter.convertHttpStatusError(finalResult, response);
                    }
                }
            } else {
                //如果结果中包含code和msg，则认为输出消息结构为统一的数据结构
                if (originalResult != null && originalResult instanceof JSONObject && ((JSONObject)originalResult).containsKey(KEY_CODE)) {
                    ctx.setResponseBody(ToolUtils.toJsonString(originalResult));
                    return null;
                }
                this.httpStatusConverter.convertHttpStatusError(finalResult, response);
            }
            finalResult.put(KEY_DATA, originalResult == null ? originalBody : originalResult);
            ctx.setResponseBody(ToolUtils.toJsonString(finalResult));
            ctx.setResponseStatusCode(HttpStatus.OK.value());
            ctx.getResponse().setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        } catch (IOException e) {
            logger.debug("Fail to convert the response structure", e);
        }
        return null;
    }
}
