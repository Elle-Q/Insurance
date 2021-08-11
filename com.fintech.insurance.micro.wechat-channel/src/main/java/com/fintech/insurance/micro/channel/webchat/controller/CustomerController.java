package com.fintech.insurance.micro.channel.webchat.controller;

import com.fintech.insurance.commons.constants.BasicConstants;
import com.fintech.insurance.commons.enums.NotificationEvent;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.annotation.RequireWechatLogin;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.BaseFintechWechatController;
import com.fintech.insurance.micro.dto.customer.CustomerVO;
import com.fintech.insurance.micro.dto.system.UserVO;
import com.fintech.insurance.micro.dto.thirdparty.ImageVO;
import com.fintech.insurance.micro.dto.thirdparty.sms.SMSCheckVerificationParamVO;
import com.fintech.insurance.micro.feign.customer.CustomerServiceFeign;
import com.fintech.insurance.micro.feign.system.SysUserServiceFeign;
import com.fintech.insurance.micro.feign.thirdparty.QiniuBusinessServiceFeign;
import com.fintech.insurance.micro.feign.thirdparty.sms.SMSServiceFeign;
import com.fintech.insurance.micro.vo.wechat.WeChatCustomerVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 渠道客户管理
 * @Author: qxy
 * @Date: 2017/12/6 18:54
 */
