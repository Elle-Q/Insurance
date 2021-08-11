package com.fintech.insurance.micro.support.persist;

import com.fintech.insurance.micro.support.persist.dao.InsuranceBranchDao;
import com.fintech.insurance.micro.support.persist.entity.InsuranceBranch;
import com.fintech.insurance.micro.support.persist.entity.InsuranceCompany;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class InsuranceBranchDaoTests {

    @Autowired
    private InsuranceBranchDao insuranceBranchDao;

    @Test
    public void testSave() {
        InsuranceBranch insuranceBranch = new InsuranceBranch();
        insuranceBranch.setCreateBy(1);
        insuranceBranch.setAccountBank("ABC");
        insuranceBranch.setAccountBankBranch("支行");
        insuranceBranch.setAccountName("刘洋坤");
        insuranceBranch.setAccountNumber("123456789");
        insuranceBranch.setContactName("瞿晓钰");
        insuranceBranch.setContactPhone("17603005625");
        insuranceBranch.setDisabledFlag(false);
        InsuranceCompany insuranceCompany = new InsuranceCompany();
        insuranceCompany.setId(1);
        insuranceBranch.setInsuranceCompany(insuranceCompany);
        insuranceBranch.setBranchAddress("深圳市南山区");
        insuranceBranch.setBranchName("平安银行深圳支行");
        insuranceBranchDao.save(insuranceBranch);
    }
}
