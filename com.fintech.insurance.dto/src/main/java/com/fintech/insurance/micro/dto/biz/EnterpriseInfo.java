package com.fintech.insurance.micro.dto.biz;

import com.fintech.insurance.commons.annotations.ImageUrl;

import java.io.Serializable;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/12/20 11:39
 */
public class EnterpriseInfo implements Serializable {
    // 企业id
    private Integer enterpriseId;
    // 企业名称
    private String enterpriseName;
    // 企业营业执照
    private String enterpriseLicenceNumber;
    // 企业营业执照照片
    @ImageUrl
    private String enterpriseLicencePhoto;
    // 企业营业执照照片url
    private String enterpriseLicencePhotoUrl;
    // 企业营业执照照片缩略图url
    private String enterpriseLicencePhotoNarrowUrl;


    public String getEnterpriseLicencePhotoUrl() {
        return enterpriseLicencePhotoUrl;
    }

    public void setEnterpriseLicencePhotoUrl(String enterpriseLicencePhotoUrl) {
        this.enterpriseLicencePhotoUrl = enterpriseLicencePhotoUrl;
    }

    public String getEnterpriseLicencePhotoNarrowUrl() {
        return enterpriseLicencePhotoNarrowUrl;
    }

    public void setEnterpriseLicencePhotoNarrowUrl(String enterpriseLicencePhotoNarrowUrl) {
        this.enterpriseLicencePhotoNarrowUrl = enterpriseLicencePhotoNarrowUrl;
    }

    public Integer getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Integer enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getEnterpriseLicenceNumber() {
        return enterpriseLicenceNumber;
    }

    public void setEnterpriseLicenceNumber(String enterpriseLicenceNumber) {
        this.enterpriseLicenceNumber = enterpriseLicenceNumber;
    }

    public String getEnterpriseLicencePhoto() {
        return enterpriseLicencePhoto;
    }

    public void setEnterpriseLicencePhoto(String enterpriseLicencePhoto) {
        this.enterpriseLicencePhoto = enterpriseLicencePhoto;
    }
}
