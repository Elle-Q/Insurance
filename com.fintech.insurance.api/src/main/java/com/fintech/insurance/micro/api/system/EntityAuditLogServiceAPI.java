package com.fintech.insurance.micro.api.system;

import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.micro.dto.biz.AuditLogVO;
import com.fintech.insurance.micro.dto.biz.OperationRecordVO;
import com.fintech.insurance.micro.dto.system.EntityAuditLogVO;
import com.fintech.insurance.micro.dto.system.QueryAuditLogVO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 审核记录
 */
@RequestMapping(value = "/system/auditlog", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface EntityAuditLogServiceAPI {

    /**
     * 审核
     * @param operationrRecordVO
     */
    @RequestMapping(path = "/audit", method = RequestMethod.POST)
    FintechResponse<VoidPlaceHolder> auditRequsition(@RequestBody OperationRecordVO operationrRecordVO);

    /**
     * 保存审核记录
     * @param auditLogVO
     */
    @RequestMapping(path = "/save",method = RequestMethod.POST)
    FintechResponse<VoidPlaceHolder> save(@RequestBody AuditLogVO auditLogVO);

    /**
     *
     * @param queryAuditLogVO
     * @return
     */
    @RequestMapping(path = "/list-auditlogs-by-current-user-and", method = RequestMethod.POST)
    FintechResponse<List<EntityAuditLogVO>> listAuditLogsByCurrentUserAnd(@RequestBody QueryAuditLogVO queryAuditLogVO);

    @GetMapping(value = "/remark")
    FintechResponse<String> getRemark(@RequestParam(value = "entityType") String entityType,
                                      @RequestParam(value = "entityId") Integer entityId,
                                      @RequestParam(value = "auditStatus") String auditStatus);

    /**
     *  获取申请单审核备注
     * @param id        申请单id
     * @param entityType  实体类型
     * @param latestAuditBatch  上次提交审核的审核批次号
     * @param auditStatus       审核状态
     */
    @GetMapping(value = "/get-remark-by-entityid-and-some")
    FintechResponse<String> getRemarkByEntityIdAndSome(@RequestParam(value = "id")Integer id,
                                                       @RequestParam(value = "entityType") String entityType,
                                                       @RequestParam(value = "latestAuditBatch") String latestAuditBatch,
                                                       @RequestParam(value = "auditStatus") String auditStatus);
}
