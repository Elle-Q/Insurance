package com.fintech.insurance.gateway.fallbacks;

import com.alibaba.fastjson.JSON;
import com.fintech.insurance.gateway.filters.GatewayResponseRestructureFilter;
import com.fintech.insurance.gateway.service.ExceptionPropertyService;
import com.netflix.zuul.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.route.ZuulFallbackProvider;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Component
public class ServiceFallbackProvider implements ZuulFallbackProvider {

    @Autowired
    private ExceptionPropertyService exceptionPropertyService;

    @Override
    public String getRoute() {
        return "*";
    }

    @Override
    public ClientHttpResponse fallbackResponse() {

        return new ClientHttpResponse() {

            @Override
            public HttpStatus getStatusCode() throws IOException {
                return HttpStatus.OK;
            }

            @Override
            public int getRawStatusCode() throws IOException {
                return HttpStatus.OK.value();
            }

            @Override
            public String getStatusText() throws IOException {
                return HttpStatus.OK.toString();
            }

            @Override
            public void close() {

            }

            @Override
            public InputStream getBody() throws IOException {
                Map<String, Object> result = new HashMap<>();
                int errorCode = 100105;
                result.put(GatewayResponseRestructureFilter.KEY_CODE, errorCode);
                RequestContext ctx = RequestContext.getCurrentContext();
                String serviceId = String.valueOf(ctx.get(FilterConstants.SERVICE_ID_KEY));
                result.put(GatewayResponseRestructureFilter.KEY_MSG, String.format(exceptionPropertyService.getProperty(String.valueOf(errorCode)), serviceId));
                String resultString = JSON.toJSONString(result);
                return new ByteArrayInputStream(resultString.getBytes());
            }

            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
                return headers;
            }
        };
    }
}
