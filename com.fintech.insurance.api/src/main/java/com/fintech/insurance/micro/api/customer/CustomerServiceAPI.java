package com.fintech.insurance.micro.api.customer;

import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.micro.dto.IdVO;
import com.fintech.insurance.micro.dto.common.BindUserWxOauthVO;
import com.fintech.insurance.micro.dto.common.OauthAccountVO;
import com.fintech.insurance.micro.dto.customer.CustomerAccountVO;
import com.fintech.insurance.micro.dto.customer.CustomerBankCardVO;
import com.fintech.insurance.micro.dto.customer.CustomerSimpleVO;
import com.fintech.insurance.micro.dto.customer.CustomerVO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RequestMapping(value = "/customer/customer", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface CustomerServiceAPI {
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
                                                           @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize);

    /**
     * 冻结客户
     * @param
     */
    @RequestMapping(value = "/freeze", method = RequestMethod.POST)
    FintechResponse<VoidPlaceHolder> freezeCustomer(@RequestBody IdVO vo);

    /**
     * 解冻客户
     * @param
     */
    @RequestMapping(value = "/unfreeze", method = RequestMethod.POST)
    FintechResponse<VoidPlaceHolder> unfreezeCustomer(@RequestBody IdVO vo);

    /**
     * 根据公众号的app id以及微信用户的openid获取客户信息
     * @param appid 公众号的app id
     * @param openid 微信openid
     * @return 客户信息
     */
    @GetMapping(value = "/find-oauth-cust")
    FintechResponse<CustomerVO> getCustomerByWxOpenid(@RequestParam(name = "appid") String appid, @RequestParam(name = "openid") String openid);

    /**
     * 根据公众号的app id以及微信用户的union id 获取客户信息
     * @param appid 公众号的appId
     * @param unionid 微信用户的union id
     * @return 客户信息
     */
    @GetMapping(value = "/find-unionid-cust")
    FintechResponse<CustomerVO> getCustomerByWxUnionid(@RequestParam(name = "appid") String appid, @RequestParam(name = "unionid") String unionid);

    /**
     * 绑定客户的微信帐户
     * @param bindUserWxOauthVO
     * @return 绑定成功则返回true，失败则返回false
     */
    @PostMapping(path = "/bind-oauth-acct")
    FintechResponse<Boolean>  bindCustomerWithWxAccount(@RequestBody BindUserWxOauthVO bindUserWxOauthVO);

    /**
     * 获取客户的openid
     * @param customerId 客户的id
     * @param appid 指定的微信公众号的app id
     * @return 客户在该公众号授权后的openid
     */
    @GetMapping(path = "/get-cust-openid")
    FintechResponse<String> getCustomerWxOpenid(@RequestParam(name = "customerId") Integer customerId, @RequestParam(name = "appid") String appid);

    /**
     * 获取客户的授权信息
     * @param customerId 客户id
     * @param oauthType 授权类型
     * @param oauthAppId 授权的app id
     * @return 用户的授权信息
     */
    @GetMapping(path = "/get-cust-oauth")
    FintechResponse<OauthAccountVO> getCustomerOauthAccount(@RequestParam(name = "customerId") Integer customerId,
                                           @RequestParam(name = "oauthType") String oauthType,
                                           @RequestParam(name = "oauthAppId") String oauthAppId);

    /**
     * 模糊查询客户信息
     * @param customerName
     * @return
     */
    @GetMapping(path = "/list-customer-by-name")
    FintechResponse<List<CustomerVO>> listCustomerByName(@RequestParam(name = "customerName", defaultValue = "") String customerName);

    /**
     * 根据客户id和渠道code查询客户信息
     * @param customerId    客户id
     * @param channelCode   渠道code
     * @return
     */
    @GetMapping(path = "/get-by-customerId-channelCode")
    FintechResponse<CustomerVO> getByCustomerIdAndChannelCode(@RequestParam(name = "customerId") Integer customerId,
                                                              @RequestParam(name = "channelCode") String channelCode);

    /**
     * WX保存客户
     * @param
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    FintechResponse<Integer> saveCustomerInfo(@RequestBody CustomerVO customerVO);

    /**
     * WX查询客户
     * @param
     */
    @RequestMapping(value = "/get")
    FintechResponse<CustomerVO> getCustomerAccountInfoById(@RequestParam(value = "id") Integer id);

    /**
     * WX删除客户
     * @param
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    FintechResponse<VoidPlaceHolder> deleteCustomerAccountInfoById(@RequestBody IdVO idVO);

    /**
     * WX查询用户渠道编号集合
     * @param
     */
    @RequestMapping(value = "/find-customer-channel-code", method = RequestMethod.GET)
    FintechResponse<List<String>> findCustomerChannelCodeByCustomerId(@RequestParam(value = "customerId") @NotNull Integer customerId);

    /**
     * WX查询用户锁定冻结状态
     * @param
     */
    @RequestMapping(value = "/find-customer-locked-status", method = RequestMethod.GET)
    FintechResponse<Boolean> getCustomerLockedStatusById(@RequestParam(value = "id") @NotNull Integer id);

    /**
     * WX根据渠道编号查询渠道账户下的客户信息
     * @param currentUserChannelCode
     * @return
     */
    @RequestMapping(value = "/page-customer-by-channnel-code", method = RequestMethod.GET)
    FintechResponse<Pagination<CustomerVO>> pageCustomerByChannnelCode(@RequestParam(value = "currentUserChannelCode") String currentUserChannelCode,
                                                      @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                                      @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize);

    /* * WX根据手机号获取客户信息
     * @param mobile
     * @return
     */
    @RequestMapping(value = "/get-customer-account-info-by-mobile", method = RequestMethod.GET)
    FintechResponse<CustomerVO> getCustomerAccountInfoByMobile(@RequestParam(value = "mobile")  String mobile);

    @GetMapping(value = "/simple-info")
    FintechResponse<CustomerSimpleVO> getCustomerSimpleInfo(@RequestParam(value = "customerId", required = false)Integer customerId);

    @PostMapping(value = "/update-bankcard")
    void updateBankCardInfo(@RequestBody CustomerAccountVO customerAccountVO);

    @RequestMapping(value = "/get-by-customer-id", method = RequestMethod.GET)
    FintechResponse<CustomerVO> getByCustomerId(@RequestParam(value = "customerInfoId") Integer customerInfoId);

    /**
     * 获得客户的银行卡信息
     * @param customerId
     * @return
     */
    @GetMapping(path = "/bank-card")
    FintechResponse<CustomerBankCardVO> getCustomerBankCard(@RequestParam(value = "customerId") Integer customerId);

    @GetMapping(value = "/get-bankcard-by-number")
    FintechResponse<CustomerBankCardVO> getBankCardInfoByNumber(@RequestParam(value = "customerId") Integer customerId,
                                                                @RequestParam(value = "bankcardNumber") String bankcardNumber);

    @GetMapping(path = "/list-by-customerid-and-channelcode")
    FintechResponse<List<CustomerVO>> listByCustomerIdAndChannelCode(@RequestParam(value = "accountId") Integer accountId,
                                                                     @RequestParam(value = "channelOf") String channelOf);

    @GetMapping(path = "/find-channelcode-by-customer-id")
    FintechResponse<List<String>> findChannelCodesByCustomerId(@RequestParam(value = "customerId") Integer customerId);

    @GetMapping(path = "/list-by-account-id")
    FintechResponse<List<CustomerVO>> listByAccountId(@RequestParam(value = "customerAccountId") Integer customerAccountId);

    @GetMapping(path = "/list-customer-account-info-by-mobile")
    FintechResponse<List<CustomerVO>> listCustomerAccountInfoByMobile(@RequestParam(value = "phoneNumber") String phoneNumber);


    /**
     * WX根据渠道用户查询渠道用户下的客户信息
     * @param channelUserId
     * @return
     */
    @RequestMapping(value = "/page-customer-by-channneluserid", method = RequestMethod.GET)
    FintechResponse<Pagination<CustomerVO>> pageCustomerByChannnelUserId(@RequestParam(value = "channelUserId") Integer channelUserId,
                                                                         @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                                                         @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize);

    /**
     * WX获取当前登录客户的基本信息
     * @return
     */
    @RequestMapping(value = "/get-customer-account-by-id", method = RequestMethod.GET)
    FintechResponse<CustomerAccountVO> getCustomerAccountById(@RequestParam(value = "currentLoginUserId") Integer currentLoginUserId);

}
