package com.fintech.insurance.micro.finance.persist.dao;

import com.fintech.insurance.commons.enums.ContractStatus;
import com.fintech.insurance.commons.enums.RefundStatus;
import com.fintech.insurance.commons.utils.DateCommonUtils;
import com.fintech.insurance.components.persist.BaseEntityDaoImpl;
import com.fintech.insurance.micro.finance.persist.entity.RepaymentPlan;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * @Description: (some words)
 * @Author: East
 * @Date: 2017/11/18 0018 11:53
 */
@Repository
public class RepaymentPlanDaoImpl extends BaseEntityDaoImpl<RepaymentPlan, Integer> implements RepaymentPlanComplexDao {

    @Override
    public Page<RepaymentPlan> queryRepaymentPlan(RefundStatus refundStatus, List<String> contractCodes, Date refundBeginDate, Date refundEndDate, Integer pageIndex, Integer pageSize) {
        StringBuilder sb = new StringBuilder(" from RepaymentPlan rp where 1 = 1 ");
        Map<String, Object> params = new HashMap<>();
        if (refundStatus != null) {
            sb.append(" and rp.repayStatus = :refundStatus");
            params.put("refundStatus", refundStatus);
        }
        if (contractCodes != null && contractCodes.size() > 0) {
            sb.append(" and rp.contractNumber in :contractCodes");
            params.put("contractCodes", contractCodes);
        }
        if (refundBeginDate != null) {
            sb.append(" and rp.repayDate >= :refundBeginDate ");
            params.put("refundBeginDate", refundBeginDate);
        }
        if (refundEndDate != null) {
            sb.append(" and rp.repayDate <= :refundEndDate ");
            params.put("refundEndDate", refundEndDate);
        }

        sb.append(" order by case when rp.repayStatus = :WAITING_REFUND then 1 when rp.repayStatus = :OVERDUE then 1 when rp.repayStatus = :WAITING_WITHDRAW then 1 " +
                " when rp.repayStatus = :FAIL_REFUND then 1 when rp.repayStatus = :HAS_WITHDRAW then 2 when rp.repayStatus = :HAS_REFUND then 2 end, rp.createAt ");

        params.put("WAITING_REFUND", RefundStatus.WAITING_REFUND);
        params.put("OVERDUE", RefundStatus.OVERDUE);
        params.put("WAITING_WITHDRAW", RefundStatus.WAITING_WITHDRAW);
        params.put("FAIL_REFUND", RefundStatus.FAIL_REFUND);
        params.put("HAS_WITHDRAW", RefundStatus.HAS_WITHDRAW);
        params.put("HAS_REFUND", RefundStatus.HAS_REFUND);

        return this.findEntityPagination("select rp " + sb.toString(), "select count(rp) " + sb.toString(), params, pageIndex, pageSize);
    }

