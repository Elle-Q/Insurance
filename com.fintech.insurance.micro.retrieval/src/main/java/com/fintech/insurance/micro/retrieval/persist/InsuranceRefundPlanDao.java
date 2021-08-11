package com.fintech.insurance.micro.retrieval.persist;

import com.fintech.insurance.micro.dto.finance.FinanceRepaymentPlanVO;
import com.fintech.insurance.micro.retrieval.persist.base.BaseNativeSQLDao;

import java.util.Date;
import java.util.List;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/11/30 18:41
 */
public interface InsuranceRefundPlanDao extends BaseNativeSQLDao {

    List<String> findWaitingForToSurrender();

    List<FinanceRepaymentPlanVO> listPlansForOverdue();

    List<FinanceRepaymentPlanVO> listPlansForRepayDate();

    List<FinanceRepaymentPlanVO> listPlansByStatus(String code);

    List<FinanceRepaymentPlanVO> listPlansForWaitingRefundAndRepayDate(Date endate);
}
