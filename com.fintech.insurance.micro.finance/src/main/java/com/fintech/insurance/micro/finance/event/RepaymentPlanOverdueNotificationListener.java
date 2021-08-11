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
 * 逾期提醒事件监听器
 */
@Component
public class RepaymentPlanOverdueNotificationListener extends AbstractRepaymentPlanListener {

    private static final Logger logger = LoggerFactory.getLogger(RepaymentPlanOverdueNotificationListener.class);

    private RepaymentPlanOverdueEvent event = null;

    @Async
    @Override
    public void onApplicationEvent(RepaymentPlanEvent event) {
        if (event == null || !(event instanceof RepaymentPlanOverdueEvent)) {
            return ;
        }
        this.event = (RepaymentPlanOverdueEvent) event;
        this.sendOverdueReminderToCustomer();
    }

    /**
     * 逾期提醒
     */
    private void sendOverdueReminderToCustomer() {
        String url = this.customerWxConfigBean.getMpContractListUrl();
        List<WxMpTemplateData> templateData = new ArrayList<>();
        templateData.add(new WxMpTemplateData(WechatConstants.TEMPLATE_MESSAGE_PARAM_FIRST, this.templateMessageConfigBean.getRepaymentOverdueCustomerNtfTitle()));
        templateData.add(new WxMpTemplateData(WechatConstants.TEMPLATE_MESSAGE_PARAM_KEYWORD1, dateFormat.format(this.event.getRefundDate())));
        templateData.add(new WxMpTemplateData(WechatConstants.TEMPLATE_MESSAGE_PARAM_KEYWORD2, String.format(this.templateMessageConfigBean.getDays(), String.valueOf(this.event.getOverdueDays()))));
        templateData.add(new WxMpTemplateData(WechatConstants.TEMPLATE_MESSAGE_PARAM_KEYWORD3, String.format(this.templateMessageConfigBean.getRepaymentOverdueAmount(),
                NumberFormatorUtils.convertFinanceNumberToShowString(this.event.getTotalAmountByToday()), NumberFormatorUtils.convertFinanceNumberToShowString(this.event.getOverdueFeeByToday()))));
        templateData.add(new WxMpTemplateData(WechatConstants.TEMPLATE_MESSAGE_PARAM_REMARK, this.templateMessageConfigBean.getRepaymentOverdueCustomerNtfRemark()));
        super.sendTemplateMessage(this.event.getCustomerId(), null, this.customerWxConfigBean.getAppid(), WeixinTemplateMessagesConfgBean.TEMPLATE_REPAYMENT_OVERDUE, url, templateData);
    }
}
