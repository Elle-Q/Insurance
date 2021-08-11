package com.fintech.insurance.micro.dto.common;

import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 模版消息对象
 */
public class TemplateMessageVO implements Serializable {

    @NotNull
    private String appid;

    @NotNull
    private String openid;

    /**
     * 模版消息id
     */
    private String templateId;

    /**
     * 模版消息字符串，可以用来获取模版消息id
     */
    private String templateString;

    private String url;

    private List<WxMpTemplateData> templateData;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<WxMpTemplateData> getTemplateData() {
        return templateData;
    }

    public void setTemplateData(List<WxMpTemplateData> templateData) {
        this.templateData = templateData;
    }

    public String getTemplateString() {
        return templateString;
    }

    public void setTemplateString(String templateString) {
        this.templateString = templateString;
    }
}
