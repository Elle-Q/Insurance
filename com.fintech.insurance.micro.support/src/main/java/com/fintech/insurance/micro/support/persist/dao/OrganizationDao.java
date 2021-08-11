package com.fintech.insurance.micro.support.persist.dao;

import com.fintech.insurance.components.persist.BaseEntityDao;
import com.fintech.insurance.micro.support.persist.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganizationDao extends JpaRepository<Organization, Integer>, BaseEntityDao<Organization, Integer>, OrganizationComplexDao {

    @Query("select o from Organization o where (o.rootOrganization is null or o.rootOrganization.id = :id)")
    List<Organization> findAllByRootId(@Param("id") Integer rootId);
}
