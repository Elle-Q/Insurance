package com.fintech.insurance.micro.customer.persist.dao;

import com.fintech.insurance.micro.customer.persist.entity.CustomerAccountInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * @Author: qxy
 * @Description:
 * @Date: 2017/11/14 13:35
 */
@NoRepositoryBean
public interface CustomerAccountInfoComplexDao {
    //分页查询渠道客户
    Page<CustomerAccountInfo> pageCustomerByChannnelCode(String currentUserChannelCode, Integer pageIndex, Integer pageSize);

    Page<CustomerAccountInfo> pageCustomerByChannnelUserId(Integer channelUserId, Integer pageIndex, Integer pageSize);
}
