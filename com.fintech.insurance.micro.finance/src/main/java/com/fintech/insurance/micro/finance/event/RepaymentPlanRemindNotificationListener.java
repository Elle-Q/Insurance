package com.fintech.insurance.micro.finance.event;

import com.fintech.insurance.commons.beans.WeixinTemplateMessagesConfgBean;
import com.fintech.insurance.commons.constants.WechatConstants;
import com.fintech.insurance.commons.enums.RefundStatus;
import com.fintech.insurance.commons.utils.NumberFormatorUtils;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.micro.dto.customer.CustomerBankCardVO;
import com.fintech.insurance.micro.finance.persist.entity.RepaymentPlan;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 还款计划提醒监听器
 */
@Component
public class RepaymentPlanRemindNotificationListener extends AbstractRepaymentPlanListener {

    private static final Logger logger = LoggerFactory.getLogger(RepaymentPlanRemindNotificationListener.class);

    /**
     * 还款提醒事件
     */
    private RepaymentPlanRemindEvent event;

    @Async
    @Override
    public void onApplicationEvent(RepaymentPlanEvent event) {
        if (event == null || !(event instanceof RepaymentPlanRemindEvent)) {
            logger.info("The event is empty or the event is not an instance of class " + RepaymentPlanRemindEvent.class.getName());
            return ;
        }
        this.event = (RepaymentPlanRemindEvent) event;
        if (this.event.getRepaymentPlans() != null && this.event.getRepaymentPlans().size() > 0) {
            this.sendRemindTemplateMessageToCustomer();
        }
    }

    /**
     * 还款提醒
     * 发送微信通知消息给客户
     */
    private void sendRemindTemplateMessageToCustomer() {
        List<RepaymentPlan> repaymentPlans = this.event.getRepaymentPlans();
        Date repaymentDate = this.event.getRefundDate();
        BigDecimal amount = BigDecimal.ZERO;
        boolean send = true;
        Integer customerId = null;
        //计算还款金额
        for (RepaymentPlan repaymentPlan : repaymentPlans) {
            amount = amount.add(repaymentPlan.getRepayTotalAmount());
            //检查当前还款计划的状态
            if (!RefundStatus.WAITING_REFUND.getCode().equalsIgnoreCase(repaymentPlan.getRepayStatus() == null ? "" : repaymentPlan.getRepayStatus().getCode())) {
                logger.error("The remind weixin template message notification will not be sent, due to the repaymen plans contain the incorrect status record");
                send = false;
                break;
            }
            //检查当前用户的id
            if (customerId == null) {
                customerId = repaymentPlan.getCustomerId();
            }
            if (customerId != null && customerId != repaymentPlan.getCustomerId()) {
                logger.error("Cannot send weixin template message notification for different customer");
                send = false;
                break;
            }
            //检查还款日期
            if (!DateUtils.isSameDay(repaymentDate, repaymentPlan.getRepayDate())) {
                logger.error("The repayment plans contains the record and its repayment date is not " + DateFormatUtils.ISO_DATETIME_FORMAT.format(repaymentDate));
                send = false;
                break;
            }
        }
        //发送模版消息
        if (send) {
            String url = this.customerWxConfigBean.getMpContractListUrl();
            List<WxMpTemplateData> templateData = new ArrayList<>();
            templateData.add(new WxMpTemplateData(WechatConstants.TEMPLATE_MESSAGE_PARAM_FIRST, this.templateMessageConfigBean.getRepaymentReminderCustomerNtfTitle()));
            templateData.add(new WxMpTemplateData(WechatConstants.TEMPLATE_MESSAGE_PARAM_KEYWORD1, String.format(this.templateMessageConfigBean.getAmount(), NumberFormatorUtils.convertFinanceNumberToShowString(amount.doubleValue()))));
            templateData.add(new WxMpTemplateData(WechatConstants.TEMPLATE_MESSAGE_PARAM_KEYWORD2, dateFormat.format(repaymentDate)));
            FintechResponse<CustomerBankCardVO> cardResponse = this.customerServiceClient.getCustomerBankCard(customerId);
            String bankAccountNumber = "";
            if (cardResponse != null && cardResponse.isOk() && cardResponse.getData() != null) {
                bankAccountNumber = cardResponse.getData().getAccountNumber();
            }
            templateData.add(new WxMpTemplateData(WechatConstants.TEMPLATE_MESSAGE_PARAM_KEYWORD3, String.format(this.templateMessageConfigBean.getBankcardSuffix(), this.getBankAccountNumberSuffix(bankAccountNumber))));
            super.sendTemplateMessage(customerId, null, this.customerWxConfigBean.getAppid(), WeixinTemplateMessagesConfgBean.TEMPLATE_REPAYMENT_REMINDER, url, templateData);
        }
    }
}
