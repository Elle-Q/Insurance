package com.fintech.insurance.micro.api.thirdparty;

import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.micro.dto.thirdparty.*;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * @Description: (some words)
 * @Author: yongneng liu
 * @Date: 2017/11/11 0011 10:56
 */
@RequestMapping(value = "/thirdparty/qiniu", produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
        method = {RequestMethod.GET, RequestMethod.POST})
@Validated
public interface QiniuBusinessServiceAPI {

    /**
     *  获取token
     * @param fileType 文件类型
     * @param isPublic 是否公有
     * @return
     */
    @RequestMapping(path = "/get-token", method = RequestMethod.GET)
    public FintechResponse<QiNiuTokenVO> getQiniuUploadToken(@NotBlank @RequestParam(value = "fileType") String fileType,
                                                             @NotNull @RequestParam(value = "isPublic") Integer isPublic);


    /**
     *  获取token
     * @param fileName 文件名称
     * @param isPublic 是否公有
     * @return
     */
    @RequestMapping(path = "/get-url", method = RequestMethod.GET)
    public FintechResponse<String> getQiniuDownloadUrl(@NotBlank @RequestParam(value = "fileName") String fileName,
                                                       @NotNull @RequestParam(value = "isPublic") Integer isPublic);


    /**
     *  获取图片url和图片缩略图url
     * @param fileName 文件名称
     * @return
     */
    @RequestMapping(path = "/get-url-two", method = RequestMethod.GET)
    FintechResponse<ImageVO> getQiniuDownloadUrlAndThumbnailUrl(@RequestParam(value = "fileName", required = false) String fileName);

    /**
     * 批量获取文件的私有下载链接
     * @param requestVO 自定义图片的尺寸等信息
     * @return
     */
    @PostMapping(path = "/get-batch-private-url")
    public FintechResponse<QiniuBatchImageVO> getBatchQiniuPrivateDownloadUrl(@Validated @RequestBody QiniuBatchImageRequestVO requestVO);

    /**
     * 后端处理文件上传
     * @param filePath 文件路径
     * @param isPublic 1公，0私
     * @param fileType 文件类型
     * @return
     */
    @RequestMapping(value="/upload-file", method = RequestMethod.POST)
    public FintechResponse<String> uploadFile(@NotBlank @RequestParam(value = "filePath") String filePath,
                                              @NotNull @RequestParam(value = "isPublic") Integer isPublic,
                                              @NotBlank @RequestParam(value = "fileType") String fileType);

    /**
     * 后端处理文件上传 - 二进制文件上传
     * @param updateFileVO 文件上传对象的封装
     * @return
     */
    @RequestMapping(value="/upload-file-data", method = RequestMethod.POST)
    FintechResponse<String> uploadFileWithData(@Validated @RequestBody QiniuFileUploadVO updateFileVO);

    /**
     * 远程抓取文件
     * @param url 远程文件地址
     * @param isPublic 是否公有
     * @param fileType 文件类型
     * @return
     */
   @RequestMapping(value="/remote-grab-file", method = RequestMethod.GET)
   public FintechResponse<QiniuFileVO> remoteGrabFile(@NotBlank @RequestParam(value = "url") String url,
                                                 @NotNull @RequestParam(value = "isPublic") Integer isPublic,
                                                 @NotBlank @RequestParam(value = "fileType") String fileType);

    /**
     * 删除文件上传
     * @param key 文件key
     * @param isPublic 1公，0私
     * @return
     */
    @RequestMapping(value="/delete-file", method = RequestMethod.GET)
    public FintechResponse<VoidPlaceHolder>  deleteFile(@NotBlank @RequestParam(value = "key") String key, @NotNull @RequestParam(value = "isPublic") Integer isPublic);
}
