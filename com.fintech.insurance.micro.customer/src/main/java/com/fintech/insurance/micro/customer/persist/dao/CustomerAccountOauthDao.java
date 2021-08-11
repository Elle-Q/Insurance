package com.fintech.insurance.micro.customer.persist.dao;

import com.fintech.insurance.components.persist.BaseEntityDao;
import com.fintech.insurance.micro.customer.persist.entity.CustomerAccountOauth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/11/13 15:15
 */
@Repository
public interface CustomerAccountOauthDao extends JpaRepository<CustomerAccountOauth, Integer>,
        BaseEntityDao<CustomerAccountOauth, Integer>, CustomerAccountOauthComplexDao {

    @Query("select c from CustomerAccountOauth c where c.oauthAppId = :appid and c.oauthAccount = :openid and c.oauthType = :oauthType")
    CustomerAccountOauth getCustomerAccountOauthByOpenid(@Param("appid") String appid, @Param("openid") String openid, @Param("oauthType") String oauthType);

    @Query("select c from CustomerAccountOauth c where c.customerAccount.id = :accountId and c.oauthAppId = :appid and c.oauthType = :oauthType")
    CustomerAccountOauth getCustomerAccountOauthByAccountId(@Param("accountId") Integer accountId, @Param("oauthType") String oauthType, @Param("appid") String appid);

    CustomerAccountOauth getCustomerAccountOauthByWxUnionId(String wxUnionid);
}
