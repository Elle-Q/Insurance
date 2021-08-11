package com.fintech.insurance.micro.finance.service.yjf.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.fintech.insurance.commons.constants.YjfConstants;
import com.fintech.insurance.micro.finance.service.yjf.YjfService;

/**
 * @Description: 扣款查询易极付请求
 * @Author: Yong Li
 * @Date: 2017/12/20 18:14
 */
@YjfService(name = YjfConstants.SERVICE_CODE_DEBT_QUERY)
public class DebtQueryRequest extends YjfRequest {

    @JSONField(name = "merchOrderNo")
    private String platformOrderNum;

    public String getPlatformOrderNum() {
        return platformOrderNum;
    }

    public void setPlatformOrderNum(String platformOrderNum) {
        this.platformOrderNum = platformOrderNum;
    }

    @Override
    public String toString() {
        return "DebtQueryRequest{" +
                "platformOrderNum='" + platformOrderNum + '\'' +
                ", returnUrl='" + returnUrl + '\'' +
                ", notifyUrl='" + notifyUrl + '\'' +
                ", protocol='" + protocol + '\'' +
                ", serviceCode='" + serviceCode + '\'' +
                ", version='" + version + '\'' +
                ", partnerId='" + partnerId + '\'' +
                ", platformSerialNum='" + platformSerialNum + '\'' +
                ", signType='" + signType + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }
}
