package com.fintech.insurance.micro.support.service;

import com.fintech.insurance.micro.dto.customer.CustomerLoanBankVO;
import com.fintech.insurance.micro.dto.support.BranchVO;
import com.fintech.insurance.micro.dto.support.CompanyNameVO;
import com.fintech.insurance.micro.dto.support.InsuranceCompanyVO;

import java.util.List;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/11/15 10:28
 */
public interface InsuranceCompanyService {

    /**
     * 获取所有保险公司及其支部信息
     *
     * @return
     */
    List<InsuranceCompanyVO> listAllInsuranceCompany();

    /**
     * 根据id删除保险公司
     *
     * @param
     */
    void deleteInsuranceCompany(Integer id);

    /**
     * 增加保险公司
     *
     * @param vo
     */
    void addInsuranceCompany(CompanyNameVO vo);

    /**
     * 新增、更新支部信息
     *
     * @param branchVO
     */
    void saveOrUpdateBranch(BranchVO branchVO);

    /**
     * 删除支部
     *
     * @param
     */
    void deleteBranch(Integer id);

    BranchVO getByAccountNumber(String loanAccountNumber);

    /**
     * 查询客户放款账户
     * @param companyId 承包公司id
     * @param branchId 承包支部id
     * @return
     */
    CustomerLoanBankVO getLoanBankVO(Integer companyId, Integer branchId);

    /**
     * 查询客户放款账户
     * @param loanAccountNumber 放款账户
     * @param loanAccountType 放款账户类型
     * @return
     */
    CustomerLoanBankVO getByAccountNumberAndType(String loanAccountNumber, String loanAccountType);
}
