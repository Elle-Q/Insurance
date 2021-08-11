package com.fintech.insurance.micro.system.persist.dao;

import com.fintech.insurance.commons.enums.RoleType;
import com.fintech.insurance.commons.enums.UserType;
import com.fintech.insurance.components.persist.BaseEntityDaoImpl;
import com.fintech.insurance.micro.system.persist.entity.Role;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class RoleDaoImpl extends BaseEntityDaoImpl<Role, Integer> implements RoleComplexDao {
    @Override
    public List<Role> listRole() {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuffer hql = new StringBuffer("from Role r");
        hql.append(" where r.roleCode != :roleCode");
        params.put("roleCode", "admin");
        return this.findList(hql.toString(), 0, params);
    }
}
