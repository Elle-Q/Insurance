package com.fintech.insurance.micro.system.persist.dao;

import com.fintech.insurance.components.persist.BaseEntityDaoImpl;
import com.fintech.insurance.micro.system.persist.entity.Permission;
import org.springframework.stereotype.Repository;

@Repository
public class PermissionDaoImpl extends BaseEntityDaoImpl<Permission, Integer> implements PermissionComplexDao {
}
