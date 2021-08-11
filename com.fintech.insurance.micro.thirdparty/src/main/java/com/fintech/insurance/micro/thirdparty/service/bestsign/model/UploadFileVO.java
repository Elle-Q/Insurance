package com.fintech.insurance.micro.thirdparty.service.bestsign.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.fintech.insurance.micro.thirdparty.service.bestsign.enums.FileType;

/**
 * @Description: 文件上传参数封装
 * @Author: Yong Li
 * @Date: 2017/11/22 14:32
 */
public class UploadFileVO {

    @JSONField(name = "account")
    private String accountId;

    @JSONField(name = "fdata")
    private String fileData;

    @JSONField(name = "fmd5")
    private String fileMD5;

    private FileType fileType;

    @JSONField(name = "fname")
    private String fileSimpleName;

    @JSONField(name = "fpages")
    private Integer filePages;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getFileData() {
        return fileData;
    }

    public void setFileData(String fileData) {
        this.fileData = fileData;
    }

    public String getFileMD5() {
        return fileMD5;
    }

    public void setFileMD5(String fileMD5) {
        this.fileMD5 = fileMD5;
    }

    @JSONField(name = "ftype")
    public String getFileTypeCode() {
        return this.fileType.getCode();
    }

    @JSONField(name = "ftype")
    public void setFileTypeCode(String code) {
        this.fileType = FileType.getByCode(code);
    }

    @JSONField(serialize = false)
    public FileType getFileType() {
        return fileType;
    }

    @JSONField(deserialize = false)
    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public String getFileSimpleName() {
        return fileSimpleName;
    }

    public void setFileSimpleName(String fileSimpleName) {
        this.fileSimpleName = fileSimpleName;
    }

    public Integer getFilePages() {
        return filePages;
    }

    public void setFilePages(Integer filePages) {
        this.filePages = filePages;
    }
}
