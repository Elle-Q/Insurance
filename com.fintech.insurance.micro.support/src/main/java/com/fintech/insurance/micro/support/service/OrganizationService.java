package com.fintech.insurance.micro.support.service;

import com.fintech.insurance.micro.dto.support.OrganizationVO;

import java.util.List;

public interface OrganizationService {
    /**
     * 根据公司名称模糊查询公司信息
     * @param companyName   公司名称
     * @return
     */
    List<OrganizationVO> listOrganizationByName(String companyName);

    OrganizationVO getById(Integer organizationId);

    List<OrganizationVO> listAllCompany(Integer rootId);

    List<OrganizationVO> queryAllCompanyByIds(List<Integer> ids);
}
