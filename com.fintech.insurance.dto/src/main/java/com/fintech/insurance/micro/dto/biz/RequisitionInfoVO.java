package com.fintech.insurance.micro.dto.biz;

import com.fintech.insurance.commons.annotations.ImageUrl;
import com.fintech.insurance.micro.dto.customer.CustomerVO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Clayburn
 * @Description: 申请资料VO
 * @Date: 2017/12/12 15:01
 */
public class RequisitionInfoVO implements Serializable {
    // 申请单id
    private Integer requisitionId;
    // 产品类型
    private String productType;
    // 企业信息
    private EnterpriseInfo enterpriseInfo;
    // 个人信息
    private CustVO customerVO;
    // 保险公司信息
    private InsuranceCompanyInfo insuranceCompanyInfo;
    // 车辆信息
    private List<CarInfo> carInfo;
    // 其他材料
    @ImageUrl
    private List<String> others = new ArrayList<>();
    public List<String> othersUrl = new ArrayList<>();
    public List<String> othersNarrowUrl = new ArrayList<>();

    public List<String> getOthersUrl() {
        return othersUrl;
    }

    public void setOthersUrl(List<String> othersUrl) {
        this.othersUrl = othersUrl;
    }

    public List<String> getOthersNarrowUrl() {
        return othersNarrowUrl;
    }

    public void setOthersNarrowUrl(List<String> othersNarrowUrl) {
        this.othersNarrowUrl = othersNarrowUrl;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public Integer getRequisitionId() {
        return requisitionId;
    }

    public void setRequisitionId(Integer requisitionId) {
        this.requisitionId = requisitionId;
    }

    public CustVO getCustomerVO() {
        return customerVO;
    }

    public void setCustomerVO(CustVO customerVO) {
        this.customerVO = customerVO;
    }

    public InsuranceCompanyInfo getInsuranceCompanyInfo() {
        return insuranceCompanyInfo;
    }

    public void setInsuranceCompanyInfo(InsuranceCompanyInfo insuranceCompanyInfo) {
        this.insuranceCompanyInfo = insuranceCompanyInfo;
    }

    public List<CarInfo> getCarInfo() {
        return carInfo;
    }

    public void setCarInfo(List<CarInfo> carInfo) {
        this.carInfo = carInfo;
    }

    public List<String> getOthers() {
        return others;
    }

    public void setOthers(List<String> others) {
        if (others != null) {
            this.others = others;
        }
    }

    public EnterpriseInfo getEnterpriseInfo() {
        return enterpriseInfo;
    }

    public void setEnterpriseInfo(EnterpriseInfo enterpriseInfo) {
        this.enterpriseInfo = enterpriseInfo;
    }
}
