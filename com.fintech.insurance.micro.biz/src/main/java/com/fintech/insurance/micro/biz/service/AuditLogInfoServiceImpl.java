package com.fintech.insurance.micro.biz.service;


import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.enums.AuditStatus;
import com.fintech.insurance.commons.enums.EntityType;
import com.fintech.insurance.commons.utils.DateCommonUtils;
import com.fintech.insurance.commons.utils.ExportUtils;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.micro.biz.persist.dao.RequisitionDetailResourceDao;
import com.fintech.insurance.micro.dto.biz.AuditLogVO;
import com.fintech.insurance.micro.dto.system.UserVO;
import com.fintech.insurance.micro.feign.system.EntityAuditLogServiceFeign;
import com.fintech.insurance.micro.feign.system.SysUserServiceFeign;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @Description: (导入导出管理)
 * @Author: yongneng liu
 * @Date: 2017/11/14 0014 16:08
 */
@Async
@Service
public class AuditLogInfoServiceImpl implements AuditLogInfoService {

    private static final Logger LOG = LoggerFactory.getLogger(AuditLogInfoServiceImpl.class);


    @Autowired
    private SysUserServiceFeign sysUserServiceFeign;

    @Autowired
    private EntityAuditLogServiceFeign entityAuditLogServiceFeign;



    @Override
    public void createAuditLogInfo(String userRoleCode, Integer requisitionId, String auditBatchNumber, Integer currentLoginUserId){
        if (requisitionId != null) {
            //获取审核角色的人员
            String roleCode = userRoleCode;
            FintechResponse<List<UserVO>> auditUsersResponse = sysUserServiceFeign.getUsersByRoleCodes(roleCode);
            if (!auditUsersResponse.isOk()) {
                throw FInsuranceBaseException.buildFromErrorResponse(auditUsersResponse);
            }
            List<UserVO> auditUsers = auditUsersResponse.getData();
            if (auditUsers == null || auditUsers.size() == 0) {
                LOG.error("Cannot find any users with auditor role who can audit the submitted requisitions");
            } else {
                //每个用户生成一条审核记录
                for (UserVO auditUser : auditUsers) {
                    AuditLogVO auditLogVO = new AuditLogVO();
                    auditLogVO.setUserId(auditUser.getId());
                    auditLogVO.setBatchNumber(auditBatchNumber);
                    auditLogVO.setEntityType(EntityType.REQUISITION.getCode());
                    auditLogVO.setEntityId(requisitionId);
                    auditLogVO.setAuditStatus(AuditStatus.PENDING.getCode());
                    auditLogVO.setCreateBy(currentLoginUserId);
                    FintechResponse<VoidPlaceHolder> voidPlaceHolderFintechResponse = entityAuditLogServiceFeign.save(auditLogVO);
                    if (!voidPlaceHolderFintechResponse.isOk()) {
                        throw FInsuranceBaseException.buildFromErrorResponse(voidPlaceHolderFintechResponse);
                    }
                }
            }
        }
    }


}
