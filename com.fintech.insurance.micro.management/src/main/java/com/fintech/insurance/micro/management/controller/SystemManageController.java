package com.fintech.insurance.micro.management.controller;

import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.BaseFintechManagementController;
import com.fintech.insurance.micro.dto.biz.OperationRecordVO;
import com.fintech.insurance.micro.dto.support.BankInfoVO;
import com.fintech.insurance.micro.dto.support.ConstantConfigVO;
import com.fintech.insurance.micro.dto.support.OrganizationVO;
import com.fintech.insurance.micro.feign.support.BankInfoServiceFeign;
import com.fintech.insurance.micro.feign.support.ConstantConfigServiceFeign;
import com.fintech.insurance.micro.feign.support.OrganizationServiceFeign;
import com.fintech.insurance.micro.feign.system.EntityOperationLogServiceFeign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/12/7 15:17
 */
@RestController
@RequestMapping(value = "/management/system/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SystemManageController extends BaseFintechManagementController {

    private static final Logger LOG = LoggerFactory.getLogger(SystemManageController.class);

    @Autowired
    private ConstantConfigServiceFeign constantConfigServiceFeign;

    @Autowired
    private EntityOperationLogServiceFeign etityOperationLogServiceFeign;

    @Autowired
    private BankInfoServiceFeign bankInfoServiceFeign;

    @Autowired
    private OrganizationServiceFeign organizationServiceFeign;


    @RequestMapping(value = "/organization/list", method = RequestMethod.GET)
    FintechResponse<List<OrganizationVO>> listAllCompany(@RequestParam(value = "rootId", defaultValue = "1") Integer rootId) {
        FintechResponse<List<OrganizationVO>> result = organizationServiceFeign.listAllCompany(rootId);
        if (!result.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(result);
        }
        return result;
    }

    /**
     * 拿到最大逾期天数，还款提醒提前天数，还款提前天数的常量配置
     * @return
     */
    @RequestMapping(value = "/constant/show", method = RequestMethod.GET)
    FintechResponse<List<ConstantConfigVO>> getConstantConfig() {
        FintechResponse<List<ConstantConfigVO>> result = constantConfigServiceFeign.getConstantConfig();
        if (!result.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(result);
        }
        return result;
    }

    /**
     * 更新常量配置
     */
    @RequestMapping(value = "/constant/update", method = RequestMethod.POST)
    void updateConstantConfig(@RequestBody Map<String, Object> map) {
        constantConfigServiceFeign.updateConstantConfig(map);
    }

    /**
     * 查询所又银行信息
     * @return
     */
    @RequestMapping(value = "/bank/list", method = RequestMethod.GET)
    FintechResponse<List<BankInfoVO>> listAllBankInfo() {
        FintechResponse<List<BankInfoVO>> result = bankInfoServiceFeign.listAllBankInfo();
        if (!result.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(result);
        }
        return result;
    }

    /**
     * 操作记录查询
     * @param id    操作实体id
     */
    @RequestMapping(value = "/operation-log/record", method = RequestMethod.GET)
    FintechResponse<List<OperationRecordVO>> listOperationrRecord(@RequestParam(name = "id") Integer id) {
        FintechResponse<List<OperationRecordVO>> result = etityOperationLogServiceFeign.listOperationrRecord(id);
        if (!result.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(result);
        }
        return result;
    }

    /**
     * 操作记录附件查看
     */
    @RequestMapping(value = "/operation-log/get-attachment", method = RequestMethod.GET)
    FintechResponse<String[]> getLogAttachment(@RequestParam(value = "userId") Integer userId, @RequestParam(value = "id") Integer id,
                                               @RequestParam(value = "operationType") String operationType) {
        FintechResponse<String[]> result = etityOperationLogServiceFeign.getLogAttachment(userId, id, operationType);
        if (!result.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(result);
        }
        return result;
    }
}
