package com.fintech.insurance.components.web.configuration;

import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.context.FInsuranceApplicationContext;
import io.jsonwebtoken.lang.Collections;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.validation.ValidationException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


/**
 * 自定义实现spring的全局异常解析器HandlerExceptionResolver
 *
 * @author yufei Feng
 * @version 1.1.0
 * @since 2017年9月14日 19:34:13
 */


@RestControllerAdvice
@Configuration
//@PropertySource(value = "classpath:exception.properties", encoding = "UTF-8")
public class GlobalExceptionHandler implements EnvironmentAware {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private Environment environment;

    @Autowired
    private ExceptionProperties exceptionProperties;

    private ObjectError error;

    private static List<String> filterExceptionStackTrace(Exception ex) {
        List<String> microScopeTraces = new ArrayList<String>();
        StackTraceElement[] traces = ex.getStackTrace();
        for (StackTraceElement trace : traces) {
            if (trace.getClassName().indexOf("com.fintech.insurance") > -1) {
                microScopeTraces.add(trace.toString());
            }
        }
        return microScopeTraces;
    }

    @ExceptionHandler(value = {Exception.class})
    public FintechResponse<Object> defaultErrorHandler(HttpServletRequest request, HttpServletResponse response, Exception ex) throws Exception {
        logger.error(String.format("System(%s) encounter following error, please pay attention.", FInsuranceApplicationContext.getMicroServiceName()), ex);
        FintechResponse<Object> fintechResponse = null;
        if (ex instanceof FInsuranceBaseException) {
            FInsuranceBaseException baseException = (FInsuranceBaseException) ex;

            String errorMsg = null;
            if (StringUtils.isNotBlank(baseException.getMessage())) {  //用户临时定义的错误消息或者从其他微服务抛出的Exception, 不需要重新根据模板生成错误消息
                errorMsg = baseException.getMessage();
            } else {
                String msgTemplate = exceptionProperties.getMessage(String.valueOf(baseException.getCode()));
                errorMsg = MessageFormat.format(msgTemplate, baseException.getMsgParams());
            }
            fintechResponse = new FintechResponse(baseException.getCode(), errorMsg);
            fintechResponse.setDomain(baseException.getDomain());
        } else if (ex instanceof ValidationException) { // 基本参数校验出错所抛出的异常
            ConstraintViolationException cve = (ConstraintViolationException) ex;
            StringBuilder errorMessage = new StringBuilder("");
            Set<ConstraintViolation<?>> set = cve.getConstraintViolations();
            ConstraintViolation<?> c = set.iterator().next();
            String messageCode = c.getMessage();
            // 当参数校验注解里写的message并不是对于在exception中配置的信息时，
            // 输出message的默认信息。
            String msg = exceptionProperties.getMessage(messageCode);

            if (StringUtils.isNotBlank(msg)) { //类似于Phonix项目定义的校验, 在VO里面，抛出errorCode
                // 拿到字段名称
                String wrongParam = "";
                Iterator<Path.Node> ite = c.getPropertyPath().iterator();
                while (ite.hasNext()) {
                    wrongParam = ite.next().getName();
                }
                logger.error(wrongParam);
                if (StringUtils.isNotBlank(msg)) {
                    msg = MessageFormat.format(msg, wrongParam);
                } else {
                    msg = messageCode;
                }

                fintechResponse = new FintechResponse(FInsuranceBaseException.SYSTEM_PARA_VALIDATION_ERROR, msg);
                logger.error(errorMessage.substring(0, errorMessage.length() - 2));
                fintechResponse = new FintechResponse(FInsuranceBaseException.SYSTEM_PARA_VALIDATION_ERROR, errorMessage.substring(0, errorMessage.length() - 2));
            } else { //@RquestParam 验证错误
                fintechResponse = new FintechResponse(FInsuranceBaseException.SYSTEM_PARA_VALIDATION_ERROR, MessageFormat.format("参数校验错误{0}:{1}", c.getPropertyPath(), c.getMessage()));
            }
        } else if (ex instanceof MissingServletRequestParameterException) {
            fintechResponse = new FintechResponse(FInsuranceBaseException.REQUEST_MISSING_PARAMETER, String.format("请求参数 %s 不能为空", ((MissingServletRequestParameterException) ex).getParameterName()));
        } else if (ex instanceof BindException || ex instanceof MethodArgumentNotValidException) { // JavaBean校验时所抛出的异常
            //这些异常都有BindingResult
            BindingResult br = null;
            if (ex instanceof BindingResult) {
                br = ((BindException) ex).getBindingResult();
            } else if (ex instanceof MethodArgumentNotValidException) {
                br = ((MethodArgumentNotValidException) ex).getBindingResult();
            }

            // 拿到第一个错误
            ObjectError error = br.getAllErrors().get(0);
            // 拿到出错的参数
            String wrongParam = error.getCodes()[0];
            // 拿到在参数校验注解时指定的message属性
            String messageCode = error.getDefaultMessage();

            String rawMsg = exceptionProperties.getMessage(messageCode);

            if (StringUtils.isEmpty(rawMsg)) { //根据messageCode找不到对应的错误消息， 或者没有设置错误消息码
                fintechResponse = new FintechResponse(FInsuranceBaseException.SYSTEM_PARA_VALIDATION_ERROR,
                        MessageFormat.format(exceptionProperties.getMessage(String.valueOf(FInsuranceBaseException.SYSTEM_PARA_VALIDATION_ERROR)), ((FieldError) error).getField()));
            } else {
                fintechResponse = new FintechResponse(Integer.parseInt(messageCode), MessageFormat.format(rawMsg, ((FieldError) error).getField()));
            }
        } else if (ex instanceof com.atomikos.datasource.ResourceException) {
            logger.error(ex.getMessage(), ex);
            fintechResponse = new FintechResponse(101518, exceptionProperties.getMessage(String.valueOf(101518)));
        }else {
            fintechResponse = new FintechResponse(FInsuranceBaseException.SYSTEM_UNKNOWN_ERROR, String.format("Caused by: %s, Exception Class: %s", ex.getMessage(), ex.getClass()));
        }

        // 设置出现错的微服务名字
        if (StringUtils.isBlank(fintechResponse.getDomain())) {
            fintechResponse.setDomain(FInsuranceApplicationContext.getMicroServiceName());
        }
        // 设置错误的errorStack
        if (ex instanceof FInsuranceBaseException && !Collections.isEmpty(((FInsuranceBaseException) ex).getMicroErrorStraces()) ) {
            FInsuranceBaseException baseException = (FInsuranceBaseException) ex;
            fintechResponse.setErrorTraces(((FInsuranceBaseException) ex).getMicroErrorStraces());
        } else {
            fintechResponse.setErrorTraces(GlobalExceptionHandler.filterExceptionStackTrace(ex));
        }

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        return fintechResponse;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
