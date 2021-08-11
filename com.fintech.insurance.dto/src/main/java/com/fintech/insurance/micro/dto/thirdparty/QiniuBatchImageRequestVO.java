package com.fintech.insurance.micro.dto.thirdparty;

import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/12/18 18:33
 */
public class QiniuBatchImageRequestVO {

    private Boolean isFetchOrigin = Boolean.TRUE;

    private String model = "1";

    private String imageWidth;

    private String imageHeight;

    @NotEmpty
    private List<String> fileIds;

    public Boolean getIsFetchOrigin() {
        return isFetchOrigin;
    }

    public void setIsFetchOrigin(Boolean fetchOrigin) {
        isFetchOrigin = fetchOrigin;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(String imageWidth) {
        this.imageWidth = imageWidth;
    }

    public String getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(String imageHeight) {
        this.imageHeight = imageHeight;
    }

    public List<String> getFileIds() {
        return fileIds;
    }

    public void setFileIds(List<String> fileIds) {
        this.fileIds = fileIds;
    }
}
