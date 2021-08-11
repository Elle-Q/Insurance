package com.fintech.insurance.micro.biz.event;

import com.fintech.insurance.commons.beans.WeixinTemplateMessagesConfgBean;
import com.fintech.insurance.commons.constants.WechatConstants;
import com.fintech.insurance.commons.enums.RequisitionStatus;
import com.fintech.insurance.commons.utils.NumberFormatorUtils;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.micro.biz.persist.entity.Requisition;
import com.fintech.insurance.micro.dto.customer.CustomerVO;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 放款成功监听器
 */
@Component
public class RequisitionLoanedListener extends RequisitionLifeCycleListenerAdaptor {

    private static final Logger logger = LoggerFactory.getLogger(RequisitionLoanedListener.class);

    @Override
    boolean canProcess(RequisitionLifeCycleEvent event) {
        //仅支持放款成功的事件
        return RequisitionStatus.Loaned.getCode().equalsIgnoreCase(event.getNewStatus());
    }

    @Override
    void onRequisitionLoaned(RequisitionLifeCycleEvent event) {
        Requisition requisition = event.getRequisition();
        //获得客户id
        Integer customerId = requisition.getCustomerId();
        //获得渠道用户id
        Integer userId = null;
        if (requisition.getChannelApplication() != null && requisition.getChannelApplication()) {
            userId = requisition.getChannelUserId();
        }
        logger.info("requisition with id " + requisition.getId() + " has been approved, and the weixin template message notification will be sent to following users: \n");
        logger.info("customer account with id " + customerId + "\n");
        logger.info("channel user with id " + userId + "\n");
        //分别对客户和渠道用户发送推送或者短信或者发送其他信息
        if (customerId != null) {
            this.sendRequisitionLoanedWxNotifitcationToCustomer(customerId);
        }
        if (userId != null) {
            this.sendRequisitionLoanedWxNotificationToChannel(userId);
        }
    }

    /**
     * 放款成功提醒
     * 向客户发送已放款通知
     * @param customerId
     */
    private void sendRequisitionLoanedWxNotifitcationToCustomer(Integer customerId) {
        //获得借款人信息
        FintechResponse<CustomerVO> customerResponse = this.customerServiceClient.getCustomerAccountInfoById(this.event.getRequisition().getCustomerAccountInfoId());
        if (customerResponse != null && customerResponse.isOk() && customerResponse.getData() != null) {
            String url = this.customerWxConfigBean.getRequisitionDetailUrl() + this.event.getRequisition().getId();
            List<WxMpTemplateData> templateData = new ArrayList<>();
            templateData.add(new WxMpTemplateData(WechatConstants.TEMPLATE_MESSAGE_PARAM_FIRST, this.templateMessageConfigBean.getRequisitionLoanedCustomerNtfTitle()));
            templateData.add(new WxMpTemplateData(WechatConstants.TEMPLATE_MESSAGE_PARAM_KEYWORD1, this.event.getRequisition().getRequisitionNumber()));
            templateData.add(new WxMpTemplateData(WechatConstants.TEMPLATE_MESSAGE_PARAM_KEYWORD2, customerResponse.getData().getName()));
            templateData.add(new WxMpTemplateData(WechatConstants.TEMPLATE_MESSAGE_PARAM_KEYWORD3, String.format(this.templateMessageConfigBean.getAmount(), NumberFormatorUtils.convertFinanceNumberToShowString(this.event.getRequisition().getTotalApplyAmount().doubleValue()))));
            String bankAccountNumber = this.event.getRequisition().getLoanAccountNumber();
            templateData.add(new WxMpTemplateData(WechatConstants.TEMPLATE_MESSAGE_PARAM_KEYWORD4, String.format(this.templateMessageConfigBean.getBankcardSuffix(), StringUtils.isEmpty(bankAccountNumber) ? "" : bankAccountNumber.substring(bankAccountNumber.length() - 4))));
            templateData.add(new WxMpTemplateData(WechatConstants.TEMPLATE_MESSAGE_PARAM_REMARK, this.templateMessageConfigBean.getRequisitionLoanedCustomerNtfRemark()));
            super.sendTemplateMessage(customerId, null, this.customerWxConfigBean.getAppid(), WeixinTemplateMessagesConfgBean.TEMPLATE_LOAN_SUCCESS, url, templateData);
        } else {
            logger.error("Fail to get customer account info for requisition with id " + this.event.getRequisition().getId());
        }
    }

    /**
     * 放款成功提醒
     * 向渠道用户发送已放款通知
     * @param userId
     */
    private void sendRequisitionLoanedWxNotificationToChannel(Integer userId) {
        //获得借款人信息
        FintechResponse<CustomerVO> customerResponse = this.customerServiceClient.getCustomerAccountInfoById(this.event.getRequisition().getCustomerAccountInfoId());
        if (customerResponse != null && customerResponse.isOk() && customerResponse.getData() != null) {
            String url = this.clientWxConfigBean.getRequisitionDetailUrl() + this.event.getRequisition().getId();
            List<WxMpTemplateData> templateData = new ArrayList<>();
            templateData.add(new WxMpTemplateData(WechatConstants.TEMPLATE_MESSAGE_PARAM_FIRST, this.templateMessageConfigBean.getRequisitionLoanedChannelNtfTitle()));
            templateData.add(new WxMpTemplateData(WechatConstants.TEMPLATE_MESSAGE_PARAM_KEYWORD1, this.event.getRequisition().getRequisitionNumber()));
            templateData.add(new WxMpTemplateData(WechatConstants.TEMPLATE_MESSAGE_PARAM_KEYWORD2, customerResponse.getData().getName()));
            templateData.add(new WxMpTemplateData(WechatConstants.TEMPLATE_MESSAGE_PARAM_KEYWORD3, String.format(this.templateMessageConfigBean.getAmount(), NumberFormatorUtils.convertFinanceNumberToShowString(this.event.getRequisition().getTotalApplyAmount().doubleValue()))));
            String bankAccountNumber = this.event.getRequisition().getLoanAccountNumber();
            templateData.add(new WxMpTemplateData(WechatConstants.TEMPLATE_MESSAGE_PARAM_KEYWORD4, String.format(this.templateMessageConfigBean.getBankcardSuffix(), StringUtils.isEmpty(bankAccountNumber) ? "" : bankAccountNumber.substring(bankAccountNumber.length() - 4))));
            templateData.add(new WxMpTemplateData(WechatConstants.TEMPLATE_MESSAGE_PARAM_REMARK, this.templateMessageConfigBean.getRequisitionLoanedChannelNtfRemark()));
            super.sendTemplateMessage(null, userId, this.clientWxConfigBean.getAppid(), WeixinTemplateMessagesConfgBean.TEMPLATE_LOAN_SUCCESS, url, templateData);
        } else {
            logger.error("Fail to get customer account info for requisition with id " + this.event.getRequisition().getId());
        }
    }
}
