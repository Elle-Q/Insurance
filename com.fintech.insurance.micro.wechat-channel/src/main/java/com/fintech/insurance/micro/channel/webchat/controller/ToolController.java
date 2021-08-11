package com.fintech.insurance.micro.channel.webchat.controller;

import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.annotation.RequireWechatLogin;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.BaseFintechWechatController;
import com.fintech.insurance.micro.dto.thirdparty.QiNiuTokenVO;
import com.fintech.insurance.micro.dto.thirdparty.QiniuBatchImageRequestVO;
import com.fintech.insurance.micro.dto.thirdparty.QiniuBatchImageVO;
import com.fintech.insurance.micro.dto.thirdparty.QiniuFileVO;
import com.fintech.insurance.micro.feign.thirdparty.QiniuBusinessServiceFeign;
import com.fintech.insurance.service.agg.ThirdPartyService;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/12/15 17:58
 */
@RestController
@RequireWechatLogin
@RequestMapping(value = "/wechat/channel/tool", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ToolController extends BaseFintechWechatController {

    @Autowired
    private ThirdPartyService thirdPartyService;

    @Autowired
    private QiniuBusinessServiceFeign qiniuBusinessServiceFeign;

    /**
     *  获取token
     * @param fileType 文件类型
     * @param isPublic 是否公有
     * @return
     */
    @RequestMapping(path = "/qiniu/get-token", method = RequestMethod.GET)
    public FintechResponse<QiNiuTokenVO> getQiniuUploadToken(@NotBlank @RequestParam(value = "fileType") String fileType,
                                                             @NotNull @RequestParam(value = "isPublic") Integer isPublic) {
        return qiniuBusinessServiceFeign.getQiniuUploadToken(fileType, isPublic);
    }

    /**
     *  获取token
     * @param fileName 文件名称
     * @param isPublic 是否公有
     * @return
     */
    @RequestMapping(path = "/qiniu/get-url", method = RequestMethod.GET)
    public FintechResponse<String> getQiniuDownloadUrl(@NotBlank @RequestParam(value = "fileName") String fileName,
                                                       @NotNull @RequestParam(value = "isPublic") Integer isPublic) {
        return qiniuBusinessServiceFeign.getQiniuDownloadUrl(fileName, isPublic);
    }


    /**
     * 远程抓取文件
     * @param url 远程文件地址
     * @param isPublic 是否公有
     * @param fileType 文件类型
     * @return
     */

    @GetMapping(value = "/qiniu/remote-grab-file")
    public FintechResponse<QiniuFileVO> emoteGrabFile(@NotBlank @RequestParam(value = "url") String url,
                                                      @RequestParam(value = "isPublic", defaultValue = "0") Integer isPublic,
                                                      @NotBlank @RequestParam(value = "fileType") String fileType) {
        FintechResponse<QiniuFileVO> response = qiniuBusinessServiceFeign.remoteGrabFile(url, isPublic, fileType);
        if (!response.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(response);
        }
        return response;
    }

    /**
     * 批量获取文件的私有下载链接
     * @param requestVO 自定义图片的尺寸等信息
     * @return
     */
    @PostMapping(path = "/qiniu/get-batch-private-url")
    public FintechResponse<QiniuBatchImageVO> getBatchQiniuPrivateDownloadUrl(@Validated @RequestBody QiniuBatchImageRequestVO requestVO) {
        FintechResponse<QiniuBatchImageVO> response = qiniuBusinessServiceFeign.getBatchQiniuPrivateDownloadUrl(requestVO);
        if (!response.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(response);
        }
        return response;
    }


}
