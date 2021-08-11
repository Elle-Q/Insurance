package com.fintech.insurance.micro.support.persist;

import com.fintech.insurance.micro.support.persist.dao.OrganizationDao;
import com.fintech.insurance.micro.support.persist.entity.Organization;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class OrganizationDaoTests {

    @Autowired
    private OrganizationDao organizationDao;

    @Test
    public void testSave() {
        Organization organization = new Organization();
        organization.setAreaCode("51");
        organization.setCreateBy(1);
        organization.setCompanyName("良品铺子");
        organization.setOrgnizationSequence("1");

        Organization parentOrganization = new Organization();
        parentOrganization.setAreaCode("25");
        parentOrganization.setCreateBy(1);
        parentOrganization.setCompanyName("三只松鼠");
        parentOrganization.setOrgnizationSequence("1");
        organizationDao.save(parentOrganization);

        Organization rootOrganization = new Organization();
        rootOrganization.setAreaCode("25");
        rootOrganization.setCreateBy(1);
        rootOrganization.setCompanyName("两只王八");
        rootOrganization.setOrgnizationSequence("1");
        organizationDao.save(rootOrganization);

        organization.setParentOrganization(parentOrganization);
        organization.setRootOrganization(rootOrganization);
        organizationDao.save(organization);
    }
}
