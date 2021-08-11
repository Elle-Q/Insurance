package com.fintech.insurance.micro.api.retrieval;

import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.micro.dto.finance.FinanceRepaymentPlanVO;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RequestMapping(value = "/insurance/refund", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface RefundQueryAPI {

    @GetMapping(value = "/find-contract-number/list")
    FintechResponse<List<String>> findWaitingForToSurrenderContractNumber();

    @GetMapping(value = "/list-plans-for-overdue")
    FintechResponse<List<FinanceRepaymentPlanVO>> listPlansForOverdue();

    @GetMapping(value = "/list-plans-for-repaydate")
    FintechResponse listPlansForRepayDate();

    @GetMapping(value = "/list-plans-by-status")
    FintechResponse<List<FinanceRepaymentPlanVO>> listPlansByStatus(@RequestParam ("code") String code);

    @PostMapping(value = "/list-plans-for-waiting-refund-and-repaydate")
    FintechResponse<List<FinanceRepaymentPlanVO>> listPlansForWaitingRefundAndRepayDate(@RequestBody Date date);
}