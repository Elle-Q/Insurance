package com.fintech.insurance.micro.thirdparty.service.bestsign.exceptions;

/**
 * @Description: 上上签业务处理出错
 * @Author: Yong Li
 * @Date: 2017/11/21 14:30
 */
public class BestsignBizException extends Exception {

    public BestsignBizException(String message) {
        super(message);
    }

    public BestsignBizException(String message, Throwable cause) {
        super(message, cause);
    }


}
