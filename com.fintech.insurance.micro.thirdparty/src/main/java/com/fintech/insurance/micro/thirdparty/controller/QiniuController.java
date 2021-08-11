package com.fintech.insurance.micro.thirdparty.controller;


import com.fintech.insurance.commons.enums.BizCategory;
import com.fintech.insurance.commons.enums.UploadFileType;
import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.cache.RedisSequenceFactory;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.BaseFintechController;
import com.fintech.insurance.micro.api.thirdparty.QiniuBusinessServiceAPI;
import com.fintech.insurance.micro.dto.thirdparty.*;
import com.fintech.insurance.micro.thirdparty.service.QiniuCloudService;
import com.fintech.insurance.micro.thirdparty.service.qiniu.QiNiuPropertiesBean;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.io.*;


@RestController
@Validated
public class QiniuController extends BaseFintechController implements QiniuBusinessServiceAPI {

    private static final Logger logger = LoggerFactory.getLogger(QiniuController.class);

    private static final String QINIU_STRING = "_";//七牛key连接符

    @Autowired
    private QiniuCloudService qiniuService;

    @Autowired
    private RedisSequenceFactory redisSequenceFactory;

    @Autowired
    private QiNiuPropertiesBean qiNiuPropertiesBean;


    @Override
    public FintechResponse<QiNiuTokenVO> getQiniuUploadToken(@NotBlank String fileType, @NotNull Integer isPublic) {
        UploadFileType targetType = UploadFileType.codeOf(fileType);
        if (targetType == null) {
            throw new FInsuranceBaseException(106001);
        }
        QiNiuTokenVO qiNiuTokenVO = null;
        if (isPublic.equals(1)) {
            //获取公有tokenMap
            qiNiuTokenVO = this.qiniuService.getPublicBucketUploadToken();
        } else {
            //获取私有tokenMap
            qiNiuTokenVO = this.qiniuService.getPrivateBucketUploadToken();
        }
        if (qiNiuTokenVO == null || StringUtils.isBlank(String.valueOf(qiNiuTokenVO.getToken()))) {
            throw new FInsuranceBaseException(106002);
        }
        return FintechResponse.responseData(qiNiuTokenVO);
    }

    @Override
    public FintechResponse<String> getQiniuDownloadUrl(@NotBlank String fileName, @NotNull Integer isPublic) {
        String finalUrl = null;
        if (isPublic.equals(1)) {//获取公有文件地址
            finalUrl = this.qiniuService.getPublicFileUrl(fileName);
        } else {//获取私有文件地址
            finalUrl = this.qiniuService.getPrivateFileUrl(fileName, null, null, null);
        }
        if (StringUtils.isBlank(finalUrl)) {
            throw new FInsuranceBaseException(106003);
        }
        return FintechResponse.responseData(finalUrl);
    }

    @Override
    public FintechResponse<ImageVO> getQiniuDownloadUrlAndThumbnailUrl(@RequestParam(value = "fileName", required = false) String fileName) {
        ImageVO imageVO = new ImageVO();
        if(StringUtils.isBlank(fileName)){
            return FintechResponse.responseData(imageVO);
        }
        logger.info("getBatchQiniuPrivateDownloadUrl with fileName=[" + fileName + "]");
        imageVO.setImageUrl(this.qiniuService.getPrivateFileUrl(fileName, null, null, null));
        imageVO.setThumbnailUrl(this.qiniuService.getPrivateFileUrl(fileName, qiNiuPropertiesBean.getNarrowImageModel(),
                qiNiuPropertiesBean.getNarrowImageWidth(), qiNiuPropertiesBean.getNarrowImageHeight()));
        return FintechResponse.responseData(imageVO);
    }

    @Override
    public FintechResponse<QiniuBatchImageVO> getBatchQiniuPrivateDownloadUrl(@RequestBody QiniuBatchImageRequestVO requestVO) {
        QiniuBatchImageVO vo = new QiniuBatchImageVO();

        if (null != requestVO.getFileIds() && requestVO.getFileIds().size() > 0) {
            for (String fileId : requestVO.getFileIds()) {
                logger.info("getBatchQiniuPrivateDownloadUrl with fileId=[" + fileId + "]");
                vo.getThumbs().add(this.qiniuService.getPrivateFileUrl(fileId, requestVO.getModel(), requestVO.getImageWidth(), requestVO.getImageHeight()));
                if (requestVO.getIsFetchOrigin()) {
                    vo.getOrigins().add(this.qiniuService.getPrivateFileUrl(fileId, null, null, null));
                }
            }
        }

        return FintechResponse.responseData(vo);
    }

