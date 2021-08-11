package com.fintech.insurance.micro.biz.event;

import com.alibaba.fastjson.JSON;
import com.fintech.insurance.commons.annotations.LifeCycleHandler;
import com.fintech.insurance.micro.biz.persist.entity.Requisition;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.scheduling.annotation.Async;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 监听申请单生命周期的变化
 */
public abstract class AbstractRequisitionLifeCycleListener implements ApplicationListener<RequisitionLifeCycleEvent> {

    public static final Logger logger = LoggerFactory.getLogger(AbstractRequisitionLifeCycleListener.class);

    /**
     * 处理器缓存
     */
    protected Map<String, Method> HANDLER_CACHE = new ConcurrentHashMap<>();

    protected RequisitionLifeCycleEvent event = null;

    @Async
    @Override
    public void onApplicationEvent(RequisitionLifeCycleEvent event) {
        if (!canProcess(event)) {
            logger.debug("current listener cannot handle the event");
            return ;
        }
        if (event != null && event.getSource() != null && StringUtils.isNotEmpty(event.getNewStatus())) {
            this.event = event;
            Method handler = HANDLER_CACHE.get(event.getNewStatus());
            if (handler == null) {
                Method[] handlers = AbstractRequisitionLifeCycleListener.class.getDeclaredMethods();
                for (Method method : handlers) {
                    LifeCycleHandler annotation = AnnotationUtils.getAnnotation(method, LifeCycleHandler.class);
                    if (annotation != null && annotation.value().equalsIgnoreCase(event.getNewStatus())) {
                        handler = method;
                        HANDLER_CACHE.put(event.getNewStatus(), method);
                        break;
                    }
                }
            }
            //判断handler是否为空
            if (handler == null) {
                logger.error("Fail to find related handler method for the requisition with id " + event.getRequisition().getId() + " new status change from " + event.getOldStatus() + " to " + event.getNewStatus());
            } else {
                try {
                    handler.invoke(this, event);
                } catch (Exception e) {
                    logger.error("Fail to call lifecycle method due to exception found", e);
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
     * 获得银行账号后缀
     * @param bankAccountNumber
     * @return
     */
    protected String getBankAccountNumberSuffix(String bankAccountNumber) {
        return StringUtils.isEmpty(bankAccountNumber) || bankAccountNumber.length() <= 4 ? bankAccountNumber : bankAccountNumber.substring(bankAccountNumber.length() - 4);
    }

    /**
     * 当前listener是否能处理当前的事件
     * @param event
     * @return true表示能处理
     */
    abstract boolean canProcess(RequisitionLifeCycleEvent event);

    /**
     * 申请单创建
     * @param event
     */
    @LifeCycleHandler("draft")
    abstract void onRequsitionCreated(RequisitionLifeCycleEvent event);

    /**
     * 申请单提交，等待客户确认
     * @param event
     */
    @LifeCycleHandler("submitted")
    abstract void onRequisitionSubmitted(RequisitionLifeCycleEvent event);

    /**
     * 申请单已经被确认，等待审核
     * @param event
     */
    @LifeCycleHandler("auditing")
    abstract void onRequisitionAuditing(RequisitionLifeCycleEvent event);

    /**
     * 申请单审核通过，服务费待支付
     * @param event
     */
    @LifeCycleHandler("waitpayment")
    abstract void onRequisitionWaitPayment(RequisitionLifeCycleEvent event);

    /**
     * 服务费支付失败
     * @param event
     */
    @LifeCycleHandler("failpayment")
    abstract void onRequisitionFailPayment(RequisitionLifeCycleEvent event);

    /**
     * 申请单审核未通过
     * @param event
     */
    @LifeCycleHandler("rejected")
    abstract void onRequisitionRejected(RequisitionLifeCycleEvent event);

    /**
     * 申请单被取消
     * @param event
     */
    @LifeCycleHandler("canceled")
    abstract void onRequisitionCancelled(RequisitionLifeCycleEvent event);

    /**
     * 服务费支付成功，等待放款
     * @param event
     */
    @LifeCycleHandler("waitloan")
    abstract void onRequisitionWaitLoan(RequisitionLifeCycleEvent event);

    /**
     * 放款成功
     * @param event
     */
    @LifeCycleHandler("loaned")
    abstract void onRequisitionLoaned(RequisitionLifeCycleEvent event);
}
