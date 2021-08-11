package com.fintech.insurance.micro.finance.persist.dao;

import com.fintech.insurance.commons.enums.ContractStatus;
import com.fintech.insurance.commons.enums.RefundStatus;
import com.fintech.insurance.micro.finance.persist.entity.RepaymentPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Date;
import java.util.List;

/**
 * @Description: (some words)
 * @Author: East
 * @Date: 2017/11/18 0018 11:37
 */
@NoRepositoryBean
public interface RepaymentPlanComplexDao {
    Page<RepaymentPlan> queryRepaymentPlan(RefundStatus refundStatus, List<String> contractCodes, Date refundBeginDate, Date refundEndDate, Integer pageIndex, Integer pageSize);

    Page<RepaymentPlan> queryRepaymentPlan(String contractCode, List<Integer> customerIds, Integer pageIndex, Integer pageSize);

    List<RepaymentPlan> queryCompletePlan(String contractCode);

    List<RepaymentPlan> findRepaymentPlanByContractStatus(ContractStatus contractStatus);

    Page<Object[]> pageWaitingForPayRepaymentPlanByCustomeId(Integer customeId, Integer days, Integer pageIndex, Integer pageSize);

    //查询待退保的还款计划
    List<RepaymentPlan> findWaitingForToSurrenderByContractNubmers(List<String> contractNubmers);

    /**
     * 根据合同号和期数查询还款计划
     * @param contractNumbers 合同号集合
     * @param currentInstalment 期数
     * @return
     */
    List<RepaymentPlan> findByContractNumberAndCurrentInstalment(List<String> contractNumbers, Integer currentInstalment);
}
