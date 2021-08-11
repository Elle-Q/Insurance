package com.fintech.insurance.micro.dto.thirdparty;

import javax.validation.constraints.NotNull;

/**
 * 用于封装上传的word文件
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2018/1/3 14:26
 */
public class WordFileVO {

    // word的二进制文件
    private byte[] fileData;

    // 客户手机号码， 用于识别文件的命名
    @NotNull
    private String customerMobile;

    // word文件的页数
    private Integer wordPages = 0;

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public Integer getWordPages() {
        return wordPages;
    }

    public void setWordPages(Integer wordPages) {
        this.wordPages = wordPages;
    }
}
