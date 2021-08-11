package com.fintech.insurance.micro.customer.persist;

import com.fintech.insurance.micro.customer.persist.dao.CustomerBankCardDao;
import com.fintech.insurance.micro.customer.persist.entity.CustomerAccount;
import com.fintech.insurance.micro.customer.persist.entity.CustomerBankCard;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CustomerBankCardDaoTest {
    @Autowired
    CustomerBankCardDao customerBankCardDao;

    @Test
//    @Transactional
    public void save() {
        CustomerBankCard customerBankCard = new CustomerBankCard();

        customerBankCard.setAccountBank("开户银行");
        customerBankCard.setAccountNumber("2aaaa234234");
        customerBankCard.setAccountMobile("ccccc");

        CustomerAccount customerAccount = new CustomerAccount();
        customerAccount.setIdNumber("gaaaaagggggg");
        customerAccount.setIdFront("正面照片");
        customerAccount.setIdBack("背面照片");

        customerBankCard.setCustomerAccount(customerAccount);

        customerBankCardDao.save(customerBankCard);
    }
}