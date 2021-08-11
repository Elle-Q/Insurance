package com.fintech.insurance.service.agg;

import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.micro.dto.thirdparty.ImageVO;
import com.fintech.insurance.micro.dto.thirdparty.QiniuFileVO;
import com.fintech.insurance.micro.dto.thirdparty.sms.SMSSendResultVO;
import com.fintech.insurance.micro.vo.wechat.SendVerificationParamVO;

import java.util.List;

public interface ThirdPartyService {

    ImageVO getImageVO(String fileName);

    /**
     * 获得图片url集合
     * @param fileNames 图片uuid集合
     * @return
     */
    List<String> getImageUrl(List<String> fileNames);

    /**
     * 获得缩略图图片url集合
     * @param fileNames 图片uuid集合
     * @return
     */
    List<String> getImageNarrowUrl(List<String> fileNames);
}
