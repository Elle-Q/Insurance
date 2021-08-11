package com.fintech.insurance.micro.support.persist.entity;

import com.fintech.insurance.components.persist.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 公司组织架构表
 */
@Entity
@Table(name = "data_organization")
public class Organization extends BaseEntity implements Serializable {

    @Column(name = "company_name", columnDefinition = "varchar(256) comment '公司名称'")
    private String companyName;

    @Column(name = "contact_name", columnDefinition = "varchar(64) comment '公司联系人姓名'")
    private String contactName;

    @Column(name = "contact_phone", columnDefinition = "varchar(32) comment '公司联系人电话号码'")
    private String contactPhone;

    @Column(name = "area_code", columnDefinition = "varchar(6) comment '公司所在的地区编码'")
    private String areaCode;

    @Column(name = "orgnization_sequence", columnDefinition = "varchar(4) comment '该公司在所属地区的序号'")
    private String orgnizationSequence;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id", columnDefinition = "int(11) default null comment '上级公司的id，关联本表的主键'")
    private Organization parentOrganization;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "root_id", columnDefinition = "int(11) default null comment '所属的母公司id'")
    private Organization rootOrganization;

    @Column(name = "created_by", columnDefinition = "int(11) comment '创建人主键'")
    private Integer createBy;

    @Column(name = "updated_by", columnDefinition = "int(11) comment '更新人主键'")
    private Integer updateBy;

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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getOrgnizationSequence() {
        return orgnizationSequence;
    }

    public void setOrgnizationSequence(String orgnizationSequence) {
        this.orgnizationSequence = orgnizationSequence;
    }

    public Organization getParentOrganization() {
        return parentOrganization;
    }

    public void setParentOrganization(Organization parentOrganization) {
        this.parentOrganization = parentOrganization;
    }

    public Organization getRootOrganization() {
        return rootOrganization;
    }

    public void setRootOrganization(Organization rootOrganization) {
        this.rootOrganization = rootOrganization;
    }
}
