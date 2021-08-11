package com.fintech.insurance.gateway.service;

import com.fintech.insurance.gateway.filters.GatewayResponseRestructureFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public class HttpStatusConverter {

    @Autowired
    private ExceptionPropertyService exceptionPropertyService;

    /**
     * 转换http状态码
     * @param finalResult
     * @param response
     */
    public void convertHttpStatusError(Map<String, Object> finalResult, HttpServletResponse response) {
        if (response.getStatus() >= HttpStatus.OK.value() && response.getStatus() < HttpStatus.MULTIPLE_CHOICES.value()) {
            finalResult.put(GatewayResponseRestructureFilter.KEY_CODE, 0);
            finalResult.put(GatewayResponseRestructureFilter.KEY_MSG, "ok");
        } else {
            int errorCode = 100100; //系统异常
            if (HttpStatus.NOT_FOUND.value() == response.getStatus()) {
                errorCode = 100101; //请求地址不存在
            } else if (HttpStatus.UNAUTHORIZED.value() == response.getStatus()) {
                errorCode = 100200; //请求未授权
            } else if (HttpStatus.BAD_REQUEST.value() == response.getStatus()) {
                errorCode = 100102; //请求发生错误
            } else if (HttpStatus.FORBIDDEN.value() == response.getStatus()) {
                errorCode = 100103; //请求的资源被禁止访问
            } else if (HttpStatus.METHOD_NOT_ALLOWED.value() == response.getStatus()) {
                errorCode = 100104; //请求方法错误
            }
            finalResult.put(GatewayResponseRestructureFilter.KEY_CODE, errorCode);
            finalResult.put(GatewayResponseRestructureFilter.KEY_MSG, this.exceptionPropertyService.getProperty(String.valueOf(errorCode)));
        }
    }
}
