package com.fintech.insurance.micro.thirdparty.service.bestsign.model;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/11/21 18:50
 */
public class UserCertVO {

    /**
     * 证书编号
     */
    @JSONField(name = "certId")
    private String certNum;

    /**
     * 证书类型
     */
    @JSONField(name = "certType")
    private String certType;

    public String getCertNum() {
        return certNum;
    }

    public void setCertNum(String certNum) {
        this.certNum = certNum;
    }

    public String getCertType() {
        return certType;
    }

    public void setCertType(String certType) {
        this.certType = certType;
    }
}