    /*
    由于待退保和已退保状态会更新后续的还款计划状态，可以完成以下对应关系：
          合同状态      还款计划状态
          待退保        最后一期的还款计划为待退保
          已退保        最后一期的还款计划为已退保
          已完成        最后一期的还款计划为已完成
          还款中        最后一期的还款计划为还款中，或者其他

     如果用还款完成的下一期做判断，那么还款，全部都已经还款的就查不出来
     */
    @Override
    public Page<RepaymentPlan> queryRepaymentPlan(String contractCode, List<Integer> customerIds, Integer pageIndex, Integer pageSize) {
        StringBuilder sb = new StringBuilder(" from RepaymentPlan rp where 1 = 1 ");
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isNotBlank(contractCode)) {
            sb.append(" and rp.contractNumber like :contractCode ");
            params.put("contractCode", "%" + contractCode + "%");
        }
        if (customerIds != null && customerIds.size() > 0) {
            sb.append(" and rp.customerId in :customerIds ");
            params.put("customerIds", customerIds);
        }
        sb.append(" and rp.currentInstalment in (select max(plan.currentInstalment) from RepaymentPlan plan group by plan.contractNumber) order by createAt");
        return this.findEntityPagination("select rp " + sb.toString(), "select count(rp) " + sb.toString(), params, pageIndex, pageSize);
    }

    @Override

    public List<RepaymentPlan> queryCompletePlan(String contractCode) {
        if (StringUtils.isNotBlank(contractCode)) {
            return Collections.EMPTY_LIST;
        }
        String hql = "select rp from RepaymentPlan rp where rp.contractNumber = :contractCode and rp.repayStatus = 'has_refund' ";
        Map<String, Object> params = new HashMap<>();
        params.put("contractCode", "%" + contractCode + "%");
        return this.findEntityList(hql, 1000, params);
    }


    public List<RepaymentPlan> findRepaymentPlanByContractStatus(ContractStatus contractStatus) {
        StringBuilder hql = new StringBuilder(" from RepaymentPlan r where r.id in ");
        hql.append(" ( select max(rp.id) from RepaymentPlan rp where 1=1 ");
        Map<String, Object> params = new HashMap<>();
        List<RefundStatus> refundStatusList = new ArrayList<RefundStatus>();
        if(contractStatus == ContractStatus.InsuranceReturned){
            refundStatusList.add(RefundStatus.HAS_WITHDRAW);
            hql.append(" and rp.repayStatus in :refundStatus");
            params.put("refundStatus", refundStatusList);
        }else if(contractStatus == ContractStatus.InsuranceReturning){
            //refundStatusList.add(RefundStatus.OVERDUE);
           // refundStatusList.add(RefundStatus.FAIL_REFUND);
            //refundStatusList.add(RefundStatus.WAITING_REFUND);
            //Date nowDate = DateCommonUtils.getCurrentDate();
            hql.append(" and rp.repayStatus in :refundStatus  ");
            params.put("refundStatus", RefundStatus.WAITING_WITHDRAW);
           // params.put("refundStatus2", refundStatusList);
        }else if(contractStatus == ContractStatus.Refunding){
            refundStatusList.add(RefundStatus.OVERDUE);
            refundStatusList.add(RefundStatus.FAIL_REFUND);
            refundStatusList.add(RefundStatus.WAITING_REFUND);
            hql.append(" and rp.repayStatus in :refundStatus  ");
            params.put("refundStatus", refundStatusList);
        }else if(contractStatus == ContractStatus.Refunded){
            refundStatusList.add(RefundStatus.HAS_WITHDRAW);
            refundStatusList.add(RefundStatus.OVERDUE);
            refundStatusList.add(RefundStatus.FAIL_REFUND);
            refundStatusList.add(RefundStatus.WAITING_WITHDRAW);
            refundStatusList.add(RefundStatus.WAITING_REFUND);
            hql.append(" and rp.repayStatus in :refundStatus and rp.repayStatus not in :refundStatus2 ");
            params.put("refundStatus", RefundStatus.HAS_REFUND);
            params.put("refundStatus2", refundStatusList);
        }
        hql.append(" group by rp.contractNumber )  ");
        hql.append(" order by r.repayDate desc ");
        return this.findEntityList(hql.toString(),0,params);
    }

    @Override
    public Page<Object[]> pageWaitingForPayRepaymentPlanByCustomeId(Integer customeId, Integer days, Integer pageIndex, Integer pageSize) {
        StringBuilder sb = new StringBuilder(" from RepaymentPlan rp where 1 = 1 ");
        Map<String, Object> params = new HashMap<>();
        if (null != customeId) {
            sb.append(" and rp.customerId = :customeId ");
            params.put("customeId", customeId);
        }
        Date currentDate = DateCommonUtils.getCurrentDate();
        sb.append(" and rp.repayDate >= :currentDate");
        params.put("currentDate", currentDate);
        if (null != days) {
            Date newDate = DateCommonUtils.getAfterDay(currentDate, days);
            sb.append(" and rp.repayDate <= :newDate");
            params.put("newDate", newDate);
        }
        List<RefundStatus> refundStatusList = new ArrayList<>();
        refundStatusList.add(RefundStatus.OVERDUE);
        refundStatusList.add(RefundStatus.FAIL_REFUND);
        refundStatusList.add(RefundStatus.WAITING_REFUND);
        refundStatusList.add(RefundStatus.HAS_REFUND);
        sb.append(" and rp.repayStatus in :refundStatus");
        params.put("refundStatus", refundStatusList);
        sb.append(" group by rp.repayDate order by rp.repayDate asc");
        return this.findEntityPagination("select rp.repayDate as repayDate,sum(rp.repayCapitalAmount + rp.repayInterestAmount) as money, rp.id " + sb.toString(), "select count(rp) " + sb.toString(), params, pageIndex, pageSize);
    }

    @Override
    public List<RepaymentPlan> findWaitingForToSurrenderByContractNubmers(List<String> contractNubmers) {
        StringBuilder hql = new StringBuilder(" from RepaymentPlan rp where 1 = 1 ");
        Map<String, Object> params = new HashMap<>();
       if(contractNubmers != null  && contractNubmers.size() > 0){
           hql.append(" and rp.contractNumber in :contractNumbers ");
           params.put("contractNumbers", contractNubmers);
       }
        hql.append(" and rp.repayStatus not in :repayStatus ");
        List<RefundStatus> refundStatusList = new ArrayList<RefundStatus>();
        refundStatusList.add( RefundStatus.HAS_REFUND);
        refundStatusList.add( RefundStatus.HAS_WITHDRAW);
        params.put("repayStatus", refundStatusList);
        return this.findList(hql, 0 ,params);
    }

    @Override
    public List<RepaymentPlan> findByContractNumberAndCurrentInstalment(List<String> contractNubmers, Integer currentInstalment) {
        StringBuilder hql = new StringBuilder(" from RepaymentPlan rp where 1 = 1 ");
        Map<String, Object> params = new HashMap<>();
        if(contractNubmers != null  && contractNubmers.size() > 0){
            hql.append(" and rp.contractNumber in :contractNumbers ");
            params.put("contractNumbers", contractNubmers);
        }
        hql.append(" and rp.currentInstalment  = :currentInstalment ");
        params.put("currentInstalment", currentInstalment);

        hql.append(" and rp.repayStatus  != :repayStatus ");
        params.put("repayStatus", RefundStatus.HAS_REFUND);
        return this.findList(hql, 0 ,params);
    }
}
