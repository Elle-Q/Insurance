package com.fintech.insurance.micro.dto.thirdparty;

import com.fintech.insurance.commons.annotations.ImageUrl;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2018/1/22 14:07
 */
public class QiniuFileVO {

    //文件ID
    @ImageUrl
    private String fileId;

    //文件私有云存储URL， 对于图片文件， 表示文件的原始图片的存储路径
    private String fileUrl;

    //文件私有云存储URL， 对于图片文件， 表示文件的缩略图图片的存储路径
    private String fileNarrowUrl;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileNarrowUrl() {
        return fileNarrowUrl;
    }

    public void setFileNarrowUrl(String fileNarrowUrl) {
        this.fileNarrowUrl = fileNarrowUrl;
    }
}
