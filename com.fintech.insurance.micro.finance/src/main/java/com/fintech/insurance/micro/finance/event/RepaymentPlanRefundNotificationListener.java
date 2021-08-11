package com.fintech.insurance.micro.finance.event;

import com.fintech.insurance.commons.beans.WeixinTemplateMessagesConfgBean;
import com.fintech.insurance.commons.constants.WechatConstants;
import com.fintech.insurance.commons.utils.NumberFormatorUtils;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 还款事件，失败或者成功
 */
@Component
public class RepaymentPlanRefundNotificationListener extends AbstractRepaymentPlanListener {

    private static final Logger logger = LoggerFactory.getLogger(RepaymentPlanRefundNotificationListener.class);

    private RepaymentPlanRefundEvent event;

    @Async
    @Override
    public void onApplicationEvent(RepaymentPlanEvent event) {
        if (event == null || !(event instanceof RepaymentPlanRefundEvent)) {
            logger.info("The event is empty or the event is not an instance of class " + RepaymentPlanRemindEvent.class.getName());
            return ;
        }
        this.event = (RepaymentPlanRefundEvent) event;
        if (this.event.isSucccess()) {
            this.sendRefundSuccessNotificationToCustomer();
        } else {
            this.sendRefundFailNotificationToCustomer();
        }
    }

    /**
     * 分期代扣成功提醒
     */
    private void sendRefundSuccessNotificationToCustomer() {
        String url = this.customerWxConfigBean.getMpContractListUrl();
        List<WxMpTemplateData> templateData = new ArrayList<>();
        templateData.add(new WxMpTemplateData(WechatConstants.TEMPLATE_MESSAGE_PARAM_FIRST, this.templateMessageConfigBean.getRepaymentWithholdSuccessCustomerNtfTitle()));
        templateData.add(new WxMpTemplateData(WechatConstants.TEMPLATE_MESSAGE_PARAM_KEYWORD1, String.format(this.templateMessageConfigBean.getAmount(), NumberFormatorUtils.convertFinanceNumberToShowString(this.event.getRefundAmount()))));
        templateData.add(new WxMpTemplateData(WechatConstants.TEMPLATE_MESSAGE_PARAM_KEYWORD2, String.format(this.templateMessageConfigBean.getBankcardSuffix(), this.getBankAccountNumberSuffix(this.event.getRefundCardNumber()))));
        super.sendTemplateMessage(this.event.getCustomerId(), null, this.customerWxConfigBean.getAppid(), WeixinTemplateMessagesConfgBean.TEMPLATE_WITHHOLD_SUCCESS, url, templateData);
    }

    /**
     * 分期代扣失败提醒
     */
    private void sendRefundFailNotificationToCustomer() {
        String url = this.customerWxConfigBean.getMpContractListUrl();
        List<WxMpTemplateData> templateData = new ArrayList<>();
        templateData.add(new WxMpTemplateData(WechatConstants.TEMPLATE_MESSAGE_PARAM_FIRST, this.templateMessageConfigBean.getRepaymentWithholdFailCustomerNtfTitle()));
        templateData.add(new WxMpTemplateData(WechatConstants.TEMPLATE_MESSAGE_PARAM_KEYWORD1, String.format(this.templateMessageConfigBean.getAmount(), NumberFormatorUtils.convertFinanceNumberToShowString(this.event.getRefundAmount()))));
        templateData.add(new WxMpTemplateData(WechatConstants.TEMPLATE_MESSAGE_PARAM_KEYWORD2, this.event.getFailReason()));
        super.sendTemplateMessage(this.event.getCustomerId(), null, this.customerWxConfigBean.getAppid(), WeixinTemplateMessagesConfgBean.TEMPLATE_REPAYMENT_FAILURE, url, templateData);
    }
}
