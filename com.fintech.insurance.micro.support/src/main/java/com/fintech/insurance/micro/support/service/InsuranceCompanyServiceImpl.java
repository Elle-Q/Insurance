package com.fintech.insurance.micro.support.service;

import com.fintech.insurance.commons.enums.LoanAccountType;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.context.FInsuranceApplicationContext;
import com.fintech.insurance.micro.dto.customer.CustomerLoanBankVO;
import com.fintech.insurance.micro.dto.support.BranchVO;
import com.fintech.insurance.micro.dto.support.CompanyNameVO;
import com.fintech.insurance.micro.dto.support.InsuranceCompanyVO;
import com.fintech.insurance.micro.support.persist.dao.InsuranceBranchDao;
import com.fintech.insurance.micro.support.persist.dao.InsuranceCompanyDao;
import com.fintech.insurance.micro.support.persist.entity.InsuranceBranch;
import com.fintech.insurance.micro.support.persist.entity.InsuranceCompany;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Pattern;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/11/15 10:28
 */
@Service
public class InsuranceCompanyServiceImpl implements InsuranceCompanyService {

    @Autowired
    private InsuranceCompanyDao insuranceCompanyDao;

    @Autowired
    private InsuranceBranchDao insuranceBranchDao;

    @Override
    @Transactional(readOnly = true)
    public List<InsuranceCompanyVO> listAllInsuranceCompany() {
        List<InsuranceCompany> entityList = insuranceCompanyDao.findAllByDisabledFlagFalse();
        return entitiesToVOs(entityList);
    }

    @Override
    @Transactional
    public void deleteInsuranceCompany(Integer id) {
        InsuranceCompany insuranceCompany = insuranceCompanyDao.getById(id);
        if (insuranceCompany == null) {
            throw new FInsuranceBaseException(102011, new Object[]{"id = " + id});
        }
        insuranceCompany.setDisabledFlag(true);
        // 删除保险公司下所有支部
        if (insuranceCompany.getInsuranceBranches() != null) {
            Iterator<InsuranceBranch> iter = insuranceCompany.getInsuranceBranches().iterator();
            while (iter.hasNext()) {
                InsuranceBranch branch = iter.next();
                branch.setDisabledFlag(true);
            }
        }
    }

    @Override
    public void addInsuranceCompany(CompanyNameVO vo) {
        String companyName = vo.getCompanyName();

        if (StringUtils.isBlank(companyName)) {
            throw new FInsuranceBaseException(102012);
        }

        InsuranceCompany insuranceCompany = new InsuranceCompany();
        insuranceCompany.setCompanyName(companyName);
        insuranceCompany.setAccountCompanyFlag(false);
        insuranceCompany.setCreateBy(FInsuranceApplicationContext.getCurrentUserId());
        insuranceCompany.setCreateAt(new Date());

        insuranceCompanyDao.save(insuranceCompany);
    }

    @Override
    @Transactional
    public void saveOrUpdateBranch(BranchVO branchVO){
        if (branchVO.getCompanyId() != null) { // 新增支部
            InsuranceCompany insuranceCompany = insuranceCompanyDao.getById(branchVO.getCompanyId());
            if (insuranceCompany == null) {
                throw new FInsuranceBaseException(102003);
            }
            InsuranceBranch branch = branchToEntity(branchVO);
            branch.setCreateBy(FInsuranceApplicationContext.getCurrentUserId());
            branch.setCreateAt(new Date());
            branch.setInsuranceCompany(insuranceCompany);
            insuranceBranchDao.save(branch);
        } else if (branchVO.getId() != null) { // 更新支部详情
            InsuranceBranch insuranceBranch = insuranceBranchDao.getById(branchVO.getId());
            if (insuranceBranch == null) {
                throw new FInsuranceBaseException(102004);
            }
            insuranceBranch.setBranchName(branchVO.getBranchName());
            insuranceBranch.setAccountName(branchVO.getAccountName());
            if (!Pattern.matches("^\\d+$", branchVO.getAccountNum())) {
                throw new FInsuranceBaseException(102016);
            }
            insuranceBranch.setAccountNumber(branchVO.getAccountNum());
            insuranceBranch.setAccountBank(branchVO.getAccountBank());
            insuranceBranch.setAccountBankBranch(branchVO.getAccountBranch());
        } else {
            throw new FInsuranceBaseException(102005);
        }

    }

