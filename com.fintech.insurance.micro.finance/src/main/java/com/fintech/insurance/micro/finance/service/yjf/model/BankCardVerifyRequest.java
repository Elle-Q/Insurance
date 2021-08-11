package com.fintech.insurance.micro.finance.service.yjf.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.fintech.insurance.commons.constants.YjfConstants;
import com.fintech.insurance.micro.finance.service.yjf.YjfService;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/12/6 15:01
 */
@YjfService(name = YjfConstants.SERIVE_CODE_VERIFY_CARD)
public class BankCardVerifyRequest extends YjfRequest {

    /**
     * 商户订单号: 新验卡订单需保证外部订单号的唯一性
     *
     * 如商户传入同一订单号，视为重发验证短信
     */
    @JSONField(name = "outOrderNo")
    private String platformOrderNum;

    /**
     * 客户真实姓名
     */
    @JSONField(name = "name")
    private String customerName;

    /**
     * 客户身份证号码
     */
    @JSONField(name = "certNo")
    private String certNo;

    /**
     * 银行卡号
     */
    @JSONField(name = "bankCardNo")
    private String bankCardNo;

    /**
     * 预留手机号: 验卡类型为四要素或四要素短信时，预留手机号必传
     * 不能为空
     */
    @JSONField(name = "mobileNo")
    private String mobileNo;


    /**
     * 验卡类型：三要素、四要素、四要素短信
     * 目前在这里使用四要素短信验卡
     */
    @JSONField(name = "verifyCardType")
    private String verifyCardType = "FOUR_ELEMENT";

    @Override
    public String toString() {
        return "BankCardVerifyRequest{" +
                "outOrderNo='" + platformOrderNum + '\'' +
                ", customerName='" + customerName + '\'' +
                ", certNo='" + certNo + '\'' +
                ", bankCardNo='" + bankCardNo + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                ", verifyCardType='" + verifyCardType + '\'' +
                ", returnUrl='" + returnUrl + '\'' +
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

    public String getPlatformOrderNum() {
        return platformOrderNum;
    }

    public void setPlatformOrderNum(String platformOrderNum) {
        this.platformOrderNum = platformOrderNum;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCertNo() {
        return certNo;
    }

    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getVerifyCardType() {
        return verifyCardType;
    }

    public void setVerifyCardType(String verifyCardType) {
        this.verifyCardType = verifyCardType;
    }
}
