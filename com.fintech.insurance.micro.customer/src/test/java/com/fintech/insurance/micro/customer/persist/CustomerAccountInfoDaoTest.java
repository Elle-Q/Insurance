package com.fintech.insurance.micro.customer.persist;

import com.fintech.insurance.micro.customer.persist.dao.CustomerAccountDao;
import com.fintech.insurance.micro.customer.persist.dao.CustomerAccountInfoDao;
import com.fintech.insurance.micro.customer.persist.entity.CustomerAccountInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CustomerAccountInfoDaoTest {
    @Autowired
    CustomerAccountInfoDao customerAccountInfoDao;

    @Autowired
    CustomerAccountDao customerAccountDao;

    @Test
//    @Transactional
//    @Repeat(10)
    public void save() {
        CustomerAccountInfo customerAccountInfo = new CustomerAccountInfo();
        customerAccountInfo.setChannelCode("qudaocode");
        customerAccountInfo.setCustomerName("彭功波");
        customerAccountInfo.setCustomerMobile("15586863535sss");
        customerAccountInfo.setAccountNumber("4399sss小游戏");
        customerAccountInfo.setAccountBankName("中国银行");
        customerAccountInfo.setAccountBankBranch("中国银行第一支行");
        customerAccountInfo.setBankCardPicture("银行照片");
        customerAccountInfo.setEnterpriseName("薪乐宝");
        customerAccountInfo.setBusinessLicence("经营许可");
        customerAccountInfo.setBusinessLicencePicture("经营许可照片");
        customerAccountInfo.setEnterpriseDefault(true);
        //customerAccountInfo.setIdNumber("665455");
        //customerAccountInfo.setIdFront("正面照片");
        //customerAccountInfo.setIdBack("背面照片");
        customerAccountInfo.setCreateAt(new Date(System.currentTimeMillis()));

        customerAccountInfoDao.save(customerAccountInfo);
    }

}