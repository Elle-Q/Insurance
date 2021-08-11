package com.fintech.insurance.micro.dto.system;

import java.util.List;

public class QueryAuditLogVO {

    //实体类型
    private String entityType;

    //实体id集合
    List<Integer> requisitionIdList;

    //实体类型
    private Integer currentLoginUserId;

    //审核状态
    private String auditStatus;

    //实体审核批次号集合
    List<String> requisitionBatchList;

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public List<Integer> getRequisitionIdList() {
        return requisitionIdList;
    }

    public void setRequisitionIdList(List<Integer> requisitionIdList) {
        this.requisitionIdList = requisitionIdList;
    }

    public Integer getCurrentLoginUserId() {
        return currentLoginUserId;
    }

    public void setCurrentLoginUserId(Integer currentLoginUserId) {
        this.currentLoginUserId = currentLoginUserId;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public List<String> getRequisitionBatchList() {
        return requisitionBatchList;
    }

    public void setRequisitionBatchList(List<String> requisitionBatchList) {
        this.requisitionBatchList = requisitionBatchList;
    }
}
