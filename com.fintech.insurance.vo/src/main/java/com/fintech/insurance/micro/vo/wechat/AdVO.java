package com.fintech.insurance.micro.vo.wechat;

import com.fintech.insurance.commons.annotations.ImageUrl;

import java.io.Serializable;

/**
 * @Author: Clayburn
 * @Description: 微信端广告VO
 * @Date: 2017/12/8 13:48
 */
public class AdVO implements Serializable {
    // 广告id
    private Integer id;
    // 广告名称
    private String title;
    // 图片uuid
    @ImageUrl
    private String img;

    // 图片url
    public String imgUrl;

    // 图片缩略图url
    public String imgNarrowUrl;

    // 显示顺序
    private Integer sequence;
    // 跳转地址
    private String url;
    // 广告位置id
    private Integer positionId;
    // 广告位置名称
    private String positionName;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImgNarrowUrl() {
        return imgNarrowUrl;
    }

    public void setImgNarrowUrl(String imgNarrowUrl) {
        this.imgNarrowUrl = imgNarrowUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getPositionId() {
        return positionId;
    }

    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }
}
