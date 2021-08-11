package com.fintech.insurance.micro.system.persist.dao;

import com.fintech.insurance.components.persist.BaseEntityDao;
import com.fintech.insurance.micro.system.persist.entity.EntityAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntityAuditLogDao extends JpaRepository<EntityAuditLog, Integer>, BaseEntityDao<EntityAuditLog, Integer>, EntityAuditLogComplexDao {

    @Query("select e from EntityAuditLog e where e.entityId = :id")
    List<EntityAuditLog> getAuditByEntityId(@Param("id") Integer id);

    @Query("select e from EntityAuditLog e where e.user.id = :userId and e.entityId = :entityId")
    EntityAuditLog getAuditByEntityIdAndUserId(@Param("userId") Integer userId, @Param("entityId") Integer entityId);

    @Query("select e from EntityAuditLog e where e.entityId = :id and e.auditStatus = :status")
    List<EntityAuditLog> getAuditByEntityIdAndStatus(@Param("id") Integer id, @Param("status") String status);

    @Query("select e from EntityAuditLog e where e.batchNumber = :latestAuditBatch and e.entityId = :entityId and e.entityType = :entityType and e.user.id = :userId and e.auditStatus = :auditStatus ")
    EntityAuditLog getUserAuditLogByBatchAndEntityIdAndStatus(@Param("latestAuditBatch") String latestAuditBatch, @Param("entityId") Integer entityId, @Param("entityType") String entityType, @Param("userId") Integer userId, @Param("auditStatus") String auditStatus);

    @Query("select e from EntityAuditLog e where e.entityId = :entityId and e.batchNumber = :latestAuditBatch")
    List<EntityAuditLog> getAuditByEntityIdAndBatch(@Param("entityId") Integer entityId, @Param("latestAuditBatch")String latestAuditBatch);

    @Query("select e from EntityAuditLog e where e.entityType = :entityType and e.entityId = :entityId and e.batchNumber = :latestAuditBatch and e.auditStatus = :auditStatus")
    List<EntityAuditLog> getAuditByEntityIdAndStatusAndBatch(@Param("entityType") String entityType, @Param("entityId") Integer entityId, @Param("latestAuditBatch") String latestAuditBatch, @Param("auditStatus") String auditStatus);

    List<EntityAuditLog> getByEntityTypeAndEntityIdAndAuditStatus(String entityType, Integer entityId, String auditStatus);

    @Query("select e from EntityAuditLog e where e.entityId = :id and e.entityType = :entityType and e.batchNumber = :latestAuditBatch and e.auditStatus = :auditStatus ")
    EntityAuditLog getRemarkByEntityIdAndSome(@Param("id") Integer id, @Param("entityType") String entityType, @Param("latestAuditBatch") String latestAuditBatch, @Param("auditStatus") String auditStatus);
}
