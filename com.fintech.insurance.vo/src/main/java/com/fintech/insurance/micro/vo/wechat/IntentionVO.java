package com.fintech.insurance.micro.vo.wechat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class IntentionVO implements Serializable{

    //id
    private Integer id;

    //姓名
    private String name;

    //手机号
    @NotNull(message = "106301")
    private String mobile;

    //咨询内容
    private String consultContent;

    @NotNull(message = "106301")
    private String finsuranceMpUser;

    public String getFinsuranceMpUser() {
        return finsuranceMpUser;
    }

    public void setFinsuranceMpUser(String finsuranceMpUser) {
        this.finsuranceMpUser = finsuranceMpUser;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getConsultContent() {
        return consultContent;
    }

    public void setConsultContent(String consultContent) {
        this.consultContent = consultContent;
    }
}
