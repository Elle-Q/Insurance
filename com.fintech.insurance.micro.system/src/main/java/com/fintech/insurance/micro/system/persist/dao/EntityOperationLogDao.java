package com.fintech.insurance.micro.system.persist.dao;

import com.fintech.insurance.components.persist.BaseDao;
import com.fintech.insurance.micro.system.persist.entity.EntityOperationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntityOperationLogDao extends JpaRepository<EntityOperationLog, Integer>, BaseDao<EntityOperationLog>, EntityOperationLogComplexDao {

    @Query("select e from EntityOperationLog e where e.entityId = :id order by e.createAt desc")
    List<EntityOperationLog> listOperationrRecord(@Param("id") Integer id);

}
