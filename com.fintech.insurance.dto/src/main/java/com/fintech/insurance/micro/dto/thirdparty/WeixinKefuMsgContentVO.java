package com.fintech.insurance.micro.dto.thirdparty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

public class WeixinKefuMsgContentVO implements Serializable {

    @NotNull
    private String appid;

    @NotNull
    private String openid;

    @NotNull
    private String content;

    private List<String> params = null;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getParams() {
        return params;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }

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
}
