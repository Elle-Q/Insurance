package com.fintech.insurance.micro.dto.biz;

import com.fintech.insurance.micro.dto.customer.CustomerLoanBankVO;

import javax.validation.constraints.NotNull;
import java.util.List;
//用户业务单
public class CustomerRequisitionVO extends BaseVO{

    //渠道code
    private String channelCode;

    //产品id
    @NotNull(message = "104107")
    private Integer productId;

    //客户id
    @NotNull(message = "104107")
    private Integer customerId;

    //客户账户info id
    private Integer customerAccountInfoId = 0;

    //其他材料
    private List<String> otherResourceList;

    //客户指定分期
    private Integer businessDuration;

    //手持身份证照片
    private String idCardEvidence;

    // 承保公司id
    private Integer companyId;

    // 承保支行id
    private Integer branchId;

    // 渠道用户id
    private Integer channelUserId;

    //放款帐户号
    private String loanAccountNumber;

    //是否渠道端申请
    private Boolean isChannelApplication;

    //客户银行卡信息
    private CustomerLoanBankVO customerLoanBankVO = new CustomerLoanBankVO();

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getCustomerAccountInfoId() {
        return customerAccountInfoId;
    }

    public void setCustomerAccountInfoId(Integer customerAccountInfoId) {
        this.customerAccountInfoId = customerAccountInfoId;
    }

    public List<String> getOtherResourceList() {
        return otherResourceList;
    }

    public void setOtherResourceList(List<String> otherResourceList) {
        this.otherResourceList = otherResourceList;
    }

    public Integer getBusinessDuration() {
        return businessDuration;
    }

    public void setBusinessDuration(Integer businessDuration) {
        this.businessDuration = businessDuration;
    }

    public String getIdCardEvidence() {
        return idCardEvidence;
    }

    public void setIdCardEvidence(String idCardEvidence) {
        this.idCardEvidence = idCardEvidence;
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

    public Integer getChannelUserId() {
        return channelUserId;
    }

    public void setChannelUserId(Integer channelUserId) {
        this.channelUserId = channelUserId;
    }

    public CustomerLoanBankVO getCustomerLoanBankVO() {
        return customerLoanBankVO;
    }

    public void setCustomerLoanBankVO(CustomerLoanBankVO customerLoanBankVO) {
        this.customerLoanBankVO = customerLoanBankVO;
    }

    public String getLoanAccountNumber() {
        return loanAccountNumber;
    }

    public void setLoanAccountNumber(String loanAccountNumber) {
        this.loanAccountNumber = loanAccountNumber;
    }

    public Boolean getChannelApplication() {
        return isChannelApplication;
    }

    public void setChannelApplication(Boolean channelApplication) {
        isChannelApplication = channelApplication;
    }
}
