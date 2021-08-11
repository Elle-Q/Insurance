package com.fintech.insurance.micro.system.persist.entity;

import com.fintech.insurance.components.persist.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 审核记录表
 * @Author: qxy
 * @Date: 2017/11/11 10:30
 */
@Entity
@Table(name = "sys_entity_audit_log")
public class EntityAuditLog extends BaseEntity implements Serializable {
    public static final String PROP_DISABLE_FLAG = "disableFlag";

    @ManyToOne
    @JoinColumn(name = "user_id", columnDefinition = "int(11) comment '外键标识，关联sys_user表的id'")
    private User user;

    @Column(name = "batch_number", length = 32, columnDefinition = "varchar(32) comment '批次号'")
    private String batchNumber;

    @Column(name = "entity_type", length = 16, columnDefinition = "varchar(16) comment '被审核的实体类型'")
    private String entityType;

    @Column(name = "entity_id", length = 10, columnDefinition = "int(10) comment '被审核的实体id'")
    private Integer entityId;

    @Column(name = "audit_status", length = 16, columnDefinition = "varchar(16) comment '审核状态'")
    private String auditStatus;

    @Column(name = "audit_time", length = 19, columnDefinition = "timestamp comment '审核时间'")
    @Temporal(TemporalType.TIMESTAMP)
    private Date auditTime;

    @Column(name = "audit_remark", columnDefinition = "varchar(1024) character set utf8 comment '备注'")
    private String auditRemark;

    @Column(name = "created_by", columnDefinition = "int(11) comment '创建人主键'")
    private Integer createBy;

    @Column(name = "updated_by", columnDefinition = "int(11) comment '更新人主键'")
    private Integer updateBy;

    public static String getPropDisableFlag() {
        return PROP_DISABLE_FLAG;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
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

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public String getAuditRemark() {
        return auditRemark;
    }

    public void setAuditRemark(String auditRemark) {
        this.auditRemark = auditRemark;
    }
}
