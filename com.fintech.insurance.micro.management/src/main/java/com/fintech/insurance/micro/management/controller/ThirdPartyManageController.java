package com.fintech.insurance.micro.management.controller;

import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.BaseFintechManagementController;
import com.fintech.insurance.micro.dto.thirdparty.QiNiuTokenVO;
import com.fintech.insurance.micro.feign.thirdparty.QiniuBusinessServiceFeign;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/12/7 14:11
 */
@RestController
@RequestMapping(value = "/management/thirdparty", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ThirdPartyManageController extends BaseFintechManagementController {

    private static final Logger LOG = LoggerFactory.getLogger(ThirdPartyManageController.class);

    @Autowired
    private QiniuBusinessServiceFeign qiniuBusinessServiceFeign;

    /**
     *  获取token
     * @param fileType 文件类型
     * @param isPublic 是否公有
     * @return
     */
    @RequestMapping(path = "/qiniu/get-token", method = RequestMethod.GET)
    public FintechResponse<QiNiuTokenVO> getQiniuUploadToken(@NotBlank String fileType, @NotNull Integer isPublic) {
        FintechResponse<QiNiuTokenVO> result = qiniuBusinessServiceFeign.getQiniuUploadToken(fileType, isPublic);
        if (!result.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(result);
        }
        return result;
    }

    /**
     *  获取token
     * @param fileName 文件名称
     * @param isPublic 是否公有
     * @return
     */
    @RequestMapping(path = "/qiniu/get-url", method = RequestMethod.GET)
    public FintechResponse<String> getQiniuDownloadUrl(@NotBlank String fileName, @NotNull Integer isPublic) {
        FintechResponse<String> result = qiniuBusinessServiceFeign.getQiniuDownloadUrl(fileName, isPublic);
        if (!result.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(result);
        }
        return result;
    }

}
