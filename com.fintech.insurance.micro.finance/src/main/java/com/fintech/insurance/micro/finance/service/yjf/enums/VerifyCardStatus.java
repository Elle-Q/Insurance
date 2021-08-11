package com.fintech.insurance.micro.finance.service.yjf.enums;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/12/9 16:53
 */
public enum VerifyCardStatus {

    VERIFY_CARD_FAIL("failed", "验卡失败"),
    VERIFY_CARD_SUCCESS("success", "验卡成功"),
    VERIFY_CARD_PROCESSING("processing", "验卡处理中");

    private String code;

    private String desc;

    private VerifyCardStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public String toString() {
        return this.name();
    }
}
