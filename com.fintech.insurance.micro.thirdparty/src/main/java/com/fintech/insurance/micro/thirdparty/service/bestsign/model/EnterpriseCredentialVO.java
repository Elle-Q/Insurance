package com.fintech.insurance.micro.thirdparty.service.bestsign.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.fintech.insurance.micro.thirdparty.service.bestsign.enums.IdentityType;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/11/20 20:40
 */
public class EnterpriseCredentialVO extends AbstractCredentialVO {

    /**
     * 工商注册号
     * 企业用户设置. 如果是三证合一，regCode/taxCode/orgCode三个填一样的值
     */
    @JSONField(name = "regCode")
    private String regCode;

    /**
     * 组织机构代码
     * 企业用户设置. 如果是三证合一，regCode/taxCode/orgCode三个填一样的值
     */
    @JSONField(name = "orgCode")
    private String orgCode;

    /**
     * 税务登记证号
     * 企业用户设置. 如果是三证合一，regCode/taxCode/orgCode三个填一样的值
     */
    @JSONField(name = "taxCode")
    private String taxCode;

    /**
     * 法人代表姓名
     * 法人代表姓名或经办人姓名
     */
    @JSONField(name = "legalPerson")
    private String legalPersonName;

    /**
     * 法人代表证件号
     * 法人代表证件号或经办人证件号
     */
    @JSONField(name = "legalPersonIdentity")
    private String legalPersonIdentity;

    /**
     * 法人代表证件类型: 法人代表证件类型或经办人证件类型，与“legalPersonIdentity”要匹配
     */
    //@JSONField(name = "legalPersonIdentityType")
    private IdentityType legalPersonIdentityType;

    /**
     * 法人代表手机号: 法人代表手机号或经办人手机号
     */
    @JSONField(name = "legalPersonMobile")
    private String legalPersonMobile;

    public String getRegCode() {
        return regCode;
    }

    public void setRegCode(String regCode) {
        this.regCode = regCode;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getLegalPersonIdentity() {
        return legalPersonIdentity;
    }

    public void setLegalPersonIdentity(String legalPersonIdentity) {
        this.legalPersonIdentity = legalPersonIdentity;
    }

    @JSONField(name = "legalPersonIdentityType")
    public String getLegalPersonIdentityTypeCode() {
        return this.legalPersonIdentityType.getCode();
    }

    @JSONField(name = "legalPersonIdentityType")
    public void setLegalPersonIdentityTypeCode(String code) {
        this.legalPersonIdentityType = IdentityType.getByCode(code);
    }

    @JSONField(serialize = false)
    public IdentityType getLegalPersonIdentityType() {
        return legalPersonIdentityType;
    }

    @JSONField(deserialize = false)
    public void setLegalPersonIdentityType(IdentityType legalPersonIdentityType) {
        this.legalPersonIdentityType = legalPersonIdentityType;
    }

    public String getLegalPersonName() {
        return legalPersonName;
    }

    public void setLegalPersonName(String legalPersonName) {
        this.legalPersonName = legalPersonName;
    }

    public String getLegalPersonMobile() {
        return legalPersonMobile;
    }

    public void setLegalPersonMobile(String legalPersonMobile) {
        this.legalPersonMobile = legalPersonMobile;
    }
}
