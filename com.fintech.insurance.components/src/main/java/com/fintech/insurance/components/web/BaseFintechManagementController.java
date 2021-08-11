package com.fintech.insurance.components.web;

import com.fintech.insurance.commons.constants.GatewayFintechHeaders;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @Description: 运营平台控制器的基类
 * @Author: Yong Li
 * @Date: 2017/12/15 16:33
 */
public class BaseFintechManagementController {

    /**
     * 获取当前用户的登录名
     * @return 当前用户的登录名，如果为非登录接口则返回为null
     */
    public static String getCurrentLoginUser() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader(GatewayFintechHeaders.PRINCIPAL);
    }

    /**
     * 获取当前登录用户的id
     * @return 当前用户的id，如果为非登录接口则返回为0
     */
    public static Integer getCurrentLoginUserId() {
        String idValue = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader(GatewayFintechHeaders.PRINCIPAL_ID);
        if (StringUtils.isEmpty(idValue)) {
            return 0;
        } else {
            return Integer.valueOf(idValue);
        }
    }



}
