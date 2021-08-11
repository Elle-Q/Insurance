package com.fintech.insurance.micro.customer.webchat.controller;

import com.fintech.insurance.commons.beans.WeixinConfigBean;
import com.fintech.insurance.commons.enums.NotificationEvent;
import com.fintech.insurance.commons.enums.OauthType;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.annotation.RequireWechatLogin;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.BaseFintechWechatController;
import com.fintech.insurance.micro.dto.biz.CustomerRequisitionVO;
import com.fintech.insurance.micro.dto.common.ClientTokenVO;
import com.fintech.insurance.micro.dto.customer.CustomerAccountVO;
import com.fintech.insurance.micro.dto.customer.CustomerConsultationVO;
import com.fintech.insurance.micro.dto.customer.CustomerVO;

import com.fintech.insurance.micro.dto.system.UserVO;
import com.fintech.insurance.micro.dto.thirdparty.sms.SMSCheckVerificationParamVO;
import com.fintech.insurance.micro.feign.customer.CustomerServiceFeign;
import com.fintech.insurance.micro.feign.customer.IntentionServiceFeign;
import com.fintech.insurance.micro.feign.system.SysUserServiceFeign;
import com.fintech.insurance.micro.feign.thirdparty.QiniuBusinessServiceFeign;
import com.fintech.insurance.micro.feign.thirdparty.sms.SMSServiceFeign;
import com.fintech.insurance.micro.feign.thirdparty.weixin.WeixinBizServiceFeign;
import com.fintech.insurance.micro.vo.wechat.IntentionVO;
import com.fintech.insurance.micro.vo.wechat.LoginVO;
import com.fintech.insurance.micro.vo.wechat.SimpleCustomerAccountVO;
import com.fintech.insurance.micro.vo.wechat.WeChatCustomerVO;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 客户端登录注册
 * @Author: qxy
 * @Date: 2017/12/6 18:54
 */
