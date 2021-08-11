package com.fintech.insurance.micro.thirdparty.service.bestsign.exceptions;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/11/21 14:35
 */
public class BestsignDataException extends RuntimeException {

    public BestsignDataException(Throwable cause) {
        super(cause);
    }

    public BestsignDataException(String message) {
        super(message);
    }

    public BestsignDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
