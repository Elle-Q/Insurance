package com.fintech.insurance.micro.customer.persist.dao;

import com.fintech.insurance.components.persist.BaseEntityDaoImpl;
import com.fintech.insurance.micro.customer.persist.entity.CustomerAccountOauth;
import org.springframework.stereotype.Repository;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/11/14 13:45
 */
@Repository
public class CustomerAccountOauthDaoImpl extends BaseEntityDaoImpl<CustomerAccountOauth, Integer> implements CustomerAccountOauthComplexDao {
}
