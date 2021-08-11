package com.fintech.insurance.micro.finance.persist.dao;

import com.fintech.insurance.commons.enums.ContractStatus;
import com.fintech.insurance.commons.enums.RefundStatus;
import com.fintech.insurance.components.persist.BaseEntityDaoImpl;
import com.fintech.insurance.micro.finance.persist.entity.RepaymentRecord;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * @Description: (some words)
 * @Author: East
 * @Date: 2017/11/18 0018 15:43
 */
@Repository
public class RepaymentRecordDaoImpl  extends BaseEntityDaoImpl<RepaymentRecord, Integer> implements RepaymentRecordComplexDao {

    public List<RepaymentRecord> listByPlanIdAndStatus(List<Integer> planIds) {

        StringBuilder hql = new StringBuilder(" from RepaymentRecord r where 1 = 1 ");
        Map<String, Object> params = new HashMap<>();
        List<RefundStatus> refundStatusList = new ArrayList<RefundStatus>();
        if (null != planIds && planIds.size() > 0) {
            hql.append("and r.repaymentPlan.id in :planIds" );
            params.put("planIds", planIds);
        }
        hql.append(" and (r.confirmStatus = 'processing' or r.confirmStatus = 'confirmed')" );
        return this.findEntityList(hql, 1000, params);
    }
}

