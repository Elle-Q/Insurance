package com.fintech.insurance.micro.retrieval.controller;

import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.micro.api.retrieval.RefundQueryAPI;
import com.fintech.insurance.micro.dto.finance.FinanceRepaymentPlanVO;
import com.fintech.insurance.micro.retrieval.persist.InsuranceRefundPlanDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/11/30 18:53
 */
@RestController
public class InsuranceRefundController implements RefundQueryAPI {

    @Autowired
    private InsuranceRefundPlanDao insuranceRefundPlanDao;

    @Override
    public FintechResponse<List<String>> findWaitingForToSurrenderContractNumber() {
        return FintechResponse.responseData(insuranceRefundPlanDao.findWaitingForToSurrender());
    }

    @Override
    public FintechResponse<List<FinanceRepaymentPlanVO>> listPlansForOverdue() {
        return FintechResponse.responseData(insuranceRefundPlanDao.listPlansForOverdue());
    }

    @Override
    public FintechResponse listPlansForRepayDate() {
        return FintechResponse.responseData(insuranceRefundPlanDao.listPlansForRepayDate());
    }

    @Override
    public FintechResponse<List<FinanceRepaymentPlanVO>> listPlansByStatus(@RequestParam ("code") String code) {
        List<FinanceRepaymentPlanVO> list = insuranceRefundPlanDao.listPlansByStatus(code);
        return FintechResponse.responseData(list);
    }

    @Override
    public FintechResponse<List<FinanceRepaymentPlanVO>> listPlansForWaitingRefundAndRepayDate(@RequestBody Date date) {
        List<FinanceRepaymentPlanVO> list = insuranceRefundPlanDao.listPlansForWaitingRefundAndRepayDate(date);
        return FintechResponse.responseData(list);
    }
}
