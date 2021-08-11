package com.fintech.insurance.micro.timer.scheduled;

import com.fintech.insurance.commons.annotations.FInsuranceTimer;
import com.fintech.insurance.micro.timer.controller.VerifyBankCardTimerController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 验卡
 * @author Nicholas
 * @since 2018/1/22
 */
@Component
@Async
public class VerifyBankCardScheduler {

    @Autowired
    private VerifyBankCardTimerController verifyBankCardTimerController;

    /**
     * 系统每过4小时将本地数据库验卡结果为处理中的请求单号重新查询第三方
     */
    @Scheduled(cron = "0 0 0/4 * * ?")
    @FInsuranceTimer(name = "updateVerifyBankcardRecord", desc = "定时更新验卡记录")
    public void updateVerifyBankcardRecord() {
        this.verifyBankCardTimerController.queryAndUpdateVerifyBankcardRecord();
    }
}
