package com.fintech.insurance.micro.dto.customer;


import javax.persistence.Column;
import java.io.Serializable;

/**
 * @Author: Clayburn
 * @Description: 客户银行卡VO
 * @Date: 2017/11/9 16:51
 */
public class CustomerLoanBankVO implements Serializable {
    //客户账户id
    private Integer id;
    // 承保公司id
    private Integer companyId;
    // 承保公司名称
    private String companyName;
    // 承保支行id
    private Integer branchId;
    // 承保支部名称
    private String branchName;
    //放款帐户类型
    private String loanAccountType;
    //放款帐户号
    private String loanAccountNumber;
    //放款帐户银行编码
    private String loanAccountBank;
    //放款银行名称
    private String loanAccountBankBranch;
    //放款帐户名称型
    private String loanAccountName;

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

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public String getLoanAccountType() {
        return loanAccountType;
    }

    public void setLoanAccountType(String loanAccountType) {
        this.loanAccountType = loanAccountType;
    }

    public String getLoanAccountNumber() {
        return loanAccountNumber;
    }

    public void setLoanAccountNumber(String loanAccountNumber) {
        this.loanAccountNumber = loanAccountNumber;
    }

    public String getLoanAccountBank() {
        return loanAccountBank;
    }

    public void setLoanAccountBank(String loanAccountBank) {
        this.loanAccountBank = loanAccountBank;
    }

    public String getLoanAccountBankBranch() {
        return loanAccountBankBranch;
    }

    public void setLoanAccountBankBranch(String loanAccountBankBranch) {
        this.loanAccountBankBranch = loanAccountBankBranch;
    }

    public String getLoanAccountName() {
        return loanAccountName;
    }

    public void setLoanAccountName(String loanAccountName) {
        this.loanAccountName = loanAccountName;
    }
}
