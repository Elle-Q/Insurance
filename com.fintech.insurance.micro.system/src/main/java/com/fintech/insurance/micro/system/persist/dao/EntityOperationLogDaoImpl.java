package com.fintech.insurance.micro.system.persist.dao;

import com.fintech.insurance.components.persist.BaseEntityDaoImpl;
import com.fintech.insurance.micro.system.persist.entity.EntityOperationLog;
import org.springframework.stereotype.Repository;

@Repository
public class EntityOperationLogDaoImpl extends BaseEntityDaoImpl<EntityOperationLog, Integer> implements EntityOperationLogComplexDao {
}
