package com.fintech.insurance.micro.finance.service.yjf.model;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/12/6 14:42
 */
public class YjfRequest extends YjfBaseModel {

    /**
     * 页面跳转返回URL: 服务平台处理完请求后，当前页面自动跳转(307重定向)到商户网站里指定页面的http路径。非跳转接口不需要传此参数
     *
     */
    protected String returnUrl;

    /**
     * 异步通知URL: 当商户通过接口发起请求时，服务平台异步完成处理后，会主动连接该URL通知接口服务处理结果
     */
    protected String notifyUrl;

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    @Override
    public String toString() {
        return "YjfRequest{" +
                "returnUrl='" + returnUrl + '\'' +
                ", notifyUrl='" + notifyUrl + '\'' +
                ", protocol='" + protocol + '\'' +
                ", serviceCode='" + serviceCode + '\'' +
                ", version='" + version + '\'' +
                ", partnerId='" + partnerId + '\'' +
                ", orderNo='" + platformSerialNum + '\'' +
                ", signType='" + signType + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }
}
