package com.fintech.insurance.micro.customer.webchat.controller;

import com.fintech.insurance.commons.enums.ProductType;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.annotation.RequireWechatLogin;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.BaseFintechWechatController;
import com.fintech.insurance.micro.dto.biz.ChannelProductVO;
import com.fintech.insurance.micro.dto.biz.SimpleProductVO;
import com.fintech.insurance.micro.feign.biz.ProductBusinessServiceFegin;
import com.fintech.insurance.micro.vo.wechat.WeChatBindingChannelVO;
import com.fintech.insurance.micro.vo.wechat.WeChatProductVO;
import com.fintech.insurance.micro.vo.wechat.WeChatSimpleProductVO;
import com.fintech.insurance.service.agg.CommonProductService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


/**
 * @Description: 微信渠道端外部接口门面
 * @Author: Yong Li
 * @Date: 2017/12/6 18:55
 */
@RestController
@RequestMapping(value = "/wechat/customer/product", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RequireWechatLogin
public class ProductController extends BaseFintechWechatController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private CommonProductService commonProductService;

    @Autowired
    private ProductBusinessServiceFegin productBusinessServiceFegin;


    @RequestMapping(value = "/find-product-info", method = RequestMethod.GET)
    public FintechResponse<WeChatProductVO> findProductInfoById(Integer id) {
        //获取单前登录用id
        Integer currentLoginUserId = this.getCurrentUserId();
        String userType = this.getCurrentUserType();
        String channelCode = this.getCurrentUserChannelCode();
        return FintechResponse.responseData(commonProductService.findProductInfoById(userType, currentLoginUserId, channelCode, id));
    }

    @RequestMapping(value = "/query-by-channel-id/page", method = RequestMethod.GET)
    public  FintechResponse<WeChatBindingChannelVO> queryProductInfoByChannelId(@RequestParam(value = "id" ,required = false) Integer id,//业务单id
                                                                                @RequestParam(value = "channelId" ,required = false) Integer channelId,
                                                                                @RequestParam(value = "productType", required = false) String productType) {
        Integer pageIndex = 1;
        Integer pageSize = Integer.MAX_VALUE;
        productType = StringUtils.isBlank(productType) ? ProductType.POLICY_LOANS.getCode() : productType;
        FintechResponse<ChannelProductVO> simpleProductVOPagination = productBusinessServiceFegin.queryWeChatProductInfoByChannelId(id, channelId, productType, pageIndex, pageSize);
        if(!simpleProductVOPagination.isOk()){
            throw FInsuranceBaseException.buildFromErrorResponse(simpleProductVOPagination);
        }
        if(simpleProductVOPagination.getData() == null){
            throw new FInsuranceBaseException(107049);
        }
        return FintechResponse.responseData(getWeChatBindingChannelVO(simpleProductVOPagination.getData()));
    }

    //得到微信端产品详情
    private WeChatBindingChannelVO getWeChatBindingChannelVO(ChannelProductVO channelProductVO){
        WeChatBindingChannelVO vo = new WeChatBindingChannelVO();
        List<WeChatSimpleProductVO> productVOList = new ArrayList<WeChatSimpleProductVO>();
        //默认选中的产品
        Integer productId = channelProductVO.getProductId();
        //产品信息
        if(channelProductVO.getSimpleProductVOList() != null && channelProductVO.getSimpleProductVOList().size() > 0) {
            for (int i = 0; i < channelProductVO.getSimpleProductVOList().size(); i++) {
                SimpleProductVO productVO = channelProductVO.getSimpleProductVOList().get(i);
                WeChatSimpleProductVO weChatSimpleProductVO = new WeChatSimpleProductVO();
                if (productId == null && i == 0) {
                    productId = productVO.getId();
                }
                //产品id
                weChatSimpleProductVO.setId(productVO.getId());
                //产品名称
                weChatSimpleProductVO.setProductName(productVO.getProductName());
                //产品还款方式
                weChatSimpleProductVO.setRepayType(productVO.getRepayType());
                //产品还款类型
                weChatSimpleProductVO.setRepayDayType(productVO.getRepayDayType());
                //产品服务费率
                weChatSimpleProductVO.setServiceFeeRate(productVO.getServiceFeeRate());
                //产品最小利率
                weChatSimpleProductVO.setMinInterestRate(productVO.getMinInterestRate());
                //产品最大利率
                weChatSimpleProductVO.setMaxInterestRate(productVO.getMaxInterestRate());
                productVOList.add(weChatSimpleProductVO);
            }
        }
        //选中的产品id
        vo.setProductId(productId);
        //渠道id
        vo.setChannelId(channelProductVO.getChannelId());
        //渠道编号，创建时自动生成，全局唯一
        vo.setChannelCode(channelProductVO.getChannelCode());
        //渠道名称
        vo.setChannelName(channelProductVO.getChannelName());
        vo.setSimpleProductVOList(productVOList);
        return vo;
    }
}
