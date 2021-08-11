package com.fintech.insurance.micro.biz.event;

import com.fintech.insurance.micro.biz.persist.entity.Requisition;
import org.springframework.context.ApplicationEvent;

/**
 * 申请单生命周期变化事件
 */
public class RequisitionLifeCycleEvent extends ApplicationEvent {

    /**
     * 状态变化的申请单对象
     */
    private Requisition requisition;

    /**
     * 旧状态
     */
    private String oldStatus;

    /**
     * 新状态
     */
    private String newStatus;

    /**
     * 构造函数
     * @param requisition
     * @param oldStatus
     * @param newStatus
     */
    public RequisitionLifeCycleEvent(Requisition requisition, String oldStatus, String newStatus) {
        super(requisition);
        this.requisition = (Requisition) this.source;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
    }

    public Requisition getRequisition() {
        return requisition;
    }

    public String getOldStatus() {
        return oldStatus;
    }

    public String getNewStatus() {
        return newStatus;
    }
}
