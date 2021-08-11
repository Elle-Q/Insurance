package com.fintech.insurance.micro.system.persist;

import com.fintech.insurance.commons.enums.AuditStatus;
import com.fintech.insurance.micro.system.persist.dao.EntityAuditLogDao;
import com.fintech.insurance.micro.system.persist.entity.EntityAuditLog;
import com.fintech.insurance.micro.system.persist.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class EntityAuditLogDaoTests {

    @Autowired
    private EntityAuditLogDao entityAuditLogDao;

    @Test
    public void testSave() {
        EntityAuditLog entityOperationLog = new EntityAuditLog();
        User user = new User();
        user.setId(1);
        entityOperationLog.setEntityId(1);
        entityOperationLog.setEntityType("requisition");
        entityOperationLog.setAuditRemark("丑，不通过");
        entityOperationLog.setBatchNumber("123456");
        entityOperationLog.setAuditStatus(AuditStatus.REJECTED.getCode());
        entityOperationLog.setCreateBy(1);
        entityOperationLog.setUser(user);
        entityAuditLogDao.save(entityOperationLog);
    }
}
