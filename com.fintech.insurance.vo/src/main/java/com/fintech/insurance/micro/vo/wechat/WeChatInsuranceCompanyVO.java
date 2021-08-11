package com.fintech.insurance.micro.vo.wechat;

import java.io.Serializable;
import java.util.List;

public class WeChatInsuranceCompanyVO implements Serializable {

    //公司id
    private Integer companyId;

    //公司名称
    private String companyName;

    //承保支部
    private List<WeChatInsuranceBranchVO> insuranceBranchVOList;

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public List<WeChatInsuranceBranchVO> getInsuranceBranchVOList() {
        return insuranceBranchVOList;
    }

    public void setInsuranceBranchVOList(List<WeChatInsuranceBranchVO> insuranceBranchVOList) {
        this.insuranceBranchVOList = insuranceBranchVOList;
    }
}
