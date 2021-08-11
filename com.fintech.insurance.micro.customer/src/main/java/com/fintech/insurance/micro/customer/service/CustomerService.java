package com.fintech.insurance.micro.customer.service;

import com.fintech.insurance.commons.enums.CustomerStatus;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.micro.dto.common.OauthAccountVO;
import com.fintech.insurance.micro.dto.customer.CustomerAccountVO;
import com.fintech.insurance.micro.dto.customer.CustomerBankCardVO;
import com.fintech.insurance.micro.dto.customer.CustomerSimpleVO;
import com.fintech.insurance.micro.dto.customer.CustomerVO;
import me.chanjar.weixin.mp.bean.result.WxMpUser;

import java.util.List;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/11/14 13:53
 */

public interface CustomerService {
    /**
     * 条件查询所有用户
     *
     * @param name           客户名称
     * @param channelOf      所属渠道
     * @param companyOf      所属公司
     * @param phone          电话
     * @param customerStatus 客户状态
     * @param pageIndex
     * @param pageSize
     * @return
     */
    Pagination<CustomerVO> queryAllCustomer(String name, String channelOf, String companyOf, String phone,
                                            CustomerStatus customerStatus, Integer pageIndex, Integer pageSize);

    /**
     * freeze 冻结客户 normal 解冻客户
     * @param id
     * @param operationType
     */
    void freezeOrNot(Integer id, CustomerStatus operationType);

    /**
     * 迷糊查询客户信息
     * @param customerName
     * @return
     */
    List<CustomerVO> listCustomerByName(String customerName);

    /**
     * 根据客户id和渠道code查询客户信息
     * @param customerId 客户id
     * @param channelCode 渠道id
     * @return
     */
    CustomerVO getCustomerByCustomerIdAndChannelCode(Integer customerId, String channelCode);

    /**
     * 根据appid和openid获取用户的信息
     * @param appid
     * @param openid
     * @return
     */
    CustomerVO getCustomerByWxOpenid(String appid, String openid);

    /**
     * 根据unionid获取用户的信息
     * @param unionid
     * @return
     */
    CustomerVO getCustomerByWxUnionid(String unionid);

    /**
     * 绑定用户到指定帐户
     * @param userId
     * @param appid
     * @param wxMpUser
     * @return
     */
    boolean bindUserWithWxAccount(Integer userId, String appid, WxMpUser wxMpUser);

    /**
     * 客户锁定冻结状态
     * @param id 客户id
     * @return
     */
    Boolean getCustomerLockedStatusById(Integer id);

    /**
     * 微信渠道端分页查询客户信息
     * @param currentUserChannelCode    当前登录渠道用户的渠道code
     * @param pageIndex
     * @param pageSize
     * @return
     */
    Pagination<CustomerVO> pageCustomerByChannnelCode(String currentUserChannelCode, Integer pageIndex, Integer pageSize);

    /* * 新增客户
     * @param customerVO 客户vo
     * @return
     */
    Integer saveCustomerInfo(CustomerVO customerVO);

    /**
     * 通过id获取客户信息
     * @param id
     * @return
     */
    CustomerVO getCustomerAccountInfoById(Integer id);

    /**
     * 根据手机号获取客户信息
     * @param mobile 客户手机号
     * @return
     */
    CustomerVO getCustomerAccountInfoByMobile(String mobile);

    CustomerSimpleVO getCustomerSimpleInfo(Integer customerId);

    /**
     * 获得用户的银行卡信息
     * @param customerId
     * @return
     */
    CustomerBankCardVO getCustomerBankCardVO(Integer customerId);

    /**
     * 根据指定的用户已经银行卡号获取用户曾经绑定过的银行卡信息
     * @param customerId 指定客户
     * @param bankcardNumber 银行卡卡号
     * @return
     */
    CustomerBankCardVO getCustomerBankCardVOByCardNumber(Integer customerId, String bankcardNumber);

    /**
     * 获取用户的授权信息
     * @param userId
     * @param oauthType
     * @param appid
     * @return
     */
    OauthAccountVO getUserOauth(Integer userId, String oauthType, String appid);

    void updateBankCard(CustomerAccountVO vo);

    //获取客户渠道集合
    List<String> findCustomerChannelCodeByCustomerId(Integer customerId);

    /**
     * 查询客户账户
     * @param accountId 客户id
     * @param channelOf 渠道code
     * @return
     */
    List<CustomerVO> listCustomerVOByCustomerIdAndChannelCode(Integer accountId, String channelOf);

    List<CustomerVO> listByAccountId(Integer customerAccountId);

    List<CustomerVO> listCustomerAccountInfoByMobile(String phoneNumber);

    Pagination<CustomerVO> pageCustomerByChannnelUserId(Integer channelUserId, Integer pageIndex, Integer pageSize);

    CustomerAccountVO getCustomerAccountById(Integer currentLoginUserId);
}


