package com.fintech.insurance.micro.biz.persist.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fintech.insurance.components.persist.BaseEntity;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * 产品VO
 *
 * @author liu man
 * @version 1.1.0
 * @since 2017-11-13 17:04:30
 */
@Entity
@Table(name = "busi_channel")
public class Channel extends BaseEntity {

    private static final long serialVersionUID = -8820461361835512411L;

    @Column(name = "channel_code", columnDefinition = "varchar(32) not null comment '渠道编码，创建时自动生成，生成规则参考需求文档，全局唯一'")
    private String channelCode;

    @Column(name = "channel_name", columnDefinition = "varchar(128) not null comment '渠道名称'")
    private String channelName;

    @Column(name = "id_number", columnDefinition = "varchar(32) comment '身份证号码'")
    private String idNumber;

    @Column(name = " business_licence", columnDefinition = "varchar(64) comment '营业执照号码'")
    private String businessLicence;

    @Column(name = "organization_id", columnDefinition = "int(10) unsigned not null comment '该渠道所属的公司id'")
    private Integer organizationId;

    @Column(name = "area_code", columnDefinition = "varchar(12) not null comment '该渠道所属的地区编码'")
    private String areaCode;

    @Column(name = "is_locked", columnDefinition = "boolean not null default 0 comment '是否被锁定，被锁定后不能在该渠道展开业务'")
    private Boolean isLocked;

    @Column(name = "created_by", columnDefinition = "int comment '创建者'")
    private Integer createBy;

    @Column(name = "updated_by", columnDefinition = "int comment '更新者'")
    private Integer updateBy;

    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE, CascadeType.REFRESH}, mappedBy = "channelSet")
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Product> productSet = new HashSet<Product>();

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

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getBusinessLicence() {
        return businessLicence;
    }

    public void setBusinessLicence(String businessLicence) {
        this.businessLicence = businessLicence;
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public Boolean getLocked() {
        return isLocked;
    }

    public void setLocked(Boolean locked) {
        isLocked = locked;
    }

    public Integer getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    public Integer getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Integer updateBy) {
        this.updateBy = updateBy;
    }

    public Set<Product> getProductSet() {
        return productSet;
    }

    public void setProductSet(Set<Product> productSet) {
        this.productSet = productSet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Channel channel = (Channel) o;

        if (channelCode != null ? !channelCode.equals(channel.channelCode) : channel.channelCode != null) return false;
        if (channelName != null ? !channelName.equals(channel.channelName) : channel.channelName != null) return false;
        if (idNumber != null ? !idNumber.equals(channel.idNumber) : channel.idNumber != null) return false;
        if (businessLicence != null ? !businessLicence.equals(channel.businessLicence) : channel.businessLicence != null)
            return false;
        if (organizationId != null ? !organizationId.equals(channel.organizationId) : channel.organizationId != null)
            return false;
        if (areaCode != null ? !areaCode.equals(channel.areaCode) : channel.areaCode != null) return false;
        if (isLocked != null ? !isLocked.equals(channel.isLocked) : channel.isLocked != null) return false;
        if (createBy != null ? !createBy.equals(channel.createBy) : channel.createBy != null) return false;
        if (updateBy != null ? !updateBy.equals(channel.updateBy) : channel.updateBy != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = channelCode != null ? channelCode.hashCode() : 0;
        result = 31 * result + (channelName != null ? channelName.hashCode() : 0);
        result = 31 * result + (idNumber != null ? idNumber.hashCode() : 0);
        result = 31 * result + (businessLicence != null ? businessLicence.hashCode() : 0);
        result = 31 * result + (organizationId != null ? organizationId.hashCode() : 0);
        result = 31 * result + (areaCode != null ? areaCode.hashCode() : 0);
        result = 31 * result + (isLocked != null ? isLocked.hashCode() : 0);
        result = 31 * result + (createBy != null ? createBy.hashCode() : 0);
        result = 31 * result + (updateBy != null ? updateBy.hashCode() : 0);
        return result;
    }
}
