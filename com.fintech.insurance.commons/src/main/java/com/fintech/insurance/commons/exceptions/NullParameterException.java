package com.fintech.insurance.commons.exceptions;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/12/20 14:00
 */
public class NullParameterException extends RuntimeException {

    public NullParameterException() {
    }

    public NullParameterException(String message) {
        super(message);
    }

    public NullParameterException(String message, Throwable cause) {
        super(message, cause);
    }
}
