
package com.fintech.insurance.micro.dto.biz;

import com.fintech.insurance.micro.dto.validate.groups.Save;
import com.fintech.insurance.micro.dto.validate.groups.Update;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

public class ChannelVO implements Serializable {

    //渠道id
    @NotNull(groups = {Update.class}, message = "104107")
    private Integer id;

    //渠道编号，创建时自动生成，全局唯一
    private String channelCode;

    //渠道名称
    @NotEmpty(groups = {Save.class}, message = "104107")
    private String channelName;

    //绑定手机号
    @NotEmpty(groups = {Save.class}, message = "104107")
    private String mobile;

    //企业营业执照号
    @NotEmpty(groups = {Save.class}, message = "104107")
    private String businessLicence;

    //企业营业执照图片
    private String licencePicture;

    //所属公司id
    @NotNull(groups = {Save.class}, message = "104107")
    private Integer companyId;

    //所属公司
    private String companyName;

    //所在地区code
    @NotEmpty(groups = {Save.class}, message = "104107")
    private String areaCode;

    //渠道所在地
    private String areaDesc;

    //添加时间
    private Date createAt;

    //渠道状态
    private Integer isLocked;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getBusinessLicence() {
        return businessLicence;
    }

    public void setBusinessLicence(String businessLicence) {
        this.businessLicence = businessLicence;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Integer getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(Integer isLocked) {
        this.isLocked = isLocked;
    }

    public String getAreaDesc() {
        return areaDesc;
    }

    public void setAreaDesc(String areaDesc) {
        this.areaDesc = areaDesc;
    }

    public String getLicencePicture() {
        return licencePicture;
    }

    public void setLicencePicture(String licencePicture) {
        this.licencePicture = licencePicture;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
}