    @Override
    public FintechResponse<String> uploadFile(@NotBlank String filePath, @NotNull Integer isPublic, @NotBlank String fileType) {
        UploadFileType targetType = UploadFileType.codeOf(fileType);
        if (targetType == null) {
            throw new FInsuranceBaseException(106001);
        }
        File oldFile = new File(filePath);

        try {
            FileInputStream is = new FileInputStream(oldFile);
            return FintechResponse.responseData(uploadFileToQiniu(is, targetType, isPublic));
        } catch (FileNotFoundException e) {
            logger.error("uploadFile failed with filePath = [" + filePath + "], isPublic = [" + isPublic + "], fileType = [" + fileType + "]", e);
            throw new FInsuranceBaseException(106004);
        }
    }

    @Override
    public FintechResponse<String> uploadFileWithData(@Validated @RequestBody QiniuFileUploadVO updateFileVO) {
        /*if (StringUtils.isBlank(updateFileVO.getFileTempPath()) || !FileUtils.getFile(updateFileVO.getFileTempPath()).exists()) {
            throw new FInsuranceBaseException(106012);
        }

        File uploadFile = FileUtils.getFile(updateFileVO.getFileTempPath());
        InputStream inputStream = null;
        try {
            inputStream = FileUtils.openInputStream(uploadFile);
        } catch (IOException e) {
            logger.error("open inputStream failed, file path:" + updateFileVO.getFileTempPath());
        }
        // 上传文件完成后删除本地临时文件
        String qiniuKey = uploadFileToQiniu(inputStream, updateFileVO.getFileType(), updateFileVO.getIsPublic());
        FileUtils.deleteQuietly(FileUtils.getFile(updateFileVO.getFileTempPath()));
        return FintechResponse.responseData(qiniuKey);*/
        if (null == updateFileVO.getFileData() || updateFileVO.getFileData().length == 0) {
            throw new FInsuranceBaseException(106012);
        }

        return FintechResponse.responseData(uploadFileToQiniu(new ByteArrayInputStream(updateFileVO.getFileData()), updateFileVO.getFileType(),
                updateFileVO.getIsPublic()));
    }

    private String uploadFileToQiniu(InputStream inputStream, UploadFileType fileType, Integer isPublic) {
        String key = cateteQiNiuKey(fileType);
        //七牛云的key
        String qiniukey = null;
        try {
            if (isPublic.equals(1)) {
                qiniukey = this.qiniuService.uploadFileToPublicBucket(key, inputStream);
            } else {
                qiniukey = this.qiniuService.uploadFileToPrivateBucket(key, inputStream);
            }
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        return qiniukey;
    }
    @Override
    public FintechResponse<QiniuFileVO> remoteGrabFile(@NotBlank @RequestParam(value = "url") String url,
                                                  @NotNull @RequestParam(value = "isPublic") Integer isPublic,
                                                  @NotBlank @RequestParam(value = "fileType") String fileType) {
        UploadFileType targetType = UploadFileType.codeOf(fileType);
        if (targetType == null) {
            throw new FInsuranceBaseException(106001);
        }
        String key = cateteQiNiuKey(targetType);
        //返回文件的key
        String qniukey = qiniuService.remoteGrabFile(url, isPublic, key);
        if (StringUtils.isBlank(qniukey)) {
            throw new FInsuranceBaseException(106003);
        }

        FintechResponse<ImageVO> imageVOResp = this.getQiniuDownloadUrlAndThumbnailUrl(qniukey);
        QiniuFileVO fileVO = new QiniuFileVO();
        fileVO.setFileId(qniukey);
        fileVO.setFileUrl(imageVOResp.getData().getImageUrl());
        fileVO.setFileNarrowUrl(imageVOResp.getData().getThumbnailUrl());

        return FintechResponse.responseData(fileVO);
    }

    @Override
    public FintechResponse<VoidPlaceHolder> deleteFile(@NotBlank String key, @NotNull Integer isPublic) {
        this.qiniuService.deleteFile(key, isPublic);
        return FintechResponse.responseData(VoidPlaceHolder.instance());
    }

    //统一创建七牛云的key
    private String cateteQiNiuKey(UploadFileType targetType){
        if(targetType == null){
            throw new FInsuranceBaseException(106009);
        }
        String key = null;
        try {
            key = targetType.getFolder() + QINIU_STRING + redisSequenceFactory.generateSerialNumber(BizCategory.QN);
        } catch (Exception e) {
            logger.error("cateteQiNiuKey failed with targetType=[" + targetType.getCode() + "]", e);
            throw new FInsuranceBaseException(106010);
        }
        if(StringUtils.isBlank(key)){
            logger.error("cateteQiNiuKey failed with targetType=[" + targetType.getCode() + "]");
            throw new FInsuranceBaseException(106010);
        }
        return  key;
    }
}
