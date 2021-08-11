package com.fintech.insurance.micro.support.persist.dao;

import com.fintech.insurance.components.persist.BaseEntityDaoImpl;
import com.fintech.insurance.micro.support.persist.entity.Organization;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrganizationDaoImpl extends BaseEntityDaoImpl<Organization, Integer> implements OrganizationComplexDao {
    @Override
    public List<Organization> listOrganizationByName(String companyName) {
        StringBuilder hql = new StringBuilder(" from Organization o where 1 = 1");
        Map<String, Object> params = new HashMap<String, Object>();

        if (!StringUtils.isEmpty(companyName)) {
            hql.append(" and o.companyName like :companyName");
            params.put("companyName", "%" + companyName + "%");
        }
        hql.append(" order by o.createAt ");
        return this.findList(hql.toString(), 0, params);
    }

    @Override
    public List<Organization> queryAllCompanyByIds(List<Integer> ids) {
        StringBuilder hql = new StringBuilder(" from Organization o where 1 = 1");
        Map<String, Object> params = new HashMap<String, Object>();

        if (ids != null) {
            hql.append(" and o.id in :ids");
            params.put("ids", ids);
        }
        hql.append(" order by o.createAt desc ");
        return this.findList(hql.toString(), 0, params);
    }
}
