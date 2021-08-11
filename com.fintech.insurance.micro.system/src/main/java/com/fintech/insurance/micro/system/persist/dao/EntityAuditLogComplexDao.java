package com.fintech.insurance.micro.system.persist.dao;

import com.fintech.insurance.micro.system.persist.entity.EntityAuditLog;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface EntityAuditLogComplexDao {

    List<EntityAuditLog> listEntityAuditLog(String auditStatus, String userId);

    List<EntityAuditLog> listAuditLogsByCurrentUserAnd(String entityType, List<Integer> requisitionIdList, Integer currentLoginUserId, String auditStatus, List<String> requisitionBatchList);
}
