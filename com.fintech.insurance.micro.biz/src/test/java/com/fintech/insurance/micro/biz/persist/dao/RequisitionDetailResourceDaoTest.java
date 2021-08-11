package com.fintech.insurance.micro.biz.persist.dao;

import com.fintech.insurance.commons.enums.ResourceType;
import com.fintech.insurance.micro.biz.persist.entity.RequisitionDetail;
import com.fintech.insurance.micro.biz.persist.entity.RequisitionDetailResource;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Description: (some words)
 * @Author: yongneng liu
 * @Date: 2017/11/14 0014 14:23
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RequisitionDetailResourceDaoTest {
    @Autowired
    ContractDao contractDao;
    @Autowired
    RequisitionDetailDao requisitionDetailDao;
    @Autowired
    RequisitionDetailResourceDao requisitionDetailResourceDao;

    @Test
    public void save() {
        RequisitionDetailResource p = new RequisitionDetailResource();
        RequisitionDetail r = requisitionDetailDao.getRequisitionDetailById(2);
        p.setRequisitionDetail(r);
        p.setResourceType(ResourceType.BUSI_INSURANCE.getCode());
        p.setResouceName(ResourceType.BUSI_INSURANCE.getDesc());
        p.setResourcePicture("fafeasjflkea");
        p.setDisplaySequence(1);
        requisitionDetailResourceDao.save(p);
        Assert.assertNotNull(p.getId());
    }

}