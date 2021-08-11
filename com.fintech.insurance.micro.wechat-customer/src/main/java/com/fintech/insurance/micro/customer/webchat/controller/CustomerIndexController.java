package com.fintech.insurance.micro.customer.webchat.controller;

import com.fintech.insurance.commons.enums.AdStatus;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.BaseFintechWechatController;
import com.fintech.insurance.micro.dto.support.AdPositionVO;
import com.fintech.insurance.micro.dto.support.AdvertisementVO;
import com.fintech.insurance.micro.dto.thirdparty.ImageVO;
import com.fintech.insurance.micro.feign.support.AdvertisementServiceFeign;
import com.fintech.insurance.micro.feign.thirdparty.QiniuBusinessServiceFeign;
import com.fintech.insurance.micro.vo.wechat.AdVO;
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
 * @Description:
 * @Date: 2017/12/9 13:42
 */
@RestController
@RequestMapping(value = "/wechat/customer", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)

public class CustomerIndexController extends BaseFintechWechatController {
    @Autowired
    private AdvertisementServiceFeign advertisementServiceFeign;

    @Autowired
    private QiniuBusinessServiceFeign qiniuBusinessServiceFeign;


    @GetMapping(path = "/index")
    public FintechResponse<List<AdVO>> getCustomerIndex() {
        // 查询出正在进行的广告
        FintechResponse<List<AdPositionVO>> response = advertisementServiceFeign.listAllPosition();
        if (response == null || !response.isOk() || response.getData() == null) {
            throw new FInsuranceBaseException(107001);
        }
        Integer positionId = null;
        for (AdPositionVO adPositionVO : response.getData()) {
            if (adPositionVO.getPositionCode().equals("CUST_WECHAT_BANNER")) {
                positionId = adPositionVO.getId();
                break;
            }
        }
        FintechResponse<Pagination<AdvertisementVO>> adResponse = advertisementServiceFeign.getAllAdvertisement(positionId, AdStatus.UNDERWAY.getCode(), 1, Integer.MAX_VALUE);
        if (adResponse == null || !response.isOk() || response.getData() == null) {
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
            vo.setSequence(advertisementVO.getSequence());
            vo.setUrl(advertisementVO.getUrl());
            vo.setPositionId(advertisementVO.getPositionId());
            vo.setPositionName(advertisementVO.getPositionName());

            adVOList.add(vo);
        }

        return FintechResponse.responseData(adVOList);
    }
}
