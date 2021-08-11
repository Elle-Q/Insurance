package com.fintech.insurance.micro.finance.controller;

import com.fintech.insurance.commons.enums.ContractStatus;
import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.enums.InstallmentEvent;
import com.fintech.insurance.commons.enums.RefundStatus;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.BaseFintechController;
import com.fintech.insurance.micro.api.finance.RepaymentPlanServiceAPI;
import com.fintech.insurance.micro.dto.IdVO;
import com.fintech.insurance.micro.dto.biz.ContractVO;
import com.fintech.insurance.micro.dto.finance.FinanceRepaymentPlanVO;
import com.fintech.insurance.micro.dto.finance.OverdueDataVO;
import com.fintech.insurance.micro.feign.biz.ContractServiceFeign;
import com.fintech.insurance.micro.finance.persist.entity.RepaymentPlan;
import com.fintech.insurance.micro.finance.service.RepaymentPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: (some words)
 * @Author: East
 * @Date: 2017/11/18 0018 13:58
 */
@RestController
public class RepaymentPlanController extends BaseFintechController implements RepaymentPlanServiceAPI {

    private static Integer CURRENT_INSTALMENT = 1;//当前期数为第一期

    @Autowired
    private RepaymentPlanService repaymentPlanService;

    @Autowired
    private ContractServiceFeign contractServiceFeign;

    @Override
    public FintechResponse<FinanceRepaymentPlanVO> getRepaymentPlan(Integer repaymentPlanId) {

        FinanceRepaymentPlanVO repaymentPlanVO = repaymentPlanService.getRepaymentPlanById(repaymentPlanId);
        return FintechResponse.responseData(repaymentPlanVO);
    }

    @Override
    public FintechResponse<Map<String,FinanceRepaymentPlanVO>> findAllRepaymentPlanByContractStatus(ContractStatus contractStatus) {
        Map<String,FinanceRepaymentPlanVO> repaymentPlanVOMap= repaymentPlanService.findAllRepaymentPlanByContractStatus(contractStatus);
        return FintechResponse.responseData(repaymentPlanVOMap);
    }

    @Override
    public FintechResponse<Pagination<FinanceRepaymentPlanVO>> pageRepaymentPlanByCustomeId(@RequestParam(value = "customeId") Integer customeId,
                                                                                            @RequestParam(value = "days", defaultValue = "") Integer days,
                                                                                            @RequestParam(value = "pageIndex") Integer pageIndex,
                                                                                            @RequestParam(value = "pageSize") Integer pageSize) {
        if (null == customeId) {
            throw new FInsuranceBaseException(102001);
        }
        return FintechResponse.responseData(repaymentPlanService.pageRepaymentPlanByCustomeId(customeId, days, pageIndex, pageSize));
    }

    @Override
    public FintechResponse<VoidPlaceHolder> confirmReturnInsurance(@RequestParam(value="contractNumber") String contractNumber) {
        repaymentPlanService.confirmReturnInsurance(contractNumber);
        return FintechResponse.voidReturnInstance();
    }

    @Override
    public FintechResponse<VoidPlaceHolder> createInitRepaymentPlan(@RequestBody FinanceRepaymentPlanVO repaymentPlanVO) {
        repaymentPlanService.createInitRepaymentPlan(repaymentPlanVO);
        return FintechResponse.voidReturnInstance();
    }

    @Override
    public FintechResponse<VoidPlaceHolder> deleteRepaymentPlanByContractNumbers(@RequestBody List<String> contractNumberStr) {
        repaymentPlanService.deleteRepaymentPlanByContractNumbers(contractNumberStr);
        return FintechResponse.voidReturnInstance();
    }

    //@Override
    public FintechResponse<List<FinanceRepaymentPlanVO>> listRepaymentPlanByStatus(@RequestParam(value="refundStatus") String refundStatus) {
        return FintechResponse.responseData(repaymentPlanService.listRepaymentPlanByStatus(refundStatus));
    }

    @Override
    public FintechResponse<VoidPlaceHolder> deleteRepaymentPlanByContractNumber(@RequestParam(value="contractCode")  String contractCode) {
        repaymentPlanService.deleteRepaymentPlanByContractNumber(contractCode);
        return FintechResponse.voidReturnInstance();
    }

    @Override
    public FintechResponse<VoidPlaceHolder> updateFirstRepaymentPlanBInitialPaymentSuccess(@RequestBody IdVO idVO) {
        //判断合同是否存在
        FintechResponse<List<ContractVO>> contractVOListFintechResponse = contractServiceFeign.getContractByRequisitionId(idVO.getId());
        if(!contractVOListFintechResponse.isOk()){
            throw FInsuranceBaseException.buildFromErrorResponse(contractVOListFintechResponse);
        }
        List<ContractVO> contractVOList = contractVOListFintechResponse.getData();
        if(contractVOList == null || contractVOList.size() < 1){
            return FintechResponse.voidReturnInstance();
        }
        List<String> contractNumbers = new ArrayList<String>();
        for(ContractVO contractVO : contractVOList){
            contractNumbers.add(contractVO.getContractCode());
        }
        List<RepaymentPlan> repaymentPlanList = repaymentPlanService.findAllRepaymentPlanByContractNubmers(contractNumbers, CURRENT_INSTALMENT);
        if(repaymentPlanList != null && repaymentPlanList.size() > 0) {
            for (RepaymentPlan repaymentPlan : repaymentPlanList){
                //期初还款确认还款
                repaymentPlanService.updateRepaymentPlanStatusByEvent(repaymentPlan, InstallmentEvent.InitialPaymentSuccessEvent);
            }
        }
        return FintechResponse.voidReturnInstance();
    }

    @Override
    public FintechResponse<OverdueDataVO> getOverdueDataVOByRepaymentPlanVO(FinanceRepaymentPlanVO repaymentPlanVO) {
        OverdueDataVO overdueDataVO = repaymentPlanService.getOverdueDataVOByRepaymentPlanVO(repaymentPlanVO);
        return FintechResponse.responseData(overdueDataVO);
    }
}
