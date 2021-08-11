package com.fintech.insurance.micro.finance.event;

import org.springframework.context.ApplicationEvent;

/**
 * 还款计划事件抽象
 */
public abstract class RepaymentPlanEvent extends ApplicationEvent {

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public RepaymentPlanEvent(Object source) {
        super(source);
    }
}
