package com.fintech.insurance.micro.finance.controller;

import com.fintech.insurance.commons.enums.RefundStatus;
import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.utils.DateCommonUtils;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.BaseFintechController;
import com.fintech.insurance.micro.api.finance.RefundServiceAPI;
import com.fintech.insurance.micro.dto.biz.ContractVO;
import com.fintech.insurance.micro.dto.biz.RecordVO;
import com.fintech.insurance.micro.dto.biz.RefundVO;
import com.fintech.insurance.micro.dto.finance.FinanceRepaymentPlanVO;
import com.fintech.insurance.micro.dto.validate.groups.Save;
import com.fintech.insurance.micro.dto.validate.groups.Update;
import com.fintech.insurance.micro.feign.biz.ContractServiceFeign;
import com.fintech.insurance.micro.feign.retrieval.RefundQueryFeign;
import com.fintech.insurance.micro.finance.service.RepaymentPlanService;
import com.fintech.insurance.micro.finance.service.RepaymentRecordService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Author: Clayburn
 * @Description: 还款管理
 * @Date: 2017/11/10 16:03
 */
@RestController
public class RefundController extends BaseFintechController implements RefundServiceAPI {

    private static final Logger LOG = LoggerFactory.getLogger(RefundController.class);

    @Autowired
    private RepaymentPlanService repaymentPlanService;

    @Autowired
    private RefundQueryFeign refundQueryFeign;

    @Autowired
    private ContractServiceFeign contractService;

    @Autowired
    RepaymentRecordService repaymentRecordService;

    @Override
    public FintechResponse<Pagination<RefundVO>> pageRefundItem(@RequestParam(value = "contractCode", defaultValue = "") String contractCode,
                                                                @RequestParam(value = "customerName", defaultValue = "") String customerName,
                                                                @RequestParam(value = "refundStatus", defaultValue = "") String refundStatus,
                                                                @RequestParam(value = "requisitionNumber", defaultValue = "") String requisitionNumber,
                                                                @RequestParam(value = "carNumber", defaultValue = "") String carNumber,
                                                                @RequestParam(value = "refundBeginDate", required = false)  Long refundBeginDate,
                                                                @RequestParam(value = "refundEndDate", required = false) Long refundEndDate,
                                                                @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                                                @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) {
        RefundStatus status = null;
        try {
            if (StringUtils.isNotBlank(refundStatus)) {
                status = RefundStatus.codeOf(refundStatus);
            }
        } catch (Exception e) {
            throw new FInsuranceBaseException("105001", new Object[]{refundStatus, RefundStatus.class.getName()});
        }
        Date begin = refundBeginDate == null ? null : DateCommonUtils.truncateDay(new Date(refundBeginDate));
        Date end = refundEndDate == null ? null : DateCommonUtils.getEndTimeOfDate(refundEndDate);

        return FintechResponse.responseData(repaymentPlanService.queryRepaymentPlan(contractCode, customerName, status, requisitionNumber, carNumber, begin, end, pageIndex, pageSize));
    }

    @Override
    public FintechResponse<Pagination<ContractVO>> pageRefundPlan(@RequestParam(value = "contractCode", defaultValue = "") String contractCode,
                                                                  @RequestParam(value = "customerName", defaultValue = "") String customerName,
                                                                  @RequestParam(value = "channelName", defaultValue = "") String channelName,
                                                                  @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                                                  @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) {
        return FintechResponse.responseData(repaymentPlanService.queryRepaymentPlan(contractCode, customerName, channelName, pageIndex, pageSize));
    }

    @Override
    public FintechResponse<List<FinanceRepaymentPlanVO>> getListByContractNumber(@RequestParam(value = "contractNumber", defaultValue = "") String contractNumber) {
        return FintechResponse.responseData(repaymentPlanService.getListByContractNumber(contractNumber));
    }

    @Override
    public FintechResponse<VoidPlaceHolder> confirmRefund(@RequestBody @Validated(Save.class) RecordVO recordVO) {
        repaymentPlanService.confirmRefund(recordVO);
        return FintechResponse.responseData(VoidPlaceHolder.instance());
    }

