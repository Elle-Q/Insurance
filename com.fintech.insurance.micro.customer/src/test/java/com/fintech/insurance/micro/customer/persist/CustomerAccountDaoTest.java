package com.fintech.insurance.micro.customer.persist;

import com.fintech.insurance.commons.enums.CustomerStatus;
import com.fintech.insurance.micro.customer.persist.dao.CustomerAccountDao;
import com.fintech.insurance.micro.customer.persist.entity.CustomerAccount;
import com.fintech.insurance.micro.customer.persist.entity.CustomerAccountInfo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CustomerAccountDaoTest {
    @Autowired
    private CustomerAccountDao customerAccountDao;

    @Test
    @Transactional
    public void save() {
        CustomerAccount customerAccount = new CustomerAccount();
        customerAccount.setIdNumber("3aa3332145665455");
        customerAccount.setIdFront("正面照片");
        customerAccount.setIdBack("背面照片");
        customerAccountDao.save(customerAccount);
        Assert.assertNotNull(customerAccount.getId());
    }

    @Test
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public void query() {
        CustomerAccount customerAccount = customerAccountDao.getById(77);

        Assert.assertNotNull(customerAccount);
    }

    @Test
    public void queryAllCustomer() {
        Page<CustomerAccountInfo> page = customerAccountDao.queryAllCustomer("客户", null, "1",  CustomerStatus.NORMAL, 1, 20);
        Assert.assertNotNull(page);
    }
}