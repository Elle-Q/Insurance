package com.fintech.insurance.micro.thirdparty.service.bestsign.exceptions;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/11/21 18:58
 */
public class BestsignCertExistingException extends Exception {

    public BestsignCertExistingException(String message) {
        super(message);
    }

    public BestsignCertExistingException(String message, Throwable cause) {
        super(message, cause);
    }
}
