package com.fintech.insurance.micro.finance.service.yjf.enums;

import com.fintech.insurance.commons.enums.DebtStatus;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * @Description: 易极付扣款状态
 * @Author: Yong Li
 * @Date: 2017/12/13 13:45
 */
public enum YJFDebtProcessStatus {

    CHECK_REJECT("CHECK_REJECT", "审核驳回"),
    WITHHOLD_FAIL("WITHHOLD_FAIL", "还款失败"),
    WITHHOLD_SUCCESS("WITHHOLD_SUCCESS", "还款成功"),
    PROFIT_SUCCESS("PROFIT_SUCCESS", "分润成功"),
    SETTLE_SUCCESS("SETTLE_SUCCESS", "结算成功");

    private String code;
    private String desc;

    private YJFDebtProcessStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static YJFDebtProcessStatus codeOf(String code) {
        for (YJFDebtProcessStatus status : YJFDebtProcessStatus.values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalStateException("Not found the mapping DebtProcessStatus for code:" + code);
    }

    public static DebtStatus convertToDebtStatus(YJFDebtProcessStatus processStatus) {
        DebtStatus debtStatus = null;
        switch (processStatus) {
            case CHECK_REJECT:
                debtStatus = DebtStatus.FAILED;
                break;
            case WITHHOLD_FAIL:
                debtStatus = DebtStatus.FAILED;
                break;
            case WITHHOLD_SUCCESS:
                debtStatus = DebtStatus.CONFIRMED;
                break;
            case PROFIT_SUCCESS:
                debtStatus = DebtStatus.CONFIRMED;
                break;
            case SETTLE_SUCCESS:
                debtStatus = DebtStatus.SETTLED;
                break;
            default:
                throw new NotImplementedException();
        }
        return debtStatus;
    }

    @Override
    public String toString() {
        return this.code;
    }
}
