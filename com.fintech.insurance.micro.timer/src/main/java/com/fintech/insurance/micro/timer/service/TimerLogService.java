package com.fintech.insurance.micro.timer.service;

import com.fintech.insurance.commons.enums.TimerStatus;
import com.fintech.insurance.micro.timer.persist.entity.TimerLog;
import org.springframework.stereotype.Service;

import java.util.Date;

public interface TimerLogService {

    //保存定时任务执行记录
    Integer save(Integer id, String name, String desc, TimerStatus status, Date startTime, Date endTime, String error);
}
