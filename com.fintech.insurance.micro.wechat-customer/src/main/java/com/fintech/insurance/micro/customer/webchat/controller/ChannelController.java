package com.fintech.insurance.micro.customer.webchat.controller;

import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.annotation.RequireWechatLogin;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.BaseFintechWechatController;
import com.fintech.insurance.micro.dto.biz.ChannelVO;
import com.fintech.insurance.micro.feign.biz.ChannelServiceFeign;
import com.fintech.insurance.micro.feign.customer.CustomerServiceFeign;
import com.fintech.insurance.micro.vo.wechat.WeChatChannelVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 渠道用户
 * @Author: qxy
 * @Date: 2017/12/6 18:54
 */
@RestController
@RequestMapping(value = "/wechat/customer/channel", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RequireWechatLogin
public class ChannelController extends BaseFintechWechatController{

    private static final Logger logger = LoggerFactory.getLogger(ChannelController.class);

    @Autowired
    private CustomerServiceFeign customerServiceFeign;

    @Autowired
    private ChannelServiceFeign channelServiceFeign;


    /**
     *WX获取用户渠道
     * @return
     */
    @RequestMapping(value = "/find-by-customer-id/page", method = RequestMethod.GET)
    public FintechResponse<List<WeChatChannelVO>> findCustomerChannelCodes(){
        Integer customerId  = this.getCurrentUserId();
        FintechResponse<List<String>>  listFintechResponse = customerServiceFeign.findCustomerChannelCodeByCustomerId(customerId);
        if(!listFintechResponse.isOk()){
            logger.error("findCustomerChannelCodes with customerId=[" + customerId + "]" );
            throw FInsuranceBaseException.buildFromErrorResponse(listFintechResponse);
        }
        List<String> list = listFintechResponse.getData();
        List<WeChatChannelVO> weChatChannelVOList = new ArrayList<WeChatChannelVO>();
        if(list == null || list.size() < 1){
            return FintechResponse.responseData(weChatChannelVOList);
        }

        FintechResponse<Pagination<ChannelVO>> channelVOPage = channelServiceFeign.findCustomerChannelByChannelCodes(list);
        if(!channelVOPage.isOk()){
            throw FInsuranceBaseException.buildFromErrorResponse(channelVOPage);
        }
        for(ChannelVO vo : channelVOPage.getData().getItems()){
            WeChatChannelVO weChatChannelVO = new WeChatChannelVO();
            weChatChannelVO.setChannelCode(vo.getChannelCode());
            weChatChannelVO.setChannelName(vo.getChannelName());
            weChatChannelVO.setId(vo.getId());
            weChatChannelVOList.add(weChatChannelVO);
        }
        return FintechResponse.responseData(weChatChannelVOList);
    };


}