package com.fintech.insurance.micro.customer.webchat.controller;

import com.fintech.insurance.commons.beans.WeixinConfigBean;
import com.fintech.insurance.commons.enums.OauthType;
import com.fintech.insurance.commons.enums.RequisitionStatus;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.annotation.RequireWechatLogin;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.BaseFintechWechatController;
import com.fintech.insurance.micro.dto.common.OauthAccountVO;
import com.fintech.insurance.micro.dto.customer.CustomerBankCardVO;
import com.fintech.insurance.micro.dto.customer.CustomerVO;
import com.fintech.insurance.micro.feign.biz.RequisitionServiceFeign;
import com.fintech.insurance.micro.feign.customer.CustomerServiceFeign;
import com.fintech.insurance.micro.vo.wechat.CountRequisitionByStatusVO;
import com.fintech.insurance.micro.vo.wechat.LoginCustomerUserVO;
import com.fintech.insurance.micro.vo.wechat.MyVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: qxy
 * @Description: 微信客户端首页接口
 * @Date: 2017/12/9 11:30
 */
@RestController
@RequireWechatLogin
@RequestMapping(value = "/wechat/customer", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class MyController extends BaseFintechWechatController {

    private static final Logger LOG = LoggerFactory.getLogger(MyController.class);

    @Autowired
    private CustomerServiceFeign customerServiceFeign;

    @Autowired
    WeixinConfigBean weixinConfigBean;

    @Autowired
    private RequisitionServiceFeign requisitionServiceFeign;


    /**
     * 渠道端我的首页
     *
     * @return
     */
    @GetMapping(value = "/my")
    public FintechResponse<MyVO> count() {
        Integer currentLoginUserId = getCurrentUserId() ;
        LOG.info("Current Login User(ID) is :{}", currentLoginUserId);
        FintechResponse<List<CustomerVO>> customerVOFintechResponse = customerServiceFeign.listByAccountId(currentLoginUserId);
        if (!customerVOFintechResponse.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(customerVOFintechResponse);
        }
        CustomerVO customerVO = null;
        if (!customerVOFintechResponse.getData().isEmpty()) {
            customerVO = customerVOFintechResponse.getData().get(0);
        } else {
            throw new FInsuranceBaseException(107040, new Object[]{currentLoginUserId});
        }
        FintechResponse<Integer> responseForSubmitted = requisitionServiceFeign.countRequisitionByStatus("", currentLoginUserId, RequisitionStatus.Submitted.getCode());
        FintechResponse<Integer> responseForWaitingPayment = requisitionServiceFeign.countRequisitionByStatus("", currentLoginUserId, RequisitionStatus.WaitingPayment.getCode());
        FintechResponse<Integer> responseForRejected = requisitionServiceFeign.countRequisitionByStatus("", currentLoginUserId, RequisitionStatus.Rejected.getCode());
        FintechResponse<Integer> responseForWaitingLoan = requisitionServiceFeign.countRequisitionByStatus("", currentLoginUserId, RequisitionStatus.WaitingLoan.getCode());
        Map<String, Integer> map = new HashMap<>();
        map.put(RequisitionStatus.Submitted.getCode(), responseForSubmitted.getData());
        map.put(RequisitionStatus.WaitingPayment.getCode(), responseForWaitingPayment.getData());
        map.put(RequisitionStatus.Rejected.getCode(), responseForRejected.getData());
        map.put(RequisitionStatus.WaitingLoan.getCode(), responseForWaitingLoan.getData());
        return FintechResponse.responseData(this.convertToVO(customerVO, map));
    }

    private MyVO convertToVO(CustomerVO customerVO, Map<String, Integer> map) {
        MyVO myVO = new MyVO();
        myVO.setLoginCustomerUserVO(convertToWeChatUserVO(customerVO));
        myVO.setCountRequisitionVOList(convertToWeChatVO(map));
        return myVO;
    }

    //userVO转化为微信端VO
    private LoginCustomerUserVO convertToWeChatUserVO(CustomerVO customerVO) {
        LoginCustomerUserVO loginCustomerUserVO = new LoginCustomerUserVO();
        loginCustomerUserVO.setIsLocked(customerVO.getIsLocked());
        loginCustomerUserVO.setName(customerVO.getName());
        loginCustomerUserVO.setIdNum(customerVO.getIdNum());

        FintechResponse<OauthAccountVO> response = customerServiceFeign.getCustomerOauthAccount(customerVO.getAccountId(), OauthType.WECHAT_MP.getCode(), weixinConfigBean.getAppid());
        if (!response.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(response);
        }
        if (null != response.getData()) {
            loginCustomerUserVO.setHeadImgUrl(response.getData().getHeaderImage());
        }
        FintechResponse<CustomerBankCardVO> bankCardResponse = customerServiceFeign.getCustomerBankCard(customerVO.getAccountId());//客户银行卡信息
        if (!bankCardResponse.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(bankCardResponse);
        }
        loginCustomerUserVO.setHasBindCard(null == bankCardResponse.getData() ? 0 : 1);
        return loginCustomerUserVO;
    }


    //CountRequisitionVO转化为微信端VO
    private List<CountRequisitionByStatusVO> convertToWeChatVO(Map<String, Integer> map) {
        List<CountRequisitionByStatusVO> countRequisitionByStatusVOS = new ArrayList<>();
        for (String s : map.keySet()) {
            CountRequisitionByStatusVO vo = new CountRequisitionByStatusVO();
            vo.setNum(map.get(s));
            vo.setRequisitionStatus(s);
            countRequisitionByStatusVOS.add(vo);
        }
        return countRequisitionByStatusVOS;
    }
}

