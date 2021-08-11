package com.fintech.insurance.micro.retrieval.persist;

import com.fintech.insurance.commons.enums.ContractStatus;
import com.fintech.insurance.commons.enums.ProductType;
import com.fintech.insurance.commons.enums.RefundStatus;
import com.fintech.insurance.commons.enums.RequisitionStatus;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.micro.dto.biz.*;
import com.fintech.insurance.micro.dto.customer.CustomerVO;
import com.fintech.insurance.micro.dto.finance.BankVO;
import com.fintech.insurance.micro.dto.retrieval.UserVO;
import com.fintech.insurance.micro.retrieval.persist.base.BaseNativeSQLDao;

import java.util.Date;
import java.util.List;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/11/30 18:41
 */
public interface BizQueryDao extends BaseNativeSQLDao {

    List<UserVO> findAllUser();

    //根据客户姓名查询客户账户id
    List<CustomerVO> listCustomerByName(String customerName);

    /**
     * 分页查询还款信息
     * @param customerContractNumber
     * @param customerName
     * @param refundStatus
     * @param requisitionNumber
     * @param refundBeginDate
     * @param refundEndDate
     * @param pageIndex
     * @param pageSize
     * @return
     */
    Pagination<RefundVO> pageRefundVOByNativeSQL(String customerContractNumber, String customerName, RefundStatus refundStatus,
                  String requisitionNumber, String carNumber, Date refundBeginDate, Date refundEndDate, Integer pageIndex, Integer pageSize);

    /**
     * 分页查询申请单信息
     * @param requisitionNumber  申请单号
     * @param status  申请单状态
     * @param type    产品类型
     * @param channelName   渠道名称
     * @param submmitStartTime  提交开始时间
     * @param submmitEndTime    提交结束时间
     * @param customerName   客户名称
     * @param pageIndex
     * @param pageSize
     * @return
     */
    Pagination<RequisitionVO> queryRequisition(String requisitionNumber, String status, String type,
                                               String channelName, Date submmitStartTime, Date submmitEndTime,
                                               String customerName, Integer pageIndex, Integer pageSize);

    /**
     * 分页查询渠道信息
     * @param channelCode
     * @param channelName
     * @param companyName
     * @param mobile
     * @param pageIndex
     * @param pageSize
     * @return
     */
    Pagination<ChannelVO> queryChannel(String channelCode, String channelName, String companyName, String mobile, Integer pageIndex, Integer pageSize);
    /**
     * 分页查询合同信息
     * @param customerContractNumber
     * @param contractStatus
     * @param channelName
     * @param customerName
     * @param loadBeginDate
     * @param loadEndDate
     * @param pageIndex
     * @param pageSize
     * @return
     */
    Pagination<ContractVO> pageContractVOByNativeSQL(String customerContractNumber, ContractStatus contractStatus, String channelName, String customerName,
                                                     String carNumber, Date loadBeginDate, Date loadEndDate, int pageIndex, int pageSize);

    Pagination<LoanVO> pageLoanVOByNativeSQL(String requisitionNumber, RequisitionStatus requisitionStatus,
                                             ProductType productType, String channelName, String customerName, Integer pageIndex, Integer pageSize);

    Integer countRequisitionByStatus(String channelUserIds, String customerId, String requisitionStatus);

    BankVO getBankInfoByCode(String accountBankCode);

    List<RequisitionVO> listRequisitionForWaitpaymentAndFail(Date preEndDay);
}
