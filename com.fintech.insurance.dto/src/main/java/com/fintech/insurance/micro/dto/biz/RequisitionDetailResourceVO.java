package com.fintech.insurance.micro.dto.biz;

import com.fintech.insurance.commons.annotations.ImageUrl;

import java.io.Serializable;
import java.util.Date;

public class RequisitionDetailResourceVO extends BaseVO implements Serializable {

    //关联资源详情
    private RequisitionDetailVO requisitionDetailVO;
    //资源类型'")
    private String resourceType;
    //资源名称，对应为保单号码或者车船税
    private String resouceName;
    //资源图片
    @ImageUrl
    private String resourcePicture;
    public String resourcePictureUrl;
    public String resourcePictureNarrowUrl;
    //排序号
    private Integer displaySequence;


    public String getResourcePictureUrl() {
        return resourcePictureUrl;
    }

    public void setResourcePictureUrl(String resourcePictureUrl) {
        this.resourcePictureUrl = resourcePictureUrl;
    }

    public String getResourcePictureNarrowUrl() {
        return resourcePictureNarrowUrl;
    }

    public void setResourcePictureNarrowUrl(String resourcePictureNarrowUrl) {
        this.resourcePictureNarrowUrl = resourcePictureNarrowUrl;
    }

    public RequisitionDetailVO getRequisitionDetailVO() {
        return requisitionDetailVO;
    }

    public void setRequisitionDetailVO(RequisitionDetailVO requisitionDetailVO) {
        this.requisitionDetailVO = requisitionDetailVO;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getResouceName() {
        return resouceName;
    }

    public void setResouceName(String resouceName) {
        this.resouceName = resouceName;
    }

    public String getResourcePicture() {
        return resourcePicture;
    }

    public void setResourcePicture(String resourcePicture) {
        this.resourcePicture = resourcePicture;
    }

    public Integer getDisplaySequence() {
        return displaySequence;
    }

    public void setDisplaySequence(Integer displaySequence) {
        this.displaySequence = displaySequence;
    }
}