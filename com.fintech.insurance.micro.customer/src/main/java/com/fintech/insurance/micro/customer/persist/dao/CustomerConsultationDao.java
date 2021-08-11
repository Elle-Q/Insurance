package com.fintech.insurance.micro.customer.persist.dao;

import com.fintech.insurance.components.persist.BaseEntityDao;
import com.fintech.insurance.micro.customer.persist.entity.CustomerConsultation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/11/13 15:16
 */
@Repository
public interface CustomerConsultationDao extends JpaRepository<CustomerConsultation, Integer>,
        BaseEntityDao<CustomerConsultation, Integer>, CustomerConsultationComplexDao {

    @Query("select c from CustomerConsultation c where c.oauthAppId = :oauthAppId and c.oauthAcount = :oauthAccount and c.oauthType = :oauthType")
    CustomerConsultation getByOauthTypeAndOauthAppidAndOauthAcount(@Param("oauthAppId") String oauthAppId,
                                                                   @Param("oauthAccount") String oauthAccount, @Param("oauthType") String oauthType);
}
