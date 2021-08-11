package com.fintech.insurance.micro.customer.persist.dao;

import com.fintech.insurance.commons.enums.CustomerStatus;
import com.fintech.insurance.components.persist.BaseEntityDaoImpl;
import com.fintech.insurance.micro.customer.persist.entity.CustomerAccount;
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
public class CustomerAccountDaoImpl extends BaseEntityDaoImpl<CustomerAccount, Integer> implements CustomerAccountComplexDao {
    @Override
    public Page<CustomerAccountInfo> queryAllCustomer(String name, List<String> channelCodeList,
                                                      String phone, CustomerStatus customerStatus, Integer pageIndex, Integer pageSize) {
        StringBuilder sb = new StringBuilder(" from CustomerAccountInfo cai where 1 = 1 ");
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isNotBlank(name)) {
            sb.append(" and cai.customerName like :name ");
            params.put("name", "%" + name + "%");
        }
        if (channelCodeList != null && channelCodeList.size() > 0) {
            sb.append(" and cai.channelCode in (:channelCodeList)");
            params.put("channelCodeList", channelCodeList);
        }
        if (StringUtils.isNotBlank(phone)) {
            sb.append(" and cai.customerMobile like :phone ");
            params.put("phone", "%" + phone + "%");
        }
        if (customerStatus != null) {
            sb.append(" and cai.customerAccount.lockedTag = :status");
            params.put("status", customerStatus == CustomerStatus.FREEZE ? true : false);
        }
        sb.append(" order by cai.createAt, cai.customerAccount.idNumber ");

        return this.findEntityPagination("select cai " + sb.toString(), "select count(cai) " + sb.toString(), params, pageIndex, pageSize);
    }

}
