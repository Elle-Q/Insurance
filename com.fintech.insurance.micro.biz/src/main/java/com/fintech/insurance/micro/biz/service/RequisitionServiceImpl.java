package com.fintech.insurance.micro.biz.service;

import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fintech.insurance.commons.constants.BasicConstants;
import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.enums.*;
import com.fintech.insurance.commons.utils.CalculationFormulaUtils;
import com.fintech.insurance.commons.utils.DateCommonUtils;
import com.fintech.insurance.commons.utils.JacksonUtils;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.cache.RedisSequenceFactory;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.context.FInsuranceApplicationContext;
import com.fintech.insurance.micro.biz.event.RequisitionLifeCycleEvent;
import com.fintech.insurance.micro.biz.persist.dao.*;
import com.fintech.insurance.micro.biz.persist.dao.ContractDao;
import com.fintech.insurance.micro.biz.persist.dao.ProductDao;
import com.fintech.insurance.micro.biz.persist.dao.RequisitionDao;
import com.fintech.insurance.micro.biz.persist.dao.RequisitionDetailDao;
import com.fintech.insurance.micro.biz.persist.entity.*;
import com.fintech.insurance.micro.dto.IdVO;
import com.fintech.insurance.micro.dto.biz.*;
import com.fintech.insurance.micro.dto.customer.*;
import com.fintech.insurance.micro.dto.finance.DebtResult;
import com.fintech.insurance.micro.dto.finance.FinanceRepaymentPlanVO;
import com.fintech.insurance.micro.dto.finance.PaymentOrderVO;
import com.fintech.insurance.micro.dto.system.UserVO;
import com.fintech.insurance.micro.feign.customer.CustomerServiceFeign;
import com.fintech.insurance.micro.feign.finance.PaymentOrderServiceFeign;
import com.fintech.insurance.micro.feign.finance.PaymentServiceFeign;
import com.fintech.insurance.micro.feign.finance.RepaymentPlanServiceFeign;
import com.fintech.insurance.micro.feign.retrieval.BizQueryFeign;
import com.fintech.insurance.micro.feign.support.InsuranceCompanyConfigServiceFeign;
import com.fintech.insurance.micro.feign.system.EntityAuditLogServiceFeign;
import com.fintech.insurance.micro.feign.system.EntityOperationLogServiceFeign;
import com.fintech.insurance.micro.feign.system.SysUserServiceFeign;
import com.fintech.insurance.micro.feign.thirdparty.QiniuBusinessServiceFeign;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Description: (some words)
 * @Author: qxy
 * @Date: 2017/11/14 0014 16:09
 */
@Service
@Transactional
public class RequisitionServiceImpl implements RequisitionService, EnvironmentAware, ApplicationContextAware {

    private static final Logger LOG = LoggerFactory.getLogger(RequisitionServiceImpl.class);

    @Autowired
    private RequisitionDao requisitionDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private RequisitionDetailDao requisitionDetailDao;

    @Autowired
    private RequisitionDetailService requisitionDetailService;

    @Autowired
    private ContractDao contractDao;

    @Autowired
    private EntityOperationLogServiceFeign entityOperationLogServiceFeign;

    @Autowired
    private EntityAuditLogServiceFeign entityAuditLogServiceFeign;

    @Autowired
    private CustomerServiceFeign customerServiceFeign;

    @Autowired
    private SysUserServiceFeign sysUserServiceFeign;

    @Autowired
    private ProductService productService;

    @Autowired
    private ChannelDao channelDao;

    @Autowired
    private RedisSequenceFactory redisSequenceFactory;

    @Autowired
    private ContractService contractService;

    @Autowired
    private PaymentServiceFeign paymentServiceFeign;

    @Autowired
    private PaymentOrderServiceFeign paymentOrderServiceFeign;

    @Autowired
    private QiniuBusinessServiceFeign qiniuBusinessServiceFeign;

    @Autowired
    private RepaymentPlanServiceFeign repaymentPlanServiceFeign;

    @Autowired
    private BizAsyncService bizAsyncService;

    @Autowired
    private InsuranceCompanyConfigServiceFeign insuranceCompanyConfigServiceFeign;

    private Environment environment;

    private ApplicationContext applicationContext;

    @Autowired
    private RequisitionDetailResourceDao requisitionDetailResourceDao;

    @Autowired
    private AuditLogInfoService auditLogInfoService;

