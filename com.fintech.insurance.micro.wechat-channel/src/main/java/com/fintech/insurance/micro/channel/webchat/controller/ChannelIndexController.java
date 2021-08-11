package com.fintech.insurance.micro.channel.webchat.controller;

import com.fintech.insurance.commons.enums.AdStatus;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.annotation.RequireWechatLogin;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.BaseFintechWechatController;
import com.fintech.insurance.micro.api.support.AdvertisementServiceAPI;
import com.fintech.insurance.micro.dto.biz.ChannelProductVO;
import com.fintech.insurance.micro.dto.biz.ChannelVO;
import com.fintech.insurance.micro.dto.biz.SimpleProductVO;
import com.fintech.insurance.micro.dto.support.AdPositionVO;
import com.fintech.insurance.micro.dto.support.AdvertisementVO;
import com.fintech.insurance.micro.dto.thirdparty.ImageVO;
import com.fintech.insurance.micro.feign.biz.ChannelServiceFeign;
import com.fintech.insurance.micro.feign.biz.ProductBusinessServiceFegin;
import com.fintech.insurance.micro.feign.thirdparty.QiniuBusinessServiceFeign;
import com.fintech.insurance.micro.vo.wechat.AdVO;
import com.fintech.insurance.micro.vo.wechat.ChannelIndexVO;
import com.fintech.insurance.micro.vo.wechat.ProductVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Clayburn
 * @Description: 微信渠道端首页接口
 * @Date: 2017/12/9 11:30
 */
@RestController
@RequireWechatLogin
@RequestMapping(value = "/wechat/channel", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ChannelIndexController extends BaseFintechWechatController {
    @Autowired
    private AdvertisementServiceAPI advertisementService;

    @Autowired
    private ProductBusinessServiceFegin productBusinessServiceFegin;

    @Autowired
    private ChannelServiceFeign channelServiceFeign;

    @Autowired
    private QiniuBusinessServiceFeign qiniuBusinessServiceFeign;

    @GetMapping(path = "/index")
    public FintechResponse<ChannelIndexVO> getChannelIndex() {

        //当前渠道客户的渠道
        String channelCode = this.getCurrentUserChannelCode();
        if(StringUtils.isBlank(channelCode)){
            throw new FInsuranceBaseException(107026);
        }

        // 查询出正在进行的广告
        FintechResponse<List<AdPositionVO>> response = advertisementService.listAllPosition();
        if (!response.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(response);
        }
        if (response.getData() == null) {
            throw new FInsuranceBaseException(107001);
        }
        Integer positionId = null;
        for (AdPositionVO  adPositionVO : response.getData()) {
            if (adPositionVO.getPositionCode().equals("CHANNEL_WECHAT_BANNER")) {
                positionId = adPositionVO.getId();
                break;
            }
        }
        FintechResponse<Pagination<AdvertisementVO>> adResponse = advertisementService.getAllAdvertisement(positionId, AdStatus.UNDERWAY.getCode(), 1, Integer.MAX_VALUE);
        if (!response.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(adResponse);
        }
        if (response.getData() == null) {
            throw new FInsuranceBaseException(107002);
        }
        List<AdvertisementVO> advertisementVOList = adResponse.getData().getItems();
        // 转换VO
        List<AdVO> adVOList = new ArrayList<>();
        for (AdvertisementVO advertisementVO : advertisementVOList) {
            AdVO vo = new AdVO();
            vo.setId(advertisementVO.getId());
            vo.setTitle(advertisementVO.getTitle());
            vo.setImg(advertisementVO.getImgKey());
            if (null != getImageVO(advertisementVO.getImgKey())) {
                ImageVO imageVO = new ImageVO();
                vo.setImgUrl(imageVO.getImageUrl());
                vo.setImgNarrowUrl(imageVO.getThumbnailUrl());
            }
            vo.setSequence(advertisementVO.getSequence());
            vo.setUrl(advertisementVO.getUrl());
            vo.setPositionId(advertisementVO.getPositionId());
            vo.setPositionName(advertisementVO.getPositionName());

            adVOList.add(vo);
        }
        // 查询出该渠道下的产品;
        FintechResponse<ChannelVO> channelResponse = channelServiceFeign.getChannelDetailByChannelCode(channelCode);
        if (!channelResponse.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(channelResponse);
        }
        if (channelResponse.getData() == null) {
            throw new FInsuranceBaseException(107026);
        }

        FintechResponse<ChannelProductVO> productResponse = productBusinessServiceFegin.queryWeChatProductInfoByChannelId( null, channelResponse.getData().getId(),null , 1, Integer.MAX_VALUE);
        if (!productResponse.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(productResponse);
        }
        if (null == productResponse.getData()) {
            throw new FInsuranceBaseException(107003);
        }
        List<SimpleProductVO> simpleProductVOList = productResponse.getData().getSimpleProductVOList();
        List<ProductVO> productVOList = new ArrayList<>();
        for (SimpleProductVO simpleProductVO : simpleProductVOList) {
            ProductVO vo = new ProductVO();
            vo.setId(simpleProductVO.getId());
            vo.setProductName(simpleProductVO.getProductName());
            vo.setProductType(simpleProductVO.getProductType());
            vo.setServiceFeeRate(simpleProductVO.getServiceFeeRate());
            vo.setMinInterestRate(simpleProductVO.getMinInterestRate());
            vo.setMaxInterestRate(simpleProductVO.getMaxInterestRate());
            vo.setRepayType(simpleProductVO.getRepayType());
            vo.setRepayDayType(simpleProductVO.getRepayDayType());

            productVOList.add(vo);
        }

        ChannelIndexVO channelIndexVO = new ChannelIndexVO();
        channelIndexVO.setAdVOList(adVOList);
        channelIndexVO.setProductVOList(productVOList);
        channelIndexVO.setIsLocked(channelResponse.getData().getIsLocked());
        return FintechResponse.responseData(channelIndexVO);
    }


    private ImageVO getImageVO(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return new ImageVO();
        }
        FintechResponse<ImageVO> imageVOFintechResponse = qiniuBusinessServiceFeign.getQiniuDownloadUrlAndThumbnailUrl(fileName);
        if (!imageVOFintechResponse.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(imageVOFintechResponse);
        }
        return imageVOFintechResponse.getData();
    }
}
