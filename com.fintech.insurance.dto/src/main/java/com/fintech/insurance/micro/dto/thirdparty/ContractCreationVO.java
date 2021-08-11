package com.fintech.insurance.micro.dto.thirdparty;

import javax.validation.constraints.NotNull;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/11/25 16:41
 */
public class ContractCreationVO {

    /**
     * 合同的客户方账户标识， 客户的身份证号码
     */
    @NotNull(message="106301")
    private String userAccountId;

    /**
     * 客户手机号码， 由于一个用户可能会有多个手机号码
     */
    @NotNull(message="106301")
    private String mobile;

    /**
     * 合同模板名称
     */
    @NotNull(message="106301")
    private String contractTemplateName;

    private BestsignContractDataVO contractTemplateVO;

    public String getUserAccountId() {
        return userAccountId;
    }

    public void setUserAccountId(String userAccountId) {
        this.userAccountId = userAccountId;
    }

    public BestsignContractDataVO getContractTemplateVO() {
        return contractTemplateVO;
    }

    public void setContractTemplateVO(BestsignContractDataVO contractTemplateVO) {
        this.contractTemplateVO = contractTemplateVO;
    }

    public String getContractTemplateName() {
        return contractTemplateName;
    }

    public void setContractTemplateName(String contractTemplateName) {
        this.contractTemplateName = contractTemplateName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
