package com.fintech.insurance.micro.customer.controller;

import com.fintech.insurance.commons.enums.ApplicationType;
import com.fintech.insurance.commons.enums.CustomerStatus;
import com.fintech.insurance.commons.enums.OauthType;
import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.BaseFintechController;
import com.fintech.insurance.micro.api.customer.CustomerServiceAPI;
import com.fintech.insurance.micro.customer.service.CustomerService;
import com.fintech.insurance.micro.dto.IdVO;
import com.fintech.insurance.micro.dto.common.BindUserWxOauthVO;
import com.fintech.insurance.micro.dto.common.OauthAccountVO;
import com.fintech.insurance.micro.dto.customer.CustomerAccountVO;
import com.fintech.insurance.micro.dto.customer.CustomerBankCardVO;
import com.fintech.insurance.micro.dto.customer.CustomerSimpleVO;
import com.fintech.insurance.micro.dto.customer.CustomerVO;
import com.fintech.insurance.micro.dto.finance.BankCardVerifyResult;
import com.fintech.insurance.micro.dto.finance.EnterpriseBankVO;
import com.fintech.insurance.micro.dto.thirdparty.BestsignUserInfoVO;
import com.fintech.insurance.micro.feign.finance.EnterpriseBankServiceFeign;
import com.fintech.insurance.micro.feign.finance.PaymentServiceFeign;
import com.fintech.insurance.micro.feign.thirdparty.BestsignServiceFeign;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: Clayburn
 * @Description: 客户管理
 * @Date: 2017/11/9 17:05
 */
@RestController
public class CustomerController extends BaseFintechController implements CustomerServiceAPI {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private CustomerService customerService;


    @Autowired
    private PaymentServiceFeign paymentServiceFeign;

    @Autowired
    private BestsignServiceFeign bestsignServiceFeign;

    @Autowired
    private EnterpriseBankServiceFeign enterpriseBankServiceFeign;

    @Override
    public FintechResponse<Pagination<CustomerVO>> pageAllCustomer(@RequestParam(value = "customerName", defaultValue = "") String customerName,
                                                                   @RequestParam(value = "channelOf", defaultValue = "") String channelOf,
                                                                   @RequestParam(value = "companyOf", defaultValue = "") String companyOf,
                                                                   @RequestParam(value = "phone", defaultValue = "") String phone,
                                                                   @RequestParam(value = "status", defaultValue = "") String status,
                                                                   @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                                                   @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) throws FInsuranceBaseException {
        CustomerStatus customerStatus = null;
        try {
            if (StringUtils.isNotBlank(status)) {
                customerStatus = CustomerStatus.codeOf(status);
            }
        } catch (Exception e) {
            logger.error("枚举转换失败！" + status + "->" + CustomerStatus.class.getSimpleName());
            throw new FInsuranceBaseException(103001);
        }
        return FintechResponse.responseData(customerService.queryAllCustomer(customerName, channelOf, companyOf, phone, customerStatus, pageIndex, pageSize));
    }


    @Override
    public FintechResponse<VoidPlaceHolder> freezeCustomer(@RequestBody IdVO vo) {
        customerService.freezeOrNot(vo.getId(), CustomerStatus.FREEZE);
        return FintechResponse.responseData(VoidPlaceHolder.instance());
    }

    @Override
    public FintechResponse<VoidPlaceHolder> unfreezeCustomer(@RequestBody IdVO vo) {
        customerService.freezeOrNot(vo.getId(), CustomerStatus.NORMAL);
        return FintechResponse.responseData(VoidPlaceHolder.instance());
    }

    @Override
    public FintechResponse<List<CustomerVO>> listCustomerByName(@RequestParam(name = "customerName", defaultValue = "") String customerName) {
        return FintechResponse.responseData(customerService.listCustomerByName(customerName));
    }

    @Override
    public FintechResponse<CustomerVO> getByCustomerIdAndChannelCode(@RequestParam(name = "customerId") Integer customerId,
                                                                     @RequestParam(name = "channelCode") String channelCode) {
        return FintechResponse.responseData(customerService.getCustomerByCustomerIdAndChannelCode(customerId, channelCode));
    }

