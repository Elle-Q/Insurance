package com.fintech.insurance.micro.biz.controller;

import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.BaseFintechController;
import com.fintech.insurance.micro.api.biz.BizNotificationServiceAPI;
import com.fintech.insurance.micro.biz.service.ContractService;
import com.fintech.insurance.micro.biz.service.RequisitionService;
import com.fintech.insurance.micro.biz.service.notification.NotificationService;
import com.fintech.insurance.micro.dto.biz.ContractVO;
import com.fintech.insurance.micro.dto.biz.NotificationRequestVO;
import com.fintech.insurance.micro.dto.biz.NotificationResponseVO;
import com.fintech.insurance.micro.dto.biz.RequisitionVO;
import com.fintech.insurance.micro.dto.finance.FinanceRepaymentPlanVO;
import com.fintech.insurance.micro.feign.finance.RepaymentPlanServiceFeign;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: (some words)
 * @Author: East
 * @Date: 2017/11/15 0015 19:20
 */
@RestController
public class BizNotificationController extends BaseFintechController implements BizNotificationServiceAPI {

    private static final Logger LOG = LoggerFactory.getLogger(BizNotificationController.class);

    @Autowired
    private RequisitionService requisitionService;

    @Autowired
    private ContractService contractService;

    @Autowired
    private RepaymentPlanServiceFeign repaymentPlanServiceFeign;

    @Autowired
    private NotificationService notificationService;

    @Override
    public FintechResponse<NotificationResponseVO> sendAuditNotification(@RequestBody(required = true) NotificationRequestVO notificationRequestVO) {
        //校验参数
        if (notificationRequestVO == null || notificationRequestVO.getRequisitionId() == null) {
            LOG.error("Null notificationRequestVO or null requisitionId of notificationRequestVO!");
            throw new FInsuranceBaseException(104901);
        }
        Integer requisitionId = notificationRequestVO.getRequisitionId();

        //Requisition info
        RequisitionVO requisitionVO = this.checkRequisition(requisitionId);

        //Send notification
        notificationService.sendAuditNotification(requisitionVO);

        return FintechResponse.responseOk();
    }


    private RequisitionVO checkRequisition(Integer requisitionId) {
        //校验业务申请ID
        if (requisitionId == null) {
            LOG.error("Null requisitionId!");
            throw new FInsuranceBaseException(104903);
        }
        //校验业务申请单
        RequisitionVO requisitionVO = requisitionService.getRequisitionById(requisitionId);
        if (requisitionVO == null) {
            LOG.error("Unmatched requisitionId [" + requisitionId + "] of Requisition!");
            throw new FInsuranceBaseException(104904, new Object[]{requisitionId});
        }
        //校验业务申请单的渠道方手机号码
        String channelUserMobile = requisitionVO.getChannelUserMobile();
        if (StringUtils.isBlank(channelUserMobile)) {
            LOG.error("Null mobile of channel user with [" + requisitionId + "] of Requisition!");
            throw new FInsuranceBaseException(104905, new Object[]{requisitionId});
        }
        //校验业务申请单的客户手机号码
        String customerMobile = requisitionVO.getCustomerMobile();
        if (StringUtils.isBlank(customerMobile)) {
            LOG.error("Null mobile of customer with [" + requisitionId + "] of Requisition!");
            throw new FInsuranceBaseException(104906, new Object[]{requisitionId});
        }

        return requisitionVO;
    }

    @Override
    public FintechResponse<NotificationResponseVO> sendLoanConfirmNotification(@RequestBody(required = true) NotificationRequestVO notificationRequestVO) {
        //校验参数
        if (notificationRequestVO == null || notificationRequestVO.getRequisitionId() == null) {
            LOG.error("Null notificationRequestVO or null requisitionId of notificationRequestVO!");
            throw new FInsuranceBaseException(104901);
        }
        Integer requisitionId = notificationRequestVO.getRequisitionId();

        //Requisition info
        RequisitionVO requisitionVO = this.checkRequisition(requisitionId);

        //Send notification
        notificationService.sendLoanConfirmNotification(requisitionVO);

        return FintechResponse.responseOk();
    }

    @Override
    public FintechResponse<NotificationResponseVO> sendRepaymentRemindNotification(@RequestBody(required = true) NotificationRequestVO notificationRequestVO) {
        //校验参数
        if (notificationRequestVO == null || notificationRequestVO.getContractId() == null || notificationRequestVO.getRepaymentPlanId() == null) {
            LOG.error("Null notificationRequestVO or null contractId and repaymentPlanId of notificationRequestVO!");
            throw new FInsuranceBaseException(104902);
        }
        Integer contractId = notificationRequestVO.getContractId();
        Integer repaymentPlanId = notificationRequestVO.getRepaymentPlanId();

        //Contract info
        ContractVO contractVO = this.checkContract(contractId);
        //RepaymentPlan info
        FinanceRepaymentPlanVO repaymentPlanVO = this.checkRepaymentPlan(repaymentPlanId);

        //Send notification
        notificationService.sendOverdueRemindNotification(contractVO, repaymentPlanVO);

        return FintechResponse.responseOk();
    }

