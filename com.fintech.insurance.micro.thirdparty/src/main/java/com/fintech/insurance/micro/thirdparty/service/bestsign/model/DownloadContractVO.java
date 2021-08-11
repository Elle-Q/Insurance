package com.fintech.insurance.micro.thirdparty.service.bestsign.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/11/22 21:01
 */
public class DownloadContractVO {

    @JSONField(name = "attachmentList")
    private List<FileDownloadVO> attachmentList;


    @JSONField(name = "contractList")
    private List<FileDownloadVO> contractList;

    public List<FileDownloadVO> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(List<FileDownloadVO> attachmentList) {
        this.attachmentList = attachmentList;
    }

    public List<FileDownloadVO> getContractList() {
        return contractList;
    }

    public void setContractList(List<FileDownloadVO> contractList) {
        this.contractList = contractList;
    }
}
