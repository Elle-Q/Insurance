package com.fintech.insurance.gateway.service;

import com.fintech.insurance.gateway.filters.GatewayResponseRestructureFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.http.converter.jaxb.JaxbOAuth2ExceptionMessageConverter;
import org.springframework.security.oauth2.provider.error.OAuth2ExceptionRenderer;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Component
@Configuration
public class GatewayOauthClientExceptionRenderer implements OAuth2ExceptionRenderer {

    private final Log logger = LogFactory.getLog(GatewayOauthClientExceptionRenderer.class);

    private List<HttpMessageConverter<?>> messageConverters = geDefaultMessageConverters();

    public void setMessageConverters(List<HttpMessageConverter<?>> messageConverters) {
        this.messageConverters = messageConverters;
    }

    @Autowired
    private ExceptionPropertyService exceptionPropertyService;

    @Autowired
    private HttpStatusConverter httpStatusConverter;

    @Override
    public void handleHttpEntityResponse(HttpEntity<?> responseEntity, ServletWebRequest webRequest) throws Exception {
        if (responseEntity == null) {
            return;
        }
        HttpInputMessage inputMessage = createHttpInputMessage(webRequest);
        HttpOutputMessage outputMessage = createHttpOutputMessage(webRequest);
        if (responseEntity instanceof ResponseEntity && outputMessage instanceof ServerHttpResponse) {
            ((ServerHttpResponse) outputMessage).setStatusCode(((ResponseEntity<?>) responseEntity).getStatusCode());
        }
        HttpHeaders entityHeaders = responseEntity.getHeaders();
        if (!entityHeaders.isEmpty()) {
            outputMessage.getHeaders().putAll(entityHeaders);
        }
        webRequest.getResponse().setStatus(HttpStatus.OK.value());
        Object body = responseEntity.getBody();
        if (body != null) {
            if (body instanceof OAuth2Exception) { //转换Oauth2Exception结构
                body = this.convertOauth2ExceptionToMap((OAuth2Exception) body);
            }
            writeWithMessageConverters(body, inputMessage, outputMessage);
        }
        else {
            Map<String, Object> finalResult = new HashMap<>();
            this.httpStatusConverter.convertHttpStatusError(finalResult, webRequest.getResponse());
            // flush headers
            writeWithMessageConverters(finalResult, inputMessage, outputMessage);
        }
    }

    private Map<String, Object> convertOauth2ExceptionToMap(OAuth2Exception oauth2Exception) {
        String errorCode = oauth2Exception.getOAuth2ErrorCode();
        String gatewayAuthErrorCode = this.exceptionPropertyService.getProperty("fintech.insurance.authz-error-mapping." + errorCode);
        if (gatewayAuthErrorCode == null) {
            gatewayAuthErrorCode = "100202";
        }
        Map<String, Object> result = new HashMap<>();
        result.put(GatewayResponseRestructureFilter.KEY_CODE, Integer.valueOf(gatewayAuthErrorCode));
        result.put(GatewayResponseRestructureFilter.KEY_MSG, this.exceptionPropertyService.getProperty(gatewayAuthErrorCode));
        result.put(GatewayResponseRestructureFilter.KEY_DATA, oauth2Exception);
        return result;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void writeWithMessageConverters(Object returnValue, HttpInputMessage inputMessage,
                                            HttpOutputMessage outputMessage) throws IOException, HttpMediaTypeNotAcceptableException {
        List<MediaType> acceptedMediaTypes = inputMessage.getHeaders().getAccept();
        if (acceptedMediaTypes.isEmpty()) {
            acceptedMediaTypes = Collections.singletonList(MediaType.ALL);
        }
        MediaType.sortByQualityValue(acceptedMediaTypes);
        Class<?> returnValueType = returnValue.getClass();
        List<MediaType> allSupportedMediaTypes = new ArrayList<MediaType>();
        for (MediaType acceptedMediaType : acceptedMediaTypes) {
            for (HttpMessageConverter messageConverter : messageConverters) {
                if (messageConverter.canWrite(returnValueType, acceptedMediaType)) {
                    messageConverter.write(returnValue, acceptedMediaType, outputMessage);
                    if (logger.isDebugEnabled()) {
                        MediaType contentType = outputMessage.getHeaders().getContentType();
                        if (contentType == null) {
                            contentType = acceptedMediaType;
                        }
                        logger.debug("Written [" + returnValue + "] as \"" + contentType + "\" using ["
                                + messageConverter + "]");
                    }
                    return;
                }
            }
        }
        for (HttpMessageConverter messageConverter : messageConverters) {
            allSupportedMediaTypes.addAll(messageConverter.getSupportedMediaTypes());
        }
        throw new HttpMediaTypeNotAcceptableException(allSupportedMediaTypes);
    }

    private List<HttpMessageConverter<?>> geDefaultMessageConverters() {
        List<HttpMessageConverter<?>> result = new ArrayList<HttpMessageConverter<?>>();
        result.addAll(new RestTemplate().getMessageConverters());
        result.add(new JaxbOAuth2ExceptionMessageConverter());
        return result;
    }

    private HttpInputMessage createHttpInputMessage(NativeWebRequest webRequest) throws Exception {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        return new ServletServerHttpRequest(servletRequest);
    }

    private HttpOutputMessage createHttpOutputMessage(NativeWebRequest webRequest) throws Exception {
        HttpServletResponse servletResponse = (HttpServletResponse) webRequest.getNativeResponse();
        return new ServletServerHttpResponse(servletResponse);
    }
}
