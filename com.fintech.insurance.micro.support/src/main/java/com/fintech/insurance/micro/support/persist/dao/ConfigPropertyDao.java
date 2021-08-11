package com.fintech.insurance.micro.support.persist.dao;

import com.fintech.insurance.components.persist.BaseEntityDao;
import com.fintech.insurance.micro.support.persist.entity.ConfigProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigPropertyDao extends JpaRepository<ConfigProperty, Integer>, BaseEntityDao<ConfigProperty, Integer>, ConfigPropertyComplexDao {

    @Query("select c from ConfigProperty c where c.configCode = 'aheadRemindDays' ")
    ConfigProperty getAheadRemindDays();
}
