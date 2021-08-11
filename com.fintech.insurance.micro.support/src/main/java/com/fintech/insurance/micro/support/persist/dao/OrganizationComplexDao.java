package com.fintech.insurance.micro.support.persist.dao;

import com.fintech.insurance.micro.support.persist.entity.Organization;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface OrganizationComplexDao {

    List<Organization> listOrganizationByName(String companyName);

    List<Organization> queryAllCompanyByIds(List<Integer> ids);
}
