package com.fintech.insurance.components.web.context;

import com.fintech.insurance.commons.constants.JWTConstant;
import com.fintech.insurance.commons.enums.SystemProfile;
import com.fintech.insurance.commons.enums.UserType;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Map;

/**
 * H5请求的上下文环境
 */
@Configuration
public class FInsuranceApplicationContext implements ApplicationContextAware  {

    private static final Logger logger = LoggerFactory.getLogger(FInsuranceApplicationContext.class);

    /**
     * 上下文环境，请求执行的拦截器中设置当前请求的上下文
     */
    public static final ThreadLocal<Map<String, Object>> JWT_CONTEXT = new ThreadLocal<>();


    /**
     * 获取当前上下文环境
     * @return
     */
    public static Map<String, Object> currentContext() {
        return JWT_CONTEXT.get();
    }

    private static ApplicationContext applicationContext;

    /**
     * 获取当前登录用户的用户名
     * @return
     */
    public static String getCurrentUserName() {
        Map<String, Object> context = currentContext();
        if (context == null) {
            return null;
        } else {
            return (String) context.get(JWTConstant.USER_NAME);
        }
    }

    /**
     * 获取当前登录用户的id
     * @return
     */
    public static Integer getCurrentUserId() {
        Map<String, Object> context = currentContext();
        if (context == null) {
            return null;
        } else {
            String idString = (String) context.get(JWTConstant.USER_ID);
            Integer id = null;
            if (StringUtils.isNotEmpty(idString)) {
                try {
                    id = Integer.valueOf(idString);
                } catch (Exception e) {
                    logger.error("Fail to convert the id string to id with integer type", e);
                }

            }
            return id;
        }
    }

    /**
     * 返回当前用户的类型
     * @return
     */
    public static String getCurrentUserType() {
        Map<String, Object> context = currentContext();
        if (context == null) {
            return "";
        } else {
            return (String) context.get(JWTConstant.USER_TYPE);
        }
    }

    /**
     * 当前用户是否为客户
     * @return
     */
    public static boolean isCustomer() {
        String userType = getCurrentUserType();
        return !StringUtils.isEmpty(userType) && UserType.CUSTOMER.getCode().equalsIgnoreCase(userType);
    }

    /**
     * 当前用户是否为渠道
     * @return
     */
    public static boolean isChannel() {
        String userType = getCurrentUserType();
        return !StringUtils.isEmpty(userType) && UserType.CHANNEL.getCode().equalsIgnoreCase(userType);
    }


    /**
     * 如果当前用户为渠道用户则获取当前渠道的渠道编码
     * @return
     */
    public static String getCurrentUserChannelCode() {
        if (isCustomer()) {
            return "";
        } else {
            Map<String, Object> context = currentContext();
            if (context == null) {
                return "";
            }
            String channelCode = (String) context.get(JWTConstant.CHANNEL_CODE);
            return channelCode == null ? "" : channelCode;
        }
    }

    /**
     * 如果当前用户为系统内部用户则获取当前用户所在的公司id
     * @return
     */
    public static Integer getCurrentUserOrganizationId() {
        if (isCustomer()) {
            return null;
        } else {
            Map<String, Object> context = currentContext();
            if (context == null) {
                return null;
            }
            String orgId = (String) context.get(JWTConstant.CHANNEL_CODE);
            return orgId == null ? null : Integer.valueOf(orgId);
        }
    }

    /**
     * 判断当前用户是否为渠道admin
     * @return
     */
    public static boolean isCurrentUserChannelAdmin() {
        if (StringUtils.isNotEmpty(getCurrentUserChannelCode())) {
            Map<String, Object> context = currentContext();
            if (context == null) {
                return false;
            } else {
                String isAdminString = (String) context.get(JWTConstant.CHANNEL_ADMIN);
                return String.valueOf(1).equals(isAdminString);
            }
        } else {
            return false;
        }
    }

    /**
     * 获取系统当前的profile.
     * @return
     */
    public static SystemProfile getSystemProfile() {
        Environment environment = applicationContext.getEnvironment();
        return FInsuranceApplicationContext.getSystemProfile(environment);
    }

    public static SystemProfile getSystemProfile(Environment environment) {
        String[] activeProfiles = environment.getActiveProfiles();
        if (activeProfiles.length == 0) {
            return SystemProfile.DEFAULT;
        } else {
            return SystemProfile.codeOf(activeProfiles[0]);
        }
    }

    /**
     * 获取当前微服务的名称.
     * @return
     */
    public static String getMicroServiceName() {
        return applicationContext.getEnvironment().getProperty("spring.application.name");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        FInsuranceApplicationContext.applicationContext = applicationContext;
    }
}
