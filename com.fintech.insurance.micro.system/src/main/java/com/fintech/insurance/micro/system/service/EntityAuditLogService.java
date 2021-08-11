package com.fintech.insurance.micro.system.service;

import com.fintech.insurance.micro.dto.biz.AuditLogVO;
import com.fintech.insurance.micro.dto.biz.RequisitionVO;
import com.fintech.insurance.micro.dto.system.EntityAuditLogVO;
import com.fintech.insurance.micro.system.persist.entity.EntityAuditLog;

import java.util.List;

public interface EntityAuditLogService {
    /**
     *
     * @param latestAuditBatch 审核批次号
     * @param entityId   实体id
     * @param entityType 实体type
     * @param userId   用户id
     * @param auditStatus 审核状态
     * @return
     */
    EntityAuditLog getUserAuditLogByBatchAndEntityIdAndStatus(String latestAuditBatch, Integer entityId, String entityType, Integer userId, String auditStatus);

    /**
     *
     * @param requisitionVO  待审核的申请单
     * @param entityAuditLog  待更新的审核记录
     * @param auditStatus    审核状态
     * @param operationRemark  审核备注
     * @param currentLoginUserID  当前登录用户
     */
    String audit(RequisitionVO requisitionVO, EntityAuditLog entityAuditLog, String auditStatus, String operationRemark, Integer currentLoginUserID);

    /**
     * 根据id查找审核记录
     * @param id
     * @return
     */
    EntityAuditLog getEntityAuditLogById(Integer id);

    /**
     * 保存审核记录
     * @param auditLogVO
     */
    void save(AuditLogVO auditLogVO);

    /**
     *
     * @param entityType        实体类型
     * @param requisitionIdList     实体id集合
     * @param currentLoginUserId    当前登录用户id集合
     * @param auditStatus         审核状态
     * @param requisitionBatchList  实体审核批次号集合
     * @return
     */
    List<EntityAuditLogVO> listAuditLogsByCurrentUserAnd(String entityType, List<Integer> requisitionIdList, Integer currentLoginUserId, String auditStatus, List<String> requisitionBatchList);

    String getRemark(String entityType, Integer entityId, String auditStatus);

    /**
     *  获取申请单审核备注
     * @param id        申请单id
     * @param entityType  实体类型
     * @param latestAuditBatch  上次提交审核的审核批次号
     * @param auditStatus       审核状态
     */
    String getRemarkByEntityIdAndSome(Integer id, String entityType, String latestAuditBatch, String auditStatus);
}
