package com.fintech.insurance.micro.biz.event;

import com.fintech.insurance.commons.beans.WeixinTemplateMessagesConfgBean;
import com.fintech.insurance.commons.constants.WechatConstants;
import com.fintech.insurance.commons.enums.RequisitionStatus;
import com.fintech.insurance.commons.utils.NumberFormatorUtils;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.micro.biz.persist.entity.Requisition;
import com.fintech.insurance.micro.dto.customer.CustomerSimpleVO;
import com.fintech.insurance.micro.dto.finance.PaymentOrderVO;
import com.fintech.insurance.micro.feign.finance.PaymentOrderServiceFeign;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务费支付成功监听器
 */
@Component
public class RequisitionWaitLoanListener extends RequisitionLifeCycleListenerAdaptor {

    private static final Logger logger = LoggerFactory.getLogger(RequisitionWaitLoanListener.class);

    @Autowired
    private PaymentOrderServiceFeign paymentOrderServiceClient;

    @Override
    boolean canProcess(RequisitionLifeCycleEvent event) {
        //仅对服务费支付成功有效
        return RequisitionStatus.WaitingLoan.getCode().equalsIgnoreCase(event.getNewStatus());
    }

    @Override
    void onRequisitionWaitLoan(RequisitionLifeCycleEvent event) {
        Requisition requisition = event.getRequisition();
        //获得客户id
        Integer customerId = requisition.getCustomerId();
        logger.info("requisition with id " + requisition.getId() + " has been approved, and the weixin template message notification will be sent to following users: \n");
        logger.info("customer account with id " + customerId + "\n");
        //获取支付订单信息
        FintechResponse<PaymentOrderVO> paymentOrderResponse = paymentOrderServiceClient.getByOrderNumber(requisition.getPaymentOrderNumber());
        if (!paymentOrderResponse.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(paymentOrderResponse);
        }
        PaymentOrderVO paymentOrderVO = paymentOrderResponse.getData();
        //对客户发送推送或者短信或者发送其他信息
        if (customerId != null && paymentOrderVO != null) {
            this.sendRequisitionWaitLoanWxNotifitcationToCustomer(customerId, paymentOrderVO);
        }
    }

    /**
     * 服务费代扣成功提醒
     * 服务费支付成功之后向客户发送推送
     * @param customerId
     * @Param paymentOrderVO
     */
    private void sendRequisitionWaitLoanWxNotifitcationToCustomer(Integer customerId, PaymentOrderVO paymentOrderVO) {
        String url = this.customerWxConfigBean.getRequisitionDetailUrl() + this.event.getRequisition().getId();
        List<WxMpTemplateData> templateData = new ArrayList<>();
        templateData.add(new WxMpTemplateData(WechatConstants.TEMPLATE_MESSAGE_PARAM_FIRST, this.templateMessageConfigBean.getServiceWithholdSuccessCustomerNtfTitle()));
        templateData.add(new WxMpTemplateData(WechatConstants.TEMPLATE_MESSAGE_PARAM_KEYWORD1, String.format(this.templateMessageConfigBean.getAmount(), NumberFormatorUtils.convertFinanceNumberToShowString(paymentOrderVO.getTotalAmount()))));
        FintechResponse<CustomerSimpleVO> customerSimpleVOResponse = this.customerServiceClient.getCustomerSimpleInfo(this.event.getRequisition().getCustomerId());
        if (customerSimpleVOResponse != null && customerSimpleVOResponse.isOk() && customerSimpleVOResponse.getData() != null) {
            String bankAccountNumber = customerSimpleVOResponse.getData().getBankCardNumber();
            templateData.add(new WxMpTemplateData(WechatConstants.TEMPLATE_MESSAGE_PARAM_KEYWORD2, String.format(this.templateMessageConfigBean.getBankcardSuffix(), this.getBankAccountNumberSuffix(bankAccountNumber))));
            super.sendTemplateMessage(customerId, null, this.customerWxConfigBean.getAppid(), WeixinTemplateMessagesConfgBean.TEMPLATE_WITHHOLD_SUCCESS, url, templateData);
        } else {
            logger.error("Fail to get the customer info according to customer with id " + this.event.getRequisition().getCustomerId() + ", so no notification message will be sent");
        }
    }
}
