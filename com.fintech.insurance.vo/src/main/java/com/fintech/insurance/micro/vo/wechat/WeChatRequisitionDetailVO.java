package com.fintech.insurance.micro.vo.wechat;

import com.fintech.insurance.commons.annotations.FinanceDataPoint;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class WeChatRequisitionDetailVO implements Serializable {

    //车辆id
    private Integer id;

    //车牌号
    private String carNumber;

    //行驶证
    private String drivingLicense;

    //行驶证图片
    private List<String> drivingLicenseResource;

    //其他材料
    private List<String> otherMaterial;


    //商业保险单号
    private String commercialInsuranceNumber;

    //商业保险金额
    @FinanceDataPoint
    private Double commercialInsuranceAmount;

    //商业保险时间起
    private Date commercialInsuranceStart;

    //商业保险时间止
    private Date commercialInsuranceEnd;

    //商业保险图片资料
    private List<String> busiInsurance;

    //交强险单号
    private String compulsoryInsuranceNumber;

    //交强险金额
    @FinanceDataPoint
    private Double compulsoryInsuranceAmount = 0.0;

    //交强险时间起
    private Date compulsoryInsuranceStart;

    //交强险时间止
    private Date compulsoryInsuranceEnd;

    //交强险材料
    private List<String> driveInsurance;

    //车船税
    private List<String> taxResource;

    //业务id
    private Integer requisitionId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getDrivingLicense() {
        return drivingLicense;
    }

    public void setDrivingLicense(String drivingLicense) {
        this.drivingLicense = drivingLicense;
    }

    public List<String> getDrivingLicenseResource() {
        return drivingLicenseResource;
    }

    public void setDrivingLicenseResource(List<String> drivingLicenseResource) {
        this.drivingLicenseResource = drivingLicenseResource;
    }

    public List<String> getOtherMaterial() {
        return otherMaterial;
    }

    public void setOtherMaterial(List<String> otherMaterial) {
        this.otherMaterial = otherMaterial;
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

    public List<String> getBusiInsurance() {
        return busiInsurance;
    }

    public void setBusiInsurance(List<String> busiInsurance) {
        this.busiInsurance = busiInsurance;
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

    public List<String> getDriveInsurance() {
        return driveInsurance;
    }

    public void setDriveInsurance(List<String> driveInsurance) {
        this.driveInsurance = driveInsurance;
    }

    public List<String> getTaxResource() {
        return taxResource;
    }

    public void setTaxResource(List<String> taxResource) {
        this.taxResource = taxResource;
    }

    public Integer getRequisitionId() {
        return requisitionId;
    }

    public void setRequisitionId(Integer requisitionId) {
        this.requisitionId = requisitionId;
    }
}
