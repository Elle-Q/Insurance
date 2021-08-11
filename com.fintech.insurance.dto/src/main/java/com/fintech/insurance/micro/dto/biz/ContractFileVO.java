package com.fintech.insurance.micro.dto.biz;


import com.fintech.insurance.commons.annotations.FinanceDataPoint;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author: Man LIU
 * @Description: 合同文件vo
 * @Date: 2017/11/9 18:22
 */
public class ContractFileVO implements Serializable {
    //借款合同内容文件，转存七牛之后的文件
    private String contentFile;

   //上上签文件
    private String bestsignFile;

    //服务合同内容文件，转存七牛之后的文件
    private String serviceContentFile;

    //转存七牛之后的文件
    private String serviceBestSignFile;

    public String getContentFile() {
        return contentFile;
    }

    public void setContentFile(String contentFile) {
        this.contentFile = contentFile;
    }

    public String getBestsignFile() {
        return bestsignFile;
    }

    public void setBestsignFile(String bestsignFile) {
        this.bestsignFile = bestsignFile;
    }

    public String getServiceContentFile() {
        return serviceContentFile;
    }

    public void setServiceContentFile(String serviceContentFile) {
        this.serviceContentFile = serviceContentFile;
    }

    public String getServiceBestSignFile() {
        return serviceBestSignFile;
    }

    public void setServiceBestSignFile(String serviceBestSignFile) {
        this.serviceBestSignFile = serviceBestSignFile;
    }
}