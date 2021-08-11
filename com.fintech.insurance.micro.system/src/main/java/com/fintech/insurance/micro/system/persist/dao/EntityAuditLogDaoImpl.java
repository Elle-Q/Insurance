package com.fintech.insurance.micro.system.persist.dao;

import com.fintech.insurance.components.persist.BaseEntityDaoImpl;
import com.fintech.insurance.micro.system.persist.entity.EntityAuditLog;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class EntityAuditLogDaoImpl extends BaseEntityDaoImpl<EntityAuditLog, Integer> implements EntityAuditLogComplexDao {
    @Override
    public List<EntityAuditLog> listEntityAuditLog(String auditStatus, String userId) {
        return null;
    }

    @Override
    public List<EntityAuditLog> listAuditLogsByCurrentUserAnd(String entityType, List<Integer> requisitionIdList, Integer currentLoginUserId, String auditStatus, List<String> requisitionBatchList) {
        StringBuilder hql = new StringBuilder(" from EntityAuditLog e where 1 = 1");
        Map<String, Object> params = new HashMap<String, Object>();

        if (!StringUtils.isEmpty(entityType)) {
            hql.append(" and e.entityType = :entityType");
            params.put("entityType", entityType);
        }
        if (null != requisitionIdList && requisitionIdList.size() > 0) {
            hql.append(" and e.entityId in (:requisitionIdList)");
            params.put("requisitionIdList", requisitionIdList);
        }
        if (currentLoginUserId != null) {
            hql.append(" and e.user.id = :currentLoginUserId");
            params.put("currentLoginUserId", currentLoginUserId);
        }
        if (!StringUtils.isEmpty(auditStatus)) {
            hql.append(" and e.auditStatus = :auditStatus");
            params.put("auditStatus", auditStatus);
        }
        if (null != requisitionBatchList && requisitionBatchList.size() > 0) {
            hql.append(" and e.batchNumber in (:requisitionBatchList)");
            params.put("requisitionBatchList", requisitionBatchList);
        }

        return this.findList(hql.toString(), 0, params);
    }
}
