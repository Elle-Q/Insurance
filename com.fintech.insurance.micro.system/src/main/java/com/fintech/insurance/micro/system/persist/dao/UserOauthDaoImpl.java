package com.fintech.insurance.micro.system.persist.dao;

import com.fintech.insurance.components.persist.BaseEntityDaoImpl;
import com.fintech.insurance.micro.system.persist.entity.UserOauth;
import org.springframework.stereotype.Repository;

@Repository
public class UserOauthDaoImpl extends BaseEntityDaoImpl<UserOauth, Integer> implements UserOauthComplexDao {

}