    @Override
    public FintechResponse<VoidPlaceHolder> dealWithHuman(@RequestBody @Validated(Update.class) RecordVO recordVO) {
        repaymentPlanService.dealWithHuman(recordVO.getRepaymentPlanId());
        return FintechResponse.responseData(VoidPlaceHolder.instance());
    }

    @Override
    public FintechResponse<VoidPlaceHolder> withHold(@RequestBody @Validated(Update.class) RecordVO recordVO) {
        repaymentPlanService.withHold(recordVO.getRepaymentPlanId());

        return FintechResponse.responseData(VoidPlaceHolder.instance());
    }

    @Override
    public FintechResponse<VoidPlaceHolder> debitForRepayDay() {
        repaymentPlanService.debitForRepayDay(null);
        return FintechResponse.responseData(VoidPlaceHolder.instance());
    }

    @Override
    public FintechResponse<VoidPlaceHolder> changeStatusToSurrender() {
        FintechResponse<List<String>> listFintechResponse = refundQueryFeign.findWaitingForToSurrenderContractNumber();
        if(!listFintechResponse.isOk()){
            throw new FInsuranceBaseException(listFintechResponse.getCode());
        }
        List<String> list = listFintechResponse.getData();
        if(list == null || list.size() < 1) {
            return FintechResponse.responseData(VoidPlaceHolder.instance());
        }
        for (String contractNumber : list) {
            LOG.info("prepar to change the contract number:{} to surrender.", contractNumber);
        }
        repaymentPlanService.changeStatusToSurrenderByContractNumber(list);
        contractService.changeStatusToSurrenderByContractNumber(list);
        return FintechResponse.responseData(VoidPlaceHolder.instance());
    }

    @Override
    public FintechResponse<VoidPlaceHolder> changeStatusFromYJF() {
        repaymentRecordService.changeRepaymentRecordStatusFromYJF();
        return FintechResponse.responseData(VoidPlaceHolder.instance());
    }

    @Override
    public FintechResponse<VoidPlaceHolder> debitForOverdue() {
        repaymentPlanService.debitForOverdue(null);
        return FintechResponse.responseData(VoidPlaceHolder.instance());
    }

    @Override
    public FintechResponse<VoidPlaceHolder> sendMsgForOverdue() {
        //repaymentPlanService.sendMsgForOverdue();
        return FintechResponse.responseData(VoidPlaceHolder.instance());
    }

    @Override
    public FintechResponse<VoidPlaceHolder> sendMsgForRepayDate() {
        repaymentPlanService.sendMsgForRepayDate();
        return FintechResponse.responseData(VoidPlaceHolder.instance());
    }

    @Override
    public FintechResponse<VoidPlaceHolder> updateRefundStatusByContractNumber(@RequestParam(value = "repayDayType", defaultValue = "") String repayDayType,
                                                                               @RequestParam(value = "contractNumber", defaultValue = "") String contractNumber) {
        repaymentPlanService.updateRefundStatusByContractNumber(repayDayType, contractNumber);
        return FintechResponse.responseData(VoidPlaceHolder.instance());
    }

    @Override
    public FintechResponse<List<FinanceRepaymentPlanVO>> findRepaymentListByContract(@RequestParam(value = "contractNumber", defaultValue = "") String contractNumber,
                                                                                     @RequestParam(value = "maxOverdueDays", required = false ) Integer maxOverdueDays,
                                                                                     @RequestParam(value = "overdueFineRate", required = false) Double overdueFineRate) {
        List<FinanceRepaymentPlanVO> repaymentPlanVOList = repaymentPlanService.findRepaymentListByContract(contractNumber, maxOverdueDays, overdueFineRate);
        return FintechResponse.responseData(repaymentPlanVOList);
    }

    @Override
    public FintechResponse<BigDecimal> getRemainRepayCapitalAmount(@RequestParam(value = "contractNumber", defaultValue = "") String contractNumber) {
        return FintechResponse.responseData(repaymentPlanService.getRemainRepayCapitalAmount(contractNumber));
    }

    @Override
    public FintechResponse<VoidPlaceHolder> changeStatusToOverdue() {
        repaymentPlanService.changeStatusToOverdue();
        return FintechResponse.responseData(VoidPlaceHolder.instance());
    }
}
