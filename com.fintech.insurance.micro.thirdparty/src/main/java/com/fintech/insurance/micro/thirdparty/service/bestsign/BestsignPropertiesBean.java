package com.fintech.insurance.micro.thirdparty.service.bestsign;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: Yong Li
 * @Date: 2017/11/17 11:03
 */
@Component
public class BestsignPropertiesBean {

    /**
     * 开发者编号
     */
    @Value("${fintech.bestsign.developerId}")
    private String developerId;

    /**
     * 签名串
     */
    @Value("${fintech.bestsign.securityKey}")
    private String privateKey;

    /**
     * 签名串计算方法
     */
    @Value("${fintech.bestsign.signType}")
    private String signType;

    /**
     * 请求主机
     */
    @Value("${fintech.bestsign.api-server}")
    private String host;

    /**
     * 保险公司在上上签的企业账号
     */
    @Value("${fintech.bestsign.enterprise.account}")
    private String enterpriseAccount;

    /**
     * 保险公司在上上签申请的企业账号数字证书编号
     */
    @Value("${fintech.bestsign.enterprise.cert}")
    private String enterpriseCert;

    /**
     * 出借人在上上签的个人账号
     */
    @Value("${fintech.bestsign.owner1.account}")
    private String lenderOwnerAccount;

    /**
     * 出借人在上上签申请的个人账号数字证书编号
     */
    @Value("${fintech.bestsign.owner1.cert}")
    private String lenderOwnerCert;

    public String getDeveloperId() {
        return developerId;
    }

    public void setDeveloperId(String developerId) {
        this.developerId = developerId;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getEnterpriseAccount() {
        return enterpriseAccount;
    }

    public void setEnterpriseAccount(String enterpriseAccount) {
        this.enterpriseAccount = enterpriseAccount;
    }

    public String getEnterpriseCert() {
        return enterpriseCert;
    }

    public void setEnterpriseCert(String enterpriseCert) {
        this.enterpriseCert = enterpriseCert;
    }

    public String getLenderOwnerAccount() {
        return lenderOwnerAccount;
    }

    public void setLenderOwnerAccount(String lenderOwnerAccount) {
        this.lenderOwnerAccount = lenderOwnerAccount;
    }

    public String getLenderOwnerCert() {
        return lenderOwnerCert;
    }

    public void setLenderOwnerCert(String lenderOwnerCert) {
        this.lenderOwnerCert = lenderOwnerCert;
    }
}
