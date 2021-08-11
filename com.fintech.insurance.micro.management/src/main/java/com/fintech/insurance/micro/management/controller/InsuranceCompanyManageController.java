package com.fintech.insurance.micro.management.controller;

import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.BaseFintechManagementController;
import com.fintech.insurance.micro.dto.IdVO;
import com.fintech.insurance.micro.dto.support.BranchVO;
import com.fintech.insurance.micro.dto.support.CompanyNameVO;
import com.fintech.insurance.micro.dto.support.InsuranceCompanyVO;
import com.fintech.insurance.micro.feign.support.InsuranceCompanyConfigServiceFeign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/12/7 16:07
 */
@RestController
@RequestMapping(value = "/management/insurance/company", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class InsuranceCompanyManageController extends BaseFintechManagementController {

    private static final Logger LOG = LoggerFactory.getLogger(InsuranceCompanyManageController.class);


    @Autowired
    private InsuranceCompanyConfigServiceFeign insuranceCompanyConfigServiceFeign;


    /**
     * 获取所有保险公司及其支部信息
     * @return
     */
    @RequestMapping(value="/list", method = RequestMethod.GET)
    public FintechResponse<List<InsuranceCompanyVO>> listAllInsuranceCompany() {
        FintechResponse<List<InsuranceCompanyVO>> result = insuranceCompanyConfigServiceFeign.listAllInsuranceCompany();
        if (!result.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(result);
        }
        return result;
    }

    /**
     * 根据id删除保险公司
     * @param
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    void deleteInsuranceCompany(@RequestBody @Validated IdVO idVO) {
        insuranceCompanyConfigServiceFeign.deleteInsuranceCompany(idVO);
    }

    /**
     * 增加保险公司
     * @param vo
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    void addInsuranceComany(@RequestBody @Validated CompanyNameVO vo) {
        insuranceCompanyConfigServiceFeign.addInsuranceComany(vo);
    }

    /**
     * 新增、更新支部信息
     * @param branchVO
     */
    @RequestMapping(value="/save-branch", method = RequestMethod.POST)
    void saveOrUpdateBranch(@RequestBody @Validated BranchVO branchVO) {
        insuranceCompanyConfigServiceFeign.saveOrUpdateBranch(branchVO);
    }

    /**
     * 删除支部
     * @param
     */
    @RequestMapping(value="/delete-branch", method = RequestMethod.POST)
    void deleteBranch(@RequestBody @Validated IdVO idVO) {
        insuranceCompanyConfigServiceFeign.deleteBranch(idVO);
    }
}
