package com.fintech.insurance.micro.system.service;

import com.fintech.insurance.micro.dto.system.RoleVO;
import com.fintech.insurance.micro.system.persist.dao.RoleDao;
import com.fintech.insurance.micro.system.persist.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleDao roleDao;


    @Override
    @Transactional(readOnly = true)
    public List<RoleVO> listRoles() {
        List<Role> roleList = roleDao.listRole();
        List<RoleVO> roleVOList = new ArrayList<>();
        if (null != roleList && roleList.size() > 0) {
            for (Role role : roleList) {
                RoleVO roleVO = this.convertToVO(role);
                roleVOList.add(roleVO);
            }
        }
        if (roleList.size() < 1) {
            return Collections.emptyList();
        } else {
            return roleVOList;
        }
    }

    private RoleVO convertToVO(Role role) {
        if (null == role) {
            return null;
        }
        RoleVO roleVO = new RoleVO();
        roleVO.setId(role.getId());
        roleVO.setRoleCode(role.getRoleCode());
        roleVO.setName(role.getRoleName());
        roleVO.setAuthDesc(role.getRemark());
        return roleVO;
    }


}
