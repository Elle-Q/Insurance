package com.fintech.insurance.micro.finance.event;

import com.alibaba.fastjson.JSON;
import com.fintech.insurance.commons.annotations.LifeCycleHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RepaymentPlanLifeCycleListenerAdaptor extends AbstractRepaymentPlanListener {

    private static final Logger logger = LoggerFactory.getLogger(RepaymentPlanLifeCycleListenerAdaptor.class);

    protected RepaymentPlanLifeCycleEvent event = null;

    /**
     * 处理器缓存
     */
    protected Map<String, Method> HANDLER_METHOD_CACHE = new ConcurrentHashMap<>();

    @Async
    @Override
    public void onApplicationEvent(RepaymentPlanEvent event) {
        if (event == null || !(event instanceof RepaymentPlanLifeCycleEvent)) {
            return ;
        }
        this.event = (RepaymentPlanLifeCycleEvent) event;
        if (!canProcess(event)) {
            logger.debug("current listener cannot handle the event");
            return ;
        }
        if (this.event.getSource() != null && StringUtils.isNotEmpty(this.event.getNewStatus())) {
            Method handler = HANDLER_METHOD_CACHE.get(this.event.getNewStatus());
            if (handler == null) {
                Method[] handlers = AbstractRepaymentPlanListener.class.getDeclaredMethods();
                for (Method method : handlers) {
                    LifeCycleHandler annotation = AnnotationUtils.getAnnotation(method, LifeCycleHandler.class);
                    if (annotation != null && annotation.value().equalsIgnoreCase(this.event.getNewStatus())) {
                        handler = method;
                        HANDLER_METHOD_CACHE.put(this.event.getNewStatus(), method);
                        break;
                    }
                }
            }
            //判断handler是否为空
            if (handler == null) {
                logger.error("Fail to find related handler method for the repayment plan with id " + this.event.getRepaymentPlan().getId() + " new status change from " + this.event.getOldStatus() + " to " + this.event.getNewStatus());
            } else {
                try {
                    handler.invoke(this, event);
                } catch (Exception e) {
                    logger.error("Fail to call repayment plan lifecycle method due to exception found", e);
                    logger.info("The event object is: \n");
                    logger.info(JSON.toJSONString(event));
                }
            }
        } else {
            logger.info("Received an empty requisition life cycle event, and the event object is: \n");
            logger.info(JSON.toJSONString(event));
        }
    }

    /**
     * 当前listener是否能处理当前的事件
     * @param event
     * @return true表示能处理
     */
    protected boolean canProcess(RepaymentPlanEvent event) {
        return true;
    }

    /**
     * 还款计划初始化
     * @param event
     */
    @LifeCycleHandler("init_refund")
    public void onRepaymentPlanInit(RepaymentPlanLifeCycleEvent event) {
        logger.info("default repayment plan life cycle listener will do nothing");
    }

    /**
     * 合同生效，还款计划生效且变成待还款
     * @param event
     */
    @LifeCycleHandler("waiting_refund")
    public void onRepaymentPlanWaitingRefund(RepaymentPlanLifeCycleEvent event) {
        logger.info("default repayment plan life cycle listener will do nothing");
    }

    /**
     * 还款计划还款成功
     * @param event
     */
    @LifeCycleHandler("has_refund")
    public void onRepaymentPlanRefunded(RepaymentPlanLifeCycleEvent event) {
        logger.info("default repayment plan life cycle listener will do nothing");
    }

    /**
     * 还款计划还款失败
     * @param event
     */
    @LifeCycleHandler("fail_refund")
    public void onRepaymentPlanFailRefund(RepaymentPlanLifeCycleEvent event) {
        logger.info("default repayment plan life cycle listener will do nothing");
    }

    /**
     * 还款已逾期
     * @param event
     */
    @LifeCycleHandler("overdue")
    public void onRepaymentPlanOverdue(RepaymentPlanLifeCycleEvent event) {
        logger.info("default repayment plan life cycle listener will do nothing");
    }

    /**
     * 待退保
     * @param event
     */
    @LifeCycleHandler("waiting_withdraw")
    public void onRepaymentPlanWaitingWithdraw(RepaymentPlanLifeCycleEvent event) {
        logger.info("default repayment plan life cycle listener will do nothing");
    }

    /**
     * 已退保
     * @param event
     */
    @LifeCycleHandler("has_withdraw")
    public void onRepaymentPlanHasWithdraw(RepaymentPlanLifeCycleEvent event) {
        logger.info("default repayment plan life cycle listener will do nothing");
    }
}
