package com.fintech.insurance.micro.timer.interceptor;

import com.fintech.insurance.commons.annotations.FInsuranceTimer;
import com.fintech.insurance.commons.enums.TimerStatus;
import com.fintech.insurance.micro.timer.persist.dao.TimerLogDao;
import com.fintech.insurance.micro.timer.persist.entity.TimerLog;
import com.fintech.insurance.micro.timer.scheduled.RefundScheduler;
import com.fintech.insurance.micro.timer.service.TimerLogService;
import com.fintech.insurance.service.agg.ThirdPartyService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.rmi.runtime.Log;

import java.util.Date;


/**
 * @Description: 开启定时任务前后监控定时器执行情况，并保存到数据库中
 * @Author: qxy
 * @Date: 2017/1/11 10:01
 */
@Aspect
@Component
public class TimerInterceptor {

    @Autowired
    private TimerLogService timerLogService;

    private static final Logger LOG = LoggerFactory.getLogger(com.fintech.insurance.components.web.interceptor.FinanceDataInterceptor.class);

    @Pointcut("@annotation(com.fintech.insurance.commons.annotations.FInsuranceTimer)")
    public void executeService(){
    }

    @Around("executeService()")
    public Object doAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        MethodSignature methodSignature = (MethodSignature)proceedingJoinPoint.getSignature();

        FInsuranceTimer fInsuranceTimer = (FInsuranceTimer) methodSignature.getMethod().getAnnotation(FInsuranceTimer.class);//获取方法注解

        String error = "";
        Date startTime = new Date();
        Date endTime = new Date();
        Object result = null;
        Integer id = 0;
        TimerStatus status = TimerStatus.PROCESSING;
        //定时任务开始前记录日志
        if (null != fInsuranceTimer) {
            LOG.info("prepare to execute timer[" + fInsuranceTimer.name() + "]");
            id = timerLogService.save(null, fInsuranceTimer.name(), fInsuranceTimer.desc(), status, startTime, null, error);
        }
        LOG.info("current timer_log's id is : {}", id);
        try {
            result = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
        } catch (Throwable e) {
            //执行出错,更新日志
            error = e.getMessage();
            status = TimerStatus.ERROR;
            endTime = new Date();
            if (null != fInsuranceTimer) {
                LOG.info("fail to execute timer[" + fInsuranceTimer.name() + "] the error is : {}", e.getMessage());
            }
            e.printStackTrace();
        }

        if (null != fInsuranceTimer) {
            if (status == TimerStatus.ERROR) {//定时任务出错后更新日志
                timerLogService.save(id, fInsuranceTimer.name(), fInsuranceTimer.desc(), status, startTime, endTime, error);
            } else {//定时任务结束后更新日志
                status = TimerStatus.END;
                endTime = new Date();
                LOG.info("success to execute timer[" + fInsuranceTimer.name() + "] with log[" + id + "]");
                timerLogService.save(id, fInsuranceTimer.name(), fInsuranceTimer.desc(), status, startTime, endTime, error);
            }
        }
        return result;
    }
}

