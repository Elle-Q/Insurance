package com.fintech.insurance.micro.thirdparty.service.bestsign;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * @Description: 上上签API返回的结果封装
 * @Author: Yong Li
 * @Date: 2017/11/21 12:54
 */
public class BestsignResponse<T> {

    @JSONField(name = "errno")
    private String code;

    @JSONField(name = "errmsg")
    private String message;

    @JSONField(name = "data")
    private T data;

    public BestsignResponse() {
    }

    public BestsignResponse(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> BestsignResponse<T> createNetworkErrorResponse(int networkStatus) {
        BestsignResponse<T> response = new BestsignResponse<T>();
        response.setCode("-1");
        response.setMessage("network error:" + networkStatus);
        return response;
    }

    public static <T> BestsignResponse<T> createResponse(String code, String message) {
        BestsignResponse<T> response = new BestsignResponse<T>();
        response.setCode(code);
        response.setMessage(message);
        return response;
    }

    public static <T> BestsignResponse<T> createOKResponse(T data, String message) {
        BestsignResponse<T> response = new BestsignResponse<T>();
        response.setCode(BestsignUtil.BESTSIGN_REQUEST_OK_STATUS);
        response.setMessage(message);
        response.setData(data);
        return response;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @java.beans.Transient
    public boolean isOK() {
        return BestsignUtil.BESTSIGN_REQUEST_OK_STATUS.equals(this.code);
    }

    public static void main(String[] args) {
        String s = "{\"errno\":241208,\"data\":\"test\",\"errmsg\":\"user exists, can't add again.\"}";
        BestsignResponse<String> obj = JSON.parseObject(s, BestsignResponse.class);
        System.out.println(obj);
    }
}
