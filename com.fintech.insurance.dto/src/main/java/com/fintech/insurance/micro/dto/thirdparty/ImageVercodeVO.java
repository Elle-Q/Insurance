package com.fintech.insurance.micro.dto.thirdparty;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/11/13 13:22
 */
public class ImageVercodeVO {

    public ImageVercodeVO() {
    }

    public ImageVercodeVO(String vercodeId, String image) {
        this.vercodeId = vercodeId;
        this.image = image;
    }

    // 图片验证码标识
    private String vercodeId;

    // 图片验证码图片（Base64 string）
    private String image;

    public String getVercodeId() {
        return vercodeId;
    }

    public void setVercodeId(String vercodeId) {
        this.vercodeId = vercodeId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
