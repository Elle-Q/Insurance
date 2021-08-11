package com.fintech.insurance.micro.system.persist.dao;

import com.fintech.insurance.components.persist.BaseDao;
import com.fintech.insurance.micro.system.persist.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleDao extends JpaRepository<Role, String>, BaseDao<Role>, RoleComplexDao {

    @Query("select r from Role r where r.id = :id")
    Role findById(@Param("id") String id);

    @Query("select r from Role r where r.roleName = :name")
    Role findByName(@Param("name") String name);

    @Query("select r from Role r where r.id in (:ids)")
    List<Role> findByIdArray(@Param("ids") List<Integer> ids);

    @Query("select r from Role r where r.roleCode = :roleCode")
    Role getByCode(@Param("roleCode") String roleCode);
}
