package com.fintech.insurance.micro.dto.biz;

import com.fintech.insurance.commons.annotations.FinanceDataPoint;
import com.fintech.insurance.commons.annotations.ImageUrl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/12/8 15:30
 */
public class CarInfo implements Serializable {
    // 车辆id
    private Integer carId;
    // 车牌号
    private String carNumber;
    // 行驶证号
    private String driverLicence;
    // 行驶证图片
    @ImageUrl
    private List<String> driverLicencePhoto;
    public List<String> driverLicencePhotoUrl;
    public List<String> driverLicencePhotoNarrowUrl;
    // 商业保险单号
    private String bizInsuranceNumber;
    // 商业保险金额
    @FinanceDataPoint
    private Double bizInsuranceAmount;
    // 商业保险开始时间
    private Date bizInsuranceBegin;
    // 商业保险结束时间
    private Date bizInsuranceEnd;
    // 商业保单图片
    @ImageUrl
    private List<String> bizInsurancePhoto;
    public List<String> bizInsurancePhotoUrl;
    public List<String> bizInsurancePhotoNarrowUrl;
    // 交强险单号
    private String cpsInsuranceNumber;
    // 交强险金额
    @FinanceDataPoint
    private Double cpsInsuranceAmount;
    // 交强险开始时间
    private Date cpsInsuranceBegin;
    // 交强险结束时间
    private Date cpsInsuranceEnd;
    // 交强险图片
    @ImageUrl
    private List<String> cpsInsurancePhoto;
    public List<String> cpsInsurancePhotoUrl;
    public List<String> cpsInsurancePhotoNarrowUrl;
    // 车船税金额
    @FinanceDataPoint
    private Double csAmount;
    // 车船税照片
    @ImageUrl
    private List<String> csPhoto;
    public List<String> csPhotoUrl;
    public List<String> csPhotoNarrowUrl;
    // 其他材料
    @ImageUrl
    private List<String> otherCarInfo;
    public List<String> otherCarInfoUrl;
    public List<String> otherCarInfoNarrowUrl;


    public List<String> getDriverLicencePhotoUrl() {
        return driverLicencePhotoUrl;
    }

    public void setDriverLicencePhotoUrl(List<String> driverLicencePhotoUrl) {
        this.driverLicencePhotoUrl = driverLicencePhotoUrl;
    }

    public List<String> getDriverLicencePhotoNarrowUrl() {
        return driverLicencePhotoNarrowUrl;
    }

    public void setDriverLicencePhotoNarrowUrl(List<String> driverLicencePhotoNarrowUrl) {
        this.driverLicencePhotoNarrowUrl = driverLicencePhotoNarrowUrl;
    }

    public List<String> getBizInsurancePhotoUrl() {
        return bizInsurancePhotoUrl;
    }

    public void setBizInsurancePhotoUrl(List<String> bizInsurancePhotoUrl) {
        this.bizInsurancePhotoUrl = bizInsurancePhotoUrl;
    }

    public List<String> getBizInsurancePhotoNarrowUrl() {
        return bizInsurancePhotoNarrowUrl;
    }

    public void setBizInsurancePhotoNarrowUrl(List<String> bizInsurancePhotoNarrowUrl) {
        this.bizInsurancePhotoNarrowUrl = bizInsurancePhotoNarrowUrl;
    }

    public List<String> getCpsInsurancePhotoUrl() {
        return cpsInsurancePhotoUrl;
    }

    public void setCpsInsurancePhotoUrl(List<String> cpsInsurancePhotoUrl) {
        this.cpsInsurancePhotoUrl = cpsInsurancePhotoUrl;
    }

    public List<String> getCpsInsurancePhotoNarrowUrl() {
        return cpsInsurancePhotoNarrowUrl;
    }

    public void setCpsInsurancePhotoNarrowUrl(List<String> cpsInsurancePhotoNarrowUrl) {
        this.cpsInsurancePhotoNarrowUrl = cpsInsurancePhotoNarrowUrl;
    }

    public List<String> getCsPhotoUrl() {
        return csPhotoUrl;
    }

    public void setCsPhotoUrl(List<String> csPhotoUrl) {
        this.csPhotoUrl = csPhotoUrl;
    }

    public List<String> getCsPhotoNarrowUrl() {
        return csPhotoNarrowUrl;
    }

