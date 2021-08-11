package com.fintech.insurance.micro.finance.service.yjf.enums;

import com.fintech.insurance.commons.annotations.MicroEnum;
import com.github.binarywang.wxpay.constant.WxPayConstants;

/**
 * @Description: 易极付Response Result Code Type
 * @Author: Yong Li
 * @Date: 2017/12/6 15:42
 */
@MicroEnum
public enum ResultCodeType {

    EXECUTE_SUCCESS("处理成功"),
    EXECUTE_FAIL("处理失败"),
    EXECUTE_PROCESSING("处理中"),
    TIME_OUT("调用超时"),
    ILLEGAL_PARAMETER("参数不合法"),
    PARAMETER_ERROR("参数错误"),
    INSTALLMENT_TRANS_ORDER_NO_DATA("还款/支付数据不存在");

    private String desc;

    private ResultCodeType(String desc) {
        this.desc = desc;
    }

    public static ResultCodeType codeOf(String name) {
        try {
            return ResultCodeType.valueOf(name);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return this.name();
    }
}