    @Override
    public FintechResponse<Integer> saveCustomerInfo(@RequestBody CustomerVO customerVO) {
        //四要素认证
        FintechResponse<BankCardVerifyResult> resultFintechResponse = paymentServiceFeign.verifyBankCard(customerVO.getName(), customerVO.getIdNum(), customerVO.getBankCard(), customerVO.getPhone());
        if (!resultFintechResponse.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(resultFintechResponse);
        }
        if (!resultFintechResponse.getData().getIsSuccess()) {
            throw new FInsuranceBaseException(107028, new Object[]{resultFintechResponse.getData().getFailedMessage()});
        }
        if (null != customerVO.getAccountInfoId()) {
            CustomerVO c = customerService.getCustomerAccountInfoById(customerVO.getAccountInfoId());
            if (null == c) {
                throw new FInsuranceBaseException(107040);
            }
            if (null == c.getCertificationId()) {//检查客户是否已经有上上签账户,没有则生成
                customerVO.setCertificationId(this.createPersonalUserAccount(customerVO));
            }
        } else {
            customerVO.setCertificationId(this.createPersonalUserAccount(customerVO));
        }

        //检查是否支持当前银行code
        FintechResponse<EnterpriseBankVO> enterpriseBankVOFintechResponse = enterpriseBankServiceFeign.getEnterpriseBank(ApplicationType.VERIFY.getCode(), resultFintechResponse.getData().getBankCode());
        if (!enterpriseBankVOFintechResponse.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(enterpriseBankVOFintechResponse);
        }
        if (null == enterpriseBankVOFintechResponse.getData()) {
            throw new FInsuranceBaseException(107045);
        }
        customerVO.setBankName(resultFintechResponse.getData().getBankCode());
        return FintechResponse.responseData(customerService.saveCustomerInfo(customerVO));
    }

    //为客户生成上上签账户
    private String createPersonalUserAccount(CustomerVO customerVO) {

        BestsignUserInfoVO bestsignUserInfoVO = new BestsignUserInfoVO();
        bestsignUserInfoVO.setUserName(customerVO.getName());
        bestsignUserInfoVO.setUserIdentityNum(customerVO.getIdNum());
        bestsignUserInfoVO.setMobile(customerVO.getPhone());
        FintechResponse<String> response = bestsignServiceFeign.createPersonalUserAccount(bestsignUserInfoVO);
        if (!response.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(response);
        }
        return response.getData();
    }


    @Override
    public FintechResponse<CustomerVO> getCustomerAccountInfoById(@RequestParam(value = "id") Integer id) {
        CustomerVO customerVO = customerService.getCustomerAccountInfoById(id);
        return FintechResponse.responseData(customerVO);
    }

    @Override
    public FintechResponse<VoidPlaceHolder> deleteCustomerAccountInfoById(@RequestBody IdVO idVO) {
        return FintechResponse.responseData(VoidPlaceHolder.instance());
    }

    @Override
    public FintechResponse<List<String>> findCustomerChannelCodeByCustomerId(@RequestParam(value = "customerId") Integer customerId) {
        List<String> list = customerService.findCustomerChannelCodeByCustomerId(customerId);
        return  FintechResponse.responseData(list);
    }

    @Override
    public FintechResponse<Boolean> getCustomerLockedStatusById(@RequestParam(value = "id") Integer id) {
        return FintechResponse.responseData(customerService.getCustomerLockedStatusById(id));
    }

    @Override
    public FintechResponse<Pagination<CustomerVO>> pageCustomerByChannnelCode(String currentUserChannelCode, Integer pageIndex, Integer pageSize) {
        return FintechResponse.responseData(customerService.pageCustomerByChannnelCode(currentUserChannelCode, pageIndex, pageSize));
    }

    @Override
    public FintechResponse<Pagination<CustomerVO>> pageCustomerByChannnelUserId(Integer channelUserId, Integer pageIndex, Integer pageSize) {
        return FintechResponse.responseData(customerService.pageCustomerByChannnelUserId(channelUserId, pageIndex, pageSize));
    }

    @Override
    public FintechResponse<CustomerVO> getCustomerAccountInfoByMobile(@RequestParam(value = "mobile")  String mobile) {
        if (StringUtils.isEmpty(mobile)) {
            throw new FInsuranceBaseException(102001);
        }
        CustomerVO vo = customerService.getCustomerAccountInfoByMobile(mobile);
        return FintechResponse.responseData(vo);
    }

    @Override
    public FintechResponse<CustomerSimpleVO> getCustomerSimpleInfo(@RequestParam(value = "customerId", required = false)Integer customerId) {
        //获取登录人id
        if(customerId == null) {
             customerId = this.getCurrentLoginUserId();
        }
        return FintechResponse.responseData(customerService.getCustomerSimpleInfo(customerId));
    }

    @Override
    public void updateBankCardInfo(@RequestBody CustomerAccountVO customerAccountVO) {
        customerService.updateBankCard(customerAccountVO);
    }

    @Override
    public FintechResponse<CustomerVO> getByCustomerId(@RequestParam(value = "customerInfoId") Integer customerInfoId) {
        return FintechResponse.responseData(customerService.getCustomerAccountInfoById(customerInfoId));
    }