@RestController
@RequestMapping(value = "/wechat/customer/customer", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class CustomerController extends BaseFintechWechatController{

    private static final Logger LOG = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private IntentionServiceFeign intentionServiceFeign;

    @Autowired
    private CustomerServiceFeign customerServiceFeign;

    @Autowired
    private SMSServiceFeign smsServiceFeign;

    @Autowired
    private WeixinBizServiceFeign weixinBizServiceFeign;

    @Autowired
    private WeixinConfigBean configBean;

    @Autowired
    private SysUserServiceFeign sysUserServiceFeign;

    /**
     * 客户端登录
     * @return
     */
    @RequestMapping(value = "/login")
    public WeChatCustomerVO login(@RequestBody LoginVO loginVO) {
        //校验短信验证码
        SMSCheckVerificationParamVO smsCheckVerificationParamVO = new SMSCheckVerificationParamVO();
        smsCheckVerificationParamVO.setEventCode(NotificationEvent.WX_CUSTOMER_LOGIN_AUTH.getCode());
        smsCheckVerificationParamVO.setPhoneNumber(loginVO.getPhoneNumber());
        smsCheckVerificationParamVO.setSequenceId(loginVO.getSequenceId());
        smsCheckVerificationParamVO.setVerification(loginVO.getVerification());
        FintechResponse<Boolean> fintechResponse = smsServiceFeign.checkSMSVerification(smsCheckVerificationParamVO);
        if (!fintechResponse.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(fintechResponse);
        }
        Boolean flag = fintechResponse.getData();
        if (!flag) {
            throw new FInsuranceBaseException(107023);//短信校验出错
        }
        FintechResponse<List<CustomerVO>> customerVOResponse = customerServiceFeign.listCustomerAccountInfoByMobile(loginVO.getPhoneNumber());
        if (!customerVOResponse.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(customerVOResponse);
        }
        if (customerVOResponse.getData().size() < 1) {//判断客户信息是否存在
            throw new FInsuranceBaseException(103002);
        }
        CustomerVO customerVO = customerVOResponse.getData().get(0);
        LOG.info("preparing bind customer: appid:{}, mpuser:{}, accountId:{}", loginVO.getAppid(), loginVO.getFinsuranceMpUser(), customerVO.getAccountId());
        FintechResponse<ClientTokenVO> response = weixinBizServiceFeign.bindOauthInfoWithUser(loginVO.getAppid(), loginVO.getFinsuranceMpUser(),
                customerVO.getAccountId());
        if (!response.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(response);
        }
        WeChatCustomerVO customer = this.convertToWeChatVO(customerVO);
        customer.setToken(response.getData().getToken());
        customer.setTokenExpireSeconds(response.getData().getTokenExpireSeconds());
        return customer;
    }

    /**
     * 客户信息登记
     * @return
     */
    @RequestMapping(value = "/intention/save")
    public FintechResponse<Integer> register(@RequestBody IntentionVO intentionVO) {
        CustomerConsultationVO customerConsultationVO = new CustomerConsultationVO();
        customerConsultationVO.setName(intentionVO.getName());
        customerConsultationVO.setBorrowReason(intentionVO.getConsultContent());
        customerConsultationVO.setMobile(intentionVO.getMobile());
        String financeMpUser = intentionVO.getFinsuranceMpUser();

        FintechResponse<WxMpUser> wxUserInfoResponse = this.weixinBizServiceFeign.getFinanceMpUserInfo(this.configBean.getAppid(), financeMpUser);
        if (wxUserInfoResponse == null || !wxUserInfoResponse.isOk() || wxUserInfoResponse.getData() == null) {
            throw new FInsuranceBaseException(106214);
        } else {
            WxMpUser wxMpUser = wxUserInfoResponse.getData();
            customerConsultationVO.setOauthType(OauthType.WECHAT_MP.getCode());
            customerConsultationVO.setOauthAppId(this.configBean.getAppid());
            customerConsultationVO.setOauthAccount(wxMpUser.getOpenId());
            customerConsultationVO.setWxUnioinId(wxMpUser.getUnionId());
        }

        LOG.info("save intention for customer with wxMpUser OauthType{},authAppId{},OauthAccount{}",
                customerConsultationVO.getOauthType(), customerConsultationVO.getOauthAppId(),customerConsultationVO.getOauthAccount());
        FintechResponse<Integer> response = intentionServiceFeign.saveConsultation(customerConsultationVO);
        if (!response.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(response);
        }
        return response;
    }

    /**
     * 保存编辑客户
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @RequireWechatLogin
    public FintechResponse<Integer> saveCustomer(@RequestBody @Validated WeChatCustomerVO weChatCustomerVO){
        if (StringUtils.isEmpty(weChatCustomerVO.getChannelCode())) {//渠道端保存客户，渠道编号不能为空
            throw new FInsuranceBaseException(107036);
        }
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
        //获取单前登录用id
        Integer currentLoginUserId = this.getCurrentUserId();
        //客户渠道
        String channelCode = weChatCustomerVO.getChannelCode();
        //渠道客户id
        Integer channelUserId = weChatCustomerVO.getChannelUserId();
        if((channelUserId == null || channelUserId.equals(0)) && StringUtils.isNoneBlank(channelCode)){
            UserVO userVO = sysUserServiceFeign.getChannelAdminByCode(channelCode);
            channelUserId = userVO == null ? null : userVO.getId();
        }
        //渠道用户必须存在
        if(channelUserId == null){
            throw new FInsuranceBaseException(107051);
        }
        weChatCustomerVO.setChannelUserId(channelUserId);
        CustomerVO customerVO = this.converToInternalVO(weChatCustomerVO);

        //保存客户信息
        FintechResponse<Integer> integerFintechResponse =  customerServiceFeign.saveCustomerInfo(customerVO);
        if (!integerFintechResponse.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(integerFintechResponse);
        }
        return integerFintechResponse;
    }

    /**
     * 获取客户详情信息
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    @RequireWechatLogin
    public FintechResponse<WeChatCustomerVO> getCustomerById(@RequestParam(value = "id") Integer id){
        FintechResponse<CustomerVO> customerVOFintechResponse = customerServiceFeign.getCustomerAccountInfoById(id);
        if (!customerVOFintechResponse.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(customerVOFintechResponse);
        }
        CustomerVO customerVO = customerVOFintechResponse.getData();
        if (null == customerVO) {
            throw new FInsuranceBaseException(107040);
        }
        return FintechResponse.responseData(this.convertToWeChatVO(customerVO));
    }

    /**
     * 获取客户基本信息
     */
    @RequestMapping(value = "/customer-account", method = RequestMethod.GET)
    @RequireWechatLogin
    public FintechResponse<SimpleCustomerAccountVO> getCustomerAccount(){
        Integer currentLoginUserId = getCurrentUserId();
        FintechResponse<CustomerAccountVO> customerVOFintechResponse = customerServiceFeign.getCustomerAccountById(currentLoginUserId);
        if (!customerVOFintechResponse.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(customerVOFintechResponse);
        }
        CustomerAccountVO customerAccountVO = customerVOFintechResponse.getData();
        if (null == customerAccountVO) {
            throw new FInsuranceBaseException(107043);
        }
        return FintechResponse.responseData(this.convertToWeChatAccountVO(customerAccountVO));
    }

    private SimpleCustomerAccountVO convertToWeChatAccountVO(CustomerAccountVO customerAccountVO) {
        if (null == customerAccountVO) {
            return null;
        }
        SimpleCustomerAccountVO simpleCustomerAccountVO = new SimpleCustomerAccountVO();
        simpleCustomerAccountVO.setName(customerAccountVO.getName());
        simpleCustomerAccountVO.setIdNum(customerAccountVO.getIdNumber());
        return simpleCustomerAccountVO;
    }

    /**
     * 查询客户信息
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @RequireWechatLogin
    public FintechResponse<List<WeChatCustomerVO>> queryCustomers(@Validated @RequestParam(value = "channelCode") @NotNull String channelCode) {

        Integer currentUserId = getCurrentUserId();//当前登录用户
        List<WeChatCustomerVO> weChatCustomerVOList = new ArrayList<>();
        FintechResponse<List<CustomerVO>> fintechResponse = customerServiceFeign.listByCustomerIdAndChannelCode(currentUserId, channelCode);
        if (!fintechResponse.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(fintechResponse);
        }

        List<CustomerVO> customerVOList = fintechResponse.getData();
        if (null != customerVOList && customerVOList.size() > 0) {
            for (CustomerVO c : customerVOList) {
                WeChatCustomerVO weChatCustomerVO = this.convertToWeChatVO(c);
                weChatCustomerVOList.add(weChatCustomerVO);
            }
        }
        return FintechResponse.responseData(weChatCustomerVOList);
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
        customerVO.setChannelOf(weChatCustomerVO.getChannelCode());
        customerVO.setChannelUserId(null == weChatCustomerVO.getChannelUserId() ? 0 : weChatCustomerVO.getChannelUserId());
        return customerVO;
    }

    //转化为微信端VO
    private WeChatCustomerVO convertToWeChatVO(CustomerVO c) {
        if (null == c) {
            return null;
        }
        WeChatCustomerVO weChatCustomerVO = new WeChatCustomerVO();
        weChatCustomerVO.setCustomerAccountInfoId(c.getAccountInfoId());
        weChatCustomerVO.setBankCard(c.getBankCard());
        weChatCustomerVO.setBusinessLicence(c.getBusinessLicence());
        weChatCustomerVO.setBankCardPicture(c.getBankCardPicture());
        weChatCustomerVO.setLicencePicture(c.getLicencePicture());
        weChatCustomerVO.setIdBack(c.getIdBack());
        weChatCustomerVO.setIdFront(c.getIdFront());
        weChatCustomerVO.setIdNum(c.getIdNum());
        weChatCustomerVO.setName(c.getName());
        weChatCustomerVO.setIsLocked(c.getIsLocked());
        weChatCustomerVO.setPhone(c.getPhone());
        weChatCustomerVO.setCompanyOf(c.getCompanyOf());
        weChatCustomerVO.setChannelCode(c.getChannelOf());
        weChatCustomerVO.setCustomerId(c.getAccountId());
        weChatCustomerVO.setChannelUserId(null == c.getChannelUserId() ? 0 : c.getChannelUserId());
        return weChatCustomerVO;
    }

}
