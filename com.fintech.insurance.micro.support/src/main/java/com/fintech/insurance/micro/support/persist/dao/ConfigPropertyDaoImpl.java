package com.fintech.insurance.micro.support.persist.dao;

import com.fintech.insurance.components.persist.BaseEntityDaoImpl;
import com.fintech.insurance.micro.support.persist.entity.ConfigProperty;
import org.springframework.stereotype.Repository;

@Repository
public class ConfigPropertyDaoImpl extends BaseEntityDaoImpl<ConfigProperty, Integer> implements ConfigPropertyComplexDao {
}
