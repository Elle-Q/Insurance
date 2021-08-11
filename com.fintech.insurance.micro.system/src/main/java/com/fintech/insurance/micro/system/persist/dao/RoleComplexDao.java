package com.fintech.insurance.micro.system.persist.dao;

import com.fintech.insurance.micro.system.persist.entity.Role;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface RoleComplexDao {

    List<Role> listRole();

}
