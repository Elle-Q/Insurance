package com.fintech.insurance.micro.system.persist;

import com.fintech.insurance.micro.system.persist.dao.PermissionDao;
import com.fintech.insurance.micro.system.persist.entity.Permission;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class PermissionDaoTests {

    @Autowired
    private PermissionDao permissionDao;

    @Test
    @Transactional
    public void testSave() {

        Permission permission = new Permission();
        permission.setCreateBy(1);
        permission.setPermissionCode("AUDIT");
        permission.setPermissionName("审核");
        permissionDao.save(permission);

    }
}
