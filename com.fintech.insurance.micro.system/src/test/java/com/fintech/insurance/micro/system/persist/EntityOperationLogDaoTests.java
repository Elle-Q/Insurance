package com.fintech.insurance.micro.system.persist;

import com.fintech.insurance.commons.enums.OperationType;
import com.fintech.insurance.micro.system.persist.dao.EntityOperationLogDao;
import com.fintech.insurance.micro.system.persist.entity.EntityOperationLog;
import com.fintech.insurance.micro.system.persist.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class EntityOperationLogDaoTests {

    @Autowired
    private EntityOperationLogDao entityOperationLogDao;

    @Test
    public void testSave() {
        EntityOperationLog entityOperationLog = new EntityOperationLog();
        User user = new User();
        user.setId(1);
        entityOperationLog.setEntityId(1);
        entityOperationLog.setOperationType(OperationType.AUDIT.getCode());
        entityOperationLog.setEntityType("requisition");
        entityOperationLog.setOperationRemark("丑，不通过");
        entityOperationLog.setCreateBy(1);
        entityOperationLog.setUser(user);
        entityOperationLogDao.save(entityOperationLog);
    }
}
