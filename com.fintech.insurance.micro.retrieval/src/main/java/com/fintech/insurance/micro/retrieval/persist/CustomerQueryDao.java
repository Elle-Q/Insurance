package com.fintech.insurance.micro.retrieval.persist;

import com.fintech.insurance.commons.enums.CustomerStatus;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.micro.dto.customer.CustomerVO;
import com.fintech.insurance.micro.retrieval.persist.base.BaseNativeSQLDao;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/12/2 15:28
 */
public interface CustomerQueryDao extends BaseNativeSQLDao {
    Pagination<CustomerVO> pageCustomerVO(String name, String channelOf, String companyOf,
                                          String phone, CustomerStatus customerStatus, Integer pageIndex, Integer pageSize);
}
