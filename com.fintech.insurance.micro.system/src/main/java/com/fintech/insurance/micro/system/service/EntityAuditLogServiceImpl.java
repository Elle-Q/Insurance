package com.fintech.insurance.micro.system.service;

import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.enums.*;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.micro.dto.biz.AuditLogVO;
import com.fintech.insurance.micro.dto.biz.RequisitionStatusVO;
import com.fintech.insurance.micro.dto.biz.RequisitionVO;
import com.fintech.insurance.micro.dto.system.EntityAuditLogVO;
import com.fintech.insurance.micro.feign.biz.RequisitionServiceFeign;
import com.fintech.insurance.micro.feign.finance.PaymentOrderServiceFeign;
import com.fintech.insurance.micro.system.persist.dao.EntityAuditLogDao;
import com.fintech.insurance.micro.system.persist.dao.EntityOperationLogDao;
import com.fintech.insurance.micro.system.persist.dao.RoleDao;
import com.fintech.insurance.micro.system.persist.dao.UserDao;
import com.fintech.insurance.micro.system.persist.entity.EntityAuditLog;
import com.fintech.insurance.micro.system.persist.entity.EntityOperationLog;
import com.fintech.insurance.micro.system.persist.entity.Role;
import com.fintech.insurance.micro.system.persist.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class EntityAuditLogServiceImpl implements EntityAuditLogService {

    private static final Logger logger = LoggerFactory.getLogger(EntityAuditLogService.class);

    @Autowired
    private EntityAuditLogDao entityAuditLogDao;

    @Autowired
    private EntityOperationLogDao entityOperationLogDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private UserService userService;

    @Autowired
    private RequisitionServiceFeign requisitionServiceFeign;

    @Autowired
    private PaymentOrderServiceFeign paymentOrderServiceFeign;

    @Override
    @Transactional(readOnly = true)
    public EntityAuditLog getUserAuditLogByBatchAndEntityIdAndStatus(String latestAuditBatch, Integer entityId, String entityType, Integer userId, String auditStatus) {
        return entityAuditLogDao.getUserAuditLogByBatchAndEntityIdAndStatus(latestAuditBatch, entityId, entityType, userId, auditStatus);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String audit(RequisitionVO requisitionVO, EntityAuditLog entityAuditLog, String auditStatus, String operationRemark, Integer currentLoginUserID) {
        User loginUser = userDao.getById(currentLoginUserID);

        AuditStatus status = AuditStatus.codeOf(auditStatus);
        entityAuditLog.setAuditStatus(auditStatus);
        entityAuditLog.setAuditTime(new Date());
        entityAuditLog.setAuditRemark(operationRemark);
        //设置公共字段
        entityAuditLog.setUpdateAt(new Date());
        entityAuditLog.setUpdateBy(currentLoginUserID);
        entityAuditLogDao.save(entityAuditLog);//更新审核记录

        List<EntityAuditLog> pendingAuditLogs = entityAuditLogDao.getAuditByEntityIdAndStatusAndBatch(EntityType.REQUISITION.getCode(), requisitionVO.getId(), requisitionVO.getLatestAuditBatch(), AuditStatus.PENDING.getCode());//所有待审核的记录
        if (status == AuditStatus.REJECTED) {//审核拒绝
            for (EntityAuditLog e : pendingAuditLogs) {//审核拒绝删除该申请单下所有的pending状态的审核记录
                entityAuditLogDao.delete(e);
            }
            logger.debug("audit result is :" + status.getDesc() + " and change the requisition Status to :" + RequisitionStatus.Rejected.getDesc());
            //业务订单状态变为已退回
            requisitionServiceFeign.changeRequisitionStatus(new RequisitionStatusVO(requisitionVO.getId(), RequisitionStatus.Rejected.getCode()));

            this.saveOperationLog(loginUser, operationRemark, requisitionVO, auditStatus);

            return AuditStatus.REJECTED.getCode();

        } else if (status == AuditStatus.PASS) {//审核通过
            /*Set<Role> loginUserRoles = loginUser.getRoleSet();//当前登录用户的角色
            Set<Role> successUserRoles = new HashSet<>();//审核成功用户的角色
            successUserRoles.addAll(loginUserRoles);*/

            Set<Role> successUserRoles = new HashSet<>();//审核成功用户的角色

            List<EntityAuditLog> successLogs = entityAuditLogDao.getAuditByEntityIdAndStatusAndBatch(EntityType.REQUISITION.getCode(), requisitionVO.getId(), requisitionVO.getLatestAuditBatch(), AuditStatus.PASS.getCode());//所有审核通过的记录
            for (EntityAuditLog e : successLogs) {//当前审核通过记录中所有用户的角色
                successUserRoles.addAll(e.getUser().getRoleSet());
            }

            // 检查带审批日志， 如果某审批日志需要一个未审批过的角色来审批， 则保留， 否则删除
            if (null != pendingAuditLogs && pendingAuditLogs.size() > 0) {
                Role operatorRole = roleDao.getByCode(RoleType.OPERATOR.getCode());
                Role riskerRole = roleDao.getByCode(RoleType.RISKER.getCode());
                Role leaderRole = roleDao.getByCode(RoleType.LEADER.getCode());

                for (EntityAuditLog e : pendingAuditLogs) {
                    Set<Role> otherUserRoles = e.getUser().getRoleSet();
                    Set<Role> resultSet = diff(successUserRoles, otherUserRoles);
                    if (!(resultSet.contains(operatorRole) || resultSet.contains(riskerRole) || resultSet.contains(leaderRole))) {
                        entityAuditLogDao.delete(e);
                    }
                }
            }
            this.saveOperationLog(loginUser, operationRemark, requisitionVO, auditStatus);

            List<EntityAuditLog> pendingAuditLogsAfter = entityAuditLogDao.getAuditByEntityIdAndStatusAndBatch(EntityType.REQUISITION.getCode(), requisitionVO.getId(), requisitionVO.getLatestAuditBatch(), AuditStatus.PENDING.getCode());//所有待审核的记录
            if (null == pendingAuditLogsAfter || pendingAuditLogsAfter.size() < 1) { //没有待审核的记录
                logger.debug("audit result is :" + status.getDesc() + " and change the requisition Status to :" + RequisitionStatus.WaitingPayment.getDesc());

                //生成支付订单
                FintechResponse<Integer> paymentOrderResponse = paymentOrderServiceFeign.savePaymentOrder(requisitionVO.getRequisitionNumber());
                if (!paymentOrderResponse.isOk()) {
                    throw FInsuranceBaseException.buildFromErrorResponse(paymentOrderResponse);
                }
                logger.info("generate Payment Order success:{}, for requestion: {}", paymentOrderResponse.getData(), requisitionVO.getRequisitionNumber());

                //业务订单状态变为待支付
                FintechResponse<VoidPlaceHolder> requsitionStatusResponse = requisitionServiceFeign.changeRequisitionStatus(
                        new RequisitionStatusVO(requisitionVO.getId(), RequisitionStatus.WaitingPayment.getCode()));

                if (!requsitionStatusResponse.isOk()) {
                    throw FInsuranceBaseException.buildFromErrorResponse(requsitionStatusResponse);
                }
                logger.info("requestion: {} had changed to waiting payment!", requisitionVO.getRequisitionNumber());

                return AuditStatus.PASS.getCode();
            }
        }
        //保存操作记录
        return AuditStatus.PENDING.getCode();
    }

    /**
     * 取两角色Set差集(减法)
     * Example:
     * src={1,2,3},dest={2,4,5},src-dest={1，3}
     * diff(dest,src)={1，3}
     */
    public static <T> Set<T> diff(Set<T> dest, Set<T> src) {
        Set<T> set = new HashSet<T>(src.size());
        set.addAll(src);
        set.removeAll(dest);
        return set;
    }

    //保存操作记录
    private void saveOperationLog(User loginUser, String operationRemark, RequisitionVO requisitionVO, String auditStatus) {
        EntityOperationLog entityOperationLog = new EntityOperationLog();
        entityOperationLog.setUser(loginUser);
        entityOperationLog.setEntityType(EntityType.REQUISITION.getCode());
        entityOperationLog.setEntityId(requisitionVO.getId());
        entityOperationLog.setOperationType(OperationType.AUDIT.getCode());
        entityOperationLog.setOperationRemark(AuditStatus.codeOf(auditStatus).getDesc() + ',' + operationRemark);
        entityOperationLog.setCreateBy(loginUser.getId());
        entityOperationLogDao.save(entityOperationLog);
    }

    @Override
    @Transactional(readOnly = true)
    public EntityAuditLog getEntityAuditLogById(Integer id) {
        return this.entityAuditLogDao.getById(id);
    }

    /**
     * 保存审核记录
     * @param auditLogVO
     */
    public void save(AuditLogVO auditLogVO) {
        if (auditLogVO != null) {
            User user = this.userDao.getById(auditLogVO.getUserId());
            if (user != null) {
                EntityAuditLog auditLog = null;
                if (auditLogVO.getId() != null) {
                    auditLog = this.getEntityAuditLogById(auditLogVO.getId());
                }
                if (auditLog == null) {
                    auditLog = new EntityAuditLog();
                    auditLog.setBatchNumber(auditLogVO.getBatchNumber());
                    auditLog.setEntityId(auditLogVO.getEntityId());
                    auditLog.setEntityType(auditLogVO.getEntityType());
                    auditLog.setAuditStatus(AuditStatus.PENDING.getCode());
                    auditLog.setCreateAt(new Date());
                    auditLog.setCreateBy(auditLogVO.getCreateBy());
                } else {
                    auditLog.setUpdateBy(auditLogVO.getCreateBy());
                    if (AuditStatus.PENDING.getCode().equalsIgnoreCase(auditLog.getAuditStatus()) && (AuditStatus.PASS.getCode().equalsIgnoreCase(auditLogVO.getAuditStatus()) || AuditStatus.REJECTED.getCode().equalsIgnoreCase(auditLogVO.getAuditStatus()))) {
                        auditLog.setAuditTime(new Date());
                    }
                    auditLog.setAuditStatus(auditLogVO.getAuditStatus());
                    auditLog.setAuditRemark(auditLogVO.getAuditRemark());
                }
                auditLog.setUser(user);
                auditLog.setUpdateAt(new Date());
                this.entityAuditLogDao.save(auditLog);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<EntityAuditLogVO> listAuditLogsByCurrentUserAnd(String entityType, List<Integer> requisitionIdList, Integer currentLoginUserId, String auditStatus, List<String> requisitionBatchList) {
        List<EntityAuditLog> entityAuditLogList = entityAuditLogDao.listAuditLogsByCurrentUserAnd(entityType, requisitionIdList, currentLoginUserId, auditStatus, requisitionBatchList);
        List<EntityAuditLogVO> entityAuditLogVOS = new ArrayList<>();
        if (null != entityAuditLogList && entityAuditLogList.size() > 0) {
            for (EntityAuditLog e : entityAuditLogList) {
                EntityAuditLogVO entityAuditLogVO = this.entityToVO(e);
                entityAuditLogVOS.add(entityAuditLogVO);
            }
        }
        if (entityAuditLogList.size() < 1) {
            return Collections.emptyList();
        } else {
            return entityAuditLogVOS;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public String getRemark(String entityType, Integer entityId, String auditStatus) {
        List<EntityAuditLog> entityAuditLogList = entityAuditLogDao.getByEntityTypeAndEntityIdAndAuditStatus(entityType, entityId, auditStatus);

/*        StringBuilder sb = new StringBuilder();

        if (entityAuditLogList != null && entityAuditLogList.size() > 0) {
            for (EntityAuditLog log : entityAuditLogList) {
                sb.append(log.getAuditRemark());
            }
        }*/
        String latestRemark = entityAuditLogList.get(0).getAuditRemark();
        Date firstDate = entityAuditLogList.get(0).getCreateAt();
        for (EntityAuditLog log : entityAuditLogList) {
            if (log.getCreateAt().after(firstDate)){
                latestRemark = log.getAuditRemark();
            }
        }
        return latestRemark;
    }

    @Override
    @Transactional(readOnly = true)
    public String getRemarkByEntityIdAndSome(Integer id, String entityType, String latestAuditBatch, String auditStatus) {
        EntityAuditLog entityAuditLog = entityAuditLogDao.getRemarkByEntityIdAndSome(id, entityType, latestAuditBatch, auditStatus);
        if (null == entityAuditLog) {
            return null;
        }
        return entityAuditLog.getAuditRemark();
    }

    //实体转VO
    private EntityAuditLogVO entityToVO(EntityAuditLog e) {
        EntityAuditLogVO vo = new EntityAuditLogVO();
        vo.setId(e.getId());
        vo.setEntityId(e.getEntityId());
        return vo;
    }
}
