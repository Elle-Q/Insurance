package com.fintech.insurance.micro.timer.scheduled;

import com.fintech.insurance.commons.annotations.FInsuranceTimer;
import com.fintech.insurance.micro.timer.controller.ContractTimerController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 生成合同文件
 * @author qxy
 * @since 2017/12/19 16:24
 */
@Component
@Async
public class ContractScheduler {

    @Autowired
    private ContractTimerController contractTimerController;

    /**
     * 对所有申请单的状态不是： 待提交， 待确认，已退回，已取消的生成合同文件
     */
    @Scheduled(cron = "0 0 2,4 * * ?")
    @FInsuranceTimer(name = "generateContractFileForRequisition", desc = "对所有申请单的状态不是： 待提交， 待确认，已退回，已取消的生成合同文件")
    public void generateContractFileForRequisition() {
        this.contractTimerController.generateContractFileForRequisition();
    }

}