    @Autowired
    private BizQueryFeign bizQueryFeign;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }


    @Override
    public WeChatRequisitionVO againApplyFor(Integer requisitionId) {

        // 再次申请，需要将原来的申请单复制一份后将状态改为草稿状态
        Requisition requisition = requisitionDao.getById(requisitionId);
        if (requisition == null) {
            throw new FInsuranceBaseException(104534);
        }
        //三种情况才能重新提交
        if(!StringUtils.equals(requisition.getRequisitionStatus(), RequisitionStatus.Draft.getCode())
                && !StringUtils.equals(requisition.getRequisitionStatus(), RequisitionStatus.Rejected.getCode())
                && !StringUtils.equals(requisition.getRequisitionStatus(), RequisitionStatus.Canceled.getCode()) ){
            throw new FInsuranceBaseException(105930);
        }
        Set<RequisitionDetail> requisitionDetailSets = requisition.getDetails();
        if(requisitionDetailSets != null && requisitionDetailSets.size() > 0) {
            for(RequisitionDetail requisitionDetail: requisitionDetailSets) {
                //查询车辆是否存在贷款中的情况
                RequisitionDetail loaningRequisitionDetail = null;
                if (StringUtils.isNoneBlank(requisitionDetail.getCarNumber())) {
                    loaningRequisitionDetail = requisitionDetailDao.findLoaningRequisitionDetailByCarNumber(requisitionDetail.getCarNumber());
                }
                if (loaningRequisitionDetail != null) {
                    throw new FInsuranceBaseException(104932);
                }
            }
        }
        try {
            LOG.info("克隆requisition对象");
            Requisition newRequisition = (Requisition) requisition.clone();

            newRequisition.setId(null);
            newRequisition.setRequisitionNumber(redisSequenceFactory.generateSerialNumber(BizCategory.RK));
            newRequisition.setRequisitionStatus(RequisitionStatus.Draft.getCode());
            newRequisition.setCreateAt(new Date());
            newRequisition.setDetails(null);
            requisitionDao.save(newRequisition);
            if (requisition.getDetails() != null) {
                Set<RequisitionDetail> requisitionDetailSet = requisition.getDetails();
                for (RequisitionDetail detail : requisitionDetailSet) {
                    LOG.info("克隆RequisitionDetail对象");
                    RequisitionDetail newDetail = (RequisitionDetail) detail.clone();
                    newDetail.setId(null);
                    newDetail.setCreateAt(new Date());
                    newDetail.setRequisition(newRequisition);
                    newDetail.setRequisitionDetailResourceSet(null);
                    requisitionDetailDao.save(newDetail);

                    Set<RequisitionDetailResource> resourceSet = detail.getRequisitionDetailResourceSet();
                    if (resourceSet != null) {
                        for (RequisitionDetailResource resource : resourceSet) {
                            LOG.info("克隆RequisitionDetailResource对象");
                            resource = (RequisitionDetailResource) resource.clone();
                            resource.setId(null);
                            resource.setCreateAt(new Date());
                            resource.setRequisitionDetail(newDetail);
                            requisitionDetailResourceDao.save(resource);
                        }
                    }
                }
            }

            return convertEntityToVO(newRequisition);

        } catch (CloneNotSupportedException e) {
            LOG.info(e.getMessage());
            throw new FInsuranceBaseException(104525);
        }

    }

    @Override
    @Transactional(readOnly = true)
    public RequisitionVO getProductTypeAndRepayDayTypeByContractNumber(String contractNumber) {
        Contract contract = contractDao.getContractInfoByContractNo(contractNumber);
        if (contract == null) {
            throw new FInsuranceBaseException(104521, new Object[]{"contractNumber = ", contractNumber});
        }
        Requisition requisition = contract.getRequisition();
        if (requisition == null) {
            throw new FInsuranceBaseException(104534, new Object[]{"contractNumber = ", contractNumber});
        }
        RequisitionVO vo = new RequisitionVO();
        vo.setRepayDayType(requisition.getRepayDayType());
        vo.setProductType(requisition.getProductType());
        vo.setOverdueFineRate(requisition.getOverdueFineRate());
        vo.setId(requisition.getId());
        return vo;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    @Transactional(timeout = 100)
    public Boolean payRequisition(Integer requisitionId) {
        Requisition requisition = requisitionDao.getById(requisitionId);
        if (requisition == null) {
            throw new FInsuranceBaseException(104534);
        }

        // 判断当前是否为待支付状态
        if (!StringUtils.equals(requisition.getRequisitionStatus(), RequisitionStatus.WaitingPayment.getCode())
                && !StringUtils.equals(requisition.getRequisitionStatus(), RequisitionStatus.FailedPayment.getCode())) {
            throw new FInsuranceBaseException(104520);
        }

        // 获取申请单的服务费支付订单
        FintechResponse<PaymentOrderVO> paymentOrderResponse = paymentOrderServiceFeign.getByOrderNumber(requisition.getPaymentOrderNumber());
        if (!paymentOrderResponse.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(paymentOrderResponse);
        }
        if (null == paymentOrderResponse.getData()) {
            LOG.error("Failed to find the payment order by number: " + requisition.getPaymentOrderNumber());
            throw new FInsuranceBaseException(105009, new Object[]{requisition.getPaymentOrderNumber()});
        }
        PaymentOrderVO paymentOrderVO = paymentOrderResponse.getData();
        // 判断当前是否为待支付状态或支付失败
        if (!StringUtils.equals(paymentOrderVO.getPaymentStatus(), DebtStatus.FAILED.getCode())
                && !StringUtils.equals(paymentOrderVO.getPaymentStatus(), DebtStatus.WAITINGDEBT.getCode())) {
            throw new FInsuranceBaseException(104529);
        }

        // 拿到客户信息
        FintechResponse<CustomerSimpleVO> response = customerServiceFeign.getCustomerSimpleInfo(paymentOrderVO.getCustomerId());
        if (response == null || response.getData() == null) {
            throw new FInsuranceBaseException(103007);
        }
        CustomerSimpleVO simpleVO = response.getData();

        // 检查银行卡扣款次数是否充足
        FintechResponse<Boolean> isAvailabDetaTimesRes = paymentServiceFeign.checkDebtAvailable(simpleVO.getBankCardNumber());
        if (!isAvailabDetaTimesRes.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(isAvailabDetaTimesRes);
        }
        if (!isAvailabDetaTimesRes.getData()) {
            throw new FInsuranceBaseException(105016);
        }

        FintechResponse<VoidPlaceHolder> fintechResponse = paymentOrderServiceFeign.updateBankAccountNumber(paymentOrderVO.getId(), simpleVO.getBankCardNumber());
        if (!fintechResponse.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(fintechResponse);
        }
        LOG.info("prepare to pay the paymentorder by number: " + requisition.getPaymentOrderNumber() + "for customer with name :" +
                simpleVO.getCustomerName());
        FintechResponse<DebtResult> debtResultFintechResponse = paymentServiceFeign.debt(simpleVO.getCustomerName(),
                simpleVO.getIdNumber(), simpleVO.getBankCardNumber(), simpleVO.getPhone(), paymentOrderVO.getTotalAmount(),
                YjfDebtInfoPrefix.REQUSITION.getSuffix() + requisition.getRequisitionNumber());
        if (!debtResultFintechResponse.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(debtResultFintechResponse);
        }

        LOG.info("debtResult's status is : " + debtResultFintechResponse.getData().getIsSuccess());

        if (!debtResultFintechResponse.getData().getIsSuccess()) { // 扣款失败
            // 更新支付单状态和订单状态
            paymentOrderServiceFeign.changePaymentOrderStatus(requisition.getPaymentOrderNumber(),
                    debtResultFintechResponse.getData().getRequestSerialNum(), DebtStatus.FAILED);
            changeRequisitionStatusByRequsitionId(requisitionId, RequisitionStatus.FailedPayment, true);
            LOG.info("Failed to pay the paymentorder by number: " + requisition.getPaymentOrderNumber() + "" +
                    "the error is :" + debtResultFintechResponse.getData().getFailedMessage());
            return false;
        } else { // 扣款受理成功
            paymentOrderServiceFeign.changePaymentOrderStatus(requisition.getPaymentOrderNumber(),
                    debtResultFintechResponse.getData().getRequestSerialNum(), DebtStatus.PROCESSING);
            LOG.info("success to pay the paymentorder by number: " + requisition.getPaymentOrderNumber());
            return true;
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void changeRequisitionStatusByRequsitionId(Integer requisitionId, RequisitionStatus requisitionStatus, boolean sendWechatNotification) {
        // 更改申请单状态
        LOG.info("update requsition status on requisitionId = {} to {}", requisitionId, requisitionStatus.getCode());
        Requisition requisition = requisitionDao.getById(requisitionId);
        if (requisition == null) {
            throw new FInsuranceBaseException(104534);
        }
        String oldStatus = requisition.getRequisitionStatus();
        if (requisitionStatus == RequisitionStatus.Auditing) { // 将订单状态提交为待审核
            // 先判断订单状态是否是草稿状态，已提交状态，取消状态
            if (requisition.getRequisitionStatus().equals(RequisitionStatus.Draft.getCode())
                    || requisition.getRequisitionStatus().equals(RequisitionStatus.Submitted.getCode())
                    || requisition.getRequisitionStatus().equals(RequisitionStatus.Canceled.getCode())) {
                requisition.setRequisitionStatus(RequisitionStatus.Auditing.getCode());
            }
        } else if (requisitionStatus == RequisitionStatus.Canceled) {
            if (requisition.getRequisitionStatus().equals(RequisitionStatus.Draft.getCode())
                    || requisition.getRequisitionStatus().equals(RequisitionStatus.Submitted.getCode())
                    || requisition.getRequisitionStatus().equals(RequisitionStatus.Auditing.getCode())
                    || requisition.getRequisitionStatus().equals(RequisitionStatus.Rejected.getCode())) {
                requisition.setRequisitionStatus(RequisitionStatus.Canceled.getCode());
            }
        } else if (requisitionStatus == RequisitionStatus.WaitingLoan) { // 支付成功
            if (requisition.getRequisitionStatus().equals(RequisitionStatus.WaitingPayment.getCode())
                    || requisition.getRequisitionStatus().equals(RequisitionStatus.FailedPayment.getCode())) {
                requisition.setRequisitionStatus(RequisitionStatus.WaitingLoan.getCode());
                contractService.generateContractInfoOfRequsition(requisitionId, true, true);
                //期初还款确认支付更新还款计划状态为已还款
                if(RepayDayType.codeOf(requisition.getRepayDayType()) == RepayDayType.INITIAL_PAYMENT){
                    FintechResponse<VoidPlaceHolder> updateRepaymentResp = repaymentPlanServiceFeign.updateFirstRepaymentPlanBInitialPaymentSuccess(new IdVO(requisitionId));
                    if(!updateRepaymentResp.isOk()){
                        LOG.error("Failed to update the first instalment of contracts relatived to requisition: " + requisitionId);
                        throw FInsuranceBaseException.buildFromErrorResponse(updateRepaymentResp);
                    }
                }
                /*LOG.info("TTTT try to Async to generate contract file: {}", requisitionId);
                bizAsyncService.generateContractFileForRequsition(requisitionId);
                LOG.info("TTTT back to save requistion from contract file generated: {}", requisitionId);*/
                // 同步产生合同文件
                try{
                    contractService.generateContractFileOfRequsition(requisitionId);
                } catch (Exception e) {//合同文件为非关键性事务， 以防出现异常导致合同号的生成。 合同文件失败后会有定时任务创建
                    LOG.error("Failed to generate contract file for requistion: " + requisitionId, e);
                }
            }
        } else if (requisitionStatus == RequisitionStatus.FailedPayment) { // 支付失败
            if (requisition.getRequisitionStatus().equals(RequisitionStatus.WaitingPayment.getCode())) {
                requisition.setRequisitionStatus(RequisitionStatus.FailedPayment.getCode());
            }
        } else if (requisitionStatus == RequisitionStatus.WaitingPayment) { // 审核通过变为待支付
            if (requisition.getRequisitionStatus().equals(RequisitionStatus.Auditing.getCode())) {
                requisition.setRequisitionStatus(RequisitionStatus.WaitingPayment.getCode());
                requisition.setAuditSuccessTime(new Date());
            }
            //审核通过生成待还款推送
            //this.applicationContext.publishEvent(new RequisitionLifeCycleEvent(requisition, oldStatus, RequisitionStatus.Auditing.getCode()));
        }else {
            requisition.setRequisitionStatus(requisitionStatus.getCode());
        }
        requisition.setUpdateBy(FInsuranceApplicationContext.getCurrentUserId());
        this.requisitionDao.save(requisition);
        // i do not know what could i say fuck!
        try {
            if (!requisitionStatus.getCode().equals(oldStatus) && sendWechatNotification) {
                this.applicationContext.publishEvent(new RequisitionLifeCycleEvent(requisition, oldStatus, requisitionStatus.getCode()));
            }
        } catch (Exception e) {
            LOG.error("publish event failed: " + e.getMessage(), e);
        }

        LOG.info("update requsition status success on requisitionId = {} to {}", requisitionId, requisitionStatus.getCode());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void changeRequisitionStatusByPaymentOrder(String paymentOrderNumber, RequisitionStatus requisitionStatus) {
        Requisition requisition = requisitionDao.getRequisitionByPaymentOrderNumber(paymentOrderNumber);
        if (null != requisition) {
            changeRequisitionStatusByRequsitionId(requisition.getId(), requisitionStatus, true);
        }
    }

    private WeChatRequisitionVO convertEntityToVO(Requisition requisition) {
        if (requisition == null) {
            throw new FInsuranceBaseException(104534);
        }
        //产品类型
        ProductType productType = ProductType.codeOf(requisition.getProductType());
        RepayDayType repayDayType = RepayDayType.codeOf(requisition.getRepayDayType());
        WeChatRequisitionVO vo = new WeChatRequisitionVO();
        if (requisition.getChannelUserId().equals(FInsuranceApplicationContext.getCurrentUserId())
                || requisition.getCustomerId().equals(FInsuranceApplicationContext.getCurrentUserId())) {
            vo.setCanEdit(1);
        }
        vo.setRequisitionId(requisition.getId());
        vo.setCustomerId(requisition.getCustomerId());
        vo.setRequisitionStatus(requisition.getRequisitionStatus());
        vo.setProductType(requisition.getProductType());
        vo.setRequisitionAmount(requisition.getTotalApplyAmount().doubleValue());
        // 计算服务费、第一期应还、保证金、
        // 服务费 =  Σ[（服务费率+其他费用率）*单个车辆借款金额
        List<Contract> contractList = contractDao.listByRequisitionId(requisition.getId());
        //服务费
        Double serviceFee =  0.00;
        // 第一期应付金额
        Double totalFirst = 0.0;
        //保证金
        Double assureMoney = 0.0;
        if (contractList != null) {
            for (Contract contract : contractList) {
                serviceFee += CalculationFormulaUtils.getFee(contract.getContractAmount().doubleValue(), requisition.getOtherFeeRate() + requisition.getServiceFeeRate());
                if(repayDayType == RepayDayType.INITIAL_PAYMENT) {//期初还款才有第一期还款金额
                    totalFirst += CalculationFormulaUtils.getFirstRepayMoney(contract.getContractAmount(), contract.getBusinessDuration(), contract.getInterestRate(), repayDayType).doubleValue();
                }
                if(productType == ProductType.CAR_INSTALMENTS){
                    assureMoney += CalculationFormulaUtils.getAssureMoney(contract.getContractAmount(), contract.getBusinessDuration()).doubleValue();
                }
            }
        }
        vo.setServiceFee(serviceFee);
        vo.setRepaymentAmountFirst(totalFirst);
        vo.setAssureMoney(assureMoney);
        vo.setTotalAmount(serviceFee + totalFirst + assureMoney);

        // 企业名称
        FintechResponse<CustomerVO> customerResponse = customerServiceFeign.getCustomerAccountInfoById(requisition.getCustomerAccountInfoId());
        if (customerResponse != null && customerResponse.getData() != null) {
            CustomerVO customerVO = customerResponse.getData();
            vo.setEnterpriseName(customerVO.getCompanyOf());
        } else {
            LOG.error("获取客户信息失败");
        }

        // 车辆数目
        List<RequisitionDetail> requisitionDetailList = requisitionDetailDao.getRequisitionDetailByRequisition_Id(requisition.getId());
        if (requisitionDetailList == null || requisitionDetailList.size() <= 0) {
            vo.setCarCount(0);
            vo.setInsuranceCount(0);
        } else {
            vo.setCarCount(requisitionDetailList.size());
            int sum = 0;
            for (RequisitionDetail detail : requisitionDetailList) {
                if (StringUtils.isNotBlank(detail.getCommercialInsuranceNumber())) {
                    sum ++;
                }
                if (StringUtils.isNotBlank(detail.getCompulsoryInsuranceNumber())) {
                    sum ++;
                }
            }
            vo.setInsuranceCount(sum);
        }
        vo.setRepayType(requisition.getRepayType());
        vo.setRepayDayType(requisition.getRepayDayType());
        vo.setRequisitionNumber(requisition.getRequisitionNumber());
        if (requisition.getChannel() != null) {
            vo.setChannelCode(requisition.getChannel().getChannelCode());
        }
        vo.setCreateAt(requisition.getCreateAt());
        // 获取备注信息
        FintechResponse<String> remarkResponse = entityAuditLogServiceFeign.getRemark(EntityType.REQUISITION.getCode(), requisition.getId(), AuditStatus.REJECTED.getCode());
        if (remarkResponse != null && StringUtils.isNoneBlank(remarkResponse.getData())) {
            vo.setRemark("退回原因:" + remarkResponse.getData());
        }
        // 支付状态
        FintechResponse<PaymentOrderVO> paymentOrderVOFintechResponse = paymentOrderServiceFeign.getByOrderNumber(requisition.getPaymentOrderNumber());
        if (paymentOrderVOFintechResponse.isOk() && paymentOrderVOFintechResponse.getData() != null) {
            vo.setDebtStatus(paymentOrderVOFintechResponse.getData().getPaymentStatus());
            Date deadlinePaymentTime = DateCommonUtils.getEndTimeOfDate(DateCommonUtils.getAfterDay(requisition.getAuditSuccessTime(), 1));
            vo.setDeadlinePaymentTime(deadlinePaymentTime);
        }
        // 是否绑卡
        FintechResponse<CustomerBankCardVO> customerBankCardVOFintechResponse = customerServiceFeign.getCustomerBankCard(requisition.getCustomerId());
        if (customerBankCardVOFintechResponse == null || customerBankCardVOFintechResponse.getData() == null) {
            vo.setHasBindCard(0);
        } else {
            vo.setHasBindCard(1);
        }
        vo.setIsChannelApplication(requisition.getChannelApplication() ? 1 : 0);
        return vo;
    }

    @Override
    @Transactional(readOnly = true)
    public WeChatRequisitionVO getWeChatRequisitionVO(Integer requisitionId) {
        Requisition requisition = requisitionDao.getRequisitionById(requisitionId);
        if(requisition == null){
            LOG.error("getWeChatRequisitionVO failed null requisition with requisitionId=[" + requisitionId + "]");
            throw new FInsuranceBaseException(104904, new Object[]{requisitionId});
        }
        return convertEntityToVO(requisition);
    }

    @Override
    @Transactional(readOnly = true)
    public RequisitionVO getRequisitionById(Integer id) {
        if (id == null) {
           return null;
        }

        Requisition requisition = requisitionDao.getRequisitionById(id);
        if (requisition == null) {
            LOG.error("Unmatched id [" + id + "] in Requisition!");
            return null;
        }

        RequisitionVO requisitionVO = this.entityToVO(requisition);

        //处理渠道用户信息
        if (requisition.getChannelUserId() != null) {
            UserVO userVO = sysUserServiceFeign.getUserById(requisition.getChannelUserId()).getData();
            if (userVO != null) {
                requisitionVO.setChannelUserName(userVO.getName());
                requisitionVO.setChannelUserMobile(userVO.getMobile());
                requisitionVO.setChannelUserId(userVO.getId());
            }
        }

        //处理客户信息
        if (requisition.getCustomerId() != null && requisition.getChannel() != null) {
            FintechResponse<CustomerVO> customerVOResponse = customerServiceFeign.getCustomerAccountInfoById(requisition.getCustomerAccountInfoId());
            if (customerVOResponse != null && customerVOResponse.getData() != null) {
                requisitionVO.setCustomerName(customerVOResponse.getData().getName());
                requisitionVO.setCustomerMobile(customerVOResponse.getData().getPhone());
                requisitionVO.setEnterpriseName(customerVOResponse.getData().getEnterpriseName());
            }
        }

        return requisitionVO;
    }

    @Override
    @Transactional(readOnly = true)
    public RequisitionVO getRequisitionVOByRequisitionNumber(String requisitionNumber) {
        Requisition requisition = requisitionDao.getRequisitionByRequisitionNumber(requisitionNumber);
        return this.entityToVO(requisition);
    }


    //实体转VO
    private RequisitionVO entityToVO(Requisition requisition) {
        if (null == requisition) {
            return null;
        }
        RequisitionVO vo = new RequisitionVO();
        vo.setId(requisition.getId());
        vo.setRequisitionNumber(requisition.getRequisitionNumber());
        vo.setCustomerId(requisition.getCustomerId());
        vo.setCustomerAccountInfoId(requisition.getCustomerAccountInfoId());
        if (requisition.getChannel() != null) {
            vo.setChannelId(requisition.getChannel().getId());
            vo.setChannelCode(requisition.getChannel().getChannelCode());
            vo.setChannelName(requisition.getChannel().getChannelName());
        }
        vo.setChannelUserId(requisition.getChannelUserId());
        vo.setProductType(requisition.getProductType());
        vo.setRequisitionStatus(requisition.getRequisitionStatus());
        vo.setServiceFeeRate(requisition.getServiceFeeRate() + requisition.getOtherFeeRate());
        vo.setServiceFee(CalculationFormulaUtils.getFee(requisition.getTotalApplyAmount().doubleValue() , requisition.getServiceFeeRate() + requisition.getOtherFeeRate()));
        vo.setOtherFeeRate(requisition.getOtherFeeRate());
        vo.setPrepaymentPenaltyRate(requisition.getPrepaymentPenaltyRate());
        vo.setPrepaymentDays(requisition.getPrepaymentDays());
        vo.setOverdueFineRate(requisition.getOverdueFineRate());
        vo.setBusinessDuration(requisition.getBusinessDuration());
        vo.setMaxOverdueDays(requisition.getMaxOverdueDays());

        vo.setPaymentOrderNumber(requisition.getPaymentOrderNumber());
        vo.setSubmmitTime(requisition.getSubmissionDate());
        vo.setBorrowAmount(requisition.getTotalApplyAmount().doubleValue());
        if (requisition.getTotalApplyAmount() <= 0) {
            vo.setPercentage(new BigDecimal(0.00));
        } else {
            BigDecimal percentage = new BigDecimal(100 * (requisition.getTotalCompulsoryAmount() + requisition.getTotalTaxAmount())).divide(new BigDecimal(requisition.getTotalApplyAmount()) , 2, BigDecimal.ROUND_HALF_UP);
            vo.setPercentage(percentage);
        }
        vo.setLoanAccountNumber(null == requisition.getLoanAccountNumber() ? "" : requisition.getLoanAccountNumber());
        vo.setTotalCommercialAmount(requisition.getTotalCommercialAmount().doubleValue());
        vo.setTotalCompulsoryAmount(requisition.getTotalCompulsoryAmount().doubleValue());
        vo.setTotalTaxAmount(requisition.getTotalTaxAmount().doubleValue());
        if(StringUtils.equals(requisition.getRequisitionStatus(), RequisitionStatus.Draft.getCode()) ){
            vo.setTotalApplyAmount(0.0);
        }else{
            vo.setTotalApplyAmount(requisition.getTotalApplyAmount().doubleValue());
        }
        vo.setAuditSuccessTime(requisition.getAuditSuccessTime());
        vo.setLatestAuditBatch(requisition.getLatestAuditBatch());
        vo.setRepayDayType(requisition.getRepayDayType());
        vo.setRepayType(requisition.getRepayType());
        vo.setLoanAccountType(requisition.getLoanAccountType());
        vo.setLoanAccountBank(requisition.getLoanAccountBank());
        vo.setLoanAccountBankBranch(requisition.getLoanAccountBankBranch());
        vo.setLoanAccountName(requisition.getLoanAccountName());
        vo.setInsuranceCompanyName(requisition.getInsuranceCompanyName());
        vo.setInsuranceBranchName(requisition.getInsuranceBranchName());
        vo.setLoanTime(requisition.getLoanTime());
        vo.setChannelApplication(requisition.getChannelApplication());
        vo.setCreateBy(requisition.getCreateBy());
        if (null != requisition.getProduct()){
            vo.setProductId(requisition.getProduct().getId());
            vo.setProductName(requisition.getProduct().getProductName());
        }
        Set<RequisitionDetail> requisitionDetailSet = requisition.getDetails();
        if (requisitionDetailSet.size() > 0) {
            vo.setCarNum(requisitionDetailSet.size());
            int insuranceNum = 0;//保单数量
            for (RequisitionDetail r : requisitionDetailSet) {
                //统计每个车辆下的保单数量
                if (null != r.getCommercialInsuranceNumber()) {//商业险
                    insuranceNum++;
                }
                if (null != r.getCompulsoryInsuranceNumber()) {//交强险
                    insuranceNum++;
                }
            }
            vo.setInsuranceNum(insuranceNum);
        }
        vo.setHandheldCertificate(requisition.getIdCardEvidence());
        if (null != requisition.getOtherResource()) {
            //其他材料
            List<String> otherResourceList = JacksonUtils.fromJson(requisition.getOtherResource(), new TypeReference<List<String>>() {
            });
            if(otherResourceList != null && otherResourceList.size() > 0) {
                vo.setOtherResource(otherResourceList.toArray(new String[otherResourceList.size()]));
            }
        }
        vo.setTotalPayAmount(this.getTotalPayAmount(requisition));
        return vo;
    }

    //获取申请单待支付金额
    private Double getTotalPayAmount(Requisition requisition){
        Double totalPayAmount = 0.0;
        if(!StringUtils.equals(requisition.getRequisitionStatus(), RequisitionStatus.WaitingPayment.getCode())) {
            return totalPayAmount;
        }
        RepayDayType repayDayType = RepayDayType.codeOf(requisition.getRepayDayType());
        ProductType productType = ProductType.codeOf(requisition.getProductType());
        // 计算服务费、第一期应还、保证金、
        List<Contract> contractList = contractDao.listByRequisitionId(requisition.getId());
        if (contractList != null) {
            for (Contract contract : contractList) {
                totalPayAmount += CalculationFormulaUtils.getFee(contract.getContractAmount().doubleValue(), requisition.getOtherFeeRate() + requisition.getServiceFeeRate());
                if(repayDayType == RepayDayType.INITIAL_PAYMENT) {//期初还款才有第一期还款金额
                    totalPayAmount += CalculationFormulaUtils.getFirstRepayMoney(contract.getContractAmount(), contract.getBusinessDuration(), contract.getInterestRate(), repayDayType).doubleValue();
                }
                if(productType == ProductType.CAR_INSTALMENTS){
                    totalPayAmount += CalculationFormulaUtils.getAssureMoney(contract.getContractAmount(), contract.getBusinessDuration()).doubleValue();
                }
            }
        }
        return totalPayAmount;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DurationVO> listDuration(Integer id) {
        Requisition requisition = requisitionDao.getById(id);
        //验证订单信息是否存在
        if (null == requisition) {
            LOG.error("null requisition with id[" + id + "]");
            throw new FInsuranceBaseException(104519);
        }
        List<Contract> contractList = contractDao.listByRequisitionId(requisition.getId());//属于该订单的合同信息
        List<DurationVO> durationVOList = new ArrayList<>();
        if (null != contractList && contractList.size() > 0) {
            for (Contract contract : contractList) {
                List<RequisitionDetail> requisitionDetailList = requisitionDetailDao.listRequisitionDetail(contract.getId());//每个合同下的车辆信息
                RequisitionDetail requisitionDetail = null;
                int insuranceNum = 0;//保单数量
                if (null != requisitionDetailList && requisitionDetailList.size() > 0) {
                    requisitionDetail = requisitionDetailList.get(0);
                    if (null != requisitionDetail.getCommercialInsuranceNumber()) {//商业险
                        insuranceNum++;
                    }
                    if (null != requisitionDetail.getCompulsoryInsuranceNumber()) {//交强险
                        insuranceNum++;
                    }
                }
                DurationVO durationVO = this.convertToDurationVO(contract, requisition, requisitionDetail, insuranceNum);
                durationVOList.add(durationVO);
            }
        }
        if (contractList == null || contractList.size() < 1) {
            return Collections.emptyList();
        }
        return durationVOList;
    }

    //转化分期信息VO
    private DurationVO convertToDurationVO(Contract contract, Requisition requisition, RequisitionDetail requisitionDetail, int insuranceNum) {

        DurationVO durationVO = new DurationVO();
        durationVO.setContractId(contract.getId());
        durationVO.setContractCode(contract.getContractNumber());
        durationVO.setRepayType(requisition.getRepayType());
        durationVO.setRepayDayType(requisition.getRepayDayType());
        durationVO.setInsuranceNum(insuranceNum);//保单数量
        durationVO.setDuration(contract.getBusinessDuration());
        durationVO.setBorrowStartTime(contract.getStartDate());
        durationVO.setBorrowEndTime(contract.getEndDate());
        durationVO.setBorrowAmount(contract.getContractAmount().doubleValue());
        durationVO.setInterestRate(contract.getInterestRate());
        durationVO.setLoanRatio(contract.getLoanRatio());
        if (null != requisitionDetail) {
            durationVO.setCarNumber(requisitionDetail.getCarNumber());
        }
        return durationVO;
    }

    @Override
    public void cancel(RequisitionVO requisitionVO, Integer currentLoginUserId) {
        Requisition requisition = requisitionDao.getById(requisitionVO.getId());
        if (null == requisition) {
            throw new FInsuranceBaseException(104505);
        }
        String oldStatus = requisition.getRequisitionStatus();
        requisition.setRequisitionStatus(RequisitionStatus.Canceled.getCode());
        requisition.setUpdateAt(new Date());
        requisitionDao.save(requisition);
        // i do not know what could i say fuck!
        if (!RequisitionStatus.Canceled.getCode().equals(oldStatus)) {
            this.applicationContext.publishEvent(new RequisitionLifeCycleEvent(requisition, oldStatus, RequisitionStatus.Canceled.getCode()));
        }

        //生成操作记录
        OperationRecordVO operationrRecordVO = new OperationRecordVO();
        operationrRecordVO.setOperationType(OperationType.CANCELED.getCode());
        operationrRecordVO.setEntityType(EntityType.REQUISITION.getCode());
        operationrRecordVO.setEntityId(requisitionVO.getId());
        operationrRecordVO.setUserId(currentLoginUserId);
        entityOperationLogServiceFeign.createLog(operationrRecordVO);
    }

    @Override
    public void save(RequisitionVO requisitionVO) {
        Requisition requisition = requisitionDao.getById(requisitionVO.getId());
        if (requisition != null) {
            //获得申请单状态的变化
            String oldStatus = requisition.getRequisitionStatus();
            String newStatus = requisitionVO.getRequisitionStatus();
            requisition.setRequisitionStatus(requisitionVO.getRequisitionStatus());
            if (null != requisitionVO.getAuditSuccessTime()) {
                requisition.setAuditSuccessTime(requisitionVO.getAuditSuccessTime());
            }
            if (null != requisitionVO.getUpdateAt()) {
                requisition.setUpdateAt(requisitionVO.getUpdateAt());
            }
            if (null != requisitionVO.getPaymentOrderNumber()) {
                requisition.setPaymentOrderNumber(requisitionVO.getPaymentOrderNumber());
            }
            if (null != requisitionVO.getUpdateBy()) {
                requisition.setUpdateBy(requisitionVO.getUpdateBy());
            }
            requisitionDao.save(requisition);
            //提交申请单状态变化事件
            if (StringUtils.isNotEmpty(newStatus) && !newStatus.equals(oldStatus)) {
                // i do not know what could i say fuck!
                this.applicationContext.publishEvent(new RequisitionLifeCycleEvent(requisition, oldStatus, newStatus));
            }
        }
    }

    /*@Override
    public void submitForCustomerConfirm(RequisitionVO requisitionVO) {
        if (requisitionVO != null) {
            if (RequisitionStatus.Draft.getCode().equalsIgnoreCase(requisitionVO.getRequisitionStatus()) || RequisitionStatus.Rejected.getCode().equalsIgnoreCase(requisitionVO.getRequisitionStatus())) {
                Requisition requisition = this.requisitionDao.getRequisitionById(requisitionVO.getId());
                String oldStatus = requisition.getRequisitionStatus();
                requisition.setRequisitionStatus(RequisitionStatus.Submitted.getCode());
                requisition.setUpdateAt(new Date());
                //计算金额并存储申请单
                this.calculateRequisition(requisition);
                // i do not know what could i say fuck!
                if (!RequisitionStatus.Submitted.getCode().equals(oldStatus)) {
                    this.applicationContext.publishEvent(new RequisitionLifeCycleEvent(requisition, oldStatus, RequisitionStatus.Submitted.getCode()));
                }
                //生成并存储合同信息
                this.generateContract(requisition);
            } else {
                LOG.warn("requisition with id " + requisitionVO.getId() + " now is in " + requisitionVO.getRequisitionStatus() + " status which cannot be submitted with this status");
            }
        }
    }*/

    @Override
    public void submitForAudit(RequisitionVO requisitionVO, Integer currentLoginUserId) {
        if (requisitionVO != null) {
            if (RequisitionStatus.Submitted.getCode().equalsIgnoreCase(requisitionVO.getRequisitionStatus())) {
                Requisition requisition = this.requisitionDao.getRequisitionById(requisitionVO.getId());
                //生成审批批次号
                String auditBatchNumber = redisSequenceFactory.generateSerialNumber(BizCategory.REQ_AUDIT_ID);
                //DigestUtils.md5DigestAsHex((RandomStringUtils.random(32) + String.valueOf(requisition.getId()) + String.valueOf(System.currentTimeMillis())).getBytes());
                requisition.setLatestAuditBatch(auditBatchNumber);
                String oldStatus = requisition.getRequisitionStatus();
                requisition.setRequisitionStatus(RequisitionStatus.Auditing.getCode());
                Date submissionTime = new Date();
                requisition.setUpdateAt(submissionTime);
                requisition.setSubmissionDate(submissionTime);
                this.requisitionDao.save(requisition);

                //生成申请单审核记录
                this.generateRequisitionAuditRecordForSubmission(requisition.getId(), auditBatchNumber, currentLoginUserId);

            }
        }
    }

    /**
     * 计算整个申请单金额
     * @param requisition 申请单
     * @return 返回总申请金额
     */
    @Override
    public Integer calculateRequisition(Requisition requisition) {
        if (requisition != null && requisition.getDetails() != null) {
            for (RequisitionDetail requisitionDetail : requisition.getDetails()) {
                if (requisitionDetail != null) {
                    this.calculateRequisitionDetail(requisitionDetail);
                    //计算可贷金额
                    int actualDuration = requisition.getBusinessDuration();
                    if (requisitionDetail.getMaxDuration() < requisition.getBusinessDuration()) {
                        actualDuration = requisitionDetail.getMaxDuration();
                    }
                    requisitionDetail.setBusinessDuration(actualDuration);
                    this.requisitionDetailDao.save(requisitionDetail);
                }
            }
            //计算总申请金额
            this.updateRequisition(requisition);
            this.requisitionDao.save(requisition);
            return requisition.getTotalApplyAmount();
        } else {
            return 0;
        }
    }

    /**
     * 计算单个申请单明细金额
     * 场景1：添加车辆时，只需设置好车辆相关的商业保险和车船税、交强险的原始信息，则通过调用此方法来自动计算单个车辆的商业保险残值以及可贷金额
     * 场景2：提交申请单时，计算整个申请单的金额
     * @param requisitionDetail
     */
    @Override
    public void calculateRequisitionDetail(RequisitionDetail requisitionDetail) {
        if (requisitionDetail != null) {
            Requisition requisition = requisitionDetail.getRequisition();
            Date beginDate = requisitionDetail.getCommercialInsuranceStart().before(DateCommonUtils.getToday()) ? DateCommonUtils.getToday() : requisitionDetail.getCommercialInsuranceStart();
            ProductType productType = ProductType.codeOf(requisition.getProductType());
            Map<String, ProductRate> productRateMap = this.productService.getProductRate(requisition.getProduct());
            //商业险残值
            Integer commercialInsuranceValue = CalculationFormulaUtils.getCommercialRisk( requisitionDetail.getCommercialInsuranceAmount(), beginDate, requisitionDetail.getCommercialInsuranceEnd(), productType).intValue();
            //最大可贷期数
            int businessDuration = this.computeMaxAvaiableInstalmentNum(beginDate, requisitionDetail.getCommercialInsuranceEnd(), requisition);

            LOG.debug("The max duration for commercial insurance with number " + requisitionDetail.getCommercialInsuranceNumber() + " is " + businessDuration);
            requisitionDetail.setMaxDuration(businessDuration);//该业务的最大期数
            requisitionDetail.setCommercialInsuranceValue(commercialInsuranceValue);//保单残余价值
            ProductRate rate = productRateMap.get(String.valueOf(businessDuration));
            if(rate != null ){
                LOG.debug("Current requisition detail with commercial insurance number " + requisitionDetail.getCommercialInsuranceNumber() + " supports to apply full amount");
                BigDecimal bigDecimal = new BigDecimal(requisitionDetail.getCompulsoryInsuranceAmount() + requisitionDetail.getTaxAmount()).add(new BigDecimal(requisitionDetail.getCommercialInsuranceValue()).divide(new BigDecimal(CalculationFormulaUtils.NUMBER), 6, BigDecimal.ROUND_HALF_UP));
                requisitionDetail.setSubTotal(CalculationFormulaUtils.getApplyMoney(bigDecimal, rate.getLoanRatio())); //商业险+交强险+车船税
            }
            this.requisitionDetailDao.save(requisitionDetail);
        }
    }

    /*/**
     * 根据申请单生成合同
     * @param requisition
     * @return 合同
     */
    /*@Override
    public List<Contract> generateContract(Requisition requisition) {
        if (requisition == null) {
            return null;
        } else {

            Set<RequisitionDetail> details = requisition.getDetails();
            List<Contract> oldContracts = this.contractDao.listByRequisitionId(requisition.getId());

            //重新生成合同数据
            Map<String, ProductRate> ratesMap = this.productService.getProductRate(requisition.getProduct());

            Map<String, Contract> contractsMap = new HashMap<>();
            ProductRate productRate = null;
            for (RequisitionDetail detail : details) {
                Contract contract = contractsMap.get(String.valueOf(detail.getBusinessDuration()));
                if (contract == null) {
                    contract = new Contract();
                    contract.setRequisition(requisition);
                    contract.setCustomerId(requisition.getCustomerId());
                    contract.setChannel(requisition.getChannel());
                    *//*contract.setSerialNumber(contractService.generateContractSerialNum(requisition.getProduct().getProductCode(),
                            requisition.getChannel().getChannelCode(), detail.getBusinessDuration(), null));*//*
                    String serialNumber = DigestUtils.md5DigestAsHex((RandomStringUtils.random(32) + String.valueOf(System.currentTimeMillis()) + String.valueOf(detail.getId())).getBytes());
                    contract.setSerialNumber(serialNumber);
                    contract.setContractStatus(ContractStatus.Init.getCode());
                    contract.setBusinessDuration(detail.getBusinessDuration());
                    Calendar calendar = Calendar.getInstance();
                    contract.setStartDate(calendar.getTime());
                    calendar.set(Calendar.MONTH, detail.getBusinessDuration());
                    contract.setEndDate(calendar.getTime()); //注意计算2月天数不够的问题
                    productRate = ratesMap.get(String.valueOf(detail.getBusinessDuration()));
                    contract.setInterestType(productRate.getInterestType());
                    contract.setInterestRate(productRate.getInterestRate());
                    contract.setLoanRatio(productRate.getLoanRatio());
                    contract.setContractAmount(BigDecimal.ZERO);
                    contract.setCreateAt(new Date());
                    contract.setUpdateAt(new Date());
                    contractsMap.put(String.valueOf(detail.getBusinessDuration()), contract);
                }
                BigDecimal detailApplyAmount = new BigDecimal(detail.getCommercialInsuranceValue()).multiply(new BigDecimal(contract.getLoanRatio() / 100)).setScale(0, BigDecimal.ROUND_HALF_UP);
                if (!detail.getCommercialOnly()) {
                    detailApplyAmount = detailApplyAmount.add(new BigDecimal(detail.getTaxAmount())).add(new BigDecimal(detail.getCompulsoryInsuranceAmount()));
                }
                contract.setContractAmount(detailApplyAmount.add(contract.getContractAmount()));
                this.contractDao.save(contract);

                //删除旧合同与申请单详情的关系
                detail.setContract(contract);
                this.requisitionDetailDao.save(detail);
            }

            //删除旧的合同
            if (oldContracts != null && oldContracts.size() > 0) {
                this.contractDao.deleteInBatch(oldContracts);
            }

            if (contractsMap.size() > 0) {
                return new ArrayList<>(contractsMap.values());
            } else {
                return Collections.emptyList();
            }
        }
    }*/

    @Override
    @Transactional(readOnly = true)
    public RequisitionVO getByNumber(String requisitionNumber) {
        Requisition requisition = requisitionDao.getRequisitionByRequisitionNumber(requisitionNumber);
        return this.entityToVO(requisition);
    }

    @Override
    @Transactional(readOnly = true)
    public RequisitionVO getRequisitionVOById(Integer id) {
        Requisition requisition = requisitionDao.getById(id);
        return this.entityToVO(requisition);
    }

    @Override
    public StatisticRequisitionVO statisticRequisitionInfoById(Integer id) {
        Requisition requisition = requisitionDao.getRequisitionById(id);
        if(requisition == null){
            throw new FInsuranceBaseException(104904, new Object[]{id});
        }
        if(requisition.getDetails() == null || requisition.getDetails().size() < 1){
            throw new FInsuranceBaseException(104933);
        }
        return getStatisticRequisitionVO(requisition);
    }

    @Override
    @Transactional(readOnly = true)
    public Requisition getRequisitionEntityById(Integer id) {
        Requisition requisition = requisitionDao.getRequisitionById(id);
        if(requisition == null){
            throw new FInsuranceBaseException(104904, new Object[]{id});
        }
        return requisition;
    }

    @Override
    public Integer saveCustomerRequisition(CustomerRequisitionVO customerRequisitionVO) {
        Requisition requisition = null;
        Integer id = customerRequisitionVO.getId();
        if(id != null){
            requisition = requisitionDao.getRequisitionById(id);
        }
        if(id != null && requisition == null){
            throw new FInsuranceBaseException(104904, new Object[]{id});
        }
        //三种情况才能重新提交
        if(requisition != null && !StringUtils.equals(requisition.getRequisitionStatus(), RequisitionStatus.Draft.getCode())
                && !StringUtils.equals(requisition.getRequisitionStatus(), RequisitionStatus.Rejected.getCode())
                && !StringUtils.equals(requisition.getRequisitionStatus(), RequisitionStatus.Canceled.getCode()) ){
            throw new FInsuranceBaseException(105930);
        }
        if(requisition == null || StringUtils.equals(requisition.getRequisitionStatus(), RequisitionStatus.Canceled.getCode())){
            requisition = new Requisition();
        }
        if(id == null || StringUtils.equals(requisition.getRequisitionStatus(), RequisitionStatus.Canceled.getCode())) {
            requisition.setRequisitionStatus(RequisitionStatus.Draft.getCode());
        }
        requisition.setCustomerId(customerRequisitionVO.getCustomerId());
        requisition.setCustomerAccountInfoId(customerRequisitionVO.getCustomerAccountInfoId());
        //渠道id，关联sys_channel表的主键
        Channel channel = channelDao.getChannelDetailByChannelCode(customerRequisitionVO.getChannelCode());
        if(channel == null){
            LOG.error("saveCustomerRequisition failed with null channel channelCode=[" + customerRequisitionVO.getChannelCode() +"]");
            throw new FInsuranceBaseException(107026);
        }
        requisition.setChannel(channel);
        //产品id，关联busi_product表的主键
        Product product = productDao.getProductById(customerRequisitionVO.getProductId());
        if(product == null){
            LOG.error("saveCustomerRequisition failed with null product productId=[" + customerRequisitionVO.getProductId() +"]");
            throw new FInsuranceBaseException(104102);
        }
        requisition.setProduct(product);
        //产品类型
        requisition.setProductType(product.getProductType());
        //是否为渠道人员录单
        requisition.setChannelApplication(customerRequisitionVO.getChannelApplication());
        //渠道用户id，关联sys_user表中的主键
        requisition.setChannelUserId(customerRequisitionVO.getChannelUserId());
       //订单号
        requisition.setRequisitionNumber(redisSequenceFactory.generateSerialNumber(BizCategory.RK));
        //还款方式  principal_interest:等额本息")
        requisition.setRepayType(product.getRepayType());
        //还款日类型 initial_payment：期初还款，final_payment：期末还款")
        requisition.setRepayDayType(product.getRepayDayType());
        //服务费率（万倍）
        requisition.setServiceFeeRate(product.getServiceFeeRate());
        //其他费用的费率（万倍）
        requisition.setOtherFeeRate(product.getOtherFeeRate());
        //提前还款罚息率（万倍）
        requisition.setPrepaymentPenaltyRate(product.getPrepaymentPenaltyRate());
        //提前还款天数
        requisition.setPrepaymentDays(product.getPrepaymentDays());
        //逾期罚息率（万倍）
        requisition.setOverdueFineRate(product.getOverdueFineRate());
        //最大逾期天数
        requisition.setMaxOverdueDays(product.getMaxOverdueDays());
        requisition.setBusinessDuration(customerRequisitionVO.getBusinessDuration() == null ? 0 : customerRequisitionVO.getBusinessDuration());
        CustomerLoanBankVO bankVO =  customerRequisitionVO.getCustomerLoanBankVO();
        requisition.setLoanAccountType(bankVO == null || bankVO.getLoanAccountType() == null  ? LoanAccountType.PERSONAL_LOAN_TYPE.getCode() : bankVO.getLoanAccountType());//默认个人账户类型
        //放款帐户号
        requisition.setLoanAccountNumber(bankVO == null ? "" : bankVO.getLoanAccountNumber());
        //放款帐户银行编码
        requisition.setLoanAccountBank(bankVO == null ? "" : bankVO.getLoanAccountBank());
        //放款银行名称
        requisition.setLoanAccountBankBranch(bankVO == null ? "" : bankVO.getLoanAccountBankBranch());
        //放款帐户名称型
        requisition.setLoanAccountName(bankVO == null ? "" : bankVO.getLoanAccountName());
        requisition.setInsuranceCompanyName(bankVO == null ? "" : bankVO.getCompanyName());
        requisition.setInsuranceBranchName(bankVO == null ? "" : bankVO.getBranchName());
        //其他图片资源
        requisition.setOtherResource(JacksonUtils.toJson(customerRequisitionVO.getOtherResourceList()));
        //手持身份证照片
        requisition.setIdCardEvidence(customerRequisitionVO.getIdCardEvidence());

        requisition.setCustomerAccountInfoId(customerRequisitionVO.getCustomerAccountInfoId());
        //清空业务单的批次号
        requisition.setLatestAuditBatch(null);
        if(id == null) {
            requisition.setCreateBy(customerRequisitionVO.getCreateBy());
            requisition.setCreateAt(new Date());
        }else {
            //更新业务单数据
            this.updateRequisition(requisition);
            //更新者
            requisition.setUpdateBy(customerRequisitionVO.getUpdateBy());
            requisition.setUpdateAt(new Date());
        }
        requisition.setChannelApplication(customerRequisitionVO.getChannelApplication());
        requisitionDao.save(requisition);
        return requisition.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerRequisitionVO getCustomerRequisitionVOById(Integer id) {
        Requisition requisition = requisitionDao.getRequisitionById(id);
        if(requisition == null){
            throw new FInsuranceBaseException(104929);
        }
        return getCustomerRequisitionVOByEntity(requisition);
    }

    @Override
    public void createContractDataForRequsition(Requisition requisition, Integer month) {
        // 先清除已有的合同数据
        contractService.cleanContractDataByRequisitionId(requisition.getId());

        // 合同的创建按车辆信息的添加次序进行依次生成
        List<RequisitionDetail> detailList = new ArrayList<RequisitionDetail>(requisition.getDetails());
        Collections.sort(detailList, new Comparator<RequisitionDetail>() {
            @Override
            public int compare(RequisitionDetail o1, RequisitionDetail o2) {
                return o1.getCreateAt().compareTo(o2.getCreateAt());
            }
        });

        for(RequisitionDetail detail : detailList) {
            //重新设置车辆残值
            requisitionDetailService.setRequisitionDetailCommercialInsuranceValue(detail);
            Contract contract = contractService.createContract(requisition, detail, month);
            contractDao.save(contract);

            //创建还款记录
            FinanceRepaymentPlanVO planVO = this.createInitRepaymentPlanVO(contract);
            FintechResponse<VoidPlaceHolder> repaymentPlanResponse = repaymentPlanServiceFeign.createInitRepaymentPlan(planVO);
            if (!repaymentPlanResponse.isOk()) {
                throw FInsuranceBaseException.buildFromErrorResponse(repaymentPlanResponse);
            }
            //让车辆级联保存合同
            detail.setContract(contract);
            detail.setBusinessDuration(contract.getBusinessDuration());
            detail.setSubTotal(contract.getContractAmount().intValue());
        }

        for(RequisitionDetail detail : detailList) {
            requisitionDetailDao.save(detail);
        }

        //更新申请单数据
        this.updateRequisition(requisition);
        this.save(requisition);
    }

    // 创建还款记录参数
    private FinanceRepaymentPlanVO createInitRepaymentPlanVO(Contract contract){
        FinanceRepaymentPlanVO planVO = new FinanceRepaymentPlanVO();
        // 客户id,关联cust_account表的主键
        planVO.setCustomerId(contract.getCustomerId());
        // 合同编号
        planVO.setContractNumber(contract.getContractNumber());
        // 渠道id
        planVO.setChannelId(contract.getChannel().getId());
        // 还款日
        planVO.setRepayDate(null);
        // 分期还款当期总金额，单位为分 默认合同金额  然后分多期
        planVO.setRepayTotalAmount(contract.getContractAmount().doubleValue());
        // 分期还款本金金额
        planVO.setRepayCapitalAmount(BasicConstants.NUMBER_FORMAT_ZERO);
        // 分期还款利息金额
        planVO.setRepayInterestAmount(BasicConstants.NUMBER_FORMAT_ZERO);
        // 还款记录所在的期数
        planVO.setCurrentInstalment(contract.getBusinessDuration());
        // 总期数
        planVO.setTotalInstalment(contract.getBusinessDuration());
        // 还款状态
        planVO.setRepayStatus(RefundStatus.INIT_REFUND);
        // 人工干预标识
        planVO.setManualFlag(false);
        // 逾期罚金
        planVO.setOverdueFine(BasicConstants.NUMBER_FORMAT_ZERO);
        // 还款时间
        planVO.setActualRepayDate(null);
        // 是否逾期
        planVO.setOverdueFlag(false);
        // 利率点（万倍）
        planVO.setInterestRate(contract.getInterestRate());
        // 逾期天数
        planVO.setOverdueDays(0);
        // 还款提前天数(自然日)
        planVO.setAdvanceRepayDays(contract.getRequisition().getProduct().getPrepaymentDays());
        planVO.setType(RepayDayType.codeOf(contract.getRequisition().getRepayDayType()));
        planVO.setProductType(ProductType.codeOf(contract.getRequisition().getProductType()));
        return planVO;
    }

    @Override
    @Transactional(readOnly = true)
    public RequisitionInfoVO getWeChatRequistionDetailVO(Integer requisitionId) {
        Requisition requisition = requisitionDao.getRequisitionById(requisitionId);
        if(requisition == null){
            throw new FInsuranceBaseException(104904, new Object[]{requisitionId});
        }
        return getRequisitionVOByEntity(requisition);
    }

    @Override
    @Transactional(readOnly = true)
    public Pagination<RequisitionVO> pageRequisitionByCustomerId(String customerIds,String channelUserIds, String productType, String requisitionStatus, Integer pageIndex, Integer pageSize) {

        List<Integer> customerIdInt = new ArrayList<>();//客户id集合
        List<Integer> channelUserIdInt = new ArrayList<>();//渠道用户id集合

        if (StringUtils.isNotEmpty(customerIds)) {
            String[] customerIdStr = customerIds.split(",");
            for (String s : Arrays.asList(customerIdStr)) {
                customerIdInt.add(Integer.parseInt(s));
            }
        }

        if (StringUtils.isNotEmpty(channelUserIds)) {
            String[] channelUserIdStr = channelUserIds.split(",");
            for (String s : Arrays.asList(channelUserIdStr)) {
                channelUserIdInt.add(Integer.parseInt(s));
            }
        }

        Page<Requisition> requisitions = requisitionDao.pageRequisitionByCustomerId(customerIdInt, channelUserIdInt, productType, requisitionStatus, pageIndex, pageSize);
        List<RequisitionVO> requisitionVOList = new ArrayList<RequisitionVO>();
        if (null != requisitions.getContent() && requisitions.getContent().size() > 0) {
            for (Requisition r : requisitions.getContent()) {
                RequisitionVO requisitionVO = this.entityToVO(r);
                requisitionVOList.add(requisitionVO);
            }
        }
        return Pagination.createInstance(pageIndex, pageSize, requisitions.getTotalElements(), requisitionVOList);
    }

    @Override
    public void save(Requisition requisition) {
        if(requisition != null){
            requisitionDao.save(requisition);
        }
    }

    @Override
    public void cancelForUnconfirmed() {
        List<Requisition> requisitionList = requisitionDao.listRequisitionByStatus(RequisitionStatus.Submitted.getCode());
        LOG.info("preparing change status to canceled for submittedRepaymentPlan");
        this.batchUpdateRequisitionStatus(requisitionList, RequisitionStatus.Canceled);
    }

    @Override
    public void cancelForWaitingpayment() {
        Date startTiem = DateCommonUtils.getBeginDateByDate(DateCommonUtils.getCurrentDate());
        Date preEndDay = DateCommonUtils.getEndTimeOfDate(DateCommonUtils.getBeforeDay(DateCommonUtils.getBeforeDay(startTiem)));
        FintechResponse<List<RequisitionVO>> requisitionListResponse = bizQueryFeign.listRequisitionForWaitpaymentAndFail(preEndDay);
        if (!requisitionListResponse.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(requisitionListResponse);
        }
        List<RequisitionVO> requisitionVOList = new ArrayList<>();
        List<Requisition> requisitionList = new ArrayList<>();
        if (null != requisitionListResponse.getData()) {
            requisitionVOList = requisitionListResponse.getData();
        }
        String waitingpaymentPlanIds = "";
        if (requisitionVOList.size() > 0) {
            for (RequisitionVO vo : requisitionVOList) {
                waitingpaymentPlanIds = vo.getId() + ",";
                Requisition requisition = requisitionDao.getById(vo.getId());
                requisitionList.add(requisition);
            }
        }
        LOG.info("preparing change status to canceled for waitingpaymentRepaymentPlan with id :{}", waitingpaymentPlanIds);
        this.batchUpdateRequisitionStatus(requisitionList, RequisitionStatus.Canceled);
    }

    protected void batchUpdateRequisitionStatus(List<Requisition> requisitionList, RequisitionStatus requisitionStatus) {
        if (requisitionStatus != null && requisitionList != null && requisitionList.size() > 0) {
            String oldStatus = "";
            for (Requisition r : requisitionList) {
                oldStatus = r.getRequisitionStatus();
                r.setRequisitionStatus(requisitionStatus.getCode());
                requisitionDao.save(r);
                LOG.info("success changing status for current requisition who's status is : {} to canceled", r.getRequisitionStatus());
                // i do not know what could i say fuck!
                if (!requisitionStatus.getCode().equals(oldStatus)) {
                    this.applicationContext.publishEvent(new RequisitionLifeCycleEvent(r, oldStatus, requisitionStatus.getCode()));
                }
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public RequisitionVO getAcquiescenceChannelByRequisitionIdAndCustomerId(Integer requisitionId, Integer customerId) {
        Requisition requisition = requisitionDao.getAcquiescenceChannelByRequisitionIdAndCustomerId(requisitionId, customerId);
        return this.entityToVO(requisition);
    }

    @Override
    public void changeStatusForOverdue() {

    }

    @Override
    public int computeMaxAvaiableInstalmentNum(Date beginDate, Date endDate, Requisition requisition) {
        //产品类型
        ProductType productType = ProductType.codeOf(requisition.getProductType());

        //计算期数
        Integer maxDuration = CalculationFormulaUtils.getBusinessDuration(beginDate, endDate, productType);

        //车贷分期返还天数
        if(ProductType.CAR_INSTALMENTS == productType) {
            if(maxDuration < BasicConstants.REQUSITION_YEAR_DAYS || (requisition.getBusinessDuration() !=null
                    && requisition.getBusinessDuration() != 0 && maxDuration < requisition.getBusinessDuration())) {
                throw new FInsuranceBaseException(104928);
            }
            //车贷分期12个月即12期
            return BasicConstants.REQUSITION_YEAR_MONTH;
        }else{
            if(maxDuration < BasicConstants.REQUSITION_MIN_MONTH) {
                throw new FInsuranceBaseException(104930, new Object[]{maxDuration});
            }
            //保单贷款最大可借款10个月即10期
            return maxDuration > BasicConstants.REQUSITION_MAX_MONTH ? BasicConstants.REQUSITION_MAX_MONTH : maxDuration;
        }
    }

    //客户申请单详情
    private CustomerRequisitionVO getCustomerRequisitionVOByEntity(Requisition entity) {
        if(entity == null){
            return null;
        }
        CustomerRequisitionVO vo = new CustomerRequisitionVO();
        vo.setChannelCode(entity.getChannel().getChannelCode());
        //产品id
        vo.setProductId(entity.getProduct().getId());
        //客户id
        vo.setCustomerId(entity.getCustomerId());
        vo.setId(entity.getId());
        //其他材料
        List<String> otherResourceList = JacksonUtils.fromJson(entity.getOtherResource(), new TypeReference<List<String>>() {
        });
        vo.setOtherResourceList(otherResourceList);
        //渠道用户id
        vo.setChannelUserId(entity.getChannelUserId());
        CustomerLoanBankVO customerLoanBankVO = new CustomerLoanBankVO();
        //客户账户id
        customerLoanBankVO.setId(entity.getCustomerId());
        // 承包公司id
       // customerLoanBankVO.setCompanyId();
        // 承保支行id
        //customerLoanBankVO.setBranchId();
        //手持身份证照片
        vo.setIdCardEvidence(entity.getIdCardEvidence());
        //放款帐户类型
        customerLoanBankVO.setLoanAccountType(entity.getLoanAccountType());
        //放款帐户号
        customerLoanBankVO.setLoanAccountNumber(entity.getLoanAccountNumber());
        //放款帐户银行编码
        customerLoanBankVO.setLoanAccountBank(entity.getLoanAccountBank());
        //放款银行名称
        customerLoanBankVO.setLoanAccountBankBranch(entity.getLoanAccountBankBranch());
        //放款帐户名称型
        customerLoanBankVO.setLoanAccountName(entity.getLoanAccountName());
        vo.setCustomerLoanBankVO(customerLoanBankVO);
        vo.setLoanAccountNumber(entity.getLoanAccountNumber());
        //客户账户info id
        vo.setCustomerAccountInfoId(entity.getCustomerAccountInfoId());
        return vo;
    }

    private RequisitionInfoVO getRequisitionVOByEntity(Requisition requisition) {
        RequisitionInfoVO infoVO = new RequisitionInfoVO();
        infoVO.setRequisitionId(requisition.getId());
        infoVO.setProductType(requisition.getProductType());
        // 客户信息
        if (requisition.getChannel() != null) {
            FintechResponse<CustomerVO> response = customerServiceFeign.getCustomerAccountInfoById(requisition.getCustomerAccountInfoId());
            if (response != null && response.getData() != null) {
                CustomerVO customerVO = response.getData();
                CustVO custVO = new CustVO();
                custVO.setCustomerId(customerVO.getAccountId());
                custVO.setCustomerName(customerVO.getName());
                custVO.setCustomerIdNumber(customerVO.getIdNum());
                custVO.setCustomerBankCardNumber(customerVO.getBankCard());
                custVO.setCustomerMobile(customerVO.getPhone());

                custVO.setCustomerIdFront(customerVO.getIdFront());
                custVO.setCustomerIdBack(customerVO.getIdBack());
                custVO.setCustomerBankCardFront(customerVO.getBankCardPicture());
                custVO.setIdCardEvidence(requisition.getIdCardEvidence());
                infoVO.setCustomerVO(custVO);
                EnterpriseInfo enterpriseInfo = new EnterpriseInfo();
                enterpriseInfo.setEnterpriseName(customerVO.getCompanyOf());
                enterpriseInfo.setEnterpriseLicenceNumber(customerVO.getBusinessLicence());

                enterpriseInfo.setEnterpriseLicencePhoto(customerVO.getLicencePicture());
                infoVO.setEnterpriseInfo(enterpriseInfo);
            } else {
                infoVO.setCustomerVO(new CustVO());
                infoVO.setEnterpriseInfo(new EnterpriseInfo());
                LOG.error("获取客户信息失败");
            }

        }
        // 保险公司信息
        InsuranceCompanyInfo insuranceCompanyInfo = new InsuranceCompanyInfo();
        CustomerLoanBankVO customerLoanBankVO = insuranceCompanyConfigServiceFeign
                .getByAccountNumberAndType(requisition.getLoanAccountNumber(), requisition.getLoanAccountType());
        if (customerLoanBankVO != null) {
            insuranceCompanyInfo.setCompanyId(customerLoanBankVO.getCompanyId());
        }
        insuranceCompanyInfo.setCompanyName(requisition.getInsuranceCompanyName());
        insuranceCompanyInfo.setBranchName(requisition.getInsuranceBranchName());
        infoVO.setInsuranceCompanyInfo(insuranceCompanyInfo);

        // 车辆信息
        List<RequisitionDetail> carList = requisitionDetailDao.getRequisitionDetailByRequisition_Id(requisition.getId());
        List<CarInfo> carInfoList = new ArrayList<>();
        if (carList != null && carList.size() > 0) {
            CarInfo carInfo = null;
            for (RequisitionDetail detail : carList) {
                carInfo = new CarInfo();
                carInfo.setCarId(detail.getId());
                carInfo.setCarNumber(detail.getCarNumber());
                carInfo.setDriverLicence(detail.getDrivingLicense());
                carInfo.setDriverLicencePhoto(Arrays.asList(detail.getDrivingLicenseMain(), detail.getDrivingLicenseAttach()));
                carInfo.setBizInsuranceNumber(detail.getCommercialInsuranceNumber());
                carInfo.setBizInsuranceAmount(detail.getCommercialInsuranceAmount().doubleValue());
                carInfo.setBizInsuranceBegin(detail.getCommercialInsuranceStart());
                carInfo.setBizInsuranceEnd(detail.getCommercialInsuranceEnd());
                carInfo.setCpsInsuranceNumber(detail.getCompulsoryInsuranceNumber());
                carInfo.setCpsInsuranceAmount(detail.getCompulsoryInsuranceAmount().doubleValue());
                carInfo.setCpsInsuranceBegin(detail.getCompulsoryInsuranceStart());
                carInfo.setCpsInsuranceEnd(detail.getCompulsoryInsuranceEnd());
                carInfo.setCsAmount(detail.getTaxAmount().doubleValue());
                Set<RequisitionDetailResource> resourcesSet = detail.getRequisitionDetailResourceSet();
                if (resourcesSet != null) {
                    List<String> commercialPhoto = null; // 商业险照片
                    List<String> compulsoryPhoto = null; // 交强险照片
                    List<String> csPhoto = null;         // 车船税照片
                    List<String> others = null;          // 其他材料
                    List<String> driveLisence = null;    // 行驶证照片
                    for (RequisitionDetailResource resource : resourcesSet) {
                        if (resource.getResourceType().equals(ResourceType.BUSI_INSURANCE.getCode())) {
                            commercialPhoto = new ArrayList<>();
                            commercialPhoto.add(resource.getResourcePicture());
                        }
                        if (resource.getResourceType().equals(ResourceType.DRIVER_INSURANCE.getCode())) {
                            compulsoryPhoto = new ArrayList<>();
                            compulsoryPhoto.add(resource.getResourcePicture());
                        }
                        if (resource.getResourceType().equals(ResourceType.TAX_INSURANCE.getCode())) {
                            csPhoto = new ArrayList<>();
                            csPhoto.add(resource.getResourcePicture());
                        }
                        if (resource.getResourceType().equals(ResourceType.OTHER_MATERIAL.getCode())) {
                            others = new ArrayList<>();
                            others.add(resource.getResourcePicture());
                        }
                        if (resource.getResourceType().equals(ResourceType.DRIVING_LICENSE.getCode())) {
                            driveLisence = new ArrayList<>();
                            driveLisence.add(resource.getResourcePicture());
                        }
                    }
                    carInfo.setBizInsurancePhoto(commercialPhoto);
                    carInfo.setCpsInsurancePhoto(compulsoryPhoto);
                    carInfo.setCsPhoto(csPhoto);
                    carInfo.setOtherCarInfo(others);
                    carInfo.setDriverLicencePhoto(driveLisence);
                }
                carInfoList.add(carInfo);
            }
        }
        String otherResource = requisition.getOtherResource();
        try {
            if (StringUtils.isNotBlank(otherResource)) {
                List<String> resources = JSONArray.parseArray(otherResource, String.class);
                infoVO.setOthers(resources);
            }
        } catch (Exception e) {
            LOG.error("申请单其他材料获取失败");
            infoVO.setOthers(Arrays.asList(otherResource));
        }
        infoVO.setCarInfo(carInfoList);
        return infoVO;
    }

    //获取业务单统计数
    private StatisticRequisitionVO getStatisticRequisitionVO(Requisition requisition){
        StatisticRequisitionVO vo = new StatisticRequisitionVO();
        vo.setId(requisition.getId());

        //最小月份
        Integer minMonth = BasicConstants.REQUSITION_MIN_MONTH;
        //最大月份
        Integer maxMonth = minMonth; //设为允许的最小期数

        //车辆信息
        Set<RequisitionDetail> requisitionDetails = requisition.getDetails();
        //车辆数
        Integer carNumber = requisitionDetails.size();
        //保险数 = 商业保单数 + 交强险数
        Integer insuranceNumber = requisitionDetails.size();

        //是否允许编辑
        boolean isCanEdit = true;
        //三种情况才能重新提交
        if(!StringUtils.equals(requisition.getRequisitionStatus(), RequisitionStatus.Draft.getCode())
                && !StringUtils.equals(requisition.getRequisitionStatus(), RequisitionStatus.Rejected.getCode())
                && !StringUtils.equals(requisition.getRequisitionStatus(), RequisitionStatus.Canceled.getCode()) ){
            isCanEdit = false;
        }
        LOG.error("getStatisticRequisitionVO failed with repeat with requsitionId = " + requisition.getId() + "]");
        //期数集合
        List<Integer> businessDurationList = new ArrayList<Integer>();
        //克隆所有车辆
        List<RequisitionDetail> details = new ArrayList<RequisitionDetail>();
        //这种情况下只能查看业务单信息不能提交申请
        for (RequisitionDetail requisitionDetail : requisitionDetails) {
            if (StringUtils.isNoneBlank(requisitionDetail.getCompulsoryInsuranceNumber())) {
                insuranceNumber += 1; // 是否包括交强险
            }
            //默认查看车辆合同
            Contract contract = requisitionDetail.getContract();
            if(isCanEdit){
                //重新设置车辆残值
                requisitionDetailService.setRequisitionDetailCommercialInsuranceValue(requisitionDetail);
                contract = contractService.createContract(requisition, requisitionDetail, null);
            }
            RequisitionDetail detail = null;
            try {
                detail = (RequisitionDetail) requisitionDetail.clone();
                if (contract != null) {
                    detail.setContract(contract);
                    businessDurationList.add(contract.getBusinessDuration());
                }
            } catch (CloneNotSupportedException e) {
                LOG.error("requisitionDetail clone error with id = [" + requisitionDetail.getId() +"]");
                continue;
            }
            if(detail != null) {
                details.add(detail);
            }
        }
        vo.setCarNumber(carNumber);
        vo.setInsuranceNumber(insuranceNumber);
        if(businessDurationList == null || businessDurationList.size() < 1){
            vo.setMinMonth(0);
            vo.setMaxMonth(0);
            return vo;
        }
        maxMonth = Collections.max(businessDurationList);
        if(!isCanEdit) {
            minMonth = maxMonth;
        }
        vo.setMinMonth(minMonth);
        vo.setMaxMonth(maxMonth);
        vo.setMonthMoneyVOList(getMonthMoney(businessDurationList, requisition, details));
        return vo;
    }

    //获取月金额
    private  List<MonthMoneyVO>  getMonthMoney(List<Integer> businessDurationList, Requisition requisition, List<RequisitionDetail> details){
        List<MonthMoneyVO> monthMoneyVOS = new ArrayList<MonthMoneyVO>();
        if (businessDurationList == null || businessDurationList.size() == 0) {
            return monthMoneyVOS;
        }
        Set<ProductRate> productRates = requisition.getProduct().getProductRateSet();

        Map<Integer, ProductRate> monthRateMap = new HashMap<Integer, ProductRate>();
        for (ProductRate rate : productRates) {
            monthRateMap.put(rate.getBusinessDuration(), rate);
        }

        int maxMonth = Collections.max(businessDurationList);
        int minMonth = Collections.max(businessDurationList);
        if(StringUtils.equals(ProductType.POLICY_LOANS.getCode(),requisition.getProductType()) && minMonth > BasicConstants.REQUSITION_MIN_MONTH){
            minMonth = BasicConstants.REQUSITION_MIN_MONTH;
        }

        for (int curMonth = maxMonth; curMonth >= minMonth; curMonth--) {
            MonthMoneyVO monthMoneyVO = new MonthMoneyVO();
            BigDecimal money = new BigDecimal(0);
            //所有合同
            for(RequisitionDetail requisitionDetail : details) {
                if(requisitionDetail.getContract() == null){
                    continue;
                }
                Contract contract = requisitionDetail.getContract();
                Integer businessDuration = contract.getBusinessDuration();
                if(curMonth >= businessDuration) {
                    money = money.add(contract.getContractAmount());//月金额已近通过合同计算过了
                }else {
                    contract = contractService.createContract(requisition, requisitionDetail, curMonth);
                    /*Double curMonthLoanRatio = monthRateMap.get(curMonth).getLoanRatio();
                    Double monthAmountKeyLoanRatio = monthRateMap.get(businessDuration).getLoanRatio();
                    //保单金额乘以可以使用比例
                    BigDecimal contractAvailableAmount = contract.getContractAmount().multiply(new BigDecimal(curMonthLoanRatio));
                    // 同期所有保单在最大可贷月份的金额即保单残值（合同金额乘以可贷款比率） / 用户早期可贷比率
                    BigDecimal bigDecimal = contractAvailableAmount.divide(new BigDecimal(monthAmountKeyLoanRatio), 0, BigDecimal.ROUND_HALF_UP);*/
                    if(contract != null) {
                        money = money.add(contract.getContractAmount());
                    }
                }
            }
            monthMoneyVO.setMonth(curMonth);
            monthMoneyVO.setMoney(money.doubleValue());
            monthMoneyVOS.add(monthMoneyVO);
        }

        return monthMoneyVOS;
    }

    /**
     * 提交审核时生成申请单的审核记录数据
     * @param requisitionId  业务单id
     */
    protected void generateRequisitionAuditRecordForSubmission(Integer requisitionId, String auditBatchNumber, Integer currentLoginUserId) {
        if (requisitionId != null) {
            //获取审核角色的人员
            String roleCode = this.environment.getProperty("fintech.biz.requisition.auditor-role-code", "auditor");
            //异步处理创建审核日志
            auditLogInfoService.createAuditLogInfo(roleCode, requisitionId, auditBatchNumber, currentLoginUserId);
        }
    }

    @Override
    public void confirmPaid(RequisitionVO requisitionVO, Integer currentLoginUserId, String remark) {
        Requisition requisition = requisitionDao.getById(requisitionVO.getId());
        if (null == requisition) {
            throw new FInsuranceBaseException(104505);
        }

        //人工确认支付生成合同
        changeRequisitionStatusByRequsitionId(requisition.getId(), RequisitionStatus.WaitingLoan, true);

        //生成操作记录
        OperationRecordVO operationrRecordVO = new OperationRecordVO();
        operationrRecordVO.setEntityId(requisition.getId());
        operationrRecordVO.setOperationType(OperationType.PAID.getCode());
        operationrRecordVO.setEntityType(EntityType.REQUISITION.getCode());
        entityOperationLogServiceFeign.createLog(operationrRecordVO);
    }

    @Override
    public Integer countRequisitionByStatus(String channelUserIds, Integer customerId, String requisitionStatus) {
        List<Integer> idInts = new ArrayList<>();
        if (StringUtils.isNotEmpty(channelUserIds)) {
            String[] ids = channelUserIds.split(",");
            for (String s : ids) {
                idInts.add(Integer.parseInt(s));
            }
        }
        return requisitionDao.countRequisitionByStatus(idInts, customerId, requisitionStatus);
    }



    @Override
    public void confirmApplyFor(Integer id) {
        //this.submitForAudit();
    }

    //更新业务单数据
    @Override
    public void updateRequisition(Requisition requisition){
        Set<RequisitionDetail> requisitionDetails = requisition.getDetails();
        Map<String, ProductRate> ratesMap = this.productService.getProductRate(requisition.getProduct());

        //商业保险总价值，单位为分
        Integer totalCommercialAmount = 0;
        //交强险总价值
        Integer totalCompulsoryAmount = 0;
        //车船税总价值
        Integer totalTaxAmount = 0;
        if(requisitionDetails != null && requisitionDetails.size() > 0) {
            for (RequisitionDetail detail : requisitionDetails) {
                ProductRate rate = ratesMap.get(String.valueOf(detail.getBusinessDuration()));
                if (rate == null) {
                    continue;
                }
                LOG.info("updateRequisition with commercialInsuranceValue=[" + detail.getCommercialInsuranceValue() +"],compulsoryInsuranceAmount=" +
                        "["+ detail.getCompulsoryInsuranceAmount() + "],taxAmount=[" + detail.getTaxAmount() + "],loanRatio=[" + rate.getLoanRatio() + "],requisitionId=["+ requisition.getId() + "]" );
                //比例
                Double ratio = CalculationFormulaUtils.NUMBER_ONE;
                if(!StringUtils.equals(ProductType.CAR_INSTALMENTS.getCode(), requisition.getProductType())){
                    ratio = CalculationFormulaUtils.NUMBER;
                }
                //商业险金额
                totalCommercialAmount += CalculationFormulaUtils.getApplyMoney(new BigDecimal(detail.getCommercialInsuranceValue()).divide(new BigDecimal(ratio),6,BigDecimal.ROUND_HALF_UP), rate.getLoanRatio());
                //交强险
                totalCompulsoryAmount += CalculationFormulaUtils.getApplyMoney(new BigDecimal(detail.getCompulsoryInsuranceAmount()), rate.getLoanRatio());
                //车船税
                totalTaxAmount += CalculationFormulaUtils.getApplyMoney(new BigDecimal(detail.getTaxAmount()), rate.getLoanRatio());
                LOG.info("updateRequisition " + totalCommercialAmount + "_" +totalCompulsoryAmount +"_" + totalTaxAmount + ", with requisitionId=["+ requisition.getId() + "]");
            }
        }
        requisition.setTotalCommercialAmount(totalCommercialAmount);
        requisition.setTotalCompulsoryAmount(totalCompulsoryAmount);
        requisition.setTotalTaxAmount(totalTaxAmount);
        requisition.setTotalApplyAmount(requisition.getTotalCommercialAmount() + requisition.getTotalCompulsoryAmount() + requisition.getTotalTaxAmount());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequisitionDetailVO> listRequisitionDetailByRequisitionId(Integer requisitionId) {
        List<RequisitionDetail> requisitionDetailList = requisitionDetailDao.getRequisitionDetailByRequisition_Id(requisitionId);

        List<RequisitionDetailVO> requisitionDetailVOList = new ArrayList<>();
        if (null != requisitionDetailList && requisitionDetailList.size() > 0) {
            for (RequisitionDetail requisitionDetail : requisitionDetailList) {
                RequisitionDetailVO vo = new RequisitionDetailVO();
                vo.setId(requisitionDetail.getId());
                vo.setCarNumber(requisitionDetail.getCarNumber());
                vo.setContractId(requisitionDetail.getContract().getId());
                requisitionDetailVOList.add(vo);
            }
        }

        return requisitionDetailVOList;
    }

    @Override
    @Transactional(readOnly = true)
    public RequisitionVO getRequisitionByContractNumber(String contractNumber) {
        Requisition requisition =  requisitionDao.getRequisitionByContractNumber(contractNumber);
        if(requisition == null){
            return null;
        }
        RequisitionVO requisitionVO = new RequisitionVO();
        requisitionVO.setMaxOverdueDays(requisition.getMaxOverdueDays());
        requisitionVO.setOverdueFineRate(requisition.getOverdueFineRate());
        requisitionVO.setId(requisition.getId());
        return requisitionVO;
    }
}
