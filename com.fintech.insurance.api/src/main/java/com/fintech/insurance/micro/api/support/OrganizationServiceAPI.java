package com.fintech.insurance.micro.api.support;

import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.micro.dto.support.OrganizationVO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Description: balala
 * @Author: qxy
 * @Date: 2017/11/15 11:50
 */
@RequestMapping(value = "/support/organization", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface OrganizationServiceAPI {

    @RequestMapping(value = "/list-organization-by-name", method = RequestMethod.GET)
    List<OrganizationVO> listOrganizationByName(@RequestParam(value = "companyName") String companyName);

    @RequestMapping(value = "/get-by-id", method = RequestMethod.GET)
    FintechResponse<OrganizationVO> getById(@RequestParam(value = "organizationId") Integer organizationId);

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    FintechResponse<List<OrganizationVO>> listAllCompany(@RequestParam(value = "rootId", defaultValue = "1") Integer rootId);

    @RequestMapping(value = "/query-by-ids", method = RequestMethod.POST)
    FintechResponse<List<OrganizationVO>> queryAllCompanyIds(@RequestBody List<Integer> ids);
}
