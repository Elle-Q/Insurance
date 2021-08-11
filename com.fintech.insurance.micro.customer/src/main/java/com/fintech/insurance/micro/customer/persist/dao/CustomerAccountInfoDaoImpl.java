package com.fintech.insurance.micro.customer.persist.dao;

import com.fintech.insurance.components.persist.BaseEntityDaoImpl;
import com.fintech.insurance.micro.customer.persist.entity.CustomerAccountInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/11/14 13:44
 */
@Repository
public class CustomerAccountInfoDaoImpl extends BaseEntityDaoImpl<CustomerAccountInfo, Integer> implements CustomerAccountInfoComplexDao {
    @Override
    public Page<CustomerAccountInfo> pageCustomerByChannnelCode(String currentUserChannelCode, Integer pageIndex, Integer pageSize) {
        StringBuilder hql = new StringBuilder(" from CustomerAccountInfo c where 1=1");
        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtils.isNotEmpty(currentUserChannelCode)) {
            hql.append(" and c.channelCode = :currentUserChannelCode");
            params.put("currentUserChannelCode", currentUserChannelCode);
        }
        String countSql = "select count(c) " + hql.toString();
        hql.append(" and c.channelUserId != 0 order by c.createAt desc");
        String querySql = "select c " + hql.toString();
        return this.findPagination(querySql, countSql, params, pageIndex, pageSize);
    }

    @Override
    public Page<CustomerAccountInfo> pageCustomerByChannnelUserId(Integer channelUserId, Integer pageIndex, Integer pageSize) {
        StringBuilder hql = new StringBuilder(" from CustomerAccountInfo c where 1=1");
        Map<String, Object> params = new HashMap<String, Object>();
        if (null != channelUserId) {
            hql.append(" and c.channelUserId = :channelUserId");
            params.put("channelUserId", channelUserId);
        }
        String countSql = "select count(c) " + hql.toString();
        hql.append(" order by c.createAt desc");
        String querySql = "select c " + hql.toString();
        return this.findPagination(querySql, countSql, params, pageIndex, pageSize);
    }
}
