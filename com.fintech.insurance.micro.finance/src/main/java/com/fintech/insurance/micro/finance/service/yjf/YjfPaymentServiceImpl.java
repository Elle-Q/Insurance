package com.fintech.insurance.micro.finance.service.yjf;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.fintech.insurance.commons.constants.YjfConstants;
import com.fintech.insurance.commons.enums.*;
import com.fintech.insurance.commons.utils.DateCommonUtils;
import com.fintech.insurance.commons.utils.ToolUtils;
import com.fintech.insurance.components.cache.RedisSequenceFactory;
import com.fintech.insurance.micro.dto.finance.BankCardVerifyResult;
import com.fintech.insurance.micro.dto.finance.DebtNotification;
import com.fintech.insurance.micro.dto.finance.DebtResult;
import com.fintech.insurance.micro.dto.finance.YjfNotification;
import com.fintech.insurance.micro.feign.biz.RequisitionServiceFeign;
import com.fintech.insurance.micro.finance.event.RepaymentPlanEvent;
import com.fintech.insurance.micro.finance.event.RepaymentPlanOverdueEvent;
import com.fintech.insurance.micro.finance.event.RepaymentPlanRefundEvent;
import com.fintech.insurance.micro.finance.model.UserDebtInfoRedisVO;
import com.fintech.insurance.micro.finance.persist.dao.*;
import com.fintech.insurance.micro.finance.persist.entity.*;
import com.fintech.insurance.micro.finance.service.*;
import com.fintech.insurance.micro.finance.service.yjf.enums.ResultCodeType;
import com.fintech.insurance.micro.finance.service.yjf.enums.VerifyCardStatus;
import com.fintech.insurance.micro.finance.service.yjf.enums.YJFDebtProcessStatus;
import com.fintech.insurance.micro.finance.service.yjf.exception.YijifuClientException;
import com.fintech.insurance.micro.finance.service.yjf.model.*;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 易极付支付接口实现
 * @Author: Yong Li
 * @Date: 2017/12/8 17:20
 */
@Service
public class YjfPaymentServiceImpl implements PaymentService , ApplicationContextAware {

    private static final Logger LOG = LoggerFactory.getLogger(YjfPaymentServiceImpl.class);

    @Value("${fintech.common.verificationEffectiveDays}")
    private Integer maxEffectiveDays;

    @Autowired
    private RepaymentPlanService repaymentPlanService;

    @Autowired
    private PaymentOrderService paymentOrderService;

    @Autowired
    private PaymentOrderDao paymentOrderDao;

    @Autowired
    private RequisitionServiceFeign requisitionServiceFeign;

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private static final List<String> DEBT_PROCESSING_STATUSES = new ArrayList<String>();
    private static final List<String> DEBT_FAILED_STATUSES = new ArrayList<String>();

    static {
        DEBT_PROCESSING_STATUSES.add("VERIFY_CARD_DEALING");
        DEBT_PROCESSING_STATUSES.add("CHECK_NEEDED");
        DEBT_PROCESSING_STATUSES.add("INIT");
        DEBT_PROCESSING_STATUSES.add("WITHHOLD_DEALING");

        DEBT_FAILED_STATUSES.add("VERIFY_CARD_FAIL");
        DEBT_FAILED_STATUSES.add("CHECK_REJECT");
        DEBT_FAILED_STATUSES.add("WITHHOLD_FAIL");
    }

    @Autowired
    private RedisSequenceFactory redisSequenceFactory;

    @Autowired
    private YjfLogDao logDao;

    @Autowired
    private YjfHttpClient yjfHttpClient;

    @Autowired
    private YjfPropertiesBean propertiesBean;

    @Autowired
    private RepaymentRecordDao repaymentRecordDao;

    @Autowired
    private RepaymentPlanDao repaymentPlanDao;


    @Autowired
    private UserDebtRedisService userDebtRedisService;

    @Autowired
    private RepaymentRecordService repaymentRecordService;

    @Autowired
    private BankcardVerifyRecordDao bankcardVerifyRecordDao;

