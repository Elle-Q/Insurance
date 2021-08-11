package com.fintech.insurance.micro.system.persist.dao;

import com.fintech.insurance.commons.enums.UserType;
import com.fintech.insurance.components.persist.BaseEntityDaoImpl;
import com.fintech.insurance.micro.system.persist.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserDaoImpl extends BaseEntityDaoImpl<User, Integer> implements UserComplexDao {

    @Override
    public List<User> listUser(String mobile, String name, Integer isLocked, String userType) {
        StringBuilder hql = new StringBuilder(" from User u where 1 = 1");
        Map<String, Object> params = new HashMap<String, Object>();

        if (!StringUtils.isEmpty(mobile)) {
            hql.append(" and u.mobilePhone like :mobile");
            params.put("mobile", "%" + mobile + "%");
        }
        if (!StringUtils.isEmpty(name)) {
            hql.append(" and u.userName like :name");
            params.put("name", "%" + name + "%");
        }
        if (isLocked != null) {
            Boolean lock = isLocked == 0 ? false : true;
            hql.append(" and u.isLocked = :isLocked");
            params.put("isLocked", lock);
        }
        hql.append(" and u.userType = :userType");
        params.put("userType", userType);
        hql.append(" order by u.createAt desc");

        return this.findList(hql.toString(), 0, params);
    }

    @Override
    public List<User> listChannelUserByMobile(String mobile) {
        StringBuilder hql = new StringBuilder(" from User u where 1 = 1");
        Map<String, Object> params = new HashMap<String, Object>();

        if (!StringUtils.isEmpty(mobile)) {
            hql.append(" and u.mobilePhone like :mobile");
            params.put("mobile", "%" + mobile + "%");
        }
        hql.append(" and u.userType = :userType and u.isChannelAdmin = true");
        params.put("userType", UserType.CHANNEL.getCode());
        return this.findList(hql.toString(), 0, params);
    }

    @Override
    public List<User> listChannelUserByCode(String channelCode) {
        StringBuilder hql = new StringBuilder(" from User u where 1 = 1");
        Map<String, Object> params = new HashMap<>();

        if (!StringUtils.isEmpty(channelCode)) {
            hql.append(" and u.channelCode = :channelCode");
            params.put("channelCode", channelCode);
        }
        hql.append(" and u.userType = :userType ");
        params.put("userType", UserType.CHANNEL.getCode());
        hql.append(" order by u.id ");
        return this.findList(hql.toString(), 0, params);
    }

/*    @Override
    public List<User> listUsersByRoleCode(String roleCode) {
        if(StringUtils.isBlank(roleCode)){
            return null;
        }
        StringBuilder hql = new StringBuilder(" from User u join u.roleSet r where 1 = 1");
        Map<String, Object> params = new HashMap<>();
        hql.append(" and r.roleCode = :roleCode");
        params.put("roleCode", roleCode);
        return this.findList(hql.toString(), 0, params);
    }*/

    @Override
    public Page<User> pageChannelUserByCode(String channelCode, Integer pageIndex, Integer pageSize) {
        StringBuilder hql = new StringBuilder(" from User u where u.isChannelAdmin = 0 ");
        Map<String, Object> params = new HashMap<String, Object>();
        if (!StringUtils.isEmpty(channelCode)) {
            hql.append(" and u.channelCode = :channelCode");
            params.put("channelCode", channelCode);
        }
        hql.append(" and u.userType = :userType ");
        params.put("userType", UserType.CHANNEL.getCode());

        String countSql = "select count(u) " + hql.toString();
        hql.append(" order by u.createAt desc");
        String querySql = "select u " + hql.toString();
        return this.findPagination(querySql, countSql, params, pageIndex, pageSize);
    }

}
