package com.fintech.insurance.commons.exceptions;

/**
 * @author Yong Li
 * @version 1.0.0
 * @since 2017/06/13
 */
public class UnExpectedNullException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UnExpectedNullException(String msg) {
        super(msg);
    }
}
