package com.fintech.insurance.micro.support.service;

import com.fintech.insurance.micro.dto.support.OrganizationVO;
import com.fintech.insurance.micro.support.persist.dao.OrganizationDao;
import com.fintech.insurance.micro.support.persist.entity.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class OrganizationServiceImpl implements OrganizationService {

    @Autowired
    OrganizationDao organizationDao;

    @Override
    @Transactional(readOnly = true)
    public List<OrganizationVO> listOrganizationByName(String companyName) {
        List<Organization> organizationList = organizationDao.listOrganizationByName(companyName);
        List<OrganizationVO> organizationVOList = new ArrayList<>();
        if (null != organizationList && organizationList.size() > 0) {
            for (Organization organization : organizationList) {
                OrganizationVO organizationVO = this.entityToVO(organization);
                organizationVOList.add(organizationVO);
            }
        }
        if (organizationVOList == null || organizationVOList.size() <= 0) {
            return Collections.EMPTY_LIST;
        }
        return organizationVOList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganizationVO> listAllCompany(Integer rootId) {
        List<Organization> list = organizationDao.findAllByRootId(rootId);
        return entitiesToVOs(list);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganizationVO> queryAllCompanyByIds(List<Integer> ids) {
        List<Organization> list = organizationDao.queryAllCompanyByIds(ids);
        return entitiesToVOs(list);
    }

    @Override
    @Transactional(readOnly = true)
    public OrganizationVO getById(Integer organizationId) {
        return this.entityToVO(organizationDao.getById(organizationId));
    }

    private OrganizationVO entityToVO(Organization entity) {
        if (null == entity){
            return null;
        }
        OrganizationVO vo = new OrganizationVO();
        vo.setId(entity.getId());
        if (entity.getParentOrganization() != null) {
            vo.setParentId(entity.getParentOrganization().getId());
        }
        vo.setCompanyName(entity.getCompanyName());
        vo.setAreaCode(entity.getAreaCode());

        return vo;
    }

    private List<OrganizationVO> entitiesToVOs(List<Organization> entities) {
        if (entities == null || entities.size() <= 0) {
            return Collections.EMPTY_LIST;
        }

        List<OrganizationVO> resultList = new ArrayList<>();
        for (Organization org : entities) {
            resultList.add(entityToVO(org));
        }

        return resultList;
    }


}
