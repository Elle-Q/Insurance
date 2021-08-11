package com.fintech.insurance.micro.retrieval.persist;

import com.fintech.insurance.commons.enums.CustomerStatus;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.micro.dto.customer.CustomerVO;
import com.fintech.insurance.micro.retrieval.mapper.CustomerVORowMapper;
import com.fintech.insurance.micro.retrieval.persist.base.BaseNativeSQLDaoImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/12/2 15:28
 */

@Repository
@Transactional(readOnly = true)
public class CustomerQueryDaoImpl extends BaseNativeSQLDaoImpl implements CustomerQueryDao {
    @Override
    public Pagination<CustomerVO> pageCustomerVO(String customerName, String channelOf, String companyOf, String phone, CustomerStatus customerStatus, Integer pageIndex, Integer pageSize) {
        StringBuilder sb = new StringBuilder(" from cust_account_info info LEFT JOIN cust_account account on info.account_id = account.id LEFT JOIN busi_channel channel on info.channel_code = channel.channel_code, data_organization org where channel.organization_id = org.id ");
        List<Object> args = new ArrayList<>();
        if (StringUtils.isNotBlank(customerName)) {
            sb.append(" and info.customer_name like ? ");
            args.add("%" + customerName + "%");
        }
        if (StringUtils.isNotBlank(channelOf)) {
            sb.append(" and channel.channel_name like ? ");
            args.add("%" + channelOf + "%");
        }
        if (StringUtils.isNotBlank(companyOf)) {
            sb.append(" and org.company_name like ? ");
            args.add("%" + companyOf + "%");
        }
        if (StringUtils.isNotBlank(phone)) {
            sb.append(" and info.customer_mobile like ? ");
            args.add("%" + phone + "%");
        }
        if (customerStatus != null) {
            sb.append(" and account.is_locked = ? ");
            args.add(customerStatus == CustomerStatus.FREEZE ? 1 : 0);
        }
        sb.append("order by info.created_at desc, account.id_number ");

        return this.createPage("SELECT info.id id, account.id account_id, info.customer_name customer_name, account.id_number id_number, info.customer_mobile customer_mobile, channel.channel_name, org.company_name company_name, account.is_locked is_locked, info.created_at created_at " + sb.toString(), "SELECT count(*) " + sb.toString(), args.toArray(), pageIndex, pageSize, new CustomerVORowMapper());
    }
}
