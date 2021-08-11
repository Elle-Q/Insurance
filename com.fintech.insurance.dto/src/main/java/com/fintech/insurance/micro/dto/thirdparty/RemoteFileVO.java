package com.fintech.insurance.micro.dto.thirdparty;

/**
 * @Description: 用于封装远程文件的对象
 * @Author: Yong Li
 * @Date: 2018/1/6 15:02
 */
public class RemoteFileVO {

    // 文件服务器的文件ID
    private String fileId;

    // 文件的下载链接
    private String fileDownloadURL;

    public RemoteFileVO() {
    }

    public RemoteFileVO(String fileId, String fileDownloadURL) {
        this.fileId = fileId;
        this.fileDownloadURL = fileDownloadURL;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileDownloadURL() {
        return fileDownloadURL;
    }

    public void setFileDownloadURL(String fileDownloadURL) {
        this.fileDownloadURL = fileDownloadURL;
    }
}
