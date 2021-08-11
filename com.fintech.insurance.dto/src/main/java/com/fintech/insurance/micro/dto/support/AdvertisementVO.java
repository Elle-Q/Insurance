package com.fintech.insurance.micro.dto.support;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fintech.insurance.commons.annotations.ImageUrl;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

public class AdvertisementVO implements Serializable {
    private Integer id;
    private String positionName;
    // 广告位置Id
    private Integer positionId;
    // 广告名称
    @NotBlank(message = "102001")
    @Size(min = 1, max = 50)
    private String title;
    // 跳转地址
    private String url;
    // 有效开始时间
    @NotNull(message = "102001")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date startTime;
    // 有效结束时间
    @NotNull(message = "102001")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date endTime;
    // 广告图标
    @ImageUrl
    private String imgKey;
    // 广告图标url
    public String imgKeyUrl;
    // 广告图标缩略图url
    public String imgKeyNarrowUrl;
    // 显示顺序
    @NotNull(message = "102001")
    private Integer sequence;
    // 广告状态
    private String adStatus;

    public String getImgKeyUrl() {
        return imgKeyUrl;
    }

    public void setImgKeyUrl(String imgKeyUrl) {
        this.imgKeyUrl = imgKeyUrl;
    }

    public String getImgKeyNarrowUrl() {
        return imgKeyNarrowUrl;
    }

    public void setImgKeyNarrowUrl(String imgKeyNarrowUrl) {
        this.imgKeyNarrowUrl = imgKeyNarrowUrl;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getAdStatus() {
        return adStatus;
    }

    public void setAdStatus(String adStatus) {
        this.adStatus = adStatus;
    }

    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public String getImgKey() {
        return imgKey;
    }

    public void setImgKey(String imgKey) {
        this.imgKey = imgKey;
    }

    public Integer getPositionId() {
        return positionId;
    }

    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }


    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
