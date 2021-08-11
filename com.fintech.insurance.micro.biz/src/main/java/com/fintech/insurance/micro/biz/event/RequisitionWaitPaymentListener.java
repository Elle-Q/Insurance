package com.fintech.insurance.micro.biz.event;

import com.fintech.insurance.commons.beans.WeixinTemplateMessagesConfgBean;
import com.fintech.insurance.commons.constants.WechatConstants;
import com.fintech.insurance.commons.enums.RequisitionStatus;
import com.fintech.insurance.commons.utils.NumberFormatorUtils;
import com.fintech.insurance.micro.biz.persist.entity.Requisition;
import com.fintech.insurance.micro.biz.service.RequisitionService;
import com.fintech.insurance.micro.biz.service.notification.NotificationService;
import com.fintech.insurance.micro.dto.biz.RequisitionVO;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 申请单审核通过监听器
 */
@Component
public class RequisitionWaitPaymentListener extends RequisitionLifeCycleListenerAdaptor {

    private static final Logger logger = LoggerFactory.getLogger(RequisitionWaitPaymentListener.class);

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private RequisitionService requisitionService;

    /**
     * 只处理审核通过的事件
     * @param event
     * @return
     */
    @Override
    boolean canProcess(RequisitionLifeCycleEvent event) {
        return RequisitionStatus.WaitingPayment.getCode().equalsIgnoreCase(event.getNewStatus());
    }

    @Override
    void onRequisitionWaitPayment(RequisitionLifeCycleEvent event) {
        Requisition requisition = event.getRequisition();
        //获得客户id
        Integer customerId = requisition.getCustomerId();
        //获得渠道用户id
        Integer userId = requisition.getChannelUserId();

        logger.info("requisition with id " + requisition.getId() + " has been approved, and the weixin template message notification will be sent to following users: \n");
        logger.info("customer account with id " + customerId + "\n");
        logger.info("channel user with id " + userId + "\n");
        //分别对客户和渠道用户发送推送或者短信或者发送其他信息
        if (customerId != null) {
            this.sendRequisitionAuditPassWxNotificationToCustomer(customerId);
        }
        if (userId != null) {
            this.sendRequisitionAuditPassWxNotificationToChannel(userId);
        }
        //发送短信
        RequisitionVO requisitionVO = this.requisitionService.getRequisitionVOById(requisition.getId());
        if (requisitionVO != null) {
            this.notificationService.sendAuditNotification(requisitionVO);
        }
    }

    /**
     * 审核通过，提醒支付
     * 向客户发送申请单已审核通过的模版消息通知
     * @param customerId
     */
    private void sendRequisitionAuditPassWxNotificationToCustomer(Integer customerId) {
        String url = this.customerWxConfigBean.getRequisitionDetailUrl() + this.event.getRequisition().getId();
        List<WxMpTemplateData> templateData = new ArrayList<>();
        templateData.add(new WxMpTemplateData(WechatConstants.TEMPLATE_MESSAGE_PARAM_FIRST, this.templateMessageConfigBean.getRequisitionPassCustomerNtfTitle()));
        templateData.add(new WxMpTemplateData(WechatConstants.TEMPLATE_MESSAGE_PARAM_KEYWORD1, this.event.getRequisition().getRequisitionNumber()));
        templateData.add(new WxMpTemplateData(WechatConstants.TEMPLATE_MESSAGE_PARAM_KEYWORD2, String.format(this.templateMessageConfigBean.getAmount(), NumberFormatorUtils.convertFinanceNumberToShowString(this.event.getRequisition().getTotalApplyAmount().doubleValue()))));
        templateData.add(new WxMpTemplateData(WechatConstants.TEMPLATE_MESSAGE_PARAM_KEYWORD3, this.dateTimeFormat.format(this.event.getRequisition().getSubmissionDate())));
        super.sendTemplateMessage(customerId, null, this.customerWxConfigBean.getAppid(), WeixinTemplateMessagesConfgBean.TEMPLATE_REQUISITION_PASS, url, templateData);
    }

    /**
     * 审核通过，提醒支付
     * 向渠道发送申请单已审核通过的模版消息通知
     * @param channelUserId
     */
    private void sendRequisitionAuditPassWxNotificationToChannel(Integer channelUserId) {
        String url = this.clientWxConfigBean.getRequisitionDetailUrl() + this.event.getRequisition().getId();
        List<WxMpTemplateData> templateData = new ArrayList<>();
        templateData.add(new WxMpTemplateData(WechatConstants.TEMPLATE_MESSAGE_PARAM_FIRST, this.templateMessageConfigBean.getRequisitionPassChannelNtfTitle()));
        templateData.add(new WxMpTemplateData(WechatConstants.TEMPLATE_MESSAGE_PARAM_KEYWORD1, this.event.getRequisition().getRequisitionNumber()));
        templateData.add(new WxMpTemplateData(WechatConstants.TEMPLATE_MESSAGE_PARAM_KEYWORD2, String.format(this.templateMessageConfigBean.getAmount(), NumberFormatorUtils.convertFinanceNumberToShowString(this.event.getRequisition().getTotalApplyAmount().doubleValue()))));
        templateData.add(new WxMpTemplateData(WechatConstants.TEMPLATE_MESSAGE_PARAM_KEYWORD3, this.dateTimeFormat.format(this.event.getRequisition().getSubmissionDate())));
        super.sendTemplateMessage(null, channelUserId, this.clientWxConfigBean.getAppid(), WeixinTemplateMessagesConfgBean.TEMPLATE_REQUISITION_PASS, url, templateData);
    }
}
