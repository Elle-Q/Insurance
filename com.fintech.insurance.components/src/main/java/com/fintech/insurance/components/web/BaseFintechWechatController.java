package com.fintech.insurance.components.web;

import com.fintech.insurance.components.web.context.FInsuranceApplicationContext;

/**
 * 微服务微信端接口基类
 */
public abstract class BaseFintechWechatController {

    /**
     * 获取当前登录用户的id
     * @return
     */
    public Integer getCurrentUserId() {
        return FInsuranceApplicationContext.getCurrentUserId();
    }

    /**
     * 返回当前用户的类型
     * @return
     */
    public String getCurrentUserType() {
        return FInsuranceApplicationContext.getCurrentUserType();
    }

    /**
     * 当前用户是否为客户
     * @return
     */
    public boolean isCustomer() {
        return FInsuranceApplicationContext.isCustomer();
    }

    /**
     * 如果当前用户为渠道用户则获取当前渠道的渠道编码
     * @return
     */
    public String getCurrentUserChannelCode() {
        return FInsuranceApplicationContext.getCurrentUserChannelCode();
    }

    /**
     * 如果当前用户为系统内部用户则获取当前用户所在的公司id
     * @return
     */
    public Integer getCurrentUserOrganizationId() {
        return FInsuranceApplicationContext.getCurrentUserOrganizationId();
    }

    /**
     * 判断当前用户是否为渠道admin
     * @return
     */
    public boolean isCurrentUserChannelAdmin() {
        return FInsuranceApplicationContext.isCurrentUserChannelAdmin();
    }

}
