package com.fintech.insurance.micro.finance.service.yjf.exception;

/**
 * @Description: 易极付处理失败
 * @Author: Yong Li
 * @Date: 2017/12/9 17:03
 */
public class YijifuProcessException extends RuntimeException {

    public YijifuProcessException(String message) {
        super(message);
    }

    public YijifuProcessException(String message, Throwable cause) {
        super(message, cause);
    }


}