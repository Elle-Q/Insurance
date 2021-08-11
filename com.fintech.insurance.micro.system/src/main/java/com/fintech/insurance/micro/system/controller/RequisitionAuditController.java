package com.fintech.insurance.micro.system.controller;


import com.fintech.insurance.commons.enums.AuditStatus;
import com.fintech.insurance.commons.enums.EntityType;
import com.fintech.insurance.commons.enums.NotificationEvent;
import com.fintech.insurance.commons.enums.RequisitionStatus;
import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.BaseFintechController;
import com.fintech.insurance.micro.api.system.EntityAuditLogServiceAPI;
import com.fintech.insurance.micro.dto.biz.AuditLogVO;
import com.fintech.insurance.micro.dto.biz.OperationRecordVO;
import com.fintech.insurance.micro.dto.biz.RequisitionVO;
import com.fintech.insurance.micro.dto.system.EntityAuditLogVO;
import com.fintech.insurance.micro.dto.system.QueryAuditLogVO;
import com.fintech.insurance.micro.dto.thirdparty.sms.SMSSendParamVO;
import com.fintech.insurance.micro.dto.thirdparty.sms.SMSSendResultVO;
import com.fintech.insurance.micro.feign.biz.RequisitionServiceFeign;
import com.fintech.insurance.micro.feign.thirdparty.sms.SMSServiceFeign;
import com.fintech.insurance.micro.system.persist.entity.EntityAuditLog;
import com.fintech.insurance.micro.system.service.EntityAuditLogService;
import com.fintech.insurance.micro.system.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 审核记录
 *
 * @author qxy
 * @since 2017-11-15 11:56
 */
@RestController
public class RequisitionAuditController extends BaseFintechController implements EntityAuditLogServiceAPI,ApplicationContextAware ,EnvironmentAware {

    private static final Logger logger = LoggerFactory.getLogger(RequisitionAuditController.class);

    @Autowired
    protected EntityAuditLogService entityAuditLogService;

    @Autowired
    protected RequisitionServiceFeign requisitionServiceFeign;

    @Autowired
    protected UserService userService;

    @Autowired
    private SMSServiceFeign smsServiceFeign;

    private ApplicationContext applicationContext;

