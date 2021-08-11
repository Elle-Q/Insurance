package com.fintech.insurance.micro.biz.service.notification;

import com.fintech.insurance.commons.enums.NotificationEvent;
import com.fintech.insurance.commons.enums.RefundStatus;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.micro.dto.biz.ContractVO;
import com.fintech.insurance.micro.dto.biz.RequisitionVO;
import com.fintech.insurance.micro.dto.finance.FinanceRepaymentPlanVO;
import com.fintech.insurance.micro.dto.thirdparty.sms.SMSSendParamVO;
import com.fintech.insurance.micro.dto.thirdparty.sms.SMSSendResultVO;
import com.fintech.insurance.micro.feign.thirdparty.sms.SMSServiceFeign;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.fintech.insurance.commons.enums.NotificationEvent.*;
import static com.fintech.insurance.commons.enums.SMSTemplateParams.*;

/**
 * @Description: 通知服务
 * @Author: East
 * @Date: 2017/12/1 0001 9:51
 */
@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Logger LOG = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @Autowired
    private SMSServiceFeign smsServiceFeign;

    /**
     * 发送审批通知，运营、风控、领导审核通过后，提醒支付
     *
     * @param requisitionVO 申请单
     */
    @Override
    public void sendAuditNotification(RequisitionVO requisitionVO) {

        if (requisitionVO == null) {
            throw new IllegalArgumentException("Null requisitionVO!");
        }

        //Send audit notification to channel with SMS
        this.sendSMSAuditNotificationToChannel(requisitionVO);

        //Send audit notification to customer with SMS
        this.sendSMSAuditNotificationToCustomer(requisitionVO);
    }

    private void sendSMSAuditNotificationToChannel(RequisitionVO requisitionVO) {
        String[] channelMobiles = new String[]{requisitionVO.getChannelUserMobile()};
        Map<String, String> channelSMSParams = new HashMap<String, String>();
        channelSMSParams.put(CHANNEL_NAME.getCode(), requisitionVO.getChannelName());
        channelSMSParams.put(REQUISITION_NUMBER.getCode(), requisitionVO.getRequisitionNumber());
        SMSSendParamVO channelSMSSendParamVO = new SMSSendParamVO(channelMobiles, AUDIT_NOTIFICATION_FOR_CHANNEL, channelSMSParams);

        //发送审批通知，运营、风控、领导审核通过后，提醒支付
        FintechResponse<SMSSendResultVO> channelSMSResponse = smsServiceFeign.sendSMS(channelSMSSendParamVO);
        if (!channelSMSResponse.isOk()) {
            LOG.error("Send audit notification for channel mobiles failed with [" + requisitionVO + "] in sendAuditNotification!");
            throw new FInsuranceBaseException(104921, new Object[]{channelSMSResponse.getMsg()});
        }
    }

    private void sendSMSAuditNotificationToCustomer(RequisitionVO requisitionVO) {
        String[] customerMobiles = new String[]{requisitionVO.getCustomerMobile()};
        Map<String, String> customerSMSParams = new HashMap<String, String>();
        customerSMSParams.put(CUSTOMER_NAME.getCode(), requisitionVO.getCustomerName());
        customerSMSParams.put(REQUISITION_NUMBER.getCode(), requisitionVO.getRequisitionNumber());
        SMSSendParamVO customerSMSSendParamVO = new SMSSendParamVO(customerMobiles, AUDIT_NOTIFICATION_FOR_CUSTOMER, customerSMSParams);

        //发送审批通知，运营、风控、领导审核通过后，提醒支付
        FintechResponse<SMSSendResultVO> customerSMSResponse = smsServiceFeign.sendSMS(customerSMSSendParamVO);
        if (!customerSMSResponse.isOk()) {
            LOG.error("Send audit notification for channel mobiles failed with [" + requisitionVO + "] in sendAuditNotification!");
            throw new FInsuranceBaseException(104921, new Object[]{customerSMSResponse.getMsg()});
        }
    }

    /**
     * 发送放款通知，放款提醒
     *
     * @param requisitionVO 申请单
     */
    @Override
    public void sendLoanConfirmNotification(RequisitionVO requisitionVO) {

        if (requisitionVO == null) {
            throw new IllegalArgumentException("Null requisitionVO!");
        }

        //Send loan confirm notification to channel with SMS
        this.sendSMSLoanConfirmNotificationToChannel(requisitionVO);

        //Send loan confirm notification to customer with SMS
        this.sendSMSLoanConfirmNotificationToCustomer(requisitionVO);
    }

    private void sendSMSLoanConfirmNotificationToChannel(RequisitionVO requisitionVO) {
        String[] channelMobiles = new String[]{requisitionVO.getChannelUserMobile()};
        Map<String, String> channelSMSParams = new HashMap<String, String>();
        channelSMSParams.put(CUSTOMER_NAME.getCode(), requisitionVO.getChannelName());
        channelSMSParams.put(REQUISITION_NUMBER.getCode(), requisitionVO.getRequisitionNumber());
        SMSSendParamVO channelSMSSendParamVO = new SMSSendParamVO(channelMobiles, LOAN_CONFIRM_NOTIFICATION_FOR_CHANNEL, channelSMSParams);

        //渠道放款确认通知，放款提醒
        FintechResponse<SMSSendResultVO> channelSMSResponse = smsServiceFeign.sendSMS(channelSMSSendParamVO);
        if (!channelSMSResponse.isOk()) {
            LOG.error("Send audit notification for channel mobiles failed with [" + requisitionVO + "]!");
            throw new FInsuranceBaseException(104922, new Object[]{channelSMSResponse.getMsg()});
        }
    }

    private void sendSMSLoanConfirmNotificationToCustomer(RequisitionVO requisitionVO) {
        String[] customerMobiles = new String[]{requisitionVO.getCustomerMobile()};
        Map<String, String> customerSMSParams = new HashMap<String, String>();
        customerSMSParams.put(CUSTOMER_NAME.getCode(), requisitionVO.getCustomerName());
        customerSMSParams.put(REQUISITION_NUMBER.getCode(), requisitionVO.getRequisitionNumber());
        SMSSendParamVO customerSMSSendParamVO = new SMSSendParamVO(customerMobiles, LOAN_CONFIRM_NOTIFICATION_FOR_CUSTOMER, customerSMSParams);

        //客户放款确认通知，放款提醒
        FintechResponse<SMSSendResultVO> customerSMSResponse = smsServiceFeign.sendSMS(customerSMSSendParamVO);
        if (!customerSMSResponse.isOk()) {
            LOG.error("Send audit notification for channel mobiles failed with [" + requisitionVO + "] in sendAuditNotification!");
            throw new FInsuranceBaseException(104922, new Object[]{customerSMSResponse.getMsg()});
        }
    }

    /**
     * 发送还款提醒通知，还款日前XXX日提醒
     *
     * @param contractVO      合同
     * @param repaymentPlanVO 还款计划
     */
    @Override
    public void sendRepaymentRemindNotification(ContractVO contractVO, FinanceRepaymentPlanVO repaymentPlanVO) {
        if (contractVO == null) {
            throw new IllegalArgumentException("Null contractVO!");
        }

        if (repaymentPlanVO == null) {
            throw new IllegalArgumentException("Null repaymentPlanVO!");
        }

        //Send repayment remind notification to customer with SMS
        this.sendSMSRepaymentRemindNotificationToCustomer(contractVO, repaymentPlanVO);
    }

    private void sendSMSRepaymentRemindNotificationToCustomer(ContractVO contractVO, FinanceRepaymentPlanVO repaymentPlanVO) {
        String[] customerMobiles = new String[]{contractVO.getCustomerMobile()};
        Map<String, String> customerSMSParams = new HashMap<String, String>();
        customerSMSParams.put(CONTRACT_NUMBER.getCode(), contractVO.getContractCode());
        customerSMSParams.put(REPAYMENT_INSTALMENT.getCode(), this.repaymentInstalmentFormat(repaymentPlanVO.getTotalInstalment(), repaymentPlanVO.getCurrentInstalment()));
        customerSMSParams.put(REPAY_DATE.getCode(), this.dateFormat(repaymentPlanVO.getRepayDate()));
        customerSMSParams.put(REPAY_TOTAL_AMOUNT.getCode(), this.amountFormat(repaymentPlanVO.getRepayTotalAmount().longValue()));
        SMSSendParamVO customerSMSSendParamVO = new SMSSendParamVO(customerMobiles, REPAYMENT_REMIND_NOTIFICATION, customerSMSParams);

        //还款提醒通知，还款日前XXX日提醒
        FintechResponse<SMSSendResultVO> customerSMSResponse = smsServiceFeign.sendSMS(customerSMSSendParamVO);
        if (!customerSMSResponse.isOk()) {
            LOG.error("Send repayment remind notification for channel mobiles failed with [" + contractVO + "] in sendRepaymentRemindNotification!");
            throw new FInsuranceBaseException(104923, new Object[]{customerSMSResponse.getMsg()});
        }
    }

    private String repaymentInstalmentFormat(Integer totalInstalment, Integer currentInstalment) {
        String[] instalmentArray = new String[]{(totalInstalment != null ? String.valueOf(totalInstalment) : "-"), (currentInstalment != null ? String.valueOf(currentInstalment) : "-")};
        return StringUtils.join(instalmentArray, "/");
    }

    private String dateFormat(Date date) {
        if (date == null) {
            return "-";
        }
        return new DateTime(date).toString("yyyy/MM/dd");
    }

    private String amountFormat(Long amount) {
        if (amount == null) {
            return "-";
        }
        double actualAmount = amount / 100.0D;

        NumberFormat format = NumberFormat.getInstance();
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(2);
        return format.format(actualAmount);
    }

    /**
     * 发送逾期提醒通知，逾期提醒通知，过了还款日没过最大逾期天数期间扣款失败提醒
     *
     * @param contractVO      合同
     * @param repaymentPlanVO 还款计划
     */
    @Override
    public void sendOverdueRemindNotification(ContractVO contractVO, FinanceRepaymentPlanVO repaymentPlanVO) {

        if (contractVO == null) {
            throw new IllegalArgumentException("Null contractVO!");
        }

        if (repaymentPlanVO == null) {
            throw new IllegalArgumentException("Null repaymentPlanVO!");
        }

        //Send overdue remind notification to customer with SMS
        this.sendSMSOverdueRemindNotificationToCustomer(contractVO, repaymentPlanVO);
    }

    private void sendSMSOverdueRemindNotificationToCustomer(ContractVO contractVO, FinanceRepaymentPlanVO repaymentPlanVO) {

        String[] customerMobiles = new String[]{contractVO.getCustomerMobile()};
        Map<String, String> customerSMSParams = new HashMap<String, String>();
        customerSMSParams.put(CONTRACT_NUMBER.getCode(), contractVO.getContractCode());
        customerSMSParams.put(REPAYMENT_INSTALMENT.getCode(), this.repaymentInstalmentFormat(repaymentPlanVO.getTotalInstalment(), repaymentPlanVO.getCurrentInstalment()));
        customerSMSParams.put(REPAY_DATE.getCode(), this.dateFormat(repaymentPlanVO.getRepayDate()));
        customerSMSParams.put(REPAY_TOTAL_AMOUNT.getCode(), this.amountFormat(repaymentPlanVO.getRepayTotalAmount().longValue()));
        SMSSendParamVO customerSMSSendParamVO = new SMSSendParamVO(customerMobiles, OVERDUE_REMIND_NOTIFICATION, customerSMSParams);

        //逾期提醒通知，过了还款日没过最大逾期天数期间扣款失败提醒
        FintechResponse<SMSSendResultVO> customerSMSResponse = smsServiceFeign.sendSMS(customerSMSSendParamVO);
        if (!customerSMSResponse.isOk()) {
            LOG.error("Send overdue remind notification for channel mobiles failed with [" + repaymentPlanVO + "] in sendOverdueRemindNotification!");
            throw new FInsuranceBaseException(104924, customerSMSResponse.getMsg());
        }
    }


    /**
     * 发送还款结果通知
     *
     * @param contractVO      合同
     * @param repaymentPlanVO 还款计划
     */
    @Override
    public void sendRepaymentResultNotification(ContractVO contractVO, FinanceRepaymentPlanVO repaymentPlanVO) {

        if (contractVO == null) {
            throw new IllegalArgumentException("Null contractVO!");
        }

        if (repaymentPlanVO == null) {
            throw new IllegalArgumentException("Null repaymentPlanVO!");
        }

        //Send repayment result notification to channel with SMS
        this.sendSMSRepaymentResultNotificationToChannel(contractVO, repaymentPlanVO);

        //Send repayment result notification to customer with SMS
        this.sendSMSRepaymentResultNotificationToCustomer(contractVO, repaymentPlanVO);
    }

    private void sendSMSRepaymentResultNotificationToChannel(ContractVO contractVO, FinanceRepaymentPlanVO repaymentPlanVO) {
        String[] channelMobiles = new String[]{contractVO.getChannelUserMobile()};
        NotificationEvent channelEvent = null;
        Map<String, String> channelSMSParams = new HashMap<String, String>();
        channelSMSParams.put(CONTRACT_NUMBER.getCode(), contractVO.getContractCode());
        channelSMSParams.put(REPAYMENT_INSTALMENT.getCode(), this.repaymentInstalmentFormat(repaymentPlanVO.getTotalInstalment(), repaymentPlanVO.getCurrentInstalment()));
        channelSMSParams.put(REPAY_DATE.getCode(), this.dateFormat(repaymentPlanVO.getRepayDate()));
        channelSMSParams.put(REPAY_TOTAL_AMOUNT.getCode(), this.amountFormat(repaymentPlanVO.getRepayTotalAmount().longValue()));

        boolean needNotifyChannel = false;

        RefundStatus repayStatus = repaymentPlanVO.getRepayStatus();
        switch (repayStatus) {
            case WAITING_REFUND:  //待还款
                break;
            case HAS_REFUND: //还款成功通知，还款日扣款成功提醒/过了还款日没过最大逾期天数期间扣款成功提醒
                break;
            case FAIL_REFUND: //还款日扣款失败提醒
                needNotifyChannel = true;
                channelEvent = REPAY_FAILURE_NOTIFICATION_FOR_CHANNEL;
                break;
            case OVERDUE:  //逾期还款失败通知
                needNotifyChannel = true;
                channelEvent = OVERDUE_REPAY_FAILURE_NOTIFICATION_FOR_CHANNEL;
                break;
            case WAITING_WITHDRAW:  //待退保
                break;
            case HAS_WITHDRAW:  //已退保
                break;
            default:
                LOG.error("Unsupported Operation for repayStatus [" + repayStatus + "] in sendRepaymentResultNotification!");
                throw new FInsuranceBaseException(104925, new Object[]{repayStatus});
        }

        //Send repayment result notification to channel
        if (needNotifyChannel) {
            SMSSendParamVO channelSMSSendParamVO = new SMSSendParamVO(channelMobiles, channelEvent, channelSMSParams);
            FintechResponse<SMSSendResultVO> channelSMSResponse = smsServiceFeign.sendSMS(channelSMSSendParamVO);
            if (!channelSMSResponse.isOk()) {
                LOG.error("Send repayment result notification for channel failed with [" + repaymentPlanVO + "] in sendRepaymentResultNotification!");
                throw new FInsuranceBaseException(104926, new Object[]{channelSMSResponse.getMsg()});
            }
        }
    }

    private void sendSMSRepaymentResultNotificationToCustomer(ContractVO contractVO, FinanceRepaymentPlanVO repaymentPlanVO) {
        String[] customerMobiles = new String[]{contractVO.getCustomerMobile()};
        NotificationEvent customerEvent = null;
        Map<String, String> customerSMSParams = new HashMap<String, String>();
        customerSMSParams.put(CONTRACT_NUMBER.getCode(), contractVO.getContractCode());
        customerSMSParams.put(REPAYMENT_INSTALMENT.getCode(), this.repaymentInstalmentFormat(repaymentPlanVO.getTotalInstalment(), repaymentPlanVO.getCurrentInstalment()));
        customerSMSParams.put(REPAY_DATE.getCode(), this.dateFormat(repaymentPlanVO.getRepayDate()));
        customerSMSParams.put(REPAY_TOTAL_AMOUNT.getCode(), this.amountFormat(repaymentPlanVO.getRepayTotalAmount().longValue()));

        RefundStatus repayStatus = repaymentPlanVO.getRepayStatus();
        switch (repayStatus) {
            case WAITING_REFUND:  //待还款
                break;
            case HAS_REFUND: //还款成功通知，还款日扣款成功提醒/过了还款日没过最大逾期天数期间扣款成功提醒
                if (this.isContractCompletion(repaymentPlanVO)) {
                    customerEvent = CONTRACT_COMPLETION_NOTIFICATION;
                } else {
                    customerEvent = REPAY_SUCCESS_NOTIFICATION;
                }
                break;
            case FAIL_REFUND: //还款日扣款失败提醒
                customerEvent = REPAY_FAILURE_NOTIFICATION_FOR_CUSTOMER;
                break;
            case OVERDUE:  //逾期还款失败通知
                customerEvent = OVERDUE_REPAY_FAILURE_NOTIFICATION_FOR_CUSTOMER;
                break;
            case WAITING_WITHDRAW:  //待退保
                break;
            case HAS_WITHDRAW:  //已退保
                break;
            default:
                LOG.error("Unsupported Operation for repayStatus [" + repayStatus + "] in sendRepaymentResultNotification!");
                throw new FInsuranceBaseException(104925, new Object[]{repayStatus});
        }

        //Send repayment result notification to customer
        SMSSendParamVO customerSMSSendParamVO = new SMSSendParamVO(customerMobiles, customerEvent, customerSMSParams);
        FintechResponse<SMSSendResultVO> customerSMSResponse = smsServiceFeign.sendSMS(customerSMSSendParamVO);
        if (!customerSMSResponse.isOk()) {
            LOG.error("Send repayment result notification for customer failed with [" + repaymentPlanVO + "] in sendRepaymentResultNotification!");
            throw new FInsuranceBaseException(104926, new Object[]{customerSMSResponse.getMsg()});
        }
    }

    private boolean isContractCompletion(FinanceRepaymentPlanVO repaymentPlanVO) {
        if (repaymentPlanVO.getCurrentInstalment() == null || repaymentPlanVO.getTotalInstalment() == null) {
            return false;
        }
        return !(repaymentPlanVO.getCurrentInstalment() < repaymentPlanVO.getTotalInstalment());
    }
}