    @Override
    public FintechResponse<CustomerBankCardVO> getCustomerBankCard(@RequestParam(value = "customerId") Integer customerId) {
        if (customerId == null) {
            throw new FInsuranceBaseException(103003);
        } else {
            CustomerBankCardVO vo = this.customerService.getCustomerBankCardVO(customerId);
            return FintechResponse.responseData(vo);
        }
    }

    @GetMapping(value = "/get-bankcard-by-number")
    public FintechResponse<CustomerBankCardVO> getBankCardInfoByNumber(@RequestParam(value = "customerId") Integer customerId,
                                                                @RequestParam(value = "bankcardNumber") String bankcardNumber) {
        return FintechResponse.responseData(this.customerService.getCustomerBankCardVOByCardNumber(customerId, bankcardNumber));
    }

    @Override
    public FintechResponse<List<CustomerVO>> listByCustomerIdAndChannelCode(Integer accountId, String channelOf) {
        return FintechResponse.responseData(customerService.listCustomerVOByCustomerIdAndChannelCode(accountId, channelOf));
    }

    @Override
    public FintechResponse<List<String>> findChannelCodesByCustomerId(Integer customerId) {
        return FintechResponse.responseData(customerService.findCustomerChannelCodeByCustomerId(customerId));
    }

    @Override
    public FintechResponse<CustomerVO> getCustomerByWxOpenid(@RequestParam(value = "appid") String appid, @RequestParam(value = "openid") String openid) {
        if (StringUtils.isEmpty(appid) || StringUtils.isEmpty(openid)) {
            throw new FInsuranceBaseException(103003);
        } else {
            CustomerVO customerVO = this.customerService.getCustomerByWxOpenid(appid, openid);
            if (customerVO == null) {
                throw new FInsuranceBaseException(103002);//客户信息不存在
            } else {
                return FintechResponse.responseData(customerVO);
            }
        }
    }

    @Override
    public FintechResponse<CustomerVO> getCustomerByWxUnionid(@RequestParam(value = "appid") String appid, @RequestParam(value = "unionid") String unionid) {
        if (StringUtils.isEmpty(unionid)) {
            throw new FInsuranceBaseException(103003);
        } else {
            CustomerVO customerVO = this.customerService.getCustomerByWxUnionid(unionid);
            if (customerVO == null) {
                throw new FInsuranceBaseException(103002);
            } else {
                return FintechResponse.responseData(customerVO);
            }
        }
    }

    @Override
    public FintechResponse<Boolean> bindCustomerWithWxAccount(@RequestBody BindUserWxOauthVO bindUserWxOauthVO) {
        if (bindUserWxOauthVO == null || bindUserWxOauthVO.getUserId() == null || StringUtils.isEmpty(bindUserWxOauthVO.getAppid()) || bindUserWxOauthVO.getWxMpUser() == null) {
            throw new FInsuranceBaseException(103004); //绑定请求出错
        } else {
            boolean bindResult = this.customerService.bindUserWithWxAccount(bindUserWxOauthVO.getUserId(), bindUserWxOauthVO.getAppid(), bindUserWxOauthVO.getWxMpUser());
            if (bindResult) {
                return FintechResponse.responseOk();
            } else {
                throw new FInsuranceBaseException(103005); //绑定失败，请稍后再再试
            }
        }
    }

    @Override
    public FintechResponse<String> getCustomerWxOpenid(Integer customerId, String appid) {
        OauthAccountVO oauthVO = this.customerService.getUserOauth(customerId, OauthType.WECHAT_MP.getCode(), appid);
        if (oauthVO == null) {
            throw new FInsuranceBaseException(103008);
        } else {
            return FintechResponse.responseData(oauthVO.getOauthAccount());
        }
    }

    @Override
    public FintechResponse<OauthAccountVO> getCustomerOauthAccount(Integer customerId, String oauthType, String oauthAppId) {
        OauthAccountVO oauthVO = this.customerService.getUserOauth(customerId, oauthType, oauthAppId);
        if (oauthVO == null) {
            logger.error("customer Oauth Account Info: customerId={}, oauthType={}, oauthAppId={}", customerId, oauthType, oauthAppId);
            throw new FInsuranceBaseException(103008);
        } else {
            return FintechResponse.responseData(oauthVO);
        }
    }

    @Override
    public FintechResponse<List<CustomerVO>> listByAccountId(Integer customerAccountId) {
        return FintechResponse.responseData(customerService.listByAccountId(customerAccountId));
    }

    @Override
    public FintechResponse<List<CustomerVO>> listCustomerAccountInfoByMobile(String phoneNumber) {
        return FintechResponse.responseData(customerService.listCustomerAccountInfoByMobile(phoneNumber));
    }

    @Override
    public FintechResponse<CustomerAccountVO> getCustomerAccountById(Integer currentLoginUserId) {
        return FintechResponse.responseData(customerService.getCustomerAccountById(currentLoginUserId));
    }
}
