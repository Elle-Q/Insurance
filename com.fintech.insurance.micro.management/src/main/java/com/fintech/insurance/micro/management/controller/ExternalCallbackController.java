package com.fintech.insurance.micro.management.controller;

import com.fintech.insurance.commons.constants.YjfConstants;
import com.fintech.insurance.components.web.BaseFintechController;
import com.fintech.insurance.components.web.BaseFintechManagementController;
import com.fintech.insurance.micro.dto.finance.DebtNotification;
import com.fintech.insurance.micro.feign.finance.PaymentServiceFeign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 供第三方应用的回调接口，接口前缀需要配置到网关确保不需要鉴权
 * @Author: Yong Li
 * @Date: 2017/12/14 17:13
 */
@RestController
@RequestMapping(value = "/management/ext-callback/",
        consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE})
public class ExternalCallbackController extends BaseFintechManagementController {

    private static final String NOTIFICATION_RECEIVED_RESPONSE = "success";

    private static final Logger LOG = LoggerFactory.getLogger(ExternalCallbackController.class);

    @Autowired
    private PaymentServiceFeign paymentServiceFeign;

    /**
     * 易极付扣款通知接口
     *
     * @param notification
     * @return
     */
    @PostMapping(path = "/yjf/debt")
    public String verifyBankcard(DebtNotification notification) {
        if (!YjfConstants.SERVICE_CODE_DEBT.equals(notification.getService())) {
            LOG.error("Receive the wrong notification: " + notification.toString());
            return null;
        }
        // 处理通知
        paymentServiceFeign.debtNotify(notification);

        LOG.info("Processed Debt Notification: {}", notification.toString());
        return NOTIFICATION_RECEIVED_RESPONSE;
    }


}
