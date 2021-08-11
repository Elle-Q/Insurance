package com.fintech.insurance.micro.finance.service.yjf.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.fintech.insurance.micro.finance.service.yjf.enums.ResultCodeType;

/**
 * @Description: 易极付通用Response对象
 * @Author: Yong Li
 * @Date: 2017/12/6 13:28
 */
public class YjfResponse extends YjfBaseModel {

    /**
     * 商户订单号: 此参数是否返回要看具体的接口
     *
     */
    @JSONField(name = "merchOrderNo")
    protected String platformOrderNo;

    /**
     * 返回码: 返回码，包括：
                         EXECUTE_SUCCESS：处理成功；
                         EXECUTE_FAIL：处理失败；
                         EXECUTE_PROCESSING：处理中；
                         TIME_OUT:调用超时
     */
    protected ResultCodeType resultCodeType;

    /**
     *  返回信息: 返回码描述信息
     */
    protected String resultMessage;

    /**
     * 成功标识: 表示接口调用是否成功。true：成功 false：失败
     */
    @JSONField(name = "success")
    protected Boolean isSuccess;

    /**
     * 通知时间: 异步消息通知时间
     */
    protected String notifyTime;

    @Override
    public String toString() {
        return "YjfResponse{" +
                "protocol='" + protocol + '\'' +
                ", serviceCode='" + serviceCode + '\'' +
                ", version='" + version + '\'' +
                ", partnerId='" + partnerId + '\'' +
                ", orderNo='" + platformSerialNum + '\'' +
                ", merchOrderNo='" + platformOrderNo + '\'' +
                ", signType='" + signType + '\'' +
                ", sign='" + sign + '\'' +
                ", resultCode='" + resultCodeType.name() + '\'' +
                ", resultMessage='" + resultMessage + '\'' +
                ", success='" + isSuccess + '\'' +
                ", notifyTime='" + notifyTime + '\'' +
                '}';
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
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

    public String getPlatformOrderNo() {
        return platformOrderNo;
    }

    public void setPlatformOrderNo(String platformOrderNo) {
        this.platformOrderNo = platformOrderNo;
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

    @JSONField(serialize = false)
    public ResultCodeType getResultCodeType() {
        return resultCodeType;
    }

    @JSONField(deserialize = false)
    public void setResultCodeType(ResultCodeType resultCodeType) {
        this.resultCodeType = resultCodeType;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }


    public String getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(String notifyTime) {
        this.notifyTime = notifyTime;
    }

    @JSONField(name="resultCode")
    public String getResultCodeString() {
        return this.resultCodeType.name();
    }

    @JSONField(name="resultCode")
    public void setResultCodeString(String code) {
        this.resultCodeType = ResultCodeType.codeOf(code);
    }


    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean success) {
        isSuccess = success;
    }


}