    private ContractVO checkContract(Integer contractId) {
        //校验合同ID
        if (contractId == null) {
            LOG.error("Null contractId!");
            throw new FInsuranceBaseException(104907);
        }
        //校验合同
        ContractVO contractVO = contractService.getContractById(contractId);
        if (contractVO == null) {
            LOG.error("Unmatched contractId [" + contractId + "] of Contract!");
            throw new FInsuranceBaseException(104908, new Object[]{contractId});
        }
        //校验合同的客户手机号码
        String customerMobile = contractVO.getCustomerMobile();
        if (StringUtils.isBlank(customerMobile)) {
            LOG.error("Null mobile of customer with [" + contractId + "] of Contract!");
            throw new FInsuranceBaseException(104910, new Object[]{contractId});
        }
        //校验合同的渠道方手机号码
        String channelMobile = contractVO.getChannelUserMobile();
        if (StringUtils.isBlank(channelMobile)) {
            LOG.error("Null mobile of channel with [" + contractId + "] of Contract!");
            throw new FInsuranceBaseException(104909, new Object[]{contractId});
        }

        return contractVO;
    }

    private FinanceRepaymentPlanVO checkRepaymentPlan(Integer repaymentPlanId) {
        //校验
        if (repaymentPlanId == null) {
            LOG.error("Null repaymentPlanId!");
            throw new FInsuranceBaseException(104911);
        }
        //校验
        FintechResponse<FinanceRepaymentPlanVO> repaymentPlanResp = repaymentPlanServiceFeign.getRepaymentPlan(repaymentPlanId);
        if (!repaymentPlanResp.isOk()) {
            LOG.error("Unmatched repaymentPlanId [" + repaymentPlanId + "] of RepaymentPlan!");
            throw new FInsuranceBaseException(104108);
        }
        //校验
        FinanceRepaymentPlanVO repaymentPlanVO = repaymentPlanResp.getData();
        if (repaymentPlanVO == null) {
            LOG.error("Null response for request RepaymentPlan with repaymentPlanId [" + repaymentPlanId + "]!");
            throw new FInsuranceBaseException(104913, new Object[]{repaymentPlanId});
        }

        return repaymentPlanVO;
    }

    @Override
    public FintechResponse<NotificationResponseVO> sendOverdueRemindNotification(@RequestBody(required = true) NotificationRequestVO notificationRequestVO) {
        //校验参数
        if (notificationRequestVO == null || notificationRequestVO.getContractId() == null || notificationRequestVO.getRepaymentPlanId() == null) {
            LOG.error("Null notificationRequestVO or null contractId and repaymentPlanId of notificationRequestVO!");
            throw new FInsuranceBaseException(104904,new Object[]{notificationRequestVO.getRequisitionId()});
        }
        Integer contractId = notificationRequestVO.getContractId();
        Integer repaymentPlanId = notificationRequestVO.getRepaymentPlanId();

        //Contract info
        ContractVO contractVO = this.checkContract(contractId);
        //RepaymentPlan info
        FinanceRepaymentPlanVO repaymentPlanVO = this.checkRepaymentPlan(repaymentPlanId);

        //Send notification
        notificationService.sendOverdueRemindNotification(contractVO, repaymentPlanVO);

        return FintechResponse.responseOk();
    }

    @Override
    public FintechResponse<NotificationResponseVO> sendRepaymentResultNotification(@RequestBody(required = true) NotificationRequestVO notificationRequestVO) {
        //校验参数
        if (notificationRequestVO == null || notificationRequestVO.getContractId() == null || notificationRequestVO.getRepaymentPlanId() == null) {
            LOG.error("Null notificationRequestVO or null contractId and repaymentPlanId of notificationRequestVO!");
            throw new FInsuranceBaseException(104904, new Object[]{notificationRequestVO.getRequisitionId()});
        }
        Integer contractId = notificationRequestVO.getContractId();
        Integer repaymentPlanId = notificationRequestVO.getRepaymentPlanId();

        //Contract info
        ContractVO contractVO = this.checkContract(contractId);
        //RepaymentPlan info
        FinanceRepaymentPlanVO repaymentPlanVO = this.checkRepaymentPlan(repaymentPlanId);

        //Send notification
        notificationService.sendRepaymentResultNotification(contractVO, repaymentPlanVO);

        return FintechResponse.responseOk();
    }
}
