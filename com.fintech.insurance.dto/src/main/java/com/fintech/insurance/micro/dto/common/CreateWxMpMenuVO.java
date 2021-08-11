package com.fintech.insurance.micro.dto.common;

import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.mp.bean.menu.WxMpMenu;

import javax.validation.constraints.NotNull;

/**
 * 创建微信菜单请求的传参对象
 */
public class CreateWxMpMenuVO {

    @NotNull
    private String appid;

    @NotNull
    private WxMenu menu;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public WxMenu getMenu() {
        return menu;
    }

    public void setMenu(WxMenu menu) {
        this.menu = menu;
    }
}
