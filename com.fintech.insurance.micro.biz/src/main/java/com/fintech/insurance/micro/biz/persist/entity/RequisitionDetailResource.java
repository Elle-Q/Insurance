package com.fintech.insurance.micro.biz.persist.entity;

import com.fintech.insurance.components.persist.BaseEntity;

import javax.persistence.*;

/**
 * 业务申请单明细表
 *
 * @author liu man
 * @version 1.1.0
 * @since 2017-11-13 17:04:30
 */
@Entity
@Table(name = "busi_requisition_detail_resource")
public class RequisitionDetailResource extends BaseEntity implements Cloneable {

    private static final long serialVersionUID = -8820461361835512425L;

    @ManyToOne
    @JoinColumn(name = "requisition_detail_id", columnDefinition = "int(11) not null comment '关联资源详情'")
    private RequisitionDetail requisitionDetail;

    @Column(name = "resource_type", columnDefinition = "varchar(16) not null comment '资源类型'")
    private String resourceType;

    @Column(name = "resouce_name", columnDefinition = "varchar(64) not null comment '资源名称，对应为保单号码或者车船税'")
    private String resouceName;

    @Column(name = "resource_picture", columnDefinition = "varchar(32) not null comment '资源图片'")
    private String resourcePicture;

    @Column(name = "display_sequence", columnDefinition = "int not null comment '排序号'")
    private Integer displaySequence;

    public RequisitionDetail getRequisitionDetail() {
        return requisitionDetail;
    }

    public void setRequisitionDetail(RequisitionDetail requisitionDetail) {
        this.requisitionDetail = requisitionDetail;
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

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
