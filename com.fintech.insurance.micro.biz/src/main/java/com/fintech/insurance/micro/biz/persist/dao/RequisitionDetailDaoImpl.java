package com.fintech.insurance.micro.biz.persist.dao;

import com.fintech.insurance.commons.enums.ContractStatus;
import com.fintech.insurance.commons.enums.RequisitionStatus;
import com.fintech.insurance.components.persist.BaseEntityDaoImpl;
import com.fintech.insurance.micro.biz.persist.entity.RequisitionDetail;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: (some words)
 * @Author: yongneng liu
 * @Date: 2017/11/13 0013 14:26
 */
public class RequisitionDetailDaoImpl extends BaseEntityDaoImpl<RequisitionDetail, Integer> implements RequisitionDetailComplexDao {

    @Override
    public List<RequisitionDetail> listRequisitionDetail(Integer contractId) {
        StringBuilder hql = new StringBuilder(" from RequisitionDetail r where 1=1 ");
        Map<String, Object> params = new HashMap<String, Object>();
        if (contractId != null) {
            hql.append(" and r.contract.id = :contractId ");
            params.put("contractId", contractId);
        }
        hql.append(" order by r.createAt desc");
        String querySql = "select r " + hql.toString();
        return this.findEntityList(querySql, 0, params);
    }

    @Override
    public Page<RequisitionDetail> findRequisitionDetailByRequisitionId(Integer requisitionId, Integer pageIndex, Integer pageSize) {
        StringBuilder hql = new StringBuilder(" from RequisitionDetail r where 1=1 ");
        Map<String, Object> params = new HashMap<String, Object>();
        if (requisitionId != null) {
            hql.append(" and r.requisition.id = :requisitionId ");
            params.put("requisitionId", requisitionId);
        }
        String countSql = "select count(r) " + hql.toString();
        hql.append(" order by r.id desc ");
        String querySql = "select r " + hql.toString();
        return this.findPagination(querySql, countSql, params, pageIndex, pageSize);
    }

    @Override
    public RequisitionDetail findLoaningRequisitionDetailByCarNumber(String carNumber) {
        StringBuilder hql = new StringBuilder(" from RequisitionDetail r where 1=1 ");
        Map<String, Object> params = new HashMap<String, Object>();
        hql.append(" and r.carNumber = :carNumber ");
        params.put("carNumber", carNumber);

        hql.append(" and ( r.contract.contractStatus in :contractStatusList  or r.requisition.requisitionStatus in :requisitionStatusList ) ");

        List<String> contractStatusList = new ArrayList<String>();
        List<String> requisitionStatusList = new ArrayList<String>();
        contractStatusList.add(ContractStatus.Refunding.getCode());
        contractStatusList.add(ContractStatus.InsuranceReturning.getCode());
        params.put("contractStatusList", contractStatusList);
        requisitionStatusList.add(RequisitionStatus.Submitted.getCode());
        requisitionStatusList.add(RequisitionStatus.Auditing.getCode());
        requisitionStatusList.add(RequisitionStatus.WaitingPayment.getCode());
        requisitionStatusList.add(RequisitionStatus.FailedPayment.getCode());
        requisitionStatusList.add(RequisitionStatus.WaitingLoan.getCode());
        params.put("requisitionStatusList", requisitionStatusList);

        String querySql = "select r " + hql.toString();
        return this.findFirstEntity(querySql,  params);
    }
}
