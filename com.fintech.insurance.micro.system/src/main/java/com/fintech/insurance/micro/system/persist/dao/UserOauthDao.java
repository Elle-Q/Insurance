package com.fintech.insurance.micro.system.persist.dao;

import com.fintech.insurance.components.persist.BaseDao;
import com.fintech.insurance.micro.system.persist.entity.UserOauth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserOauthDao extends JpaRepository<UserOauth, Integer>, BaseDao<UserOauth> {

    @Query("select o from UserOauth o where o.oauthAppId = :appid and o.oauthAccount = :openid and o.oauthType = :oauthType")
    UserOauth getUserOauthByOpenid(@Param("appid") String appid, @Param("openid") String openid, @Param("oauthType") String oauthType);

    @Query("select o from UserOauth  o where o.user.id = :userId and o.oauthAppId = :appid and o.oauthType = :oauthType")
    UserOauth getUserOauthByUserId(@Param("userId") Integer userId, @Param("appid") String appid, @Param("oauthType") String oauthType);

    UserOauth getUserOauthByWxUnionid(String unionid);
}
