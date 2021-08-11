package com.fintech.insurance.micro.dto.biz;

import com.fintech.insurance.commons.annotations.FinanceDataPoint;
import com.fintech.insurance.commons.annotations.ImageUrl;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class RequisitionDetailInfoVO implements Serializable {

    //车辆id
    private Integer id;

    //车牌号
    private String carNumber;

    //行驶证号
    private String drivingLicense;

    //行驶证图片
    @ImageUrl
    private List<String> drivingLicenseResource;

    //行驶证图片url
    public List<String> drivingLicenseResourceUrl;

    //行驶证图片缩略图url
    public List<String> drivingLicenseResourceNarrowUrl;

    //其他材料
    @ImageUrl
    private List<String> otherMaterial;

    //其他材料url
    public List<String> otherMaterialUrl;

    //其他材料缩略图url
    public List<String> otherMaterialNarrowUrl;

    //商业保险单号
    private String commercialInsuranceNumber;

    //商业保险金额
    @FinanceDataPoint
    private Double commercialInsuranceAmount;

    //商业保险时间起
    @NotNull(message ="104107")
    private Date commercialInsuranceStart;

    //商业保险时间止
    @NotNull(message ="104107")
    private Date commercialInsuranceEnd;

    //商业保险图片资料
    @ImageUrl
    private List<String> busiInsurance;

    //商业保险图片资料url
    public List<String> busiInsuranceUrl;

    //商业保险图片资料缩略图url
    public List<String> busiInsuranceNarrowUrl;

    //交强险单号
    private String compulsoryInsuranceNumber;

    //交强险金额
    @FinanceDataPoint
    private Double compulsoryInsuranceAmount = 0.0;

    //车船税
    @FinanceDataPoint
    private Double taxAmount = 0.0;

    //交强险时间起
    private Date compulsoryInsuranceStart;

    //交强险时间止
    private Date compulsoryInsuranceEnd;

    //交强险材料
    @ImageUrl
    private List<String> driveInsurance;

    //交强险材料url
    public List<String> driveInsuranceUrl;

    //交强险材料缩略图url
    public List<String> driveInsuranceNarrowUrl;

    //车船税
    @ImageUrl
    private List<String> taxResource;

    //车船税url
    public List<String> taxResourceUrl;

    //车船税缩略图url
    public List<String> taxResourceNarrowUrl;

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

    public Double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(Double taxAmount) {
        this.taxAmount = taxAmount;
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

    public List<String> getDrivingLicenseResourceUrl() {
        return drivingLicenseResourceUrl;
    }

    public void setDrivingLicenseResourceUrl(List<String> drivingLicenseResourceUrl) {
        this.drivingLicenseResourceUrl = drivingLicenseResourceUrl;
    }

    public List<String> getDrivingLicenseResourceNarrowUrl() {
        return drivingLicenseResourceNarrowUrl;
    }

    public void setDrivingLicenseResourceNarrowUrl(List<String> drivingLicenseResourceNarrowUrl) {
        this.drivingLicenseResourceNarrowUrl = drivingLicenseResourceNarrowUrl;
    }

    public List<String> getOtherMaterialUrl() {
        return otherMaterialUrl;
    }

    public void setOtherMaterialUrl(List<String> otherMaterialUrl) {
        this.otherMaterialUrl = otherMaterialUrl;
    }

    public List<String> getOtherMaterialNarrowUrl() {
        return otherMaterialNarrowUrl;
    }

    public void setOtherMaterialNarrowUrl(List<String> otherMaterialNarrowUrl) {
        this.otherMaterialNarrowUrl = otherMaterialNarrowUrl;
    }

    public List<String> getBusiInsuranceUrl() {
        return busiInsuranceUrl;
    }

    public void setBusiInsuranceUrl(List<String> busiInsuranceUrl) {
        this.busiInsuranceUrl = busiInsuranceUrl;
    }

    public List<String> getBusiInsuranceNarrowUrl() {
        return busiInsuranceNarrowUrl;
    }

    public void setBusiInsuranceNarrowUrl(List<String> busiInsuranceNarrowUrl) {
        this.busiInsuranceNarrowUrl = busiInsuranceNarrowUrl;
    }

    public List<String> getDriveInsuranceUrl() {
        return driveInsuranceUrl;
    }

    public void setDriveInsuranceUrl(List<String> driveInsuranceUrl) {
        this.driveInsuranceUrl = driveInsuranceUrl;
    }

    public List<String> getDriveInsuranceNarrowUrl() {
        return driveInsuranceNarrowUrl;
    }

    public void setDriveInsuranceNarrowUrl(List<String> driveInsuranceNarrowUrl) {
        this.driveInsuranceNarrowUrl = driveInsuranceNarrowUrl;
    }

    public List<String> getTaxResourceUrl() {
        return taxResourceUrl;
    }

    public void setTaxResourceUrl(List<String> taxResourceUrl) {
        this.taxResourceUrl = taxResourceUrl;
    }

    public List<String> getTaxResourceNarrowUrl() {
        return taxResourceNarrowUrl;
    }

    public void setTaxResourceNarrowUrl(List<String> taxResourceNarrowUrl) {
        this.taxResourceNarrowUrl = taxResourceNarrowUrl;
    }
}