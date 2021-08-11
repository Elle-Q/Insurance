package com.fintech.insurance.micro.vo.wechat;

import java.io.Serializable;
import java.util.List;

public class WeChatInsuranceBranchVO implements Serializable {

    //支部id
    private Integer branchId;

    //支部名称
    private String branchName;

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

}
