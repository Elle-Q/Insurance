package com.fintech.insurance.commons.web;

import com.alibaba.fastjson.annotation.JSONField;
import com.fintech.insurance.commons.constants.VoidPlaceHolder;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 所有接口返回的封装对象
 * @Author: Yong Li
 * @Date: 2017/11/15 20:13
 */
public class FintechResponse<T> implements Serializable  {

    /**
     * 响应状态码
     */
    private int code = 0;

    /**
     * 响应的描述
     */
    private String msg = "ok";

    /**
     * 响应数据
     */
    private T data = null;

    /**
     * 响应所属的微服务名称
     */
    private String domain;

    /**
     * 错误栈
     */
    private List<String> errorTraces;

    /**
     * 默认构造函数
     */
    protected FintechResponse() {}

    /**
     * 根据代码和响应描述生成响应消息对象
     * @param code
     * @param msg
     */
    public FintechResponse(int code, String msg) {
        this();
        this.code = code;
        this.msg = msg;
    }

    public FintechResponse(int code, String msg, T data) {
        this(code, msg);
        if (data != null) {
            this.data = data;
        }
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    /**
     * 响应成功，同时设置成功时返回的数据和成功消息
     * @param data
     * @param msg
     * @return
     */
    public static <T> FintechResponse<T> responseOk(T data, String msg) {
        FintechResponse<T> response = responseData(data);
        response.setMsg(msg);
        return response;
    }

    /**
     * 响应成功，不设置消息，只设置数据
     * @param data
     * @return
     */
    public static <T> FintechResponse<T> responseData(T data) {
        FintechResponse<T> response = new FintechResponse<>();
        response.setData(data);
        return response;
    }

    public static FintechResponse<VoidPlaceHolder> voidReturnInstance(){
        return FintechResponse.responseData(VoidPlaceHolder.instance());
    }

    /**
     * 响应成功或者失败
     * @param code
     * @param msg
     * @param data
     * @param <T>
     * @return
     */
    public static <T> FintechResponse<T> responseData(int code, String msg, T data) {
        FintechResponse<T> response = new FintechResponse<>();
        response.setData(data);
        response.setMsg(msg);
        response.setCode(code);
        return response;
    }

    public List<String> getErrorTraces() {
        return errorTraces;
    }

    public void setErrorTraces(List<String> errorTraces) {
        this.errorTraces = errorTraces;
    }

    /**
     * 响应成功
     * @return
     */
    public static <T> FintechResponse<T> responseOk() {
        return new FintechResponse<>();
    }

    @Override
    public String toString() {
        return "FintechResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                ", domain='" + domain + '\'' +
                ", errorTraces=" + errorTraces +
                '}';
    }

    /**
     * 响应是否成功
     * @return
     */
    @JSONField(serialize = false, deserialize = false)
    @java.beans.Transient
    public boolean isOk() {
        return this.code == 0;
    }
}
