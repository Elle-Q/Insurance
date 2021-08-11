package com.fintech.insurance.micro.vo.wechat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author: qxy
 * @Description: 短信验证码VO
 * @Date: 2017/12/8 14:53
 */
public class SendVerificationParamVO implements Serializable{
    /**
     * 短信接收号码
     */
    @NotNull(message = "106123")
    private String phoneNumber;
    /**
     * 短信事件编码
     */
    @NotNull(message = "106122")
    private String eventCode;

    // 图片验证码标识
    private String vercodeId;

    //用户输入的图片验证码
    private String content;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    public String getVercodeId() {
        return vercodeId;
    }

    public void setVercodeId(String vercodeId) {
        this.vercodeId = vercodeId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
