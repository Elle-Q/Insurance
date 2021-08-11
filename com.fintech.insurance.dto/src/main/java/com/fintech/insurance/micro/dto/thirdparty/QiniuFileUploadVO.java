package com.fintech.insurance.micro.dto.thirdparty;

import com.fintech.insurance.commons.enums.UploadFileType;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * @Description: 封装七牛云上传文件（二进制数据）的参数
 * @Author: Yong Li
 * @Date: 2018/1/6 17:05
 */
public class QiniuFileUploadVO {

    @NotNull
    private byte[] fileData;

    @NotNull
    private Integer isPublic;

    @NotNull
    private UploadFileType fileType;

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public Integer getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Integer isPublic) {
        this.isPublic = isPublic;
    }

    public UploadFileType getFileType() {
        return fileType;
    }

    public void setFileType(UploadFileType fileType) {
        this.fileType = fileType;
    }
}
