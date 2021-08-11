package com.fintech.insurance.micro.biz.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Description: (some words)
 * @Author: Administrator
 * @Date: 2017/11/10 0010 16:00
 */
@Component
@ConfigurationProperties(prefix = "fintech.export")
public class ExportExcelPropertiesBean {
    private Integer maxsize;//超过多少条导出压缩文件
    private String fileType;//区分xlsx,xls

    public ExportExcelPropertiesBean(Integer maxSize, String fileType) {
        this.maxsize = maxSize;
        this.fileType = fileType;
    }

    public ExportExcelPropertiesBean() {
    }

    public Integer getMaxsize() {
        return maxsize;
    }

    public void setMaxsize(Integer maxsize) {
        this.maxsize = maxsize;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
