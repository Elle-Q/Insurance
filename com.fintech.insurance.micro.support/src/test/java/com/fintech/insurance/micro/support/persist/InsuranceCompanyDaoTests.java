package com.fintech.insurance.micro.support.persist;

import com.fintech.insurance.micro.support.persist.dao.InsuranceCompanyDao;
import com.fintech.insurance.micro.support.persist.entity.InsuranceCompany;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class InsuranceCompanyDaoTests {

    @Autowired
    private InsuranceCompanyDao insuranceCompanyDao;

    @Test
    public void testSave() {
        InsuranceCompany insuranceCompany = new InsuranceCompany();
        insuranceCompany.setCreateBy(1);
        insuranceCompany.setAccountBank("ABC");
        insuranceCompany.setAccountBankBranch("支行");
        insuranceCompany.setAccountName("刘洋坤");
        insuranceCompany.setAccountCompanyFlag(false);
        insuranceCompany.setAccountNumber("123456789");
        insuranceCompany.setContactName("瞿晓钰");
        insuranceCompany.setContactPhone("17603005625");
        insuranceCompany.setDisabledFlag(false);
        insuranceCompany.setCompanyName("平安保险");
        insuranceCompanyDao.save(insuranceCompany);
    }
}
