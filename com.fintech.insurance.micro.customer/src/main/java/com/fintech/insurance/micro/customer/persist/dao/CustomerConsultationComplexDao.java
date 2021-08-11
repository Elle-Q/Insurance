package com.fintech.insurance.micro.customer.persist.dao;

import com.fintech.insurance.micro.customer.persist.entity.CustomerConsultation;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Date;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/11/14 13:37
 */
@NoRepositoryBean
public interface CustomerConsultationComplexDao {
    Page<CustomerConsultation> query(Date submmitStartTime, Date submmitEndTime, String mobile, String name, Integer pageIndex, Integer pageSize);
}
