package com.fintech.insurance.micro.system.persist.dao;

import com.fintech.insurance.components.persist.BaseEntityDao;
import com.fintech.insurance.micro.system.persist.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionDao extends JpaRepository<Permission, Integer>, BaseEntityDao<Permission, Integer>, PermissionComplexDao {
}
