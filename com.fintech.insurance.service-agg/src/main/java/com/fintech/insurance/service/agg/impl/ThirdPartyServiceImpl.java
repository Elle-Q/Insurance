package com.fintech.insurance.service.agg.impl;

import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.micro.dto.thirdparty.ImageVO;
import com.fintech.insurance.micro.dto.thirdparty.QiniuFileVO;
import com.fintech.insurance.micro.dto.thirdparty.sms.SMSSendResultVO;
import com.fintech.insurance.micro.feign.thirdparty.QiniuBusinessServiceFeign;
import com.fintech.insurance.micro.vo.wechat.SendVerificationParamVO;
import com.fintech.insurance.service.agg.ThirdPartyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/12/15 17:51
 */
@Service
public class ThirdPartyServiceImpl implements ThirdPartyService {

    @Autowired
    private QiniuBusinessServiceFeign qiniuBusinessServiceFeign;

    @Override
    public ImageVO getImageVO(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return new ImageVO();
        }
        FintechResponse<ImageVO> imageVOFintechResponse = qiniuBusinessServiceFeign.getQiniuDownloadUrlAndThumbnailUrl(fileName);
        if (!imageVOFintechResponse.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(imageVOFintechResponse);
        }
        return imageVOFintechResponse.getData();
    }

    //获得图片url集合
    @Override
    public List<String> getImageUrl(List<String> fileNames) {
        if (null == fileNames || fileNames.isEmpty()) {
            return null;
        }
        List<String> imageUrls = new ArrayList<>();
        for (String fileName : fileNames) {
            ImageVO imageVO = null;
            FintechResponse<ImageVO> imageVOFintechResponse = qiniuBusinessServiceFeign.getQiniuDownloadUrlAndThumbnailUrl(fileName);
            if (!imageVOFintechResponse.isOk()) {
                throw FInsuranceBaseException.buildFromErrorResponse(imageVOFintechResponse);
            }
            if (null != imageVOFintechResponse.getData()) {
                imageVO = imageVOFintechResponse.getData();
                imageUrls.add(imageVO.getImageUrl());
            }
        }
        return imageUrls;
    }


    //获得缩略图图片url集合
    @Override
    public List<String> getImageNarrowUrl(List<String> fileNames) {
        if (null == fileNames || fileNames.isEmpty()) {
            return null;
        }
        List<String> imageNarrowUrls = new ArrayList<>();
        for (String fileName : fileNames) {
            ImageVO imageVO = null;
            FintechResponse<ImageVO> imageVOFintechResponse = qiniuBusinessServiceFeign.getQiniuDownloadUrlAndThumbnailUrl(fileName);
            if (!imageVOFintechResponse.isOk()) {
                throw FInsuranceBaseException.buildFromErrorResponse(imageVOFintechResponse);
            }
            if (null != imageVOFintechResponse.getData()) {
                imageVO = imageVOFintechResponse.getData();
                imageNarrowUrls.add(imageVO.getThumbnailUrl());
            }
        }
        return imageNarrowUrls;
    }
}
