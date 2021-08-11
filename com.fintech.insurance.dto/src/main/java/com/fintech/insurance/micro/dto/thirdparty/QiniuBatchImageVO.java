package com.fintech.insurance.micro.dto.thirdparty;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/12/18 18:16
 */
public class QiniuBatchImageVO {

    // 缩略图下载URL 数组
    private List<String> thumbs = new ArrayList<String>();


    // 原始图下载URL 数组
    private List<String> origins = new ArrayList<String>();

    public List<String> getThumbs() {
        return thumbs;
    }

    public void setThumbs(List<String> thumbs) {
        this.thumbs = thumbs;
    }

    public List<String> getOrigins() {
        return origins;
    }

    public void setOrigins(List<String> origins) {
        this.origins = origins;
    }
}
