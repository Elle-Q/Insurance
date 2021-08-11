package com.fintech.insurance.micro.system.persist.entity;

import com.fintech.insurance.components.persist.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 业务操作日志表
 */
@Entity
@Table(name = "sys_entity_operation_log")
public class EntityOperationLog extends BaseEntity implements Serializable {

    @ManyToOne
    @JoinColumn(name = "user_id", columnDefinition = "int(11) comment '外键标识，关联sys_user表的id'")
    private User user;

    @Column(name = "entity_type", length = 16, columnDefinition = "varchar(16) comment '订单类型'")
    private String entityType;

    @Column(name = "entity_id", length = 10, columnDefinition = "int(10) comment '订单号'")
    private Integer entityId;

    @Column(name = "operation_type", length = 16, columnDefinition = "varchar(16) comment '操作类型'")
    private String operationType;

    @Column(name = "operation_remark", columnDefinition = "varchar(1024) character set utf8 comment '备注'")
    private String operationRemark;

    @Column(name = "attachments", columnDefinition = "varchar(1024) character set utf8 comment '附件'")
    private String attachments;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getOperationRemark() {
        return operationRemark;
    }

    public void setOperationRemark(String operationRemark) {
        this.operationRemark = operationRemark;
    }

    public String getAttachments() {
        return attachments;
    }

    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }
}
