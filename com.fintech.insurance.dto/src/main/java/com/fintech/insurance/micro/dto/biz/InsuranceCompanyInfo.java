package com.fintech.insurance.micro.dto.biz;

import java.io.Serializable;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/12/8 15:28
 */
public class InsuranceCompanyInfo implements Serializable {
    // 保险公司id
    private Integer companyId;
    // 承保公司
    private String companyName;
    // 承保支部
    private String branchName;

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

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }
}