    private static ValueFilter nullValueFilter = new ValueFilter() {
        @Override
        public Object process(Object object, String name, Object value) {
            if (null == value) {
                return "";
            }
            return value.toString();
        }
    };

    /**
     * 根据用户的姓名 身份证号  银行卡号 银行卡预留手机号码做四要素+短信验证
     *
     * @param userName 用户真实姓名
     * @param certNum 身份证号
     * @param bankCardNum 银行卡号
     * @param reservedMobile 银行卡预留手机号码
     */
    public BankCardVerifyResult verifyBankCard(String userName, String certNum, String bankCardNum, String reservedMobile) {
        if (!StringUtils.isNoneBlank(userName, certNum, bankCardNum, reservedMobile)) {
            throw new IllegalArgumentException("the parameter for verifyBankCard can not be null or empty.");
        }
        BankCardVerifyResult result = this.verifyBankCardByLocalData(userName, certNum, bankCardNum, reservedMobile);
        if(result.getIsSuccess()) {
            return result;
        }

        result = this.verifyBankCardByYijifu(userName, certNum, bankCardNum, reservedMobile);
        BankcardVerifyRecord verifyRecord = new BankcardVerifyRecord();
        verifyRecord.setUserName(userName);
        verifyRecord.setIdNumber(certNum);
        verifyRecord.setBankCardNumber(bankCardNum);
        verifyRecord.setReservedMobile(reservedMobile);
        verifyRecord.setBankCode(result.getBankCode());
        verifyRecord.setBankName(result.getBankName());
        verifyRecord.setPlatformOrderNumber(result.getRequestSerialNum());
        verifyRecord.setVerificationStatus(result.getVerificationStatus());
        verifyRecord.setVerificationTime(new Date());
        verifyRecord.setRemarks(result.getFailedMessage());
        bankcardVerifyRecordDao.save(verifyRecord);

        return result;
    }

    // 通过数据库数据验卡
    private BankCardVerifyResult verifyBankCardByLocalData(String userName, String certNum, String bankCardNum, String reservedMobile) {
        BankCardVerifyResult result = new BankCardVerifyResult();
        result.setIsSuccess(false);
        List<BankcardVerifyRecord> successRecords = bankcardVerifyRecordDao.querySuccessResultByFourElements(userName,
                certNum, bankCardNum, reservedMobile);
        if(successRecords != null && successRecords.size() > 0 && maxEffectiveDays != null) {
            BankcardVerifyRecord recentRecord = successRecords.get(0);
            result.setBankCode(recentRecord.getBankCode());
            result.setBankName(recentRecord.getBankName());
            result.setRequestSerialNum(recentRecord.getPlatformOrderNumber());
            Date effectiveTime = DateCommonUtils.getAfterDay(recentRecord.getVerificationTime(), maxEffectiveDays);
            if(effectiveTime.after(new Date())) {
                // 本地验卡通过 并且本地验卡数据的有效截止时间未到
                result.setIsSuccess(true);
            }
        }
        return result;
    }

    private BankCardVerifyResult verifyBankCardByYijifu(String userName, String certNum, String bankCardNum, String reservedMobile) {
        BankCardVerifyRequest request = new BankCardVerifyRequest();
        request.setPlatformOrderNum(redisSequenceFactory.generateSerialNumber(BizCategory.YJF_VB));
        request.setCustomerName(userName);
        request.setCertNo(certNum);
        request.setBankCardNo(bankCardNum);
        request.setMobileNo(reservedMobile);

        BankCardVerifyResponse response = this.callYjfService(request, BankCardVerifyResponse.class);

        BankCardVerifyResult result = new BankCardVerifyResult();
        // 只有验证成功
        result.setIsSuccess(VerifyCardStatus.VERIFY_CARD_SUCCESS == response.getVerifyCardStatus());
        result.setVerificationStatus(response.getVerifyCardStatus() == null ? null : response.getVerifyCardStatus().getCode());
        result.setRequestSerialNum(response.getPlatformOrderNum());

        if (!result.getIsSuccess()) {
            if ("comn_04_0003".equals(response.getErrorCode())) {
                result.setFailedMessage("请输入正确的身份证号码");
            } else {
                result.setFailedMessage(response.getResultMessage());
            }
        } else {
            result.setBankName(response.getBankName());
            result.setBankCode(response.getBankCode());
        }
        return result;
    }

