package com.fintech.insurance.micro.retrieval.controller;

import com.fintech.insurance.commons.enums.*;
import com.fintech.insurance.commons.utils.DateCommonUtils;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.micro.api.retrieval.BizQueryAPI;
import com.fintech.insurance.micro.dto.biz.*;
import com.fintech.insurance.micro.dto.customer.CustomerVO;
import com.fintech.insurance.micro.dto.finance.BankVO;
import com.fintech.insurance.micro.dto.retrieval.UserVO;
import com.fintech.insurance.micro.retrieval.persist.BizQueryDao;
import com.fintech.insurance.micro.retrieval.persist.CustomerQueryDao;
import com.fintech.insurance.micro.retrieval.persist.ReportQueryDao;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class BizQueryController implements BizQueryAPI {

    private static final Logger LOG = LoggerFactory.getLogger(BizQueryController.class);

    @Autowired
    private BizQueryDao bizQueryDao;

    @Autowired
    private ReportQueryDao reportQueryDao;

    @Autowired
    private CustomerQueryDao customerQueryDao;

    @Override
    public FintechResponse<List<UserVO>> getAllUsers() {
        return FintechResponse.responseData(bizQueryDao.findAllUser());
    }

    @Override
    public FintechResponse<Pagination<RefundVO>> pageRefundVO(@RequestParam(name = "contractNumber", defaultValue = "") String contractNumber,
                                                              @RequestParam(name = "customerName", defaultValue = "") String customerName,
                                                              @RequestParam(name = "refundStatus", required = false) RefundStatus refundStatus,
                                                              @RequestParam(name = "requisitionNumber", defaultValue = "") String requisitionNumber,
                                                              @RequestParam(value = "carNumber", defaultValue = "") String carNumber,
                                                              @RequestParam(name = "refundBeginDate", required = false) Long refundBeginDate,
                                                              @RequestParam(name = "refundEndDate", required = false) Long refundEndDate,
                                                              @RequestParam(name = "pageIndex", defaultValue = "1") Integer pageIndex,
                                                              @RequestParam(name = "pageSize", defaultValue = "20") Integer pageSize) {
        Date begin = refundBeginDate == null ? null : DateCommonUtils.truncateDay(new Date(refundBeginDate));
        Date end = refundEndDate == null ? null : DateCommonUtils.getEndTimeOfDate(refundEndDate);
        LOG.info("pageRefundVO start !");
        LOG.error("pageRefundVO start !");
        return FintechResponse.responseData(bizQueryDao.pageRefundVOByNativeSQL(contractNumber, customerName,
                refundStatus, requisitionNumber, carNumber, begin, end, pageIndex, pageSize));
    }

    public FintechResponse<List<CustomerVO>> listCustomerByName(@RequestParam(value = "customerName") String customerName) {
        return FintechResponse.responseData(bizQueryDao.listCustomerByName(customerName));
    }

    @Override
    public FintechResponse<Pagination<RequisitionVO>> queryRequisition(@RequestParam(name = "requisitionNumber", defaultValue = "")String requisitionNumber,
                                                                       @RequestParam(name = "requisitionStatus", defaultValue = "")String requisitionStatus,
                                                                       @RequestParam(name = "productType", defaultValue = "")String productType,
                                                                       @RequestParam(name = "channelName", defaultValue = "")String channelName,
                                                                       @RequestParam(name = "submmitStartTime", required = false)Long submmitStartTime,
                                                                       @RequestParam(name = "submmitEndTime", required = false)Long submmitEndTime,
                                                                       @RequestParam(name = "customerName", defaultValue = "")String customerName,
                                                                       @RequestParam(name = "pageIndex", defaultValue = "1")Integer pageIndex,
                                                                       @RequestParam(name = "pageSize", defaultValue = "20")Integer pageSize){
        Date startTime = null;
        Date endTime = null;
        if (null != submmitStartTime) {
            startTime = DateCommonUtils.stampToDate(submmitStartTime);
        }
        if (null != submmitEndTime) {
            endTime = DateCommonUtils.stampToDate(submmitEndTime);
        }

        return FintechResponse.responseData(bizQueryDao.queryRequisition(requisitionNumber,
                requisitionStatus, productType, channelName, startTime, endTime, customerName, pageIndex, pageSize));
    }

    @Override
    public FintechResponse<Pagination<ChannelVO>> queryChannel(@RequestParam(name = "channelCode", defaultValue = "")String channelCode,
                                                               @RequestParam(name = "channelName", defaultValue = "")String channelName,
                                                               @RequestParam(name = "companyName", defaultValue = "")String companyName,
                                                               @RequestParam(name = "mobile", defaultValue = "")String mobile,
                                                               @RequestParam(name = "pageIndex", defaultValue = "1")Integer pageIndex,
                                                               @RequestParam(name = "pageSize", defaultValue = "20")Integer pageSize) {
        return FintechResponse.responseData(bizQueryDao.queryChannel(channelCode,
                channelName, companyName, mobile, pageIndex, pageSize));
    }

    public FintechResponse<Pagination<ContractVO>> pageContractVOByNativeSQL(@RequestParam(value = "contractCode", defaultValue = "") String contractCode,
                                                                             @RequestParam(value = "contractStatus", defaultValue = "")ContractStatus contractStatus,
                                                                             @RequestParam(value = "channelName", defaultValue = "")String channelName,
                                                                             @RequestParam(value = "customerName", defaultValue = "")String customerName,
                                                                             @RequestParam(value = "carNumber", defaultValue = "")String carNumber,
                                                                             @RequestParam(value = "loadBeginDate", required = false)Long loadBeginDate,
                                                                             @RequestParam(value = "loadEndDate", required = false)Long loadEndDate,
                                                                             @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                                                             @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) {

        Date begin = loadBeginDate == null ? null : DateCommonUtils.truncateDay(new Date(loadBeginDate));
        Date end = loadEndDate == null ? null : DateCommonUtils.getEndTimeOfDate(loadEndDate);
        return FintechResponse.responseData(bizQueryDao.pageContractVOByNativeSQL(contractCode, contractStatus, channelName, customerName, carNumber, begin, end, pageIndex, pageSize));
    }

    @Override
    public FintechResponse<Pagination<LoanVO>> pageLoanVO(@RequestParam(value = "requisitionNumber", defaultValue = "") String requisitionNumber,
                                                          @RequestParam(value = "requisitionStatus", required = false) RequisitionStatus requisitionStatus,
                                                          @RequestParam(value = "productType", required = false) ProductType productType,
                                                          @RequestParam(value = "channelName", defaultValue = "") String channelName,
                                                          @RequestParam(value = "customerName", defaultValue = "") String customerName,
                                                          @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                                          @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) {
        return FintechResponse.responseData(bizQueryDao.pageLoanVOByNativeSQL(requisitionNumber, requisitionStatus, productType, channelName, customerName, pageIndex, pageSize));
    }

    @Override
    public FintechResponse<Pagination<CustomerVO>> pageCustomerVO(@RequestParam(value = "customerName", defaultValue = "") String customerName,
                                                                  @RequestParam(value = "channelOf", defaultValue = "") String channelOf,
                                                                  @RequestParam(value = "companyOf", defaultValue = "") String companyOf,
                                                                  @RequestParam(value = "phone", defaultValue = "") String phone,
                                                                  @RequestParam(value = "customerStatus", required = false) CustomerStatus customerStatus,
                                                                  @RequestParam(value = "pageIndex", required = false)Integer pageIndex,
                                                                  @RequestParam(value = "pageSize", required = false)Integer pageSize) {


        return FintechResponse.responseData(customerQueryDao.pageCustomerVO(customerName, channelOf, companyOf, phone, customerStatus, pageIndex, pageSize));
    }

    @Override
    public FintechResponse<Integer> countRequisitionByStatus(@RequestParam(value = "userIds", defaultValue = "") String userIds,
                                                             @RequestParam(value = "customerId", defaultValue = "") String customerId ,
                                                             @RequestParam(value = "requisitionStatus", defaultValue = "") String requisitionStatus) {
        return FintechResponse.responseData(bizQueryDao.countRequisitionByStatus(userIds, customerId, requisitionStatus));
    }

    @Override
    public FintechResponse<BankVO> getBankInfoByCode(String accountBankCode) {
        return FintechResponse.responseData(bizQueryDao.getBankInfoByCode(accountBankCode));
    }


    @Override
    public FintechResponse<Pagination<BizReportVO>> pageBizReportVO(@RequestParam(value = "channelCode", required = false) String channelCode,
                                                                    @RequestParam(value = "customerName", required = false) String customerName,
                                                                    @RequestParam(value = "borrowStartTime", required = false) String borrowStartTime,
                                                                    @RequestParam(value = "borrowEndTime", required = false) String borrowEndTime,
                                                                    @RequestParam(value = "carNumber", required = false) String carNumber,
                                                                    @RequestParam(value = "productType", required = false) String productType,
                                                                    @RequestParam(value = "companyId", required = false) Integer companyId,
                                                                    @RequestParam(value = "contractStatus", required = false) String contractStatus,
                                                                    @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                                                    @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) {

        Date startTime = null;
        Date endTime = null;
        if (null != borrowStartTime) {
            startTime = DateCommonUtils.formDateString(borrowStartTime);
        }
        if (null != borrowEndTime) {
            endTime = DateCommonUtils.getEndTimeOfDate(borrowEndTime);
        }
        ProductType type = null;
        if(StringUtils.isNoneBlank(productType)) {
            type =  ProductType.codeOf(productType);
        }
        ContractStatus status = null;
        if(StringUtils.isNoneBlank(contractStatus)) {
            status = ContractStatus.codeOf(contractStatus);
        }
        return FintechResponse.responseData(reportQueryDao.pageBizReportVO(channelCode, customerName, companyId,startTime, endTime, carNumber, type, status, pageIndex, pageSize));
    }

    @Override
    public FintechResponse<List<RequisitionVO>> listRequisitionForWaitpaymentAndFail(@RequestBody Date preEndDay) {
        return FintechResponse.responseData(bizQueryDao.listRequisitionForWaitpaymentAndFail(preEndDay));
    }
}
