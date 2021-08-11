package com.fintech.insurance.micro.api.system;

import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.micro.dto.biz.OperationRecordVO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping(value = "/system/operation-log", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface EntityOperationLogServiceAPI {

    /**
     * 操作记录查询
     * @param id    操作实体id
     */
    @RequestMapping(value = "/record", method = RequestMethod.GET)
    FintechResponse<List<OperationRecordVO>> listOperationrRecord(@RequestParam(name = "id") Integer id);


    /**
     * 生成操作记录
     * @param operationrRecordVO
     */
    @RequestMapping(value = "/create-log", method = RequestMethod.POST)
    FintechResponse<VoidPlaceHolder> createLog(@RequestBody OperationRecordVO operationrRecordVO);

    /**
     * 操作记录附件查看
     */
    @RequestMapping(value = "/get-attachment", method = RequestMethod.GET)
    FintechResponse<String[]> getLogAttachment(@RequestParam(value = "userId") Integer userId, @RequestParam(value = "id") Integer id,
                                               @RequestParam(value = "operationType") String operationType);
    }
