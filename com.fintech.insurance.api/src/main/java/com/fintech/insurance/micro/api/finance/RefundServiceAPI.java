package com.fintech.insurance.micro.api.finance;

import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.micro.dto.biz.ContractVO;
import com.fintech.insurance.micro.dto.biz.RecordVO;
import com.fintech.insurance.micro.dto.biz.RefundVO;
import com.fintech.insurance.micro.dto.finance.FinanceRepaymentPlanVO;
import com.fintech.insurance.micro.dto.validate.groups.Save;
import com.fintech.insurance.micro.dto.validate.groups.Update;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RequestMapping(value = "/finance/refund", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface RefundServiceAPI {
    /**
     * 条件查询还款信息
     * @param contractCode 合同编号
     * @param customerName 客户名称
     * @param refundStatus 还款状态
     * @param requisitionNumber 业务编号
     * @param refundBeginDate 还款开始时间
     * @param refundEndDate 还款结束时间
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    FintechResponse<Pagination<RefundVO>> pageRefundItem(@RequestParam(value = "contractCode", defaultValue = "") String contractCode,
                                                         @RequestParam(value = "customerName", defaultValue = "") String customerName,
                                                         @RequestParam(value = "refundStatus", defaultValue = "") String refundStatus,
                                                         @RequestParam(value = "requisitionNumber", defaultValue = "") String requisitionNumber,
                                                         @RequestParam(value = "carNumber", defaultValue = "") String carNumber,
                                                         @RequestParam(value = "refundBeginDate", required = false)  Long refundBeginDate,
                                                         @RequestParam(value = "refundEndDate", required = false) Long refundEndDate,
                                                         @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                                         @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize);

    @RequestMapping(value = "/list-refund-plan", method = RequestMethod.GET)
    FintechResponse<Pagination<ContractVO>> pageRefundPlan(@RequestParam(value = "contractCode", defaultValue = "") String contractCode,
                                                           @RequestParam(value = "customerName", defaultValue = "") String customerName,
                                                           @RequestParam(value = "channelName", defaultValue = "") String channelName,
                                                           @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                                           @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize);

    @RequestMapping(value = "/list-by-contract-number", method = RequestMethod.GET)
    FintechResponse<List<FinanceRepaymentPlanVO>> getListByContractNumber(@RequestParam(value = "contractNumber", defaultValue = "") String contractNumber);

    @RequestMapping(value = "/confirm-refund", method = RequestMethod.POST)
    FintechResponse<VoidPlaceHolder> confirmRefund(@RequestBody @Validated(Save.class) RecordVO recordVO);

    @RequestMapping(value = "/manual-handling", method = RequestMethod.POST)
    FintechResponse<VoidPlaceHolder> dealWithHuman(@RequestBody @Validated(Update.class) RecordVO recordVO);

    @RequestMapping(value = "/withhold", method = RequestMethod.POST)
    FintechResponse<VoidPlaceHolder> withHold(@RequestBody @Validated(Update.class) RecordVO recordVO);

    /**
     * 正常申请单还款日自动扣款（timer）
     * @return
     */
    @RequestMapping(value = "/debit-for-repayDay", method = RequestMethod.GET)
    FintechResponse<VoidPlaceHolder> debitForRepayDay();

    /**
     * 已过最大预期天数的订单(过滤掉人工处理的订单)，如果扣款失败， 变更状态为待退保（timer）
     * @return
     */
    @GetMapping(value = "/change-status-to-surrender")
    FintechResponse<VoidPlaceHolder> changeStatusToSurrender();

    /**
     * 查询扣款状态为processing， confirmed的支付记录更新状态（timer）
     * @return
     */
    @GetMapping(value = "/change-status-from-yjf")
    FintechResponse<VoidPlaceHolder> changeStatusFromYJF();

    @GetMapping(value = "/debit-for-overdue")
    FintechResponse<VoidPlaceHolder> debitForOverdue();

    /**
     * 还款日提醒：还款日前XXX日上午09:00推送微信与短息消息给客户
     * @return
     */
    @GetMapping(value = "/send-msg-for-overdue")
    FintechResponse<VoidPlaceHolder> sendMsgForOverdue();

    /**
     * 已过还款日期的订单变更状态为已逾期
     * @return
     */
    @GetMapping(value = "/change-status-to-overdue")
    FintechResponse<VoidPlaceHolder> changeStatusToOverdue();

    /**
     * 还款日提醒：还款日前XXX日上午09:00推送微信与短息消息给客户
     * @return
     */
    @GetMapping(value = "/send-msg-for-repaydate")
    FintechResponse<VoidPlaceHolder> sendMsgForRepayDate();

    @RequestMapping(value = "/update-refundstatus-by-contract-number", method = RequestMethod.GET)
    FintechResponse<VoidPlaceHolder> updateRefundStatusByContractNumber( @RequestParam(value = "repayDayType", defaultValue = "") String repayDayType, @RequestParam(value = "contractNumber", defaultValue = "") String contractNumber);

    @RequestMapping(value = "/list-by-contract", method = RequestMethod.GET)
    FintechResponse<List<FinanceRepaymentPlanVO>> findRepaymentListByContract(@RequestParam(value = "contractNumber", defaultValue = "") String contractNumber,
                                                                              @RequestParam(value = "maxOverdueDays", required = false ) Integer maxOverdueDays,
                                                                              @RequestParam(value = "overdueFineRate", required = false) Double overdueFineRate);

    @RequestMapping(value = "/get-remain-repay-capital-amont", method = RequestMethod.GET)
    FintechResponse<BigDecimal> getRemainRepayCapitalAmount(@RequestParam(value = "contractNumber", defaultValue = "") String contractNumber);
}