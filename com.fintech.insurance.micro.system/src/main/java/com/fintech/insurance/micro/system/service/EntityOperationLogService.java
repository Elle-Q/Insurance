package com.fintech.insurance.micro.system.service;

import com.fintech.insurance.micro.dto.biz.OperationRecordVO;

import java.util.List;

public interface EntityOperationLogService {
    /**
     * 操作记录查询
     * @param id   操作实体id
     * @return
     */
    List<OperationRecordVO> listOperationrRecord(Integer id);


    /**
     * 生成操作记录
     * @param operationrRecordVO  操作记录vo
     */
    void createLog(OperationRecordVO operationrRecordVO);
}
