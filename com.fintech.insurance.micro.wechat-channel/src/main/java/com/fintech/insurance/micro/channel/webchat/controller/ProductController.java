package com.fintech.insurance.micro.channel.webchat.controller;

import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.annotation.RequireWechatLogin;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.BaseFintechWechatController;
import com.fintech.insurance.micro.vo.wechat.WeChatProductVO;
import com.fintech.insurance.service.agg.CommonProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Description: 微信渠道端外部接口门面
 * @Author: Yong Li
 * @Date: 2017/12/6 18:55
 */
@RestController
@RequestMapping(value = "/wechat/channel/product", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RequireWechatLogin
public class ProductController extends BaseFintechWechatController {

    @Autowired
    private CommonProductService commonProductService;

    @RequestMapping(value = "/find-product-info", method = RequestMethod.GET)
    public FintechResponse<WeChatProductVO> findProductInfoById(Integer id) {
        if(id == null){
            throw new FInsuranceBaseException(104107);
        }
        //获取单前登录用id
        Integer currentLoginUserId = this.getCurrentUserId();
        String userType = this.getCurrentUserType();
        String channelCode = this.getCurrentUserChannelCode();
        return FintechResponse.responseData(commonProductService.findProductInfoById(userType, currentLoginUserId, channelCode, id));
    }
}