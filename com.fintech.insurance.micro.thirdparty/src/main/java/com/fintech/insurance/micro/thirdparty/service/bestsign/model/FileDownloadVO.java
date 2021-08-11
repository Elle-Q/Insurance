package com.fintech.insurance.micro.thirdparty.service.bestsign.model;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/11/22 20:59
 */
public class FileDownloadVO {

    /**
     * 文件名
     */
    @JSONField(name = "name")
    private String fileName;

    /**
     * 下载地址
     */
    @JSONField(name = "url")
    private String downloadUrl;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}