    /***
     * 代扣接口
     *
     * @param userName 用户真实姓名
     * @param certNum 身份证号
     * @param bankCardNum 银行卡号
     * @param reservedMobile 银行卡预留手机号码
     * @param amount 扣款金额
     * @param contactInfo 合同信息
     *
     * @return 是否受理成功
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public DebtResult debtAmount(String userName, String certNum, String bankCardNum, String reservedMobile, Double amount, String contactInfo) {
        if (!StringUtils.isNoneBlank(userName, certNum, bankCardNum, reservedMobile, contactInfo)) {
            throw new IllegalArgumentException("the parameter for verifyBankCard can not be null or empty.");
        }
        if (null == amount) {
            throw new IllegalArgumentException("the debt amount can not be null or empty.");
        }
        DebtRequest request = new DebtRequest();
        request.setPlatformOrderNum(redisSequenceFactory.generateSerialNumber(BizCategory.YJF_DM));
        request.setCustomerName(userName);
        request.setCertNo(certNum);
        request.setBankCardNo(bankCardNum);
        request.setMobileNo(reservedMobile);
        request.setAmount(amount / 100); // 单位从分换算成元
        request.setContractInfo(contactInfo);

        // 设置通知URL
        request.setNotifyUrl(propertiesBean.getDebtNotifyUrl());

        YjfResponse response = this.callYjfService(request, YjfResponse.class);

        DebtResult result = new DebtResult();
        result.setIsSuccess(ResultCodeType.EXECUTE_SUCCESS == response.getResultCodeType() || ResultCodeType.EXECUTE_PROCESSING == response.getResultCodeType());
        result.setRequestSerialNum(request.getPlatformOrderNum());
        result.setAmount(amount);
        if (!result.getIsSuccess()) {
            result.setFailedMessage(response.getResultMessage());
        }
        // 更新扣款信息进入缓存
        userDebtRedisService.saveOrUpdate(new UserDebtInfoRedisVO(bankCardNum, request.getPlatformOrderNum(), amount,
                result.getIsSuccess() ? DebtStatus.PROCESSING: DebtStatus.FAILED));

        return result;
    }

    @Override
    public DebtStatus queryDebtStatus(String platformDebtOrderNum, String debtBankcardNum) {
        DebtQueryResponse response = this.queryDebtStatusWithYjfResponse(platformDebtOrderNum, debtBankcardNum);
        return this.convertDebtQueryResponseToDebtStatus(platformDebtOrderNum, debtBankcardNum, response);
    }

    @Override
    public DebtQueryResponse queryDebtStatusWithYjfResponse(String platformDebtOrderNum, String debtBankcardNum) {
        if (StringUtils.isBlank(platformDebtOrderNum) || StringUtils.isBlank(debtBankcardNum)) {
            throw new IllegalArgumentException("the parameter for platformDebtOrderNum or debtBankcardNum can not be null or empty.");
        }
        DebtQueryRequest request = new DebtQueryRequest();
        request.setPlatformOrderNum(platformDebtOrderNum);
        try {
            return this.callYjfService(request, DebtQueryResponse.class);
        } catch (Exception e) {
            LOG.error("YJF query failed:" + e.getMessage(), e);
            return null;
        }
    }

    @Override
    public DebtStatus convertDebtQueryResponseToDebtStatus(String platformDebtOrderNum, String debtBankcardNum, DebtQueryResponse response) {
        if (StringUtils.isBlank(platformDebtOrderNum) || StringUtils.isBlank(debtBankcardNum)) {
            throw new IllegalArgumentException("the parameter for platformDebtOrderNum or debtBankcardNum can not be null or empty.");
        }
        if (response == null) {
            return null;
        }
        try {
            if (ResultCodeType.EXECUTE_SUCCESS == response.getResultCodeType()) {
                //将易极付的处理状态映射成扣款状态
                DebtStatus newStatus = null;
                if (DEBT_PROCESSING_STATUSES.contains(response.getServiceStatus())) {
                    newStatus = DebtStatus.PROCESSING;
                } else if (DEBT_FAILED_STATUSES.contains(response.getServiceStatus())) {
                    newStatus = DebtStatus.FAILED;
                } else {
                    if ("WITHHOLD_SUCCESS".equals(response.getServiceStatus())) {
                        newStatus = DebtStatus.CONFIRMED;
                    } else if ("SETTLE_SUCCESS".equals(response.getServiceStatus())) {
                        newStatus = DebtStatus.SETTLED;
                    }
                }
                if (null == newStatus) {
                    throw new NotImplementedException("Unknow the process status " + response.getServiceStatus());
                } else {
                    userDebtRedisService.saveOrUpdate(new UserDebtInfoRedisVO(debtBankcardNum, platformDebtOrderNum, response.getAmount(), newStatus));
                    return newStatus;
                }
            } else if (ResultCodeType.INSTALLMENT_TRANS_ORDER_NO_DATA == response.getResultCodeType()) {//交易数据在支付方无数据
                return DebtStatus.FAILED;
            }
        } catch (Exception e) {
            LOG.error("YJF query failed:" + e.getMessage(), e);
        }
        return null;
    }

    @Transactional
    @Override
    public void saveNotification(YjfNotification notification) {
        logDao.save(YjfLog.createNotificationLog(notification.getService(), notification.getOrderNo(), ToolUtils.toJsonString(notification)));
        LOG.info("persistent Yijifu notification success. ");
    }

    @Transactional
    @Override
    public void updateDebtStatusByNotification(DebtNotification debtNotification) {
        if (!YjfConstants.SERVICE_CODE_DEBT.equals(debtNotification.getService())) {
            LOG.error("The notification: {} does not match the DEBT business. ", debtNotification.toString());
            return;
        }
        String bizSerialNum = debtNotification.getMerchOrderNo();//也即为合并扣款的批次号
        YJFDebtProcessStatus processStatus = YJFDebtProcessStatus.codeOf(debtNotification.getServiceStatus());
        DebtStatus newDebtStatus = YJFDebtProcessStatus.convertToDebtStatus(processStatus);

        // 更新扣款状态到缓存
        String repaymentBankcardNum = repaymentRecordService.getRepaymentBankcardByDebtOrder(bizSerialNum);
        if (repaymentBankcardNum == null) {
            repaymentBankcardNum = paymentOrderService.getBankcardNumByDebtSerialNum(bizSerialNum);
        }

        if (null == repaymentBankcardNum) {
            LOG.error("can not find the payment order or repayment plan by the debt serial num: {}, skip at first", bizSerialNum);
            return ;
        }
        userDebtRedisService.saveOrUpdate(new UserDebtInfoRedisVO(repaymentBankcardNum, bizSerialNum,
                debtNotification.getTransAmount(), newDebtStatus));


        // 根据还款记录的状态更新还款计划的状态- 扣款主要用于服务单支付以及分期还款的支付
        if (null != paymentOrderDao.getByTransactionSerial(bizSerialNum)) {
            this.updatePaymentOrderDebtStatus(bizSerialNum, newDebtStatus);
        } else {
            this.updateRepaymentRecordsDebtStatus(bizSerialNum, newDebtStatus, debtNotification.getResultMessage());
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updatePaymentOrderDebtStatus(String debtOrderNum, DebtStatus newDebtStatus) {
        PaymentOrder paymentOrder = paymentOrderDao.getByTransactionSerial(debtOrderNum);
        if (null != paymentOrder) {
            LOG.info("prepare to update payment order for debut order num: {} with status: {}", debtOrderNum, newDebtStatus);

            if (paymentOrder.getPaymentStatus() != null && null != newDebtStatus && !newDebtStatus.getCode().equals(paymentOrder.getPaymentStatus())) {
                paymentOrder.setPaymentStatus(newDebtStatus.getCode()); // 更新
                paymentOrderDao.save(paymentOrder);

                // 申请单的状态可重复更新
                if (DebtStatus.CONFIRMED == newDebtStatus || DebtStatus.SETTLED == newDebtStatus) {
                    requisitionServiceFeign.changeRequisitionStatusByPaymentOrder(paymentOrder.getOrderNumber(), RequisitionStatus.WaitingLoan);
                } else if (DebtStatus.FAILED == newDebtStatus) {
                    requisitionServiceFeign.changeRequisitionStatusByPaymentOrder(paymentOrder.getOrderNumber(), RequisitionStatus.FailedPayment);
                }
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void updateRepaymentRecordsDebtStatus(String debtOrderNum, DebtStatus newDebtStatus, String resultMessage) {
        //List<RepaymentRecord> repaymentRecords = repaymentRecordDao.getByRepayBatchNo(debtOrderNum);
        List<RepaymentRecord> repaymentRecords = repaymentRecordDao.listByTransactionSerial(debtOrderNum);
        if (repaymentRecords.isEmpty()) {
            LOG.error("No repayment record match the debt batch order number: {}", debtOrderNum);
            return ;
        }

        Integer customerId = null;
        double totalRepayAmount = 0.0;
        long totalOverdueInterestAmount = 0L;
        Date repayDate = null;
        String repayBankNumber = null;
        boolean isNeedTryDebtAgain = false; // 是否需要再次发起扣款: 如果是自动还款并且成功， 则继续发起扣款

        for (RepaymentRecord repaymentRecord : repaymentRecords) {
            LOG.info("prepare to update repayment record {} with status: {}", repaymentRecord.getId(), newDebtStatus);

            if (null != newDebtStatus && !StringUtils.equals(newDebtStatus.getCode(), repaymentRecord.getConfirmStatus())) {
                //更新支付记录的扣款状态
                LOG.info("update repayment record {} status from {} to {}", repaymentRecord.getId(),
                        repaymentRecord.getConfirmStatus(), newDebtStatus);
                repaymentRecord.setConfirmStatus(newDebtStatus.getCode());
                // 由于扣款接口中没有支付方的记录号， 暂时也用合并扣款的批次号替代
                /*if (StringUtils.isBlank(repaymentRecord.getTransactionSerial())) {
                    repaymentRecord.setTransactionSerial(debtOrderNum);
                }*/
                //如果扣款已经成功 或 失败 或已结算，需要更新还款计划的状态
                RepaymentPlan repaymentPlan = repaymentRecord.getRepaymentPlan();
                //设置发送微信消息的参数
                if (customerId == null) {
                    customerId = repaymentPlan.getCustomerId();
                }
                if (repayDate == null) {
                    repayDate = repaymentPlan.getRepayDate();
                }
                if (StringUtils.isEmpty(repayBankNumber)) {
                    repayBankNumber = repaymentRecord.getBankAccountNumber();
                }
                totalRepayAmount += repaymentRecord.getRepayTotalAmount().doubleValue();
                totalOverdueInterestAmount += repaymentRecord.getOverdueInterestAmount().longValue();

