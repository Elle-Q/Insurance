package com.fintech.insurance.micro.api.retrieval;

import com.fintech.insurance.commons.enums.*;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.micro.dto.biz.*;
import com.fintech.insurance.micro.dto.customer.CustomerVO;
import com.fintech.insurance.micro.dto.finance.BankVO;
import com.fintech.insurance.micro.dto.retrieval.UserVO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/11/30 18:54
 */
@RequestMapping(value = "/retrieval/biz", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface BizQueryAPI {

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    FintechResponse<List<UserVO>> getAllUsers();

    @GetMapping(path = "/page-refundvo")
    public FintechResponse<Pagination<RefundVO>> pageRefundVO(@RequestParam(name = "contractNumber", defaultValue = "") String contractNumber,
                                                              @RequestParam(name = "customerName", defaultValue = "") String customerName,
                                                              @RequestParam(name = "refundStatus", required = false) RefundStatus refundStatus,
                                                              @RequestParam(name = "requisitionNumber", defaultValue = "") String requisitionNumber,
                                                              @RequestParam(name = "carNumber", defaultValue = "") String carNumber,
                                                              @RequestParam(name = "refundBeginDate", required = false) Long refundBeginDate,
                                                              @RequestParam(name = "refundEndDate", required = false) Long refundEndDate,
                                                              @RequestParam(name = "pageIndex", defaultValue = "1") Integer pageIndex,
                                                              @RequestParam(name = "pageSize", defaultValue = "20") Integer pageSize);

    @RequestMapping(value = "/list-customer-by-name", method = RequestMethod.GET)
    FintechResponse<List<CustomerVO>> listCustomerByName(@RequestParam(value = "customerName") String customerName);


    @RequestMapping(value = "/query-requisition", method = RequestMethod.GET)
    FintechResponse<Pagination<RequisitionVO>> queryRequisition(@RequestParam(name = "requisitionNumber", defaultValue = "")String requisitionNumber,
                                                                @RequestParam(name = "requisitionStatus", defaultValue = "")String requisitionStatus,
                                                                @RequestParam(name = "productType", defaultValue = "")String productType,
                                                                @RequestParam(name = "channelName", defaultValue = "")String channelName,
                                                                @RequestParam(name = "submmitStartTime", required = false)Long submmitStartTime,
                                                                @RequestParam(name = "submmitEndTime", required = false)Long submmitEndTime,
                                                                @RequestParam(name = "customerName", defaultValue = "")String customerName,
                                                                @RequestParam(name = "pageIndex", defaultValue = "1")Integer pageIndex,
                                                                @RequestParam(name = "pageSize", defaultValue = "20")Integer pageSize);

    @RequestMapping(value = "/query-channel", method = RequestMethod.GET)
    FintechResponse<Pagination<ChannelVO>> queryChannel(@RequestParam(name = "channelCode", defaultValue = "")String channelCode,
                                                        @RequestParam(name = "channelName", defaultValue = "")String channelName,
                                                        @RequestParam(name = "companyName", defaultValue = "")String companyName,
                                                        @RequestParam(name = "mobile", defaultValue = "")String mobile,
                                                        @RequestParam(name = "pageIndex", defaultValue = "1")Integer pageIndex,
                                                        @RequestParam(name = "pageSize", defaultValue = "20")Integer pageSize);
    /**
     *  ????????????????????????????????????
     *
     * @param channelCode ????????????
     * @param customerName ????????????
     * @param productType ????????????
     * @param companyId ??????????????????????????????ID
     * @param contractStatus ????????????????????????
     * @param borrowStartTime ??????????????????????????????
     * @param borrowEndTime ??????????????????????????????
     * @param carNumber ?????????
     * @param pageIndex ??????
     * @param pageSize ???????????????
     * @return
     */
    @RequestMapping(value = "/page-biz-report", method = RequestMethod.GET)
    public FintechResponse<Pagination<BizReportVO>> pageBizReportVO(@RequestParam(value = "channelCode", required = false) String channelCode,
                                                                    @RequestParam(value = "customerName", required = false) String customerName,
                                                                    @RequestParam(value = "borrowStartTime", required = false) String borrowStartTime,
                                                                    @RequestParam(value = "borrowEndTime", required = false) String borrowEndTime,
                                                                    @RequestParam(value = "carNumber", required = false) String carNumber,
                                                                    @RequestParam(value = "productType", required = false) String productType,
                                                                    @RequestParam(value = "companyId", required = false) Integer companyId,
                                                                    @RequestParam(value = "contractStatus", required = false) String contractStatus,
                                                                    @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                                                    @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize);

    /**
     * ??????????????????
     * @param contractCode  ????????????
     * @param contractStatus ????????????
     * @param channelName ????????????
     * @param customerName ????????????
     * @param loadBeginDate ??????????????????
     * @param loadEndDate ??????????????????
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/page-contractvo", method = RequestMethod.GET)
    FintechResponse<Pagination<ContractVO>> pageContractVOByNativeSQL(@RequestParam(value = "contractCode", defaultValue = "") String contractCode,
                                                         @RequestParam(value = "contractStatus", defaultValue = "")ContractStatus contractStatus,
                                                         @RequestParam(value = "channelName", defaultValue = "")String channelName,
                                                         @RequestParam(value = "customerName", defaultValue = "")String customerName,
                                                         @RequestParam(value = "carNumber", defaultValue = "")String carNumber,
                                                         @RequestParam(value = "loadBeginDate", required = false)Long loadBeginDate,
                                                         @RequestParam(value = "loadEndDate", required = false)Long loadEndDate,
                                                         @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                                         @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize);

    /**
     * ????????????????????????
     * @param requisitionNumber
     * @param requisitionStatus
     * @param productType
     * @param channelName
     * @param customerName
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GetMapping(path = "/page-loanvo")
    FintechResponse<Pagination<LoanVO>> pageLoanVO(@RequestParam(value = "requisitionNumber", defaultValue = "") String requisitionNumber,
                                                   @RequestParam(value = "requisitionStatus", required = false) RequisitionStatus requisitionStatus,
                                                   @RequestParam(value = "productType", required = false) ProductType productType,
                                                   @RequestParam(value = "channelName", defaultValue = "") String channelName,
                                                   @RequestParam(value = "customerName", defaultValue = "") String customerName,
                                                   @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                                   @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize);

    @GetMapping(path = "/page-customervo")
    FintechResponse<Pagination<CustomerVO>> pageCustomerVO(@RequestParam(value = "customerName", defaultValue = "") String customerName,
                                                           @RequestParam(value = "channelOf", defaultValue = "") String channelOf,
                                                           @RequestParam(value = "companyOf", defaultValue = "") String companyOf,
                                                           @RequestParam(value = "phone", defaultValue = "") String phone,
                                                           @RequestParam(value = "customerStatus", required = false) CustomerStatus customerStatus,
                                                           @RequestParam(value = "pageIndex", required = false)Integer pageIndex,
                                                           @RequestParam(value = "pageSize", required = false)Integer pageSize);

    /**
     * ???????????????????????????????????????
     * @param userIds channelUserId ??????
     * @return
     */
    @GetMapping(path = "/count-requisition-by-status")
    FintechResponse<Integer> countRequisitionByStatus(@RequestParam(value = "userIds", defaultValue = "") String userIds,
                                                      @RequestParam(value = "customerId", defaultValue = "") String customerId,
                                                      @RequestParam(value = "requisitionStatus", defaultValue = "") String requisitionStatus);

    /**
     * ??????????????????
     * @parama ccountBankCode ??????code
     * @return
     */
    @GetMapping(path = "/get-bankinfo-by-code")
    FintechResponse<BankVO> getBankInfoByCode(@RequestParam(value = "accountBankCode") String accountBankCode);


    /**
     * ??????????????????????????????????????????????????????????????????
     * @return
     */
    @PostMapping(path = "/list-requisition-for-waitpayment-and-fail")
    FintechResponse<List<RequisitionVO>> listRequisitionForWaitpaymentAndFail(@RequestBody Date preEndDay);
}
