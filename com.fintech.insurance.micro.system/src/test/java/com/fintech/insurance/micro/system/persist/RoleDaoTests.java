package com.fintech.insurance.micro.system.persist;

import com.fintech.insurance.micro.system.persist.dao.RoleDao;
import com.fintech.insurance.micro.system.persist.entity.Role;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RoleDaoTests {

    @Autowired
    private RoleDao roleDao;

    @Test
    public void testSave() {
        Role role = new Role();
        role.setRoleCode("GLY");
        role.setRoleName("管理员");
        role.setCreateBy(1);
        roleDao.save(role);

    }
}
