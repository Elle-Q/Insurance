package com.fintech.insurance.micro.support.controller;

import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.BaseFintechController;
import com.fintech.insurance.micro.api.support.OrganizationServiceAPI;
import com.fintech.insurance.micro.dto.support.OrganizationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description: balala
 * @Author: qxy
 * @Date: 2017/11/15
 */
@RestController
public class OrganizationController extends BaseFintechController implements OrganizationServiceAPI {

    @Autowired
    com.fintech.insurance.micro.support.service.OrganizationService organizationService;

    @Override
    public List<OrganizationVO> listOrganizationByName(@RequestParam(value = "companyName") String companyName) {
        return organizationService.listOrganizationByName(companyName);
    }

    @Override
    public FintechResponse<OrganizationVO> getById(@RequestParam(value = "organizationId") Integer organizationId) {
        OrganizationVO data = organizationService.getById(organizationId);
        return FintechResponse.responseData(data);
    }

    @Override
    public FintechResponse<List<OrganizationVO>> listAllCompany(@RequestParam(value = "rootId", defaultValue = "1") Integer rootId) {
        return FintechResponse.responseData(organizationService.listAllCompany(rootId));
    }

    @Override
    public FintechResponse<List<OrganizationVO>> queryAllCompanyIds(@RequestBody List<Integer> ids) {
        if(ids == null || ids.size()<1 ){
            throw new FInsuranceBaseException(102001);
        }
        return FintechResponse.responseData(organizationService.queryAllCompanyByIds(ids));
    }
}
