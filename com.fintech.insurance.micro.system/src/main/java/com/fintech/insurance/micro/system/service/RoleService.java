package com.fintech.insurance.micro.system.service;

import com.fintech.insurance.micro.dto.system.RoleVO;

import java.util.List;

public interface RoleService {

    /**
     * 所有角色
     *
     * @return
     */
    List<RoleVO> listRoles();

}
