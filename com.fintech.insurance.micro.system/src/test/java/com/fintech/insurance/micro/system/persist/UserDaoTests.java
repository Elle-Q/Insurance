package com.fintech.insurance.micro.system.persist;

import com.fintech.insurance.micro.system.persist.dao.RoleDao;
import com.fintech.insurance.micro.system.persist.dao.UserDao;
import com.fintech.insurance.micro.system.persist.entity.Role;
import com.fintech.insurance.micro.system.persist.entity.User;
import org.eclipse.jetty.websocket.jsr356.annotations.Param;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UserDaoTests {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Test
    public void testSave() {
        User user = new User();
        user.setId(-1);
        user.setMobilePhone("123456");
        user.setUserName("瞿晓钰");
        user.setPassword("123654");
        user.setCreateBy(1);
        user.setUserType("staff");
        Role r1 = roleDao.getById(1);
        Role r2 = roleDao.getById(2);
        Set<Role> roleList = new HashSet<>();
        roleList.add(r1);
        roleList.add(r2);
        user.setRoleSet(roleList);
        user.setCreateAt(new Date());
        userDao.save(user);
        System.out.println(user.getRoleSet().size());
    }

    @Test
    public void testGet() {
        User user = userDao.getByMobile("123456");
        System.out.println(user.getId());
        System.out.println(user.getRoleSet().size());
    }
}
