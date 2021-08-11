package com.fintech.insurance.micro.customer.persist;

import com.fintech.insurance.micro.customer.persist.dao.CustomerConsultationDao;
import com.fintech.insurance.micro.customer.persist.entity.CustomerConsultation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CustomerConsultationDaoTest {
    @Autowired
    CustomerConsultationDao customerConsultationDao;

    @Test
    public void save() {
        CustomerConsultation customerConsultation = new CustomerConsultation();
        customerConsultation.setCustomerMobile("15sssss520");
        customerConsultation.setCustomerName("sss");
        customerConsultation.setConsultContent("问你妹啊，别咨询，烦！");
        customerConsultation.setProcessStatus("sss");
        customerConsultation.setProcessTime(new Date(System.currentTimeMillis()));
        customerConsultation.setProcessRemark("滚");
        customerConsultation.setOauthType("aaassss");
        customerConsultation.setOauthAcount("dddsss");
        customerConsultation.setOauthAppId("cccccsss");
        customerConsultation.setWxUnionid("asdfas");

        customerConsultationDao.save(customerConsultation);
    }
}