package com.fintech.insurance.micro.support.persist.entity;

import com.fintech.insurance.components.persist.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * 广告位表
 */
@Entity
@Table(name = "data_advertisement_position")
public class AdvertisementPosition extends BaseEntity implements Serializable {

    @Transient
    public static final String channelBannerCode = "CHANNEL_WECHAT_BANNER";
    @Transient
    public static final String customerBannerCode = "CUSTOMER_WECHAT_BANNER";

    @Column(name = "code", columnDefinition = "varchar(64) comment 'code'")
    private String code;

    @Column(name = "name", columnDefinition = "varchar(128) comment ''")
    private String name;

    @Column(name = "display_limit", columnDefinition = "int(10) default 10 comment ''")
    private Integer display_limit;

    @Column(name = "description", columnDefinition = "varchar(256) comment '内容'")
    private String description;

    @Column(name = "created_by", columnDefinition = "int(11) comment '创建人主键'")
    private Integer createBy;

    @Column(name = "updated_by", columnDefinition = "int(11) comment '更新人主键'")
    private Integer updateBy;

    public Integer getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Integer updateBy) {
        this.updateBy = updateBy;
    }

    public Integer getCreateBy() {

        return createBy;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDisplay_limit() {
        return display_limit;
    }

    public void setDisplay_limit(Integer display_limit) {
        this.display_limit = display_limit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
