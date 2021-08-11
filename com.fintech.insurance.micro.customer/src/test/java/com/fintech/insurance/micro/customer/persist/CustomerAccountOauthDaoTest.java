package com.fintech.insurance.micro.customer.persist;

import com.fintech.insurance.micro.customer.persist.dao.CustomerAccountOauthDao;
import com.fintech.insurance.micro.customer.persist.entity.CustomerAccount;
import com.fintech.insurance.micro.customer.persist.entity.CustomerAccountOauth;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CustomerAccountOauthDaoTest {
    @Autowired
    CustomerAccountOauthDao customerAccountOauthDao;

    @Test
//    @Transactional
    public void save() {
        CustomerAccountOauth customerAccountOauth = new CustomerAccountOauth();
        customerAccountOauth.setOauthType("认证类型");
        customerAccountOauth.setOauthAppId("3");
        customerAccountOauth.setOauthAccount("csds3sw");
        customerAccountOauth.setNickName("ddd");
        customerAccountOauth.setGender("男");
        customerAccountOauth.setHeaderImage("a;sldjf;a");
        customerAccountOauth.setWxUnionId("uuid");
        customerAccountOauth.setOauthContent("啊啊啊啊啊啊啊啊");


        CustomerAccount customerAccount = new CustomerAccount();
        customerAccount.setIdNumber("ggggggggg");
        customerAccount.setIdFront("正面照片");
        customerAccount.setIdBack("背面照片");

        customerAccountOauth.setCustomerAccount(customerAccount);

        customerAccountOauthDao.save(customerAccountOauth);
    }
}