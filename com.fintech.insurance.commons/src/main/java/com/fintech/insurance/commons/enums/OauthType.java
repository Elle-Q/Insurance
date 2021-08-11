package com.fintech.insurance.commons.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum OauthType {

    MOBILE("mobile", "手机号码"),
    WECHAT_MP("wechat_mp", "微信公众号"),
    WECHAT_CP("wechat_cp", "微信企业号"),
    WECHAT_APP("wechat_app", "微信APP"),
    WECHAT_PC("wechat_pc", "微信PC端"),
    QQ("qq", "QQ"),
    QZONE("qzone", "QQ空间"),
    WEIBO("weibo", "微博");


    private String code;
    private String desc;

    private OauthType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static OauthType codeOf(String code) {
        for (OauthType type : OauthType.values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    public String getDesc() {
        return this.desc;
    }

    @Override
    public String toString() {
        return this.code;
    }
}
