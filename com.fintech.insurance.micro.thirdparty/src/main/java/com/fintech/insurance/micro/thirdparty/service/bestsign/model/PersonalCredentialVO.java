package com.fintech.insurance.micro.thirdparty.service.bestsign.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.fintech.insurance.micro.thirdparty.service.bestsign.enums.IdentityType;

/**
 * @Description: 个人用户设置证件信息，该证件信息用于申请数字证书
 * @Author: Yong Li
 * @Date: 2017/11/20 20:20
 */
public class PersonalCredentialVO extends AbstractCredentialVO {

    /**
     * 证件号 需要和证件上登记的号码一致
     */
    @JSONField(name = "identity")
    private String identity;
    /**
     * 证件类型
     */
    private IdentityType identityType = IdentityType.RESIDENT_ID_CARD;

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    @JSONField(name="identityType")
    public String getIdentityTypeCode() {
        return identityType.getCode();
    }

    @JSONField(name="identityType")
    public void setIdentityTypeCode(String code) {
        this.identityType = IdentityType.getByCode(code);
    }

    @JSONField(serialize = false)
    public IdentityType getIdentityType() {
        return identityType;
    }

    @JSONField(deserialize = false)
    public void setIdentityType(IdentityType identityType) {
        this.identityType = identityType;
    }
}
