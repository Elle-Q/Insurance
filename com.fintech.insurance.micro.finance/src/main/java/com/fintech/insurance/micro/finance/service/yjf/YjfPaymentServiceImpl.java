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
 * @Description: ???????????????????????????
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
     * ????????????????????? ????????????  ???????????? ???????????????????????????????????????+????????????
     *
     * @param userName ??????????????????
     * @param certNum ????????????
     * @param bankCardNum ????????????
     * @param reservedMobile ???????????????????????????
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

    // ???????????????????????????
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
                // ?????????????????? ???????????????????????????????????????????????????
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
        // ??????????????????
        result.setIsSuccess(VerifyCardStatus.VERIFY_CARD_SUCCESS == response.getVerifyCardStatus());
        result.setVerificationStatus(response.getVerifyCardStatus() == null ? null : response.getVerifyCardStatus().getCode());
        result.setRequestSerialNum(response.getPlatformOrderNum());

        if (!result.getIsSuccess()) {
            if ("comn_04_0003".equals(response.getErrorCode())) {
                result.setFailedMessage("?????????????????????????????????");
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
     * ????????????
     *
     * @param userName ??????????????????
     * @param certNum ????????????
     * @param bankCardNum ????????????
     * @param reservedMobile ???????????????????????????
     * @param amount ????????????
     * @param contactInfo ????????????
     *
     * @return ??????????????????
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
        request.setAmount(amount / 100); // ????????????????????????
        request.setContractInfo(contactInfo);

        // ????????????URL
        request.setNotifyUrl(propertiesBean.getDebtNotifyUrl());

        YjfResponse response = this.callYjfService(request, YjfResponse.class);

        DebtResult result = new DebtResult();
        result.setIsSuccess(ResultCodeType.EXECUTE_SUCCESS == response.getResultCodeType() || ResultCodeType.EXECUTE_PROCESSING == response.getResultCodeType());
        result.setRequestSerialNum(request.getPlatformOrderNum());
        result.setAmount(amount);
        if (!result.getIsSuccess()) {
            result.setFailedMessage(response.getResultMessage());
        }
        // ??????????????????????????????
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
                //????????????????????????????????????????????????
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
            } else if (ResultCodeType.INSTALLMENT_TRANS_ORDER_NO_DATA == response.getResultCodeType()) {//?????????????????????????????????
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
        String bizSerialNum = debtNotification.getMerchOrderNo();//?????????????????????????????????
        YJFDebtProcessStatus processStatus = YJFDebtProcessStatus.codeOf(debtNotification.getServiceStatus());
        DebtStatus newDebtStatus = YJFDebtProcessStatus.convertToDebtStatus(processStatus);

        // ???????????????????????????
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


        // ??????????????????????????????????????????????????????- ????????????????????????????????????????????????????????????
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
                paymentOrder.setPaymentStatus(newDebtStatus.getCode()); // ??????
                paymentOrderDao.save(paymentOrder);

                // ?????????????????????????????????
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
        boolean isNeedTryDebtAgain = false; // ??????????????????????????????: ???????????????????????????????????? ?????????????????????

        for (RepaymentRecord repaymentRecord : repaymentRecords) {
            LOG.info("prepare to update repayment record {} with status: {}", repaymentRecord.getId(), newDebtStatus);

            if (null != newDebtStatus && !StringUtils.equals(newDebtStatus.getCode(), repaymentRecord.getConfirmStatus())) {
                //?????????????????????????????????
                LOG.info("update repayment record {} status from {} to {}", repaymentRecord.getId(),
                        repaymentRecord.getConfirmStatus(), newDebtStatus);
                repaymentRecord.setConfirmStatus(newDebtStatus.getCode());
                // ??????????????????????????????????????????????????? ??????????????????????????????????????????
                /*if (StringUtils.isBlank(repaymentRecord.getTransactionSerial())) {
                    repaymentRecord.setTransactionSerial(debtOrderNum);
                }*/
                //???????????????????????? ??? ?????? ????????????????????????????????????????????????
                RepaymentPlan repaymentPlan = repaymentRecord.getRepaymentPlan();
                //?????????????????????????????????
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

                if (DebtStatus.CONFIRMED == newDebtStatus || DebtStatus.SETTLED == newDebtStatus) {//???????????????????????????
                    if (RefundStatus.HAS_REFUND != repaymentPlan.getRepayStatus()) {
                        LOG.info("update repayment plan {} status {} to {} by repayment record status {}", repaymentPlan.getId(),
                                repaymentPlan.getRepayStatus(), RefundStatus.HAS_REFUND, DebtStatus.CONFIRMED);
                        //?????????????????????????????????
                        repaymentPlanService.updateRepaymentPlanStatusByEvent(repaymentPlan, InstallmentEvent.AutoRefundSuccessEvent);
                        isNeedTryDebtAgain = true; // ???????????????????????? ????????????????????????
                    }
                } else if (DebtStatus.FAILED == newDebtStatus) {// ???????????????????????????
                    if (RefundStatus.FAIL_REFUND != repaymentPlan.getRepayStatus()) {
                        LOG.info("update repayment plan {} status {} to {} by repayment record status {}", repaymentPlan.getId(),
                                repaymentPlan.getRepayStatus(), RefundStatus.FAIL_REFUND, DebtStatus.FAILED);
                        //?????????????????????????????????
                        repaymentPlanService.updateRepaymentPlanStatusByEvent(repaymentPlan, InstallmentEvent.AutoRefundFailedEvent);
                    }
                }
                repaymentRecord.setUpdateAt(new Date());
                repaymentRecordDao.save(repaymentRecord);
                LOG.info("update repayment record status success: {}", repaymentRecord.getId());
            }
        }

        //??????????????????????????????????????????????????????
        try {
            RepaymentPlanEvent repaymentPlanEvent = null;
            if (DebtStatus.CONFIRMED == newDebtStatus || DebtStatus.SETTLED == newDebtStatus) {
                repaymentPlanEvent = RepaymentPlanRefundEvent.refundSuccessEvent(customerId, totalRepayAmount, repayDate, repayBankNumber);
            } else if (DebtStatus.FAILED == newDebtStatus) {
                repaymentPlanEvent = RepaymentPlanRefundEvent.refundFailEvent(customerId, totalRepayAmount, repayDate, resultMessage);
            }
            if (null != repaymentPlanEvent) {
                this.applicationContext.publishEvent(repaymentPlanEvent);
                LOG.info("??????????????????????????????");
            }
        } catch (Exception e) { // ?????????????????????
            LOG.error("Fail to send yjf payment notification to user", e);
        }

        try {
            //??????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            RepaymentPlan samplePlan = repaymentRecords.get(0).getRepaymentPlan();
            LOG.info("sample plan is the first debt on today: {}", userDebtRedisService.isFirstDebtOnToday(repaymentRecords.get(0).getBankAccountNumber(), debtOrderNum));
            LOG.info("sample plan: {}, repayStatus: {}, new debt status: {}", samplePlan.getId(), samplePlan.getRepayStatus(), newDebtStatus);
            if (samplePlan != null && userDebtRedisService.isFirstDebtOnToday(repaymentRecords.get(0).getBankAccountNumber(), debtOrderNum)
                    && RefundStatus.OVERDUE == samplePlan.getRepayStatus() && DebtStatus.FAILED == newDebtStatus) { //????????????????????????????????????????????????????????????
                LOG.info("prepare to send overdue reminder on plan: {}", samplePlan.getId());
                RepaymentPlanOverdueEvent repaymentPlanOverdueEvent = new RepaymentPlanOverdueEvent(samplePlan.getCustomerId(),
                        samplePlan.getRepayDate(), DateCommonUtils.intervalDays(samplePlan.getRepayDate(), DateCommonUtils.getCurrentDate()),
                        totalRepayAmount, totalOverdueInterestAmount);
                applicationContext.publishEvent(repaymentPlanOverdueEvent);
                LOG.info("send overdue reminder success on plan: {}", samplePlan.getId());
            }
        } catch (Exception e) { // ?????????????????????
            LOG.error(e.getMessage(), e);
        }
        LOG.info("Finish update all repayment records status by debt order serial number: {}", debtOrderNum);

        // ?????????????????????????????????????????????
        if ( !repaymentRecords.get(0).getRepaymentPlan().getManualFlag() && isNeedTryDebtAgain) {
            LOG.info("Auto debt for the second debt for customer:" + customerId);
            try {
                this.tryTheSecondDebt(repaymentRecords.get(0));
            } catch (Exception e) {
                LOG.error("Failed to finished the second debt for " + e.getMessage(), e);
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRED) //?????????????????????????????????????????????repaymentPlan??????????????? ?????????????????????????????????- ????????????
    @Override
    public void tryTheSecondDebt(RepaymentRecord sampleRecord) {
        LOG.info("the last repayment success and raise the second repayment...");

        if (DateCommonUtils.intervalDays(DateCommonUtils.getCurrentDate(), sampleRecord.getRepaymentPlan().getRepayDate()) == 0) { // ?????????????????????
            LOG.info("start to the second to debt for Repay Day for customer id:" + sampleRecord.getRepaymentPlan().getCustomerId());
            repaymentPlanService.debitForRepayDay(sampleRecord.getRepaymentPlan().getCustomerId());
            LOG.info("done to the second to debt for Repay Day for customer id:" + sampleRecord.getRepaymentPlan().getCustomerId());
        } else { // ????????????
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
        // ?????????????????????????????????,????????????:mobileNo:????????????????????????????????????
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
