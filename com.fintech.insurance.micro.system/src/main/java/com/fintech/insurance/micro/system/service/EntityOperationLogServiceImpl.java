package com.fintech.insurance.micro.system.service;

import com.fintech.insurance.commons.enums.OperationType;
import com.fintech.insurance.micro.dto.biz.OperationRecordVO;
import com.fintech.insurance.micro.dto.system.UserVO;
import com.fintech.insurance.micro.system.persist.dao.EntityAuditLogDao;
import com.fintech.insurance.micro.system.persist.dao.EntityOperationLogDao;
import com.fintech.insurance.micro.system.persist.dao.RoleDao;
import com.fintech.insurance.micro.system.persist.dao.UserDao;
import com.fintech.insurance.micro.system.persist.entity.EntityOperationLog;
import com.fintech.insurance.micro.system.persist.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qxy
 */
@Service
@Transactional
public class EntityOperationLogServiceImpl implements EntityOperationLogService {

    @Autowired
    EntityOperationLogDao entityOperationLogDao;

    @Autowired
    EntityAuditLogDao entityAuditLogDao;

    @Autowired
    UserService userService;

    @Autowired
    UserDao userDao;

    @Autowired
    RoleDao roleDao;

    @Override
    @Transactional(readOnly = true)
    public List<OperationRecordVO> listOperationrRecord(Integer id) {
        List<EntityOperationLog> entityOperationLogList = entityOperationLogDao.listOperationrRecord(id);//操作记录
        List<OperationRecordVO> operationrRecordVOList = new ArrayList<>();
        if (null != entityOperationLogList && entityOperationLogList.size() > 0){
            for (EntityOperationLog entityOperationLog : entityOperationLogList){
                UserVO userVO = userService.getUserById(entityOperationLog.getCreateBy());
                OperationRecordVO operationrRecordVO = this.entityToVO(entityOperationLog, userVO);
                if (null != operationrRecordVO) {
                    operationrRecordVOList.add(operationrRecordVO);
                }
            }
        }
        return operationrRecordVOList;
    }


    @Override
    public void createLog(OperationRecordVO operationrRecordVO) {
        User loginUser = userDao.getById(operationrRecordVO.getUserId());
        //保存操作记录
        EntityOperationLog entityOperationLog = new EntityOperationLog();
        entityOperationLog.setUser(loginUser);
        entityOperationLog.setCreateBy(operationrRecordVO.getUserId());
        entityOperationLog.setEntityType(operationrRecordVO.getEntityType());//实体类型
        entityOperationLog.setEntityId(operationrRecordVO.getEntityId());
        if (StringUtils.isNotEmpty(operationrRecordVO.getOperationType())) {
            entityOperationLog.setOperationType(operationrRecordVO.getOperationType());
        }
        if (StringUtils.isNotEmpty(operationrRecordVO.getOperationRemark())) {
            entityOperationLog.setOperationRemark(operationrRecordVO.getOperationRemark());
        }
        entityOperationLogDao.save(entityOperationLog);
    }

    private OperationRecordVO entityToVO(EntityOperationLog entityOperationLog, UserVO userVO) {

        OperationRecordVO operationrRecordVO = new OperationRecordVO();
        if (null == entityOperationLog){
            return null;
        }
        if (OperationType.REFUND.getCode().equals(entityOperationLog.getOperationType())) {//过滤确认还款的操作记录
            return null;
        }
        if (null != userVO){
            operationrRecordVO.setRoles(userVO.getRoles());
            operationrRecordVO.setMobile(userVO.getMobile());
            operationrRecordVO.setName(userVO.getName());
        }
        operationrRecordVO.setId(entityOperationLog.getId());
        operationrRecordVO.setUserId(entityOperationLog.getUser().getId());
        operationrRecordVO.setEntityId(entityOperationLog.getEntityId());
        operationrRecordVO.setOperationRemark(entityOperationLog.getOperationRemark());
        operationrRecordVO.setOperationType(entityOperationLog.getOperationType());
        operationrRecordVO.setCreateAt(entityOperationLog.getCreateAt());
        operationrRecordVO.setCreateBy(entityOperationLog.getCreateBy());
        return operationrRecordVO;
    }
}
