package com.fintech.insurance.micro.dto.support;

import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * @Author: Clayburn
 * @Description: 公司支部VO
 * @Date: 2017/11/9 10:21
 */
public class BranchVO implements Serializable {
    // 支部id
    private Integer id;
    // 所属公司id
    private Integer companyId;
    // 所属公司名称
    private String companyName;
    // 支部名称
    @NotBlank(message = "102001")
    private String branchName;
    // 开户账户
    @NotBlank(message = "102001")
    private String accountName;
    // 银行账号
    @NotBlank(message = "102001")
    private String accountNum;
    // 开户行
    @NotBlank(message = "102001")
    private String accountBank;
    // 开户支行
    @NotBlank(message = "102001")
    private String accountBranch;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNum() {
        return accountNum;
    }

    public void setAccountNum(String accountNum) {
        this.accountNum = accountNum;
    }

    public String getAccountBank() {
        return accountBank;
    }

    public void setAccountBank(String accountBank) {
        this.accountBank = accountBank;
    }

    public String getAccountBranch() {
        return accountBranch;
    }

    public void setAccountBranch(String accountBranch) {
        this.accountBranch = accountBranch;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
