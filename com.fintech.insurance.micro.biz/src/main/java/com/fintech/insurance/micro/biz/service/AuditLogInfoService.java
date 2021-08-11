package com.fintech.insurance.micro.biz.service;


import java.util.List;
import java.util.Map;

/**
 * @Description: (生产审核信息)
 * @Author: yongNeng Liu
 * @Date: 2017/11/13 10:28
 */
public interface AuditLogInfoService {
    /**
     * 创建审核信息
     * @param userRoleCode 用户角色
     * @param requisitionId 业务id
     * @param auditBatchNumber 批次号
     * @param currentLoginUserId 登录用户id
     */
    public void createAuditLogInfo(String userRoleCode,Integer requisitionId, String auditBatchNumber, Integer currentLoginUserId);
}
