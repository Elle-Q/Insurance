package com.fintech.insurance.micro.biz.persist.dao;

import com.fintech.insurance.micro.biz.persist.entity.Contract;
import com.fintech.insurance.micro.biz.persist.entity.Requisition;
import com.fintech.insurance.micro.biz.persist.entity.RequisitionDetail;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * @Description: (some words)
 * @Author: yongneng liu
 * @Date: 2017/11/14 0014 14:06
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RequisitionDetailDaoTest {
    @Autowired
    ContractDao contractDao;
    @Autowired
    RequisitionDao requisitionDao;
    @Autowired
    RequisitionDetailDao requisitionDetailDao;

    @Test
    public void save() {
        RequisitionDetail p = new RequisitionDetail();
        Requisition r = requisitionDao.getRequisitionById(1);
        //产品名称
        p.setRequisition(r);
        p.setDrivingLicense("123455");
        p.setDrivingLicenseMain("12345");
        p.setDrivingLicenseAttach("1234252");
        p.setCommercialInsuranceNumber("qr3awra");
        p.setCommercialInsuranceAmount(1000000);
        p.setCommercialInsuranceStart(new Date());
        p.setCommercialInsuranceEnd(new Date());
        p.setCommercialInsuranceValue(1000000);
        p.setBusinessDuration(1);
        p.setCompulsoryInsuranceNumber("feafeaf");
        p.setCompulsoryInsuranceAmount(1000000);
        p.setCompulsoryInsuranceStart(new Date());
        p.setCompulsoryInsuranceEnd(new Date());
        p.setTaxAmount(1000000);
        p.setCommercialOnly(true);
        p.setSubTotal(1000000);
        Contract contract = contractDao.getContractById(2);
        p.setContract(contract);
        requisitionDetailDao.save(p);
        Assert.assertNotNull(p.getId());
    }
}