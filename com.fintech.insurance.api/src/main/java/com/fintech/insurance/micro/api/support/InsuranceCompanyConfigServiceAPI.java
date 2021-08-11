package com.fintech.insurance.micro.api.support;

import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.micro.dto.IdVO;
import com.fintech.insurance.micro.dto.customer.CustomerLoanBankVO;
import com.fintech.insurance.micro.dto.support.BranchVO;
import com.fintech.insurance.micro.dto.support.CompanyNameVO;
import com.fintech.insurance.micro.dto.support.InsuranceCompanyVO;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;
import java.util.List;

@RequestMapping(value = "/support/insurance-company", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface InsuranceCompanyConfigServiceAPI {
    /**
     * 获取所有保险公司及其支部信息
     * @return
     */
    @RequestMapping(value="/list", method = RequestMethod.GET)
    FintechResponse<List<InsuranceCompanyVO>> listAllInsuranceCompany();

    /**
     * 根据id删除保险公司
     * @param
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    FintechResponse<VoidPlaceHolder> deleteInsuranceCompany(@RequestBody @Validated IdVO idVO);

    /**
     * 增加保险公司
     * @param vo
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    FintechResponse<VoidPlaceHolder> addInsuranceComany(@RequestBody @Validated CompanyNameVO vo);

    /**
     * 新增、更新支部信息
     * @param branchVO
     */
    @RequestMapping(value="/save-branch", method = RequestMethod.POST)
    FintechResponse<VoidPlaceHolder> saveOrUpdateBranch(@RequestBody @Validated BranchVO branchVO);

    /**
     * 删除支部
     * @param
     */
    @RequestMapping(value="/delete-branch", method = RequestMethod.POST)
    FintechResponse<VoidPlaceHolder> deleteBranch(@RequestBody @Validated IdVO idVO);

    /**
     * 查找保险公司信息
     * @param loanAccountNumber  放款账户号
     * @return
     */
    @RequestMapping(value="/get-by-account-number", method = RequestMethod.GET)
    BranchVO getByAccountNumber(@RequestParam(value = "loanAccountNumber") String loanAccountNumber);

    /**
     * 查找保险公司信息
     * @param companyId  承保公司id
     * @param branchId  承保支部id
     * @return
     */
    @RequestMapping(value="/get-loan-bank", method = RequestMethod.GET)
    CustomerLoanBankVO getLoanBankVO(@RequestParam(value = "companyId", required = false) @NotNull Integer companyId, @RequestParam(value = "branchId", required = false) Integer branchId);

    /**
     * 查找保险公司信息
     * @param loanAccountNumber  放款账户号
     * @return
     */
    @RequestMapping(value="/get-by-account-number-type", method = RequestMethod.GET)
    CustomerLoanBankVO getByAccountNumberAndType(@RequestParam(value = "loanAccountNumber") String loanAccountNumber,@RequestParam(value = "loanAccountType",required = false) String loanAccountType);
}