@RestController
@RequestMapping(value = "/wechat/channel/customer", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RequireWechatLogin
public class CustomerController extends BaseFintechWechatController {

    @Autowired
    private CustomerServiceFeign customerServiceFeign;

    @Autowired
    private SMSServiceFeign smsServiceFeign;

    @Autowired
    private SysUserServiceFeign sysUserServiceFeign;

    /**
     * 查询所有客户
     */
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public FintechResponse<Pagination<WeChatCustomerVO>> queryCustomers(@RequestParam(value = "pageIndex", defaultValue = BasicConstants.DEFAULT_PAGE_INDEX) Integer pageIndex,
                                                                        @RequestParam(value = "pageSize", defaultValue = BasicConstants.DEFAULT_PAGE_INDEX) Integer pageSize) {

        Integer currentUsreId = getCurrentUserId();
        FintechResponse<UserVO> userVOFintechResponse = sysUserServiceFeign.getUserById(currentUsreId);
        if (!userVOFintechResponse.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(userVOFintechResponse);
        }
        FintechResponse<Pagination<CustomerVO>> fintechResponse = customerServiceFeign.pageCustomerByChannnelUserId(currentUsreId, pageIndex, pageSize);
        if (!fintechResponse.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(fintechResponse);
        }
        Pagination<CustomerVO> customerVOPagination = fintechResponse.getData();
        List<WeChatCustomerVO> weChatCustomerVOList = new ArrayList<>();
        if (null != customerVOPagination.getItems() && customerVOPagination.getItems().size() > 0) {
            for (CustomerVO c : customerVOPagination.getItems()) {
                WeChatCustomerVO weChatCustomerVO = this.convertToWeChatVO(c);
                weChatCustomerVOList.add(weChatCustomerVO);
            }
        }
        return FintechResponse.responseData(Pagination.createInstance(pageIndex, pageSize, customerVOPagination.getTotalRowsCount(), weChatCustomerVOList));
    }

    /**
     * 获取客户详情信息
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public FintechResponse<WeChatCustomerVO> getCustomerById(@RequestParam(value = "id") Integer id){
        FintechResponse<CustomerVO> customerVOFintechResponse = customerServiceFeign.getCustomerAccountInfoById(id);
        if (!customerVOFintechResponse.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(customerVOFintechResponse);
        }
        CustomerVO customerVO = customerVOFintechResponse.getData();
        if (null == customerVO) {
            throw new FInsuranceBaseException(107024);
        }
        return FintechResponse.responseData(this.convertToWeChatVO(customerVO));
    }


    /**
     * 保存编辑客户
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public FintechResponse<Integer> saveCustomer(@RequestBody @Validated WeChatCustomerVO weChatCustomerVO){
       //校验短信验证码
        SMSCheckVerificationParamVO smsCheckVerificationParamVO = new SMSCheckVerificationParamVO();
        smsCheckVerificationParamVO.setEventCode(NotificationEvent.BIND_CARD.getCode());
        smsCheckVerificationParamVO.setPhoneNumber(weChatCustomerVO.getPhone());
        smsCheckVerificationParamVO.setSequenceId(weChatCustomerVO.getSequenceId());
        smsCheckVerificationParamVO.setVerification(weChatCustomerVO.getVerification());
        FintechResponse<Boolean> fintechResponse = smsServiceFeign.checkSMSVerification(smsCheckVerificationParamVO);
        if (!fintechResponse.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(fintechResponse);
        }
        Boolean flag = fintechResponse.getData();
        if (!flag) {
            throw new FInsuranceBaseException(107023);//短信校验出错
        }
        CustomerVO customerVO = this.converToInternalVO(weChatCustomerVO);

        //保存客户信息
        FintechResponse<Integer> integerFintechResponse =  customerServiceFeign.saveCustomerInfo(customerVO);
        if (!integerFintechResponse.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(integerFintechResponse);
        }
        return integerFintechResponse;
    }

    //转化为内部VO
    private CustomerVO converToInternalVO(WeChatCustomerVO weChatCustomerVO) {
        if (null == weChatCustomerVO) {
            return null;
        }
        CustomerVO customerVO = new CustomerVO();
        customerVO.setAccountInfoId(weChatCustomerVO.getCustomerAccountInfoId());
        customerVO.setCompanyOf(weChatCustomerVO.getCompanyOf());
        customerVO.setBusinessLicence(weChatCustomerVO.getBusinessLicence());
        customerVO.setLicencePicture(weChatCustomerVO.getLicencePicture());
        customerVO.setName(weChatCustomerVO.getName());
        customerVO.setIdNum(weChatCustomerVO.getIdNum());
        customerVO.setBankCard(weChatCustomerVO.getBankCard());
        customerVO.setIdBack(weChatCustomerVO.getIdBack());
        customerVO.setIdFront(weChatCustomerVO.getIdFront());
        customerVO.setBankCardPicture(weChatCustomerVO.getBankCardPicture());
        customerVO.setPhone(weChatCustomerVO.getPhone());
        customerVO.setIsLocked(0);
        customerVO.setChannelOf(getCurrentUserChannelCode());
        customerVO.setChannelUserId(getCurrentUserId());//标志当前客户是由哪个渠道用户创建的
        return customerVO;
    }

    //转化为微信端VO
    private WeChatCustomerVO convertToWeChatVO(CustomerVO c) {
        if (null == c) {
            return null;
        }
        WeChatCustomerVO weChatCustomerVO = new WeChatCustomerVO();
        weChatCustomerVO.setCustomerAccountInfoId(c.getAccountInfoId());
        weChatCustomerVO.setCustomerId(c.getAccountId());
        weChatCustomerVO.setBankCard(c.getBankCard());

        weChatCustomerVO.setBankCardPicture(c.getBankCardPicture());
        weChatCustomerVO.setBankCardPictureUrl(c.getBankCardPictureUrl());
        weChatCustomerVO.setBankCardPictureNarrowUrl(c.getBankCardPictureNarrowUrl());

        weChatCustomerVO.setLicencePicture(c.getLicencePicture());
        weChatCustomerVO.setLicencePictureUrl(c.getLicencePictureUrl());
        weChatCustomerVO.setLicencePictureNarrowUrl(c.getLicencePictureNarrowUrl());

        weChatCustomerVO.setIdBack(c.getIdBack());
        weChatCustomerVO.setIdBackUrl(c.getIdBackUrl());
        weChatCustomerVO.setIdBackNarrowUrl(c.getIdBackNarrowUrl());

        weChatCustomerVO.setIdFront(c.getIdFront());
        weChatCustomerVO.setIdFrontUrl(c.getIdFrontUrl());
        weChatCustomerVO.setIdFrontNarrowUrl(c.getIdFrontNarrowUrl());

        weChatCustomerVO.setBusinessLicence(c.getBusinessLicence());
        weChatCustomerVO.setIdNum(c.getIdNum());
        weChatCustomerVO.setName(c.getName());
        weChatCustomerVO.setIsLocked(c.getIsLocked());
        weChatCustomerVO.setPhone(c.getPhone());
        weChatCustomerVO.setCompanyOf(c.getCompanyOf());
        weChatCustomerVO.setChannelCode(c.getChannelOf());
        weChatCustomerVO.setChannelUserId(c.getChannelUserId());
        return weChatCustomerVO;
    }

}
