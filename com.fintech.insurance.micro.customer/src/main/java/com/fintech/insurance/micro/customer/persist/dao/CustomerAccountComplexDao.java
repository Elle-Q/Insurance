package com.fintech.insurance.micro.customer.persist.dao;

import com.fintech.insurance.commons.enums.CustomerStatus;
import com.fintech.insurance.micro.customer.persist.entity.CustomerAccountInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/11/14 13:35
 */
@NoRepositoryBean
public interface CustomerAccountComplexDao {
    Page<CustomerAccountInfo> queryAllCustomer(String name, List<String> channelCodeList,
                                               String phone, CustomerStatus customerStatus, Integer pageIndex, Integer pageSize);

}