    private Environment environment;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }


    /**
     * 审核
     * @param operationrRecordVO
     */
    @Override
    public FintechResponse<VoidPlaceHolder> auditRequsition(@RequestBody OperationRecordVO operationrRecordVO) {
        //参数校验
        if (null == operationrRecordVO.getId() || StringUtils.isEmpty(operationrRecordVO.getOperationRemark()) || StringUtils.isEmpty(operationrRecordVO.getAuditStatus())) {
            throw new FInsuranceBaseException(102001);
        }
        //获取当前登录用户id
        Integer currentLoginUserId = getCurrentLoginUserId();
        FintechResponse<RequisitionVO> requisitionVOResponse = requisitionServiceFeign.getRequisitionById(operationrRecordVO.getId());
        if (requisitionVOResponse == null || requisitionVOResponse.getData() == null) {//申请单是否存在
            logger.error("null requisition with id[" + operationrRecordVO.getId() + "]");
            throw new FInsuranceBaseException(104534);
        }
        if (!requisitionVOResponse.getData().getRequisitionStatus().equals(RequisitionStatus.Auditing.getCode())) {//判断当前的状态是否为审核中的状态
            logger.error("current requisition with id[" + operationrRecordVO.getId() + "] who's status is :" + requisitionVOResponse.getData().getRequisitionStatus() + "mismatch 'auditing' ");
            throw new FInsuranceBaseException(101507);
        }
        operationrRecordVO.setEntityType(EntityType.REQUISITION.getCode());
        //在判断当前待审核的批次中是否有当前用户的审核记录
        EntityAuditLog auditLog = entityAuditLogService.getUserAuditLogByBatchAndEntityIdAndStatus(requisitionVOResponse.getData().getLatestAuditBatch(),
                operationrRecordVO.getId(), EntityType.REQUISITION.getCode(), currentLoginUserId, AuditStatus.PENDING.getCode()) ;
        if (auditLog == null) {
            logger.error("there is no auditLog of requisition[" + operationrRecordVO.getId() + "] for current loginUser[" + currentLoginUserId + "]");
            throw new FInsuranceBaseException(101508);
        }
        String result = entityAuditLogService.audit(requisitionVOResponse.getData(), auditLog, operationrRecordVO.getAuditStatus(), operationrRecordVO.getOperationRemark(), currentLoginUserId);
        // 审核通过发送短信给渠道和客户
        if (result.equals(AuditStatus.PASS.getCode())) {
            this.sendAuditSMS(requisitionVOResponse.getData());
        }

        return FintechResponse.responseData(VoidPlaceHolder.instance());
    }

    @Override
    public FintechResponse<VoidPlaceHolder> save(@RequestBody AuditLogVO auditLogVO) {
        if (auditLogVO != null) {
            this.entityAuditLogService.save(auditLogVO);
        }
        return FintechResponse.responseData(VoidPlaceHolder.instance());
    }

    @Override
    public  FintechResponse<List<EntityAuditLogVO>> listAuditLogsByCurrentUserAnd(@RequestBody QueryAuditLogVO queryAuditLogVO) {
        List<EntityAuditLogVO> entityAuditLogVOS = entityAuditLogService.listAuditLogsByCurrentUserAnd(queryAuditLogVO.getEntityType(),
                queryAuditLogVO.getRequisitionIdList(), queryAuditLogVO.getCurrentLoginUserId(), queryAuditLogVO.getAuditStatus(),
                queryAuditLogVO.getRequisitionBatchList());
        return FintechResponse.responseData(entityAuditLogVOS);
    }

    @Override
    public FintechResponse<String> getRemark(String entityType, Integer entityId, String auditStatus) {
        return FintechResponse.responseData(entityAuditLogService.getRemark(entityType, entityId, auditStatus));
    }

    @Override
    public FintechResponse<String> getRemarkByEntityIdAndSome(Integer id, String entityType, String latestAuditBatch, String auditStatus) {
        return FintechResponse.responseData(entityAuditLogService.getRemarkByEntityIdAndSome(id, entityType, latestAuditBatch, auditStatus));
    }

    public void sendAuditSMS(RequisitionVO requisitionVO){
        // 审核通过发送短息给渠道
        SMSSendParamVO smsSendParamVO = new SMSSendParamVO();
        smsSendParamVO.setEvent(NotificationEvent.AUDIT_NOTIFICATION_FOR_CHANNEL);
        smsSendParamVO.setPhoneNumbers(new String[] {requisitionVO.getChannelUserMobile()});
        Map<String,String> map = new HashMap<String, String>();
        map.put("customerName","【" + requisitionVO.getEnterpriseName() + "】");
        map.put("requisitionNumber",requisitionVO.getRequisitionNumber());
        smsSendParamVO.setSmsParams(map);
        FintechResponse<SMSSendResultVO> smsSendResultVOFintechResponse = smsServiceFeign.sendSMS(smsSendParamVO);
        if (!smsSendResultVOFintechResponse.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(smsSendResultVOFintechResponse);
        }

        // 审核通过发送短信给客户
        SMSSendParamVO smsSendParamVO1 = new SMSSendParamVO();
        smsSendParamVO1.setPhoneNumbers(new String[] {requisitionVO.getCustomerMobile()});
        smsSendParamVO1.setEvent(NotificationEvent.AUDIT_NOTIFICATION_FOR_CUSTOMER);
        Map<String,String> map1 = new HashMap<String, String>();
        map1.put("requisitionNumber",requisitionVO.getRequisitionNumber());
        smsSendParamVO1.setSmsParams(map1);
        FintechResponse<SMSSendResultVO> smsSendResultVOFintechResponse1 = smsServiceFeign.sendSMS(smsSendParamVO1);
        if (!smsSendResultVOFintechResponse1.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(smsSendResultVOFintechResponse);
        }
    }
}
