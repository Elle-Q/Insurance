package com.fintech.insurance.micro.support.controller;

import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.web.BaseFintechController;
import com.fintech.insurance.micro.api.support.InsuranceCompanyConfigServiceAPI;
import com.fintech.insurance.micro.dto.IdVO;
import com.fintech.insurance.micro.dto.customer.CustomerLoanBankVO;
import com.fintech.insurance.micro.dto.support.BranchVO;
import com.fintech.insurance.micro.dto.support.CompanyNameVO;
import com.fintech.insurance.micro.dto.support.InsuranceCompanyVO;
import com.fintech.insurance.micro.support.service.InsuranceCompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: Clayburn
 * @Description: 保险公司资料管理
 * @Date: 2017/11/9 11:25
 */
@RestController
public class InsuranceCompanyController extends BaseFintechController implements InsuranceCompanyConfigServiceAPI {
    private static final Logger logger = LoggerFactory.getLogger(InsuranceCompanyController.class);

    @Autowired
    private InsuranceCompanyService insuranceCompanyService;

    @Override
    public FintechResponse<List<InsuranceCompanyVO>> listAllInsuranceCompany() {
        return FintechResponse.responseData(insuranceCompanyService.listAllInsuranceCompany());
    }

    @Override
    public FintechResponse<VoidPlaceHolder> deleteInsuranceCompany(@RequestBody @Validated IdVO idVO) {
        insuranceCompanyService.deleteInsuranceCompany(idVO.getId());
        return FintechResponse.responseData(VoidPlaceHolder.instance());
    }

    @Override
    public FintechResponse<VoidPlaceHolder> addInsuranceComany(@RequestBody @Validated CompanyNameVO vo) {
        insuranceCompanyService.addInsuranceCompany(vo);
        return FintechResponse.responseData(VoidPlaceHolder.instance());
    }

    @Override
    public FintechResponse<VoidPlaceHolder> saveOrUpdateBranch(@RequestBody @Validated BranchVO branchVO) {
        insuranceCompanyService.saveOrUpdateBranch(branchVO);
        return FintechResponse.responseData(VoidPlaceHolder.instance());
    }


    @Override
    public FintechResponse<VoidPlaceHolder> deleteBranch(@RequestBody @Validated IdVO idVO) {
        insuranceCompanyService.deleteBranch(idVO.getId());
        return FintechResponse.responseData(VoidPlaceHolder.instance());
    }

    @Override
    public BranchVO getByAccountNumber(@RequestParam(value = "loanAccountNumber")String loanAccountNumber) {
        BranchVO data = insuranceCompanyService.getByAccountNumber(loanAccountNumber);
        return data;
    }

    @Override
    public CustomerLoanBankVO getLoanBankVO(@RequestParam(value = "companyId", required = false) Integer companyId, @RequestParam(value = "branchId", required = false) Integer branchId) {
        return insuranceCompanyService.getLoanBankVO(companyId, branchId);
    }

    @Override
    public CustomerLoanBankVO getByAccountNumberAndType(@RequestParam(value = "loanAccountNumber") String loanAccountNumber,@RequestParam(value = "loanAccountType",required = false) String loanAccountType) {
        return insuranceCompanyService.getByAccountNumberAndType(loanAccountNumber, loanAccountType);
    }
}
