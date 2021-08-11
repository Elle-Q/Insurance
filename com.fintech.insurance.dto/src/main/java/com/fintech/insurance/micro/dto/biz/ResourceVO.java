package com.fintech.insurance.micro.dto.biz;

import java.io.Serializable;

public class ResourceVO implements Serializable {

    //资源类型
    private String resourceType;

    //资源图片
    private String resourcePicture;

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getResourcePicture() {
        return resourcePicture;
    }

    public void setResourcePicture(String resourcePicture) {
        this.resourcePicture = resourcePicture;
    }
}