                if (DebtStatus.CONFIRMED == newDebtStatus || DebtStatus.SETTLED == newDebtStatus) {//更新还款计划的状态
                    if (RefundStatus.HAS_REFUND != repaymentPlan.getRepayStatus()) {
                        LOG.info("update repayment plan {} status {} to {} by repayment record status {}", repaymentPlan.getId(),
                                repaymentPlan.getRepayStatus(), RefundStatus.HAS_REFUND, DebtStatus.CONFIRMED);
                        //更新还款状态及合同状态
                        repaymentPlanService.updateRepaymentPlanStatusByEvent(repaymentPlan, InstallmentEvent.AutoRefundSuccessEvent);
                        isNeedTryDebtAgain = true; // 只要是自动还款， 则还可以发起扣款
                    }
                } else if (DebtStatus.FAILED == newDebtStatus) {// 更新还款计划的状态
                    if (RefundStatus.FAIL_REFUND != repaymentPlan.getRepayStatus()) {
                        LOG.info("update repayment plan {} status {} to {} by repayment record status {}", repaymentPlan.getId(),
                                repaymentPlan.getRepayStatus(), RefundStatus.FAIL_REFUND, DebtStatus.FAILED);
                        //更新还款状态及合同状态
                        repaymentPlanService.updateRepaymentPlanStatusByEvent(repaymentPlan, InstallmentEvent.AutoRefundFailedEvent);
                    }
                }
                repaymentRecord.setUpdateAt(new Date());
                repaymentRecordDao.save(repaymentRecord);
                LOG.info("update repayment record status success: {}", repaymentRecord.getId());
            }
        }

        //发送通知并不触发事务回滚（捕获异常）
        try {
            RepaymentPlanEvent repaymentPlanEvent = null;
            if (DebtStatus.CONFIRMED == newDebtStatus || DebtStatus.SETTLED == newDebtStatus) {
                repaymentPlanEvent = RepaymentPlanRefundEvent.refundSuccessEvent(customerId, totalRepayAmount, repayDate, repayBankNumber);
            } else if (DebtStatus.FAILED == newDebtStatus) {
                repaymentPlanEvent = RepaymentPlanRefundEvent.refundFailEvent(customerId, totalRepayAmount, repayDate, resultMessage);
            }
            if (null != repaymentPlanEvent) {
                this.applicationContext.publishEvent(repaymentPlanEvent);
                LOG.info("代扣状态提醒发送成功");
            }
        } catch (Exception e) { // 不触发事务回滚
            LOG.error("Fail to send yjf payment notification to user", e);
        }

        try {
            //在扣款失败后查看还款计划的状态是否为已逾期，如果为已逾期则发送微信消息给客户
            RepaymentPlan samplePlan = repaymentRecords.get(0).getRepaymentPlan();
            LOG.info("sample plan is the first debt on today: {}", userDebtRedisService.isFirstDebtOnToday(repaymentRecords.get(0).getBankAccountNumber(), debtOrderNum));
            LOG.info("sample plan: {}, repayStatus: {}, new debt status: {}", samplePlan.getId(), samplePlan.getRepayStatus(), newDebtStatus);
            if (samplePlan != null && userDebtRedisService.isFirstDebtOnToday(repaymentRecords.get(0).getBankAccountNumber(), debtOrderNum)
                    && RefundStatus.OVERDUE == samplePlan.getRepayStatus() && DebtStatus.FAILED == newDebtStatus) { //逾期还款第一次还款失败发送微信消息给用户
                LOG.info("prepare to send overdue reminder on plan: {}", samplePlan.getId());
                RepaymentPlanOverdueEvent repaymentPlanOverdueEvent = new RepaymentPlanOverdueEvent(samplePlan.getCustomerId(),
                        samplePlan.getRepayDate(), DateCommonUtils.intervalDays(samplePlan.getRepayDate(), DateCommonUtils.getCurrentDate()),
                        totalRepayAmount, totalOverdueInterestAmount);
                applicationContext.publishEvent(repaymentPlanOverdueEvent);
                LOG.info("send overdue reminder success on plan: {}", samplePlan.getId());
            }
        } catch (Exception e) { // 不触发事务回滚
            LOG.error(e.getMessage(), e);
        }
        LOG.info("Finish update all repayment records status by debt order serial number: {}", debtOrderNum);

        // 自动还款成功后可发起下一次还款
        if ( !repaymentRecords.get(0).getRepaymentPlan().getManualFlag() && isNeedTryDebtAgain) {
            LOG.info("Auto debt for the second debt for customer:" + customerId);
            try {
                this.tryTheSecondDebt(repaymentRecords.get(0));
            } catch (Exception e) {
                LOG.error("Failed to finished the second debt for " + e.getMessage(), e);
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRED) //不能开启新事务，如果父层事务对repaymentPlan进行变更， 子事务是查询不到结果的- 脏数据。
    @Override
    public void tryTheSecondDebt(RepaymentRecord sampleRecord) {
        LOG.info("the last repayment success and raise the second repayment...");

        if (DateCommonUtils.intervalDays(DateCommonUtils.getCurrentDate(), sampleRecord.getRepaymentPlan().getRepayDate()) == 0) { // 还款日正常还款
            LOG.info("start to the second to debt for Repay Day for customer id:" + sampleRecord.getRepaymentPlan().getCustomerId());
            repaymentPlanService.debitForRepayDay(sampleRecord.getRepaymentPlan().getCustomerId());
            LOG.info("done to the second to debt for Repay Day for customer id:" + sampleRecord.getRepaymentPlan().getCustomerId());
        } else { // 逾期还款
            LOG.info("start to the second to debt for Overdue for customer id:" + sampleRecord.getRepaymentPlan().getCustomerId());
            repaymentPlanService.debitForOverdue(sampleRecord.getRepaymentPlan().getCustomerId());
            LOG.info("done to the second to debt for Overdue for customer id:" + sampleRecord.getRepaymentPlan().getCustomerId());
        }
    }

    private Map<String, String> convertObjectToMap(Object obj) {
        String jsonStr = JSON.toJSONString(obj, nullValueFilter, SerializerFeature.WriteEnumUsingToString, SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNonStringValueAsString);
        return (Map<String, String>) JSON.parse(jsonStr);
    }

    private <R extends YjfRequest, Q extends YjfResponse> Q callYjfService(R request, Class<Q> clazz) {
        try {
            YjfService serviceCodeAnnation = request.getClass().getAnnotation(YjfService.class);
            if (null == serviceCodeAnnation || StringUtils.isBlank(serviceCodeAnnation.name())) {
                throw new IllegalStateException(String.format("%s do not have YjfService annotation or name property is empty."));
            }
            request.setServiceCode(serviceCodeAnnation.name());
            request.setPartnerId(propertiesBean.getPartnerId());
            request.setPlatformSerialNum(redisSequenceFactory.generateSerialNumber(BizCategory.YJF));

            Map<String, String> paramMap = this.convertObjectToMap(request);
            String responseStr = yjfHttpClient.doPost(propertiesBean.getGatewayUrl(), paramMap, propertiesBean.getPrivateKey());
            Q yjfResponse = JSON.parseObject(responseStr, clazz);

            if (yjfResponse.getResultCodeType() == ResultCodeType.PARAMETER_ERROR) {
                yjfResponse.setResultMessage(this.convertParameterErrorMsg(yjfResponse.getResultMessage()));
            }

            return yjfResponse;
        } catch (Exception e) {
            throw new YijifuClientException(String.format("Yijifu Request process failed on service code:%s, request ID:%s, reason: %s",
                    request.getServiceCode(), request.getPlatformSerialNum(), e.getMessage()), e);
        }
    }

    private String convertParameterErrorMsg(String originalMsg) {
        // 原始消息类似于：“失败,参数错误:mobileNo:请传入正确格式的手机号”
        String convertedMsg = originalMsg;
        if (StringUtils.isNotBlank(originalMsg) && originalMsg.contains(":") && originalMsg.lastIndexOf(":") < originalMsg.length() - 1) {
            convertedMsg = originalMsg.substring(originalMsg.lastIndexOf(":") + 1);
        }
        return convertedMsg;
    }

    public void queryAndUpdateVerifyBankcardRecord() {
        List<BankcardVerifyRecord> verificationUserResults = bankcardVerifyRecordDao.queryByVerificationStatus(VerifyCardStatus.VERIFY_CARD_PROCESSING.getCode());
        for (BankcardVerifyRecord record : verificationUserResults) {
            QueryBankCardVerifyRequest request  = new QueryBankCardVerifyRequest();
            request.setPlatformOrderNum(record.getPlatformOrderNumber());
            QueryBankCardVerifyResponse response = this.callYjfService(request, QueryBankCardVerifyResponse.class);
            if(response != null && response.getIsSuccess()) {
                record.setVerificationStatus(VerifyCardStatus.VERIFY_CARD_SUCCESS.getCode());
                bankcardVerifyRecordDao.save(record);
            } else if(response != null && VerifyCardStatus.VERIFY_CARD_FAIL.equals(response.getVerifyCardStatus())) {
                record.setVerificationStatus(VerifyCardStatus.VERIFY_CARD_FAIL.getCode());
                record.setRemarks(response.getResultMessage());
                bankcardVerifyRecordDao.save(record);
            }
        }
    }

}
