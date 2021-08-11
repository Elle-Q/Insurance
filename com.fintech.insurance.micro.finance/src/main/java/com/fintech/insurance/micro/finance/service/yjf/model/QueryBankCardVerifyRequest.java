package com.fintech.insurance.micro.finance.service.yjf.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.fintech.insurance.commons.constants.YjfConstants;
import com.fintech.insurance.micro.finance.service.yjf.YjfService;

/**
 * @Description: 验卡查询
 * @Author: Nicholas
 * @Date: 2018/1/22
 */
@YjfService(name = YjfConstants.SERVICE_CODE_QUERY_VERIFY_CARD)
public class QueryBankCardVerifyRequest extends YjfRequest {

    @JSONField(name = "outOrderNo")
    private String platformOrderNum;

    public String getPlatformOrderNum() {
        return platformOrderNum;
    }

    public void setPlatformOrderNum(String platformOrderNum) {
        this.platformOrderNum = platformOrderNum;
    }

    @Override
    public String toString() {
        return "QueryBankCardVerifyRequest{" +
                "platformOrderNum='" + platformOrderNum + '\'' +
                '}';
    }
}
