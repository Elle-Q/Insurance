package com.fintech.insurance.micro.vo.wechat;

import com.fintech.insurance.commons.annotations.ImageUrl;

import java.io.Serializable;
import java.util.List;

//用户业务单
public class WeChatCustomerRequisitionVO implements Serializable{
    //业务id
    private Integer id;

    //渠道用户
    private Integer channelUserId;

    //渠道code
    private String channelCode;

    //产品id
    private Integer productId;

    //客户info id
    private Integer customerAccountInfoId = 0;

    //客户id
    private Integer customerId;

    //客户名称
    private String customerName;

    //客户手机号码
    private String customerMobile;

    //企业名称
    private String companyName;

    //客户身份证
    private String idNumber;

    //其他材料
    @ImageUrl
    private List<String> otherResourceList;

    //其他材料url
    public List<String> otherResourceListUrl;

    //其他材料缩略图url
    public List<String> otherResourceListNarrowUrl;

    //承保公司id
    private Integer companyId;

    //承保分支id
    private Integer branchId;

    //产品类型
    private String productType;


    //客户指定分期
    private Integer businessDuration;

    //手持身份证照片url
    public String idCardEvidenceUrl;

    //手持身份证照片缩略图url
    public String idCardEvidenceNarrowUrl;

    //手持身份证照片
    @ImageUrl
    private String idCardEvidence;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getChannelUserId() {
        return channelUserId;
    }

    public void setChannelUserId(Integer channelUserId) {
        this.channelUserId = channelUserId;
    }

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

    public Integer getCustomerAccountInfoId() {
        return customerAccountInfoId;
    }

    public void setCustomerAccountInfoId(Integer customerAccountInfoId) {
        this.customerAccountInfoId = customerAccountInfoId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public List<String> getOtherResourceList() {
        return otherResourceList;
    }

    public void setOtherResourceList(List<String> otherResourceList) {
        this.otherResourceList = otherResourceList;
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

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
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

    public List<String> getOtherResourceListUrl() {
        return otherResourceListUrl;
    }

    public void setOtherResourceListUrl(List<String> otherResourceListUrl) {
        this.otherResourceListUrl = otherResourceListUrl;
    }

    public List<String> getOtherResourceListNarrowUrl() {
        return otherResourceListNarrowUrl;
    }

    public void setOtherResourceListNarrowUrl(List<String> otherResourceListNarrowUrl) {
        this.otherResourceListNarrowUrl = otherResourceListNarrowUrl;
    }

    public String getIdCardEvidenceUrl() {
        return idCardEvidenceUrl;
    }

    public void setIdCardEvidenceUrl(String idCardEvidenceUrl) {
        this.idCardEvidenceUrl = idCardEvidenceUrl;
    }

    public String getIdCardEvidenceNarrowUrl() {
        return idCardEvidenceNarrowUrl;
    }

    public void setIdCardEvidenceNarrowUrl(String idCardEvidenceNarrowUrl) {
        this.idCardEvidenceNarrowUrl = idCardEvidenceNarrowUrl;
    }
}
