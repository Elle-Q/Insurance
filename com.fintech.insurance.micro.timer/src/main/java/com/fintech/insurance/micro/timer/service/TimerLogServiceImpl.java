package com.fintech.insurance.micro.timer.service;

import com.fintech.insurance.commons.enums.TimerStatus;
import com.fintech.insurance.micro.timer.persist.dao.TimerLogDao;
import com.fintech.insurance.micro.timer.persist.entity.TimerLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class TimerLogServiceImpl implements TimerLogService {

    @Autowired
    private TimerLogDao timerLogDao;

    @Override
    public Integer save(Integer id, String name, String desc, TimerStatus status, Date startTime, Date endTime, String error) {
        TimerLog timerLog = null;
        if (null == id) {
            timerLog = new TimerLog();
        } else {
            timerLog = timerLogDao.getOne(id);
        }
        timerLog.setName(name);
        timerLog.setStatus(status.getCode());
        timerLog.setDescription(desc);
        timerLog.setStartTime(startTime);
        timerLog.setEndTime(endTime);
        timerLog.setError(error);
        return timerLogDao.save(timerLog).getId();
    }
}
