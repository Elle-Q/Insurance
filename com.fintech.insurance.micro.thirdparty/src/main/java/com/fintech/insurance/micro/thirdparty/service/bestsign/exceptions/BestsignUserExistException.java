package com.fintech.insurance.micro.thirdparty.service.bestsign.exceptions;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/11/25 15:01
 */
public class BestsignUserExistException extends Exception {

    public BestsignUserExistException(String message) {
        super(message);
    }

    public BestsignUserExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
