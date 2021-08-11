package com.fintech.insurance.components.web.interceptor;

import com.alibaba.fastjson.JSON;
import com.fintech.insurance.commons.beans.BaseTokenSignConfigBean;
import com.fintech.insurance.commons.constants.JWTConstant;
import com.fintech.insurance.commons.constants.MockConstants;
import com.fintech.insurance.commons.enums.SystemProfile;
import com.fintech.insurance.commons.enums.UserType;
import com.fintech.insurance.commons.utils.JWTUtils;
import com.fintech.insurance.commons.utils.URLUtils;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.annotation.RequireWechatLogin;
import com.fintech.insurance.components.web.BaseFintechWechatController;
import com.fintech.insurance.components.web.context.FInsuranceApplicationContext;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 微信端接口的拦截器，用于检查微信端的登录授权
 */
public class WxMpTokenInterceptor extends BaseTokenSignConfigBean implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(WxMpTokenInterceptor.class);

    private Environment environment;

    private AntPathMatcher pathMatcher = new AntPathMatcher();

    public WxMpTokenInterceptor(Environment environment) {
        this.environment = environment;
    }

    /**
     * 用于缓存请求地址的授权
     */
    private static final Map<String, Boolean> REQUEST_LOGIN_VALIDATION_CACHE = new ConcurrentHashMap<>();

    /**
     * This implementation always returns {@code true}.
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        //仅仅处理来自于微信的请求
        if (this.isRequestFromWxPlatform(handler)) {
            SystemProfile currentProfile = FInsuranceApplicationContext.getSystemProfile();
            if (currentProfile == null || SystemProfile.DEFAULT.getCode().equals(currentProfile.getCode()) || SystemProfile.DEV.getCode().equals(currentProfile.getCode()) || SystemProfile.JUNIT.getCode().equals((currentProfile.getCode()))) {
                return this.preHandle4DevEnvironment(request, response, handler);
            } else {
                return this.preHandle4NoneDevEnvironment(request, response, handler);
            }
        } else {
            //如果请求不来自于微信则无需处理
            return true;
        }
    }

    /**
     * 联调或junit环境下的处理
     * @param request
     * @param response
     * @param handler
     * @return
     */
    private boolean preHandle4DevEnvironment(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //检查_mock参数是否存在
        String mockEnabledValue = request.getParameter(MockConstants.PARAM_MOCK_ENABLED);
        //如果mockEnabledValue的值为空或者0时，标识不mock
        if (mockEnabledValue == null || mockEnabledValue.trim().equals("") || mockEnabledValue.trim().equals("0")) {
            return this.preHandle4NoneDevEnvironment(request, response, handler);
        } else {
            //mock之前检查用户的id是否设置完整
            String mockUserIdString = request.getParameter(MockConstants.PARAM_MOCK_USER_ID);
            String mockAppId = request.getParameter(MockConstants.PARAM_MOCK_APP_ID);
            Integer mockUserId = null;
            if (StringUtils.isNotEmpty(mockUserIdString)) {
                try {
                    mockUserId = Integer.valueOf(mockUserIdString);
                } catch (Exception e) {
                    logger.error("Fail to convert the " + mockUserIdString + " to a valid integer value");
                }
            }
            if (mockUserId == null || StringUtils.isEmpty(mockAppId)) {
                this.writeFintechResponseToServletResponse(response, 106222, null);
                return false;
            } else {
                String userType = this.getRequestUserType(request);
                Map<String, Object> context = new HashMap<>();
                context.put(JWTConstant.TOKEN_TYPE, MockConstants.MOCK_TOKEN_TYPE);
                context.put(JWTConstant.USER_TYPE, userType);
                context.put(JWTConstant.APPID, mockAppId);
                context.put(JWTConstant.USER_ID, mockUserIdString);
                FInsuranceApplicationContext.JWT_CONTEXT.set(context);
                return true;
            }
        }
    }

    /**
     * test或者product环境下的处理逻辑
     * @param request
     * @param response
     * @param handler
     * @return
     */
    private boolean preHandle4NoneDevEnvironment(HttpServletRequest request, HttpServletResponse response, Object handler) {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        //检查当前请求中是否包含了授权信息 Authorization头，token类型为Bearer
        String tokenHeader = this.getTokenHeader(request);
        if (StringUtils.isEmpty(tokenHeader)) { //如果当前请求中不存在token设置，则检查当前handler是否需要授权才能访问
            if (this.isWechatLoginRequired(request, handlerMethod)) {
                //返回前端结果，需要登录才能访问
                this.writeFintechResponseToServletResponse(response, 106214, null);
                return false;
            } else {
                return true;
            }
        } else {
            //检查授权头是否为合法的头
            int tokenPrefixLength = JWTConstant.TOKEN_PREFIX.length();
            String tokenType = tokenHeader.length() <= tokenPrefixLength ? "" : tokenHeader.substring(0, tokenPrefixLength);
            if (!JWTConstant.TOKEN_PREFIX.equalsIgnoreCase(tokenType)) {
                this.writeFintechResponseToServletResponse(response, 106214, null);
                return false;
            } else {
                String tokenValue = tokenHeader.substring(tokenPrefixLength).trim();
                Jws<Claims> jwtToken = JWTUtils.decode(tokenValue, this.mpTokenSignKey);
                if (jwtToken == null || jwtToken.getHeader() == null || jwtToken.getBody() == null
                        || (jwtToken.getBody() != null && jwtToken.getBody().getNotBefore() != null && jwtToken.getBody().getNotBefore().getTime() > System.currentTimeMillis())
                        || (jwtToken.getBody() != null && StringUtils.isNotEmpty(jwtToken.getBody().getIssuer()) && !jwtToken.getBody().getIssuer().equalsIgnoreCase(this.mpTokenIssuer))) {
                    //token解析失败或者token已过期或者签发者不一致
                    this.writeFintechResponseToServletResponse(response, 106214, null);
                    return false;
                } else {
                    //检查token中的userType以及当前请求的路径，customer和channel的token不能混合使用
                    String userTypeInToken = (String) jwtToken.getHeader().get(JWTConstant.USER_TYPE);
                    String userTypeInRequest = this.getRequestUserType(request);
                    if (!userTypeInRequest.equalsIgnoreCase(userTypeInToken)) {
                        this.writeFintechResponseToServletResponse(response, 106221, null);
                        return false;
                    } else {
                        Map<String, Object> context = new HashMap<>();
                        context.putAll(jwtToken.getHeader());
                        context.putAll(jwtToken.getBody());
                        FInsuranceApplicationContext.JWT_CONTEXT.set(context);
                        return true;
                    }
                }
            }
        }
    }

    /**
     * 获得请求的用户类型
     * @param request
     * @return
     */
    private String getRequestUserType(HttpServletRequest request) {
        String userType = "";
        if (this.pathMatcher.match(URLUtils.WECHAT_CUSTOMER_URL_PATTERN, request.getRequestURI())) {
            userType = UserType.CUSTOMER.getCode();
        } else if (this.pathMatcher.match(URLUtils.WECHAT_CHANNEL_URL_PATTERN, request.getRequestURI())) {
            userType = UserType.CHANNEL.getCode();
        }
        return userType;
    }

    /**
     * 将响应结果写入http servlet response中
     * @param response
     * @param code
     * @param data
     */
    private void writeFintechResponseToServletResponse(HttpServletResponse response, int code, Object data) {
        if (response != null) {
            FintechResponse result = null;
            if (code == 0) {
                result = FintechResponse.responseData(data);
            } else {
                result = FintechResponse.responseData(code, this.environment.getProperty(String.valueOf(code)), data);
            }
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            PrintWriter out = null;
            try {
                out = response.getWriter();
                out.append(JSON.toJSONString(result));
            } catch (IOException e) {
                logger.error("Fail to send response to http servlet response object, due to exception found", e);
            } finally {
                if (out != null) {
                    out.close();
                }
            }
        }
    }

    /**
     * 检查当前请求是否必须要求微信授权
     * @param handlerMethod
     * @return
     */
    private boolean isWechatLoginRequired(HttpServletRequest request, HandlerMethod handlerMethod) {
        String requestUrl = request.getRequestURL().toString();
        String method = request.getMethod();
        String key = method + " " + requestUrl;
        if (REQUEST_LOGIN_VALIDATION_CACHE.containsKey(key)) {
            return REQUEST_LOGIN_VALIDATION_CACHE.get(key);
        } else {
            boolean isLoginRequired = false;
            if (handlerMethod != null) {
                RequireWechatLogin loginAnnotation = AnnotationUtils.getAnnotation(handlerMethod.getBeanType(), RequireWechatLogin.class);
                if (loginAnnotation == null) {
                    loginAnnotation = AnnotationUtils.getAnnotation(handlerMethod.getMethod(), RequireWechatLogin.class);
                }
                isLoginRequired = (loginAnnotation != null);
            }
            REQUEST_LOGIN_VALIDATION_CACHE.put(key, isLoginRequired);
            return isLoginRequired;
        }
    }

    /**
     * 检查请求是否来自于微信
     * @param handler
     * @return
     */
    private boolean isRequestFromWxPlatform(Object handler) {
        if (handler == null || !(handler instanceof HandlerMethod)) {
            return false;
        } else {
            return ((HandlerMethod) handler).getBean() instanceof BaseFintechWechatController;
        }
    }

    /**
     * 检查当前请求头中是否包含了token，如果包含token则校验token是否有效（并不关注当前handler是否需要微信授权登录）
     * @param request
     * @return
     */
    private String getTokenHeader(HttpServletRequest request) {
        String tokenHeader = request.getHeader(JWTConstant.HEADER);
        if (StringUtils.isEmpty(tokenHeader)) {
            return "";
        } else {
            return tokenHeader.trim();
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
