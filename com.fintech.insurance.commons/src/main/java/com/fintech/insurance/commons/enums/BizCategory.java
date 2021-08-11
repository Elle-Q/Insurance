package com.fintech.insurance.commons.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/12/12 19:48
 */
public enum BizCategory {

    VH("VH", "凭证序列号"),
    SN("SN", "合同序列缓存的key"),
    PON("PON", "服务单(支付单)缓存的key"),
    REQ_AUDIT_ID("REQAUT", "申请单的审批批次号"),
    YJF_VB("YJFVB", "易极付验卡Verify Bankcard序列号"),
    YJF_DM("YJFDM", "易极付扣款Debt Money序列号"),
    YJF("YJF", "易极付交易流水序列号"),
    DEBT_BATCH("DEBT_BATCH", "合并扣款批次序列号"),
    RK("RK", "申请单号"),
    IMAGE_VERCODE("IMGV", "图片验证码序列号"),
    QN("QN", "七牛云存储文件序列号");


    private String code;
    private String desc;

    private BizCategory(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return this.code;
    }


}
