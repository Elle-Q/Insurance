package com.fintech.insurance.micro.dto.biz;


import java.io.Serializable;
import java.util.List;

public class OperationRecordVO extends BaseVO {

    //id
    private Integer id;

    //用户id
    private Integer userId;

    //操作实体id
    private Integer entityId;

    //操作实体类型
    private String entityType;

    //姓名
    private String name;

    //角色
    private List<String> roles;

    //手机号码
    private String mobile;

    //操作类型
    private String operationType;

    //操作备注
    private String operationRemark;

    //审核状态
    private String auditStatus;

    //图片资源
    private String[] imageKeys;

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String[] getImageKeys() {
        return imageKeys;
    }

    public void setImageKeys(String[] imageKeys) {
        this.imageKeys = imageKeys;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
