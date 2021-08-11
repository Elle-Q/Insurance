package com.fintech.insurance.service.agg.impl;

import com.fintech.insurance.commons.enums.UserType;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.micro.dto.biz.ChannelVO;
import com.fintech.insurance.micro.dto.biz.ProductVO;
import com.fintech.insurance.micro.dto.biz.SimpleProductVO;
import com.fintech.insurance.micro.dto.thirdparty.ImageVO;
import com.fintech.insurance.micro.feign.biz.ChannelServiceFeign;
import com.fintech.insurance.micro.feign.biz.ProductBusinessServiceFegin;
import com.fintech.insurance.micro.feign.customer.CustomerServiceFeign;
import com.fintech.insurance.micro.feign.thirdparty.QiniuBusinessServiceFeign;
import com.fintech.insurance.micro.vo.wechat.WeChatProductVO;
import com.fintech.insurance.service.agg.CommonProductService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;

/**
 * @Description: (some words)
 * @Author: Administrator
 * @Date: 2017/12/11 0011 16:12
 */
@Service
public class CommonProductServiceImpl implements CommonProductService {

    @Autowired
    private ProductBusinessServiceFegin productBusinessServiceFegin;

    @Autowired
    private CustomerServiceFeign customerServiceFeign;

    @Autowired
    private ChannelServiceFeign channelServiceFeign;

    @Autowired
    private QiniuBusinessServiceFeign qiniuBusinessServiceFeign;

    public WeChatProductVO findProductInfoById(String userType, Integer userId, String channelCode, Integer productId) {
        //获取单前登录用id
        Integer currentLoginUserId = userId;
        ProductVO productVO = null;
        if(productId != null) {
            FintechResponse<ProductVO> voFintechResponse = productBusinessServiceFegin.findWeChatProductInfoById(productId);
            if (!voFintechResponse.isOk() || voFintechResponse.getData() == null) {
                throw new FInsuranceBaseException(104102);
            }
            productVO = voFintechResponse.getData();
        }
        WeChatProductVO vo = getWeChatProductVOByEntity(productVO);
        if(StringUtils.equals(userType, UserType.CUSTOMER.getCode())) {
            FintechResponse<Boolean> lockedFlagResponse = customerServiceFeign.getCustomerLockedStatusById(currentLoginUserId);
            vo.setIsLocked(lockedFlagResponse != null && lockedFlagResponse.isOk() && lockedFlagResponse.getData() != null && lockedFlagResponse.getData() ? 1 : 0);
        }else if(StringUtils.equals(userType, UserType.CHANNEL.getCode())){
            ChannelVO channelVO = channelServiceFeign.getChannelDetailByChannelCode(channelCode).getData();
            vo.setIsLocked(channelVO != null ? 1 : channelVO.getIsLocked());
        }else{
            vo.setIsLocked(1);
        }
        return vo;
    }

    //转微信产品vo
    private WeChatProductVO getWeChatProductVOByEntity(ProductVO entity) {
        WeChatProductVO vo = new WeChatProductVO();
        if (entity == null) {
            return vo;
        }
        vo.setId(entity.getId());
        vo.setProductBanner(entity.getProductBanner());
        vo.setProductDescription(entity.getProductDescription());
        vo.setProductName(entity.getProductName());
        vo.setProductType(entity.getProductType());
        return vo;
    }
}
