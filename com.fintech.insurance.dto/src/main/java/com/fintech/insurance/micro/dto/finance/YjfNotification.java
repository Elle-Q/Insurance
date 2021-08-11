package com.fintech.insurance.micro.dto.finance;

/**
 * @Description: 由于易极付那边只支持POST formData形式， 所以这里使用该对象表示抽象的通知数据，所有的字段属性必须与文档
 *               中的属性名称一致。
 * @Author: Yong Li
 * @Date: 2017/12/13 14:38
 */
public class YjfNotification {
    /**
     * 协议类型: 报文协议格式, httpPost(默认)
     *
     */
    protected String protocol;

    /**
     * 服务代码:  接口服务代码, 请根据接口服务定义填写
     */
    protected String service;

    /**
     * 服务版本: 默认1.0
     */
    protected String version;

    /**
     * 商户ID: 签约的服务平台账号对应的合作方ID（由笨熊金服提供），定长20字符
     */
    protected String partnerId;

    /**
     * 请求流水号: 服务平台合作商户网站唯一流水号。长度：20-40字符
     */
    protected String orderNo;

    /**
     * 签名方式: 目前支持MD5
     */
    protected String signType;

    /**
     * 签名: 签名串
     */
    protected String sign;

    /**
     * 返回码，包括：
     * EXECUTE_SUCCESS：处理成功；
     * EXECUTE_FAIL：处理失败；
     * EXECUTE_PROCESSING：处理中
     */
    private String resultCode;


    /**
     *  返回信息: 返回码描述信息
     */
    protected String resultMessage;

    /**
     * 成功标识: 表示接口调用是否成功。true：成功 false：失败
     */
    protected String success;

    /**
     * 异步消息通知时间
     */
    protected  String notifyTime;


    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(String notifyTime) {
        this.notifyTime = notifyTime;
    }
}
