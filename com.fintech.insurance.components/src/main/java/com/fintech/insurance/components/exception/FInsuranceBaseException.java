package com.fintech.insurance.components.exception;

import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.web.context.FInsuranceApplicationContext;
import io.jsonwebtoken.lang.Collections;

import java.text.MessageFormat;
import java.util.List;

/**
 * @Description: 基本异常基类
 * @Author: Yong Li
 * @Date: 2017/12/20 11:56
 */
public class FInsuranceBaseException extends RuntimeException {

    public static final int SYSTEM_UNKNOWN_ERROR = 999999;

    public static final int UNAUTHENTICATED_ERROR = 999998;

    public static final int SYSTEM_PARA_VALIDATION_ERROR = 101001;

    public static final int REQUEST_MISSING_PARAMETER = 999001;

    private static final long serialVersionUID = -3454276718528685349L;

    protected int code = SYSTEM_UNKNOWN_ERROR;

    protected Object[] msgParams = new Object[]{};

    protected List<String> microErrorStraces = null;

    /**
     * 错误抛出属于哪个微服务
     */
    protected String domain = "";

    public FInsuranceBaseException() {
        super();
        this.domain = FInsuranceApplicationContext.getMicroServiceName();
    }

    public String getDomain() {
        return domain;
    }

    public FInsuranceBaseException(int code) {
        this();
        this.code = code;
    }

    public FInsuranceBaseException(String msg) {
        super(msg);
        this.domain = FInsuranceApplicationContext.getMicroServiceName();
    }

    public FInsuranceBaseException(String msgTemplate, Object[] params) {
        this(MessageFormat.format(msgTemplate, params));
        this.msgParams = params;
    }

    public FInsuranceBaseException(int code, Object[] params) {
        this(code);
        this.msgParams = params;
    }

    public FInsuranceBaseException(int code, String msg) {
        this(msg);
        this.code = code;
    }

    public FInsuranceBaseException(int resultCode, String msgTemplate, Object[] params) {
        this(resultCode, MessageFormat.format(msgTemplate, params));
        this.msgParams = params;
    }

    public FInsuranceBaseException(String msg, Throwable cause) {
        super(msg, cause);
        this.domain = FInsuranceApplicationContext.getMicroServiceName();
    }

    public FInsuranceBaseException(int code, String msg, Throwable cause) {
        this(msg, cause);
        this.code = code;
    }

    public FInsuranceBaseException(Throwable cause) {
        super(cause);
        this.domain = FInsuranceApplicationContext.getMicroServiceName();
    }

    public FInsuranceBaseException(int code, Throwable cause) {
        this(cause);
        this.code = code;
    }

    public FInsuranceBaseException(int code, String msg, String domain) {
        super(msg);
        this.code = code;
        this.domain = domain;
    }

    public List<String> getMicroErrorStraces() {
        return microErrorStraces;
    }

    public Object[] getMsgParams() {
        return msgParams;
    }

    public void setMsgParams(Object[] msgParams) {
        this.msgParams = msgParams;
    }

    public Integer getCode() {
        return this.code;
    }

    public static FInsuranceBaseException buildFromErrorResponse(FintechResponse response) {
        if (null == response || response.isOk()) {
            throw new IllegalStateException("Do not permit to convert the correct/null response to Exception.");
        }

        FInsuranceBaseException exception = new FInsuranceBaseException(response.getCode(), response.getMsg(), response.getDomain());
        if (!Collections.isEmpty(response.getErrorTraces())) {
            exception.microErrorStraces = response.getErrorTraces();
        }
        return exception;
    }
}
