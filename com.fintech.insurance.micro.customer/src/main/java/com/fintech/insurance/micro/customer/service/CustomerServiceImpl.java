package com.fintech.insurance.micro.customer.service;

import com.alibaba.fastjson.JSON;
import com.fintech.insurance.commons.enums.ApplicationType;
import com.fintech.insurance.commons.enums.CustomerStatus;
import com.fintech.insurance.commons.enums.OauthType;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.context.FInsuranceApplicationContext;
import com.fintech.insurance.micro.customer.persist.entity.CustomerAccount;
import com.fintech.insurance.micro.customer.persist.entity.CustomerAccountInfo;
import com.fintech.insurance.micro.customer.persist.entity.CustomerAccountOauth;
import com.fintech.insurance.micro.customer.persist.entity.CustomerBankCard;
import com.fintech.insurance.micro.customer.persist.dao.CustomerAccountDao;
import com.fintech.insurance.micro.customer.persist.dao.CustomerAccountInfoDao;
import com.fintech.insurance.micro.customer.persist.dao.CustomerAccountOauthDao;
import com.fintech.insurance.micro.customer.persist.dao.CustomerBankCardDao;
import com.fintech.insurance.micro.dto.common.OauthAccountVO;
import com.fintech.insurance.micro.dto.customer.*;
import com.fintech.insurance.micro.dto.thirdparty.ImageVO;
import com.fintech.insurance.micro.feign.finance.EnterpriseBankServiceFeign;
import com.fintech.insurance.micro.feign.retrieval.BizQueryFeign;
import com.fintech.insurance.micro.feign.thirdparty.QiniuBusinessServiceFeign;
import com.vdurmont.emoji.EmojiParser;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/11/14 13:54
 */