    public void setCsPhotoNarrowUrl(List<String> csPhotoNarrowUrl) {
        this.csPhotoNarrowUrl = csPhotoNarrowUrl;
    }

    public List<String> getOtherCarInfoUrl() {
        return otherCarInfoUrl;
    }

    public void setOtherCarInfoUrl(List<String> otherCarInfoUrl) {
        this.otherCarInfoUrl = otherCarInfoUrl;
    }

    public List<String> getOtherCarInfoNarrowUrl() {
        return otherCarInfoNarrowUrl;
    }

    public void setOtherCarInfoNarrowUrl(List<String> otherCarInfoNarrowUrl) {
        this.otherCarInfoNarrowUrl = otherCarInfoNarrowUrl;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public Double getCpsInsuranceAmount() {
        return cpsInsuranceAmount;
    }

    public void setCpsInsuranceAmount(Double cpsInsuranceAmount) {
        this.cpsInsuranceAmount = cpsInsuranceAmount;
    }

    public Integer getCarId() {
        return carId;
    }

    public void setCarId(Integer carId) {
        this.carId = carId;
    }

    public String getDriverLicence() {
        return driverLicence;
    }

    public void setDriverLicence(String driverLicence) {
        this.driverLicence = driverLicence;
    }

    public List<String> getDriverLicencePhoto() {
        return driverLicencePhoto;
    }

    public void setDriverLicencePhoto(List<String> driverLicencePhoto) {
        if (driverLicencePhoto != null) {
            this.driverLicencePhoto = driverLicencePhoto;
        }
    }

    public String getBizInsuranceNumber() {
        return bizInsuranceNumber;
    }

    public void setBizInsuranceNumber(String bizInsuranceNumber) {
        this.bizInsuranceNumber = bizInsuranceNumber;
    }

    public Double getBizInsuranceAmount() {
        return bizInsuranceAmount;
    }

    public void setBizInsuranceAmount(Double bizInsuranceAmount) {
        this.bizInsuranceAmount = bizInsuranceAmount;
    }

    public Date getBizInsuranceBegin() {
        return bizInsuranceBegin;
    }

    public void setBizInsuranceBegin(Date bizInsuranceBegin) {
        this.bizInsuranceBegin = bizInsuranceBegin;
    }

    public Date getBizInsuranceEnd() {
        return bizInsuranceEnd;
    }

    public void setBizInsuranceEnd(Date bizInsuranceEnd) {
        this.bizInsuranceEnd = bizInsuranceEnd;
    }

    public List<String> getBizInsurancePhoto() {
        return bizInsurancePhoto;
    }

    public void setBizInsurancePhoto(List<String> bizInsurancePhoto) {
        if (bizInsurancePhoto != null) {
            this.bizInsurancePhoto = bizInsurancePhoto;
        }
    }

    public String getCpsInsuranceNumber() {
        return cpsInsuranceNumber;
    }

    public void setCpsInsuranceNumber(String cpsInsuranceNumber) {
        this.cpsInsuranceNumber = cpsInsuranceNumber;
    }

    public Date getCpsInsuranceBegin() {
        return cpsInsuranceBegin;
    }

    public void setCpsInsuranceBegin(Date cpsInsuranceBegin) {
        this.cpsInsuranceBegin = cpsInsuranceBegin;
    }

    public Date getCpsInsuranceEnd() {
        return cpsInsuranceEnd;
    }

    public void setCpsInsuranceEnd(Date cpsInsuranceEnd) {
        this.cpsInsuranceEnd = cpsInsuranceEnd;
    }

    public List<String> getCpsInsurancePhoto() {
        return cpsInsurancePhoto;
    }

    public void setCpsInsurancePhoto(List<String> cpsInsurancePhoto) {
        if (cpsInsurancePhoto != null) {
            this.cpsInsurancePhoto = cpsInsurancePhoto;
        }
    }

    public Double getCsAmount() {
        return csAmount;
    }

    public void setCsAmount(Double csAmount) {
        this.csAmount = csAmount;
    }

    public List<String> getCsPhoto() {
        return csPhoto;
    }

    public void setCsPhoto(List<String> csPhoto) {
        if (csPhoto != null) {
            this.csPhoto = csPhoto;
        }
    }

    public List<String> getOtherCarInfo() {
        return otherCarInfo;
    }

    public void setOtherCarInfo(List<String> otherCarInfo) {
        this.otherCarInfo = otherCarInfo;
    }
}
