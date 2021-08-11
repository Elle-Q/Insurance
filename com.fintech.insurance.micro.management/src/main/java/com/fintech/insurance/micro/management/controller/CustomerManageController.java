package com.fintech.insurance.micro.management.controller;

import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.BaseFintechManagementController;
import com.fintech.insurance.micro.dto.IdVO;
import com.fintech.insurance.micro.dto.customer.CustomerVO;
import com.fintech.insurance.micro.feign.customer.CustomerServiceFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @Description: 客户管理接口
 * @Author: Yong Li
 * @Date: 2017/12/7 13:16
 */
@RestController
@RequestMapping(value = "/management/customer", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class CustomerManageController extends BaseFintechManagementController {

    @Autowired
    private CustomerServiceFeign customerServiceFeign;

    /**
     * 根据添加查询所有客户信息
     * @param customerName 客户名称
     * @param channelOf  所属渠道
     * @param companyOf 所属公司
     * @param phone 电话
     * @param status 状态
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    FintechResponse<Pagination<CustomerVO>> pageAllCustomer(@RequestParam(value = "customerName", defaultValue = "") String customerName,
                                                            @RequestParam(value = "channelOf", defaultValue = "") String channelOf,
                                                            @RequestParam(value = "companyOf", defaultValue = "") String companyOf,
                                                            @RequestParam(value = "phone", defaultValue = "") String phone,
                                                            @RequestParam(value = "status", defaultValue = "") String status,
                                                            @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                                            @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) {
        FintechResponse<Pagination<CustomerVO>> result = customerServiceFeign.pageAllCustomer(customerName, channelOf, companyOf, phone, status, pageIndex, pageSize);
        if (!result.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(result);
        }
        return result;
    }

    /**
     * 冻结客户
     * @param
     */
    @RequestMapping(value = "/freeze", method = RequestMethod.POST)
    void freezeCustomer(@RequestBody IdVO vo) {
        FintechResponse<VoidPlaceHolder> result = customerServiceFeign.freezeCustomer(vo);

        if (!result.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(result);
        }
    }

    /**
     * 解冻客户
     * @param
     */
    @RequestMapping(value = "/unfreeze", method = RequestMethod.POST)
    void unfreezeCustomer(@RequestBody IdVO vo) {
        FintechResponse<VoidPlaceHolder> result = customerServiceFeign.unfreezeCustomer(vo);

        if (!result.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(result);
        }

    }
}