    @Override
    @Transactional
    public void deleteBranch(Integer id) {
        InsuranceBranch insuranceBranch = insuranceBranchDao.getById(id);
        if (insuranceBranch != null) {
            insuranceBranch.setDisabledFlag(true);
        } else {
            throw new FInsuranceBaseException(102013, new Object[]{"id = " + id});
        }
    }

    @Override
    @Transactional(readOnly = true)
    public BranchVO getByAccountNumber(String loanAccountNumber) {
        InsuranceBranch insuranceBranch =  insuranceBranchDao.getByAccountNumber(loanAccountNumber);
        return this.branchToVO(insuranceBranch);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerLoanBankVO getLoanBankVO(Integer companyId, Integer branchId) {
        CustomerLoanBankVO bankVO = null;
        if(companyId == null && branchId == null){
            return bankVO;
        }
        if(branchId == null){
            InsuranceCompany insuranceCompany = insuranceCompanyDao.getById(companyId);
            if (insuranceCompany == null) {
                throw new FInsuranceBaseException(102011, new Object[]{"companyId = " + companyId});
            }
            bankVO = bankVO = getLoanBankVO(insuranceCompany, null);
        }else{
            InsuranceBranch insuranceBranch = insuranceBranchDao.getById(branchId);
            if (insuranceBranch == null) {
                throw new FInsuranceBaseException(102011, new Object[]{"branchId = " + branchId});
            }
            bankVO = getLoanBankVO(null, insuranceBranch);
        }
        return bankVO;
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerLoanBankVO getByAccountNumberAndType(String loanAccountNumber, String loanAccountType) {
        CustomerLoanBankVO bankVO = null;
        if(StringUtils.isBlank(loanAccountNumber) || StringUtils.isBlank(loanAccountType)){
            return null;
        }
        if(StringUtils.equals(loanAccountType, LoanAccountType.COMPAEY_LOAN_TYPE.getCode())){
            InsuranceCompany insuranceCompany = insuranceCompanyDao.getByAccountNumber(loanAccountNumber);
            if (insuranceCompany == null) {
                throw new FInsuranceBaseException(102011, new Object[]{"loanAccountNumber = " + loanAccountNumber});
            }
            bankVO = getLoanBankVO(insuranceCompany, null);
        }else{
            InsuranceBranch insuranceBranch = insuranceBranchDao.getByAccountNumber(loanAccountNumber);
            if (insuranceBranch == null) {
                throw new FInsuranceBaseException(102011, new Object[]{"loanAccountNumber = " + loanAccountNumber});
            }
            bankVO = getLoanBankVO(null, insuranceBranch);
        }
        return bankVO;
    }

    //获取客户放款银行
    @Transactional(readOnly = true)
    private CustomerLoanBankVO getLoanBankVO(InsuranceCompany insuranceCompany, InsuranceBranch insuranceBranch){
        CustomerLoanBankVO bankVO =  new CustomerLoanBankVO();
        Integer companyId = null;
        String companyName = null;
        Integer branchId = null;
        String branchName = null;
        String loanAccountNumber = null;
        String loanAccountBank = null;
        String loanAccountBankBranch = null;
        String loanAccountName = null;
        String loanAccountType = null;
        if(insuranceCompany != null){
            companyId = insuranceCompany.getId();
            companyName = insuranceCompany.getCompanyName();
            loanAccountNumber = insuranceCompany.getAccountNumber();
            loanAccountBank = insuranceCompany.getAccountBank();
            loanAccountBankBranch = insuranceCompany.getAccountBankBranch();
            loanAccountName = insuranceCompany.getAccountName();
            loanAccountType = LoanAccountType.COMPAEY_LOAN_TYPE.getCode();
        }else if (insuranceBranch != null){
            companyId = insuranceBranch.getInsuranceCompany().getId();
            if (insuranceBranch.getInsuranceCompany() != null) {
                companyName = insuranceBranch.getInsuranceCompany().getCompanyName();
            }
            branchId = insuranceBranch.getId();
            branchName = insuranceBranch.getBranchName();
            loanAccountNumber = insuranceBranch.getAccountNumber();
            loanAccountBank = insuranceBranch.getAccountBank();
            loanAccountBankBranch = insuranceBranch.getAccountBankBranch();
            loanAccountName = insuranceBranch.getAccountName();
            loanAccountType = LoanAccountType.BRANCH_LOAN_TYPE.getCode();
        }

        // 承包公司id
        bankVO.setCompanyId(companyId);
        // 承保公司名称
        bankVO.setCompanyName(companyName);
        // 承保支行id
        bankVO.setBranchId(branchId);
        bankVO.setBranchName(branchName);
        //放款帐户类型
        bankVO.setLoanAccountType(loanAccountType);
        //放款帐户号
        bankVO.setLoanAccountNumber(loanAccountNumber);
        //放款帐户银行编码
        bankVO.setLoanAccountBank(loanAccountBank);
        //放款银行名称
        bankVO.setLoanAccountBankBranch(loanAccountBankBranch);
        //放款帐户名称型
        bankVO.setLoanAccountName(loanAccountName);
        return bankVO;
    }

    private List<InsuranceCompanyVO> entitiesToVOs(List<InsuranceCompany> entities) {
        if (entities == null || entities.size() <= 0) {
            return Collections.EMPTY_LIST;
        }
        List<InsuranceCompanyVO> list = new ArrayList<>();
        for (InsuranceCompany entity : entities) {
            list.add(entityToVO(entity));
        }
        return list;
    }

    private InsuranceCompanyVO entityToVO(InsuranceCompany entity) {
        if (entity == null) {
            return null;
        }
        InsuranceCompanyVO vo = new InsuranceCompanyVO();
        vo.setId(entity.getId());
        vo.setCompanyName(entity.getCompanyName());
        Set<BranchVO> branches = new HashSet<>();
        if (entity.getInsuranceBranches() != null) {
            Set<InsuranceBranch> branchSet = entity.getInsuranceBranches();
            for (InsuranceBranch insuranceBranch : branchSet) {
                BranchVO branchVO = branchToVO(insuranceBranch);
                branchVO.setCompanyId(entity.getId());
                branches.add(branchVO);
            }
        }
        vo.setBranches(branches);
        return vo;
    }

    // InsuranceBranch --> BranchVO
    private BranchVO branchToVO(InsuranceBranch entity) {
        if (entity == null) {
            return null;
        }
        BranchVO vo = new BranchVO();
        vo.setId(entity.getId());
        if (entity.getInsuranceCompany() != null) {
            vo.setCompanyName(entity.getInsuranceCompany().getCompanyName());
        }
        vo.setAccountNum(entity.getAccountNumber());
        vo.setBranchName(entity.getBranchName());
        vo.setAccountBranch(entity.getAccountBankBranch());
        vo.setAccountBank(entity.getAccountBank());
        vo.setAccountName(entity.getAccountName());

        return vo;
    }

    private InsuranceBranch branchToEntity(BranchVO vo) {
        InsuranceBranch branch = new InsuranceBranch();
        branch.setBranchName(vo.getBranchName());
        branch.setAccountName(vo.getAccountName());

        if (!Pattern.matches("^\\d+$", vo.getAccountNum())) {
            throw new FInsuranceBaseException(102016);
        }
        if (insuranceBranchDao.getByAccountNumber(vo.getAccountNum()) != null) {
            throw new FInsuranceBaseException(102015);
        }
        branch.setAccountNumber(vo.getAccountNum());
        branch.setAccountBank(vo.getAccountBank());
        branch.setAccountBankBranch(vo.getAccountBranch());

        return branch;
    }
}
