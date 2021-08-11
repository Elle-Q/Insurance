package com.fintech.insurance.micro.customer.persist.dao;

import com.fintech.insurance.commons.utils.DateCommonUtils;
import com.fintech.insurance.components.persist.BaseEntityDaoImpl;
import com.fintech.insurance.micro.customer.persist.entity.CustomerConsultation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: qxy
 * @Description:
 * @Date: 2017/11/14 13:46
 */
@Repository
public class CustomerConsultationDaoImpl extends BaseEntityDaoImpl<CustomerConsultation, Integer> implements CustomerConsultationComplexDao {
    @Override
    public Page<CustomerConsultation> query(Date submmitStartTime, Date submmitEndTime, String mobile, String name, Integer pageIndex, Integer pageSize) {
        StringBuilder hql = new StringBuilder(" from CustomerConsultation c where 1=1 ");
        Map<String, Object> params = new HashMap<String, Object>();
        if (submmitStartTime != null) {
            hql.append(" and c.createAt >= :submmitStartTime ");
            params.put("submmitStartTime", submmitStartTime);
        }
        if (submmitEndTime != null) {
            Date endTime = DateCommonUtils.getAfterDay(submmitEndTime, 1);
            hql.append(" and c.createAt <= :endTime ");
            params.put("endTime", endTime);
        }
        if (StringUtils.isNotBlank(mobile)) {
            hql.append(" and c.customerMobile like :mobile");
            params.put("mobile", "%" + mobile + "%");
        }
        if (StringUtils.isNotBlank(name)) {
            hql.append(" and c.customerName like :name");
            params.put("name", "%" + name + "%");
        }
        String countSql = "select count(c) " + hql.toString();
        hql.append(" order by c.createAt desc");
        String querySql = "select c " + hql.toString();
        return this.findPagination(querySql, countSql, params, pageIndex, pageSize);
    }
}
