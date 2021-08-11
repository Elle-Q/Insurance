package com.fintech.insurance.micro.dto.biz;

import com.fintech.insurance.commons.annotations.FinanceDataPoint;
import com.fintech.insurance.commons.annotations.ImageUrl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class RequisitionDetailVO implements Serializable {

    //车辆id
    private Integer id;

    //合同id
    private Integer contractId;
    //车牌号
    private String carNumber;

    //分期期数
    private Integer duration;

    //借款金额
    @FinanceDataPoint
    private Double borrowAmount;

    //产品类型
    private String productType;

    //商业保险单号
    private String commercialInsuranceNumber;

    //商业保险金额
    @FinanceDataPoint
    private Double commercialInsuranceAmount;

    //商业保险时间起
    private Date commercialInsuranceStart;

    //商业保险时间止
    private Date commercialInsuranceEnd;

    //交强险单号
    private String compulsoryInsuranceNumber;

    //交强险金额
    @FinanceDataPoint
    private Double compulsoryInsuranceAmount;

    //交强险时间起
    private Date compulsoryInsuranceStart;

    //交强险时间止
    private Date compulsoryInsuranceEnd;

    //车船税金额
    @FinanceDataPoint
    private Double taxAmount;

    //行驶证
    private String drivingLicense;

    //行驶证正本照片
    @ImageUrl
    private String drivingLicenseMain;
    public String drivingLicenseMainUrl;
    public String drivingLicenseMainNarrowUrl;

    //行驶证副本照片
    @ImageUrl
    private String drivingLicenseAttach;
    public String drivingLicenseAttachUrl;
    public String drivingLicenseAttachNarrowUrl;

    //车辆资源图片
    private List<RequisitionDetailResourceVO> resource;


    public String getDrivingLicenseMainUrl() {
        return drivingLicenseMainUrl;
    }

    public void setDrivingLicenseMainUrl(String drivingLicenseMainUrl) {
        this.drivingLicenseMainUrl = drivingLicenseMainUrl;
    }

    public String getDrivingLicenseMainNarrowUrl() {
        return drivingLicenseMainNarrowUrl;
    }

    public void setDrivingLicenseMainNarrowUrl(String drivingLicenseMainNarrowUrl) {
        this.drivingLicenseMainNarrowUrl = drivingLicenseMainNarrowUrl;
    }

    public String getDrivingLicenseAttachUrl() {
        return drivingLicenseAttachUrl;
    }

    public void setDrivingLicenseAttachUrl(String drivingLicenseAttachUrl) {
        this.drivingLicenseAttachUrl = drivingLicenseAttachUrl;
    }

    public String getDrivingLicenseAttachNarrowUrl() {
        return drivingLicenseAttachNarrowUrl;
    }

    public void setDrivingLicenseAttachNarrowUrl(String drivingLicenseAttachNarrowUrl) {
        this.drivingLicenseAttachNarrowUrl = drivingLicenseAttachNarrowUrl;
    }

    public Integer getContractId() {
        return contractId;
    }

    public void setContractId(Integer contractId) {
        this.contractId = contractId;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public List<RequisitionDetailResourceVO> getResource() {
        return resource;
    }

    public void setResource(List<RequisitionDetailResourceVO> resource) {
        this.resource = resource;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCommercialInsuranceNumber() {
        return commercialInsuranceNumber;
    }

    public void setCommercialInsuranceNumber(String commercialInsuranceNumber) {
        this.commercialInsuranceNumber = commercialInsuranceNumber;
    }

    public Double getCommercialInsuranceAmount() {
        return commercialInsuranceAmount;
    }

    public void setCommercialInsuranceAmount(Double commercialInsuranceAmount) {
        this.commercialInsuranceAmount = commercialInsuranceAmount;
    }

    public Date getCommercialInsuranceStart() {
        return commercialInsuranceStart;
    }

    public void setCommercialInsuranceStart(Date commercialInsuranceStart) {
        this.commercialInsuranceStart = commercialInsuranceStart;
    }

    public Date getCommercialInsuranceEnd() {
        return commercialInsuranceEnd;
    }

    public void setCommercialInsuranceEnd(Date commercialInsuranceEnd) {
        this.commercialInsuranceEnd = commercialInsuranceEnd;
    }

    public String getCompulsoryInsuranceNumber() {
        return compulsoryInsuranceNumber;
    }

    public void setCompulsoryInsuranceNumber(String compulsoryInsuranceNumber) {
        this.compulsoryInsuranceNumber = compulsoryInsuranceNumber;
    }

    public Double getCompulsoryInsuranceAmount() {
        return compulsoryInsuranceAmount;
    }

    public void setCompulsoryInsuranceAmount(Double compulsoryInsuranceAmount) {
        this.compulsoryInsuranceAmount = compulsoryInsuranceAmount;
    }

    public Date getCompulsoryInsuranceStart() {
        return compulsoryInsuranceStart;
    }

    public void setCompulsoryInsuranceStart(Date compulsoryInsuranceStart) {
        this.compulsoryInsuranceStart = compulsoryInsuranceStart;
    }

    public Date getCompulsoryInsuranceEnd() {
        return compulsoryInsuranceEnd;
    }

    public void setCompulsoryInsuranceEnd(Date compulsoryInsuranceEnd) {
        this.compulsoryInsuranceEnd = compulsoryInsuranceEnd;
    }

    public Double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(Double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public String getDrivingLicenseMain() {
        return drivingLicenseMain;
    }

    public void setDrivingLicenseMain(String drivingLicenseMain) {
        this.drivingLicenseMain = drivingLicenseMain;
    }

    public String getDrivingLicenseAttach() {
        return drivingLicenseAttach;
    }

    public void setDrivingLicenseAttach(String drivingLicenseAttach) {
        this.drivingLicenseAttach = drivingLicenseAttach;
    }

    public String getDrivingLicense() {
        return drivingLicense;
    }

    public void setDrivingLicense(String drivingLicense) {
        this.drivingLicense = drivingLicense;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Double getBorrowAmount() {
        return borrowAmount;
    }

    public void setBorrowAmount(Double borrowAmount) {
        this.borrowAmount = borrowAmount;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }
}