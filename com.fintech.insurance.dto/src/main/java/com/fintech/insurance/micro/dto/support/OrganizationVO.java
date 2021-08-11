package com.fintech.insurance.micro.dto.support;

import java.io.Serializable;

/**
 * @Author: Clayburn
 * @Description: 组织接口vo
 * @Date: 2017/11/9 16:05
 */
public class OrganizationVO implements Serializable {

    private Integer id;
    // 公司名称
    private String companyName;
    // 父公司id
    private Integer parentId;
    // 地区编码
    private String areaCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }
}
