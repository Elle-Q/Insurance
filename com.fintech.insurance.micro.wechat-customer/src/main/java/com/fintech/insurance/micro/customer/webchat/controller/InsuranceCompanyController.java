package com.fintech.insurance.micro.customer.webchat.controller;

import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.annotation.RequireWechatLogin;
import com.fintech.insurance.components.web.BaseFintechWechatController;
import com.fintech.insurance.micro.vo.wechat.WeChatInsuranceCompanyVO;
import com.fintech.insurance.service.agg.CommonInsuranceCompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: Clayburn
 * @Description: 保险公司资料管理
 * @Date: 2017/11/9 11:25
 */
@RestController
@RequestMapping(value = "/wechat/customer/insurance-company", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RequireWechatLogin
public class InsuranceCompanyController extends BaseFintechWechatController {
    private static final Logger logger = LoggerFactory.getLogger(InsuranceCompanyController.class);

    @Autowired
    private CommonInsuranceCompanyService insuranceCompanyService;

    @GetMapping(value = "/insurance-company-list")
    public FintechResponse<List<WeChatInsuranceCompanyVO>> listAllInsuranceCompany() {
        return FintechResponse.responseData(insuranceCompanyService.queryAllInsuranceCompany());
    }
}
