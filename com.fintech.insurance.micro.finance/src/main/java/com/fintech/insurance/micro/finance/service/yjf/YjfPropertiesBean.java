package com.fintech.insurance.micro.finance.service.yjf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * @Description: 易极付配置属性
 * @Author: Yong Li
 * @Date: 2017/12/11 20:07
 */
@Component
public class YjfPropertiesBean {

    /**
     * 商户ID
     */
    @Value("${fintech.yjf.partnerId}")
    private String partnerId;

    /**
     * 商户加密秘钥
     */
    @Value("${fintech.yjf.privateKey}")
    private String privateKey;


    /**
     * 易极付访问地址
     */
    @Value("${fintech.yjf.gatewayUrl}")
    private String gatewayUrl;


    @Value("${fintech.yjf.debtNotifiyUrl}")
    private String debtNotifyUrl;

    @Value("${fintech.yjf.dailyDebtTimes}")
    private Integer debtTimes;

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getGatewayUrl() {
        return gatewayUrl;
    }

    public void setGatewayUrl(String gatewayUrl) {
        this.gatewayUrl = gatewayUrl;
    }

    public String getDebtNotifyUrl() {
        return debtNotifyUrl;
    }

    public void setDebtNotifyUrl(String debtNotifyUrl) {
        this.debtNotifyUrl = debtNotifyUrl;
    }


    public Integer getDebtTimes() {
        return debtTimes;
    }

    public void setDebtTimes(Integer debtTimes) {
        this.debtTimes = debtTimes;
    }
}
