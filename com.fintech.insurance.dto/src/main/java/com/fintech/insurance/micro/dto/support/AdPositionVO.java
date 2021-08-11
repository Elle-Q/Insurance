package com.fintech.insurance.micro.dto.support;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/11/16 9:04
 */
public class AdPositionVO {
    // 主键id
    private Integer id;
    // 广告位编码
    private String positionCode;
    // 广告位名称
    private String positionName;
    // 广告位置名称
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPositionCode() {
        return positionCode;
    }

    public void setPositionCode(String positionCode) {
        this.positionCode = positionCode;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }
}