@Service
public class CustomerServiceImpl implements CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Autowired
    private CustomerAccountDao customerAccountDao;

    @Autowired
    private CustomerAccountInfoDao customerAccountInfoDao;

    @Autowired
    private CustomerAccountOauthDao customerAccountOauthDao;

    @Autowired
    private BizQueryFeign bizQueryFeign;

    @Autowired
    private CustomerBankCardDao customerBankCardDao;

    @Autowired
    private QiniuBusinessServiceFeign qiniuBusinessServiceFeign;


    @Override
    public Pagination<CustomerVO> queryAllCustomer(String name, String channelOf, String companyOf,
                                                 String phone, CustomerStatus customerStatus, Integer pageIndex, Integer pageSize) {
        FintechResponse<Pagination<CustomerVO>> response =  bizQueryFeign.pageCustomerVO(name, channelOf, companyOf, phone, customerStatus, pageIndex, pageSize);

        if (response == null || !response.isOk()) {
            throw new FInsuranceBaseException(103004);
        }

        return response.getData();
    }

    @Override
    @Transactional
    public void freezeOrNot(Integer id, CustomerStatus operationType) {
        CustomerAccount customerAccount = customerAccountDao.getById(id);
        if (customerAccount != null) {
            customerAccount.setLockedTag(CustomerStatus.FREEZE == operationType);
        } else {
            throw new FInsuranceBaseException(104002, new Object[]{"id = " + id});
        }
        logger.info("冻结或解冻用户成功， id = " + id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerVO> listCustomerByName(String customerName) {
        List<CustomerAccountInfo> customerAccountList = customerAccountInfoDao.listByCustomerNameLike("%" + customerName + "%");
        List<CustomerVO> customerVOList = new ArrayList<>();
        if (customerAccountList != null){
            for(CustomerAccountInfo customerAccountInfo : customerAccountList){
                CustomerVO customerVO = this.convertCustomerAccountVO(customerAccountInfo);
                customerVOList.add(customerVO);
            }
        }
        if (customerAccountList == null || customerVOList.size() <= 0) {
            return Collections.emptyList();
        }
       return customerVOList;
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerVO getCustomerByCustomerIdAndChannelCode(Integer customerId, String channelCode) {
        if(customerId == null || StringUtils.isBlank(channelCode)){
            return new CustomerVO();
        }
        List<CustomerAccountInfo> customerAccountInfoList = customerAccountInfoDao.findByCustomerIdAndChannelCode(customerId, channelCode);
        return this.convertCustomerAccountVO(customerAccountInfoList == null || customerAccountInfoList.isEmpty() ? null : customerAccountInfoList.get(0));
    }

    @Override
    public CustomerVO getCustomerByWxOpenid(String appid, String openid) {
        if (StringUtils.isEmpty(appid) || StringUtils.isEmpty(openid)) {
            return null;
        } else {
            CustomerAccountOauth customerAccountOauth = this.customerAccountOauthDao.getCustomerAccountOauthByOpenid(appid, openid, OauthType.WECHAT_MP.getCode());
            if (customerAccountOauth == null) {
                return null;
            } else {
                return this.convertCustomerAccountVO(customerAccountOauth.getCustomerAccount());
            }
        }
    }

    @Override
    public CustomerVO getCustomerByWxUnionid(String unionid) {
        if (StringUtils.isEmpty(unionid)) {
            return null;
        } else {
            CustomerAccountOauth customerAccountOauth = this.customerAccountOauthDao.getCustomerAccountOauthByWxUnionId(unionid);
            if (customerAccountOauth == null) {
                return null;
            } else {
                return this.convertCustomerAccountVO(customerAccountOauth.getCustomerAccount());
            }
        }
    }

    @Override
    public boolean bindUserWithWxAccount(Integer userId, String appid, WxMpUser wxMpUser) {
        logger.info("tryto bind account user: {}, with appid: {}, wxMpUser: {}", userId, appid, wxMpUser);
        if (userId != null && StringUtils.isNotEmpty(appid) && wxMpUser != null) {
            CustomerAccount cAccount = this.customerAccountDao.getById(userId);
            if (cAccount == null) {
                throw new FInsuranceBaseException(103002); //客户不存在
            } else {
                CustomerAccountOauth cAccountOauth = this.customerAccountOauthDao.getCustomerAccountOauthByOpenid(appid, wxMpUser.getOpenId(), OauthType.WECHAT_APP.getCode());
                if (cAccountOauth == null) {
                    //还需检查目标用户是否已经绑定了微信
                    Set<CustomerAccountOauth> oauths = cAccount.getCustomerAccountOauths();
                    if (oauths != null && oauths.size() > 0) {
                        boolean hasBind = false;
                        for (CustomerAccountOauth oauth : oauths) {
                            if (oauth != null && OauthType.WECHAT_MP.getCode().equalsIgnoreCase(oauth.getOauthType()) && appid.equals(oauth.getOauthAppId())) {
                                hasBind = true;
                            }
                        }
                        if (hasBind) {
                            throw new FInsuranceBaseException(106216); //该账号已经绑定了微信，不能重复绑定
                        }
                    }
                    cAccountOauth = new CustomerAccountOauth();
                    cAccountOauth.setCreateAt(new Date());
                } else {
                    if (!cAccountOauth.getOauthAccount().equals(wxMpUser.getOpenId()) || !cAccountOauth.getWxUnionId().equals(wxMpUser.getUnionId())) {
                        throw new FInsuranceBaseException(106216); //该账号已经绑定了微信，不能重复绑定
                    }
                }
                cAccountOauth.setCustomerAccount(cAccount);
                cAccountOauth.setOauthType(OauthType.WECHAT_MP.getCode());
                cAccountOauth.setOauthAppId(appid);
                cAccountOauth.setOauthAccount(wxMpUser.getOpenId());
                cAccountOauth.setNickName(EmojiParser.removeAllEmojis(wxMpUser.getNickname()));
                cAccountOauth.setGender(wxMpUser.getSex());
                cAccountOauth.setHeaderImage(wxMpUser.getHeadImgUrl());
                cAccountOauth.setWxUnionId(wxMpUser.getUnionId());
                cAccountOauth.setOauthContent(EmojiParser.removeAllEmojis(JSON.toJSONString(wxMpUser)));
                cAccountOauth.setUpdateAt(new Date());
                this.customerAccountOauthDao.save(cAccountOauth);
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean getCustomerLockedStatusById(Integer id) {
        return customerAccountDao.getCustomerLockedStatusById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Pagination<CustomerVO> pageCustomerByChannnelCode(String currentUserChannelCode, Integer pageIndex, Integer pageSize) {
        Page<CustomerAccountInfo> customerVOPage = customerAccountInfoDao.pageCustomerByChannnelCode(currentUserChannelCode, pageIndex, pageSize);
        List<CustomerVO> customerVOList = new ArrayList<>();
        if (null != customerVOPage.getContent() && customerVOPage.getContent().size() > 0) {
            for (CustomerAccountInfo c : customerVOPage.getContent()) {
                CustomerVO customerVO = this.convertCustomerAccountVO(c);
                customerVOList.add(customerVO);
            }
        }
        if (customerVOPage.getContent().size() < 1) {
            customerVOList = Collections.emptyList();
        }
        return Pagination.createInstance(pageIndex, pageSize, customerVOPage.getTotalElements(), customerVOList);
    }

    @Override
    @Transactional(readOnly = true)
    public Pagination<CustomerVO> pageCustomerByChannnelUserId(Integer channelUserId, Integer pageIndex, Integer pageSize) {
        Page<CustomerAccountInfo> customerVOPage = customerAccountInfoDao.pageCustomerByChannnelUserId(channelUserId, pageIndex, pageSize);
        List<CustomerVO> customerVOList = new ArrayList<>();
        if (null != customerVOPage.getContent() && customerVOPage.getContent().size() > 0) {
            for (CustomerAccountInfo c : customerVOPage.getContent()) {
                CustomerVO customerVO = this.convertCustomerAccountVO(c);
                customerVOList.add(customerVO);
            }
        }
        if (customerVOPage.getContent().size() < 1) {
            customerVOList = Collections.emptyList();
        }
        return Pagination.createInstance(pageIndex, pageSize, customerVOPage.getTotalElements(), customerVOList);
    }

    @Override
    public Integer saveCustomerInfo(CustomerVO customerVO) {
        CustomerAccountInfo customerAccountInfo = customerAccountInfoDao.getById(customerVO.getAccountInfoId());
        CustomerAccount customerAccount = customerAccountDao.getCustomerAccountByIdNumber(customerVO.getIdNum());
        if (customerAccountInfo == null) {
            customerAccountInfo = new CustomerAccountInfo();
            customerAccountInfo.setCreateAt(new Date());//设置公共字段
        } else {
            customerAccountInfo.setUpdateAt(new Date());//设置公共字段
        }
        //验证身份证信息是否已经存在
        if (null == customerAccount) {
            customerAccount = new CustomerAccount();
            customerAccount.setLockedTag(customerVO.getIsLocked() != 0);
            customerAccount.setCreateAt(new Date());
        } else {
            customerAccount.setUpdateAt(new Date());
        }
        if (StringUtils.isNotEmpty(customerVO.getBankCard())) {
            CustomerAccountInfo preCustomer = customerAccountInfoDao.getByBankNumAndChannelUserId(customerVO.getBankCard(), customerVO.getChannelUserId());
            //验证银行卡号是否已经被渠道账户创建过
            if ((customerVO.getAccountInfoId() == null && preCustomer != null)
                    || (customerVO.getAccountInfoId() != null && preCustomer != null && !customerVO.getAccountInfoId().equals(preCustomer.getId()))) {
                logger.error("the accountNumber [" + customerVO.getBankCard() + "]" + "already used by user[ " + preCustomer.getId() + "] create by channelUser[" + customerVO.getChannelUserId());
                throw new FInsuranceBaseException(107042);
            }
            customerAccountInfo.setAccountNumber(customerVO.getBankCard());
        }

        customerAccountInfo.setCustomerMobile(customerVO.getPhone());
        customerAccount.setIdNumber(customerVO.getIdNum());
        customerAccount.setIdBack(customerVO.getIdBack());
        customerAccount.setIdFront(customerVO.getIdFront());
        customerAccount.setCertificationId(customerVO.getCertificationId());
        customerAccountDao.save(customerAccount);

        customerAccountInfo.setCustomerAccount(customerAccount);
        customerAccountInfo.setChannelCode(customerVO.getChannelOf());
        customerAccountInfo.setCustomerName(customerVO.getName());
        customerAccountInfo.setBankCardPicture(customerVO.getBankCardPicture());
        customerAccountInfo.setEnterpriseName(customerVO.getCompanyOf());
        customerAccountInfo.setBusinessLicence(customerVO.getBusinessLicence());
        customerAccountInfo.setBusinessLicencePicture(customerVO.getLicencePicture());
        customerAccountInfo.setAccountBankName(customerVO.getBankName());
        customerAccountInfo.setEnterpriseDefault(false);//默认为非企业信息
        customerAccountInfo.setChannelUserId(null == customerVO.getChannelUserId() ? 0 : customerVO.getChannelUserId());
        customerAccountInfoDao.save(customerAccountInfo);

        return customerAccountInfo.getId();
    }


    @Override
    @Transactional(readOnly = true)
    public CustomerVO getCustomerAccountInfoById(Integer id) {
        CustomerAccountInfo info = customerAccountInfoDao.getById(id);
        return convertCustomerAccountVO(info);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerVO getCustomerAccountInfoByMobile(String mobile) {
        CustomerAccountInfo info = customerAccountInfoDao.getByMobile(mobile);
        return convertCustomerAccountVO(info);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerSimpleVO getCustomerSimpleInfo(Integer customerId) {
        if (null == customerId || 0 == customerId) {
            customerId = FInsuranceApplicationContext.getCurrentUserId();
        }
        logger.info("try to get simple info by customer Id is:" + customerId);
        if (customerId == null) {
            throw new FInsuranceBaseException(103002);
        }

        CustomerSimpleVO vo = new CustomerSimpleVO();
        CustomerAccount customerAccount = customerAccountDao.getById(customerId);
        if (customerAccount == null) {
            throw new FInsuranceBaseException(103002);
        }
        //客戶id
        vo.setCustomerId(customerAccount.getId());
        if (customerAccount.getCustomerAccountInfos() != null && customerAccount.getCustomerAccountInfos().size() > 0) {
            //客戶名稱
            vo.setCustomerName(customerAccount.getCustomerAccountInfos().iterator().next().getCustomerName());
        }
        //客戶身份證號
        vo.setIdNumber(customerAccount.getIdNumber());
        //客戶支付銀行卡
        List<CustomerBankCard> bankCards = customerBankCardDao.findCustomerBankCardByCustomerId(customerId);
        if (bankCards != null && bankCards.size() > 0) {
            CustomerBankCard card = bankCards.get(0);
            if (card.getCustomerAccount() != null) {
                vo.setBankName(card.getAccountBank());
                vo.setBankCardNumber(card.getAccountNumber());
                vo.setPhone(card.getAccountMobile());
            }
        }
        /*else {
            throw new FInsuranceBaseException(103009);
        }*/
        return vo;
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerBankCardVO getCustomerBankCardVO(Integer customerId) {
        if (customerId == null) {
            return null;
        } else {
            List<CustomerBankCard> bankCards = this.customerBankCardDao.findCustomerBankCardByCustomerId(customerId);
            if (bankCards != null && bankCards.size() > 0) {
                CustomerBankCard card = bankCards.get(0);
                CustomerBankCardVO bankCardVO = new CustomerBankCardVO();
                bankCardVO.setAccountBank(card.getAccountBank());
                bankCardVO.setAccountMobile(card.getAccountMobile());
                bankCardVO.setAccountNumber(card.getAccountNumber());
                bankCardVO.setCustomerId(customerId);
                return bankCardVO;
            } else {
                return null;
            }
        }
    }

    private CustomerAccountInfoVO convertCustomerAccountInfoVO(CustomerAccountInfo entity){
        if(entity == null){
            return null;
        }
        CustomerAccountInfoVO vo = new CustomerAccountInfoVO();
        // 客户账户id
        vo.setId(entity.getId());
        // 客户id
        vo.setAccountId(entity.getCustomerAccount().getId());

        List<CustomerBankCard> bankCards = null;
        if(entity.getCustomerAccount() != null) {
            bankCards = customerBankCardDao.findCustomerBankCardByCustomerId(entity.getCustomerAccount().getId());
        }
        // 客户银行卡id
        vo.setCustomerBankCardId(bankCards == null ? null : bankCards.get(0).getId());
        //企业名称
        vo.setEnterpriseName(entity.getEnterpriseName());
        //营业执照号码
        vo.setBusinessLicence(entity.getBusinessLicence());
        //营业执照照片
        vo.setBusinessLicencePicture(entity.getBusinessLicencePicture());
        //渠道编码，该客户的来源渠道
        vo.setChannelCode(entity.getChannelCode());
        //客户姓名
        vo.setCustomerName(entity.getCustomerName());
        //身份证号码，全局唯一
        vo.setIdNumber(entity.getCustomerAccount().getIdNumber());
        //银行账户号
        vo.setAccountNumber(entity.getAccountNumber());
        //客户手机号码
        vo.setCustomerMobile(entity.getCustomerMobile());
        //银行帐户开户行名称
        vo.setAccountBankName(entity.getAccountBankName());
        //银行帐户开户行支行afe21
        vo.setAccountBankBranch(entity.getAccountBankBranch());
        //身份证正面照片
        vo.setIdFront(entity.getCustomerAccount().getIdFront());
        //身份证反面照片
        vo.setIdBack(entity.getCustomerAccount().getIdBack());
        //银行卡正面照
        vo.setBankCardPicture(entity.getBankCardPicture());
        //是否为企业默认信息
        vo.setEnterpriseDefault(entity.getEnterpriseDefault());
        //是否冻结标志位
        vo.setLockedTag(entity.getCustomerAccount().getLockedTag());
        //冻结时间
        vo.setLockedAt(entity.getCustomerAccount().getLockedAt());
        return vo;
    }

    private CustomerVO convertCustomerAccountVO(CustomerAccount entity) {
        if (entity == null) {
            return null;
        } else {
            CustomerVO customerVO = new CustomerVO();
            customerVO.setAccountId(entity.getId());
            customerVO.setIdNum(entity.getIdNumber());
            customerVO.setIdFront(entity.getIdFront());
            customerVO.setIdBack(entity.getIdBack());
            return customerVO;
        }
    }

    private CustomerVO convertCustomerAccountVO(CustomerAccountInfo entity) {
        if (entity == null) {
            return null;
        }
        CustomerVO customerVO = new CustomerVO();
        //个人信息
        customerVO.setAccountInfoId(entity.getId());
        customerVO.setPhone(entity.getCustomerMobile());
        customerVO.setName(entity.getCustomerName());
        customerVO.setBankBranch(entity.getAccountBankBranch());
        customerVO.setBankCardPicture(entity.getBankCardPicture());

        if(entity.getCustomerAccount() != null) {
            customerVO.setAccountId(entity.getCustomerAccount().getId());
            customerVO.setIdFront(entity.getCustomerAccount().getIdFront());
            customerVO.setIdBack(entity.getCustomerAccount().getIdBack());
            customerVO.setIdNum(entity.getCustomerAccount().getIdNumber());
            customerVO.setIsLocked(entity.getCustomerAccount().getLockedTag() ? 1 : 0);
            customerVO.setCertificationId(entity.getCustomerAccount().getCertificationId());
        }
        // 客户银行卡信息
        customerVO.setBankCard(entity.getAccountNumber());
        customerVO.setBankName(entity.getAccountBankName());
        //企业信息
        customerVO.setBusinessLicence(entity.getBusinessLicence());
        customerVO.setCompanyOf(entity.getEnterpriseName());
        customerVO.setEnterpriseName(entity.getEnterpriseName());
        customerVO.setLicencePicture(entity.getBusinessLicencePicture());
        customerVO.setMobile(entity.getCustomerMobile());
        customerVO.setChannelUserId(entity.getChannelUserId());
        return customerVO;
    }

    private OauthAccountVO convertToVO(CustomerAccountOauth oauth) {
        if (oauth == null) {
            return null;
        } else {
            OauthAccountVO userOauthVO = new OauthAccountVO();
            userOauthVO.setId(oauth.getId());
            userOauthVO.setUserId(oauth.getCustomerAccount().getId());
            userOauthVO.setOauthType(OauthType.codeOf(oauth.getOauthType()));
            userOauthVO.setOauthAppId(oauth.getOauthAppId());
            userOauthVO.setOauthAccount(oauth.getOauthAccount());
            userOauthVO.setNickName(oauth.getNickName());
            userOauthVO.setGender(oauth.getGender());
            userOauthVO.setHeaderImage(oauth.getHeaderImage());
            userOauthVO.setOauthContent(oauth.getOauthContent());
            userOauthVO.setCreateAt(oauth.getCreateAt());
            userOauthVO.setUpdateAt(oauth.getUpdateAt());
            userOauthVO.setUserLocked(oauth.getCustomerAccount().getLockedTag());
            return userOauthVO;
        }
    }

    @Override
    public CustomerBankCardVO getCustomerBankCardVOByCardNumber(Integer customerId, String bankcardNumber) {
        CustomerBankCard bankCard = this.customerBankCardDao.findCustomerBankCardByCustomerIdAndCardNumber(customerId, bankcardNumber);
        if (null != bankCard) {
            CustomerBankCardVO bankCardVO = new CustomerBankCardVO();
            bankCardVO.setAccountBank(bankCard.getAccountBank());
            bankCardVO.setAccountMobile(bankCard.getAccountMobile());
            bankCardVO.setAccountNumber(bankCard.getAccountNumber());
            bankCardVO.setCustomerId(customerId);
            return bankCardVO;
        }
        return null;
    }

    @Override
    public OauthAccountVO getUserOauth(Integer userId, String oauthType, String appid) {
        if (userId == null || StringUtils.isEmpty(oauthType) || StringUtils.isEmpty(appid)) {
            return null;
        } else {
            CustomerAccountOauth oauth = this.customerAccountOauthDao.getCustomerAccountOauthByAccountId(userId, oauthType, appid);
            if (oauth == null) {
                return null;
            } else {
                return this.convertToVO(oauth);
            }
        }
    }

    @Override
    @Transactional
    public void updateBankCard(CustomerAccountVO vo) {

        Integer customerId = FInsuranceApplicationContext.getCurrentUserId();

        CustomerAccount customerAccount = customerAccountDao.getById(customerId);
        if (customerAccount == null) {
            throw new FInsuranceBaseException(103007);
        }

        logger.info("更换银行卡信息， customerId = " + customerId);
        List<CustomerBankCard> cards = customerBankCardDao.findCustomerBankCardByCustomerId(customerId);
        if (cards != null && cards.size() > 0) { //新增
            CustomerBankCard oldCard = cards.get(0);
            oldCard.setUpdateAt(new Date());
            oldCard.setDisableFlag(true);
            customerBankCardDao.save(oldCard);
        }
        CustomerBankCard newCard = new CustomerBankCard();
        newCard.setAccountNumber(vo.getBankCardNumber());
        newCard.setAccountMobile(vo.getPhone());
        newCard.setAccountBank(vo.getBankName());
        newCard.setCustomerAccount(customerAccount);
        newCard.setCreateAt(new Date());
        customerBankCardDao.save(newCard);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> findCustomerChannelCodeByCustomerId(Integer customerId) {
        return customerAccountInfoDao.findCustomerChannelCodeByCustomerId(customerId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerVO> listCustomerVOByCustomerIdAndChannelCode(Integer accountId, String channelOf) {
        if (accountId == null || StringUtils.isBlank(channelOf)) {
            return null;
        }
        List<CustomerVO> customerVOList = new ArrayList<>();
        List<CustomerAccountInfo> customerAccountInfoList = customerAccountInfoDao.findByCustomerIdAndChannelCode(accountId, channelOf);
        if (null != customerAccountInfoList && customerAccountInfoList.size() > 0) {
            for (CustomerAccountInfo a : customerAccountInfoList) {
                CustomerVO customerVO = this.convertCustomerAccountVO(a);
                customerVOList.add(customerVO);
            }
        }
        return customerVOList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerVO> listByAccountId(Integer customerAccountId) {
        if (customerAccountId == null) {
            return null;
        }
        List<CustomerVO> customerVOList = new ArrayList<>();
        List<CustomerAccountInfo> customerAccountInfoList = customerAccountInfoDao.listByAccountId(customerAccountId);
        if (null != customerAccountInfoList && customerAccountInfoList.size() > 0) {
            for (CustomerAccountInfo a : customerAccountInfoList) {
                CustomerVO customerVO = this.convertCustomerAccountVO(a);
                customerVOList.add(customerVO);
            }
        }
        return customerVOList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerVO> listCustomerAccountInfoByMobile(String phoneNumber) {
        List<CustomerVO> customerVOList = new ArrayList<>();
        List<CustomerAccountInfo> customerAccountInfoList = customerAccountInfoDao.listCustomerAccountInfoByMobile(phoneNumber);
        if (null != customerAccountInfoList && customerAccountInfoList.size() > 0) {
            for (CustomerAccountInfo a : customerAccountInfoList) {
                CustomerVO customerVO = this.convertCustomerAccountVO(a);
                customerVOList.add(customerVO);
            }
        }
        return customerVOList;
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerAccountVO getCustomerAccountById(Integer currentLoginUserId) {
        CustomerAccount customerAccount = customerAccountDao.getById(currentLoginUserId);
        return this.convertToVOAccountVO(customerAccount);
    }

    private CustomerAccountVO convertToVOAccountVO(CustomerAccount customerAccount) {
        if (null == customerAccount) {
            return null;
        }
        CustomerAccountVO customerAccountVO = new CustomerAccountVO();
        Iterator<CustomerAccountInfo> it = customerAccount.getCustomerAccountInfos().iterator();
        if (it.hasNext()) {
            customerAccountVO.setName(it.next().getCustomerName());
        }
        customerAccountVO.setIdNumber(customerAccount.getIdNumber());
        customerAccountVO.setIdBack(customerAccount.getIdBack());
        customerAccountVO.setIdFront(customerAccount.getIdFront());
        return customerAccountVO;
    }
}
