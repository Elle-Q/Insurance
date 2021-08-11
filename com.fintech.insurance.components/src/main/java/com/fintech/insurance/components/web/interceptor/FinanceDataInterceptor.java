package com.fintech.insurance.components.web.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fintech.insurance.commons.annotations.FinanceDataPoint;
import com.fintech.insurance.commons.enums.SystemProfile;
import com.fintech.insurance.commons.utils.NumberFormatorUtils;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.BaseFintechController;
import com.fintech.insurance.components.web.context.FInsuranceApplicationContext;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @Description: 控制层拦截器，对金额相关的数据进行统一转换
 * @Author: Yong Li
 * @Date: 2017/12/1 10:01
 */
@Aspect
@Order(-1000)
@Component
public class FinanceDataInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(FinanceDataInterceptor.class);

    @Autowired
    @Qualifier("jacksonObjectMapper")
    private ObjectMapper jacksonObjectMapper;

    private static final List<Type> EFFECTIVE_TYPES = Arrays.asList(new Type[]{Long.class, Double.class});

    @Pointcut("execution(* com.fintech.insurance.micro..*.controller..*.*(..))")
    public void executeService(){

    }

    @Around("executeService()")
    public Object doAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        if (!BaseFintechController.isRequestFromFeignInvoke()) {
            Method targetMethod = ((MethodSignature)proceedingJoinPoint.getSignature()).getMethod();

            LOG.info("==Request METHOD: {}.{}", targetMethod.getDeclaringClass(), targetMethod.getName());
            Type[] paramTypes = targetMethod.getGenericParameterTypes();
            if (paramTypes.length > 0) { //方法有参数
                Annotation[][] paramAnnotations = targetMethod.getParameterAnnotations();

                LOG.info("request info as below:");
                for (int paramIndex = 0; paramIndex < paramTypes.length; paramIndex++) {
                    if (null == proceedingJoinPoint.getArgs()[paramIndex] || paramAnnotations[paramIndex] == null || paramAnnotations[paramIndex].length == 0) {
                        continue; //参数值为空或参数没有注解
                    }

                    // 检查参数是否有指定注解，如果有，则做对应的逻辑: 下面的遍历可用于检查是否把注解用到了错误的参数类型上.
                    for (int annotationIdex = 0; annotationIdex < paramAnnotations[paramIndex].length; annotationIdex++) {
                        //形如 method(@FinanceDataPoint Long bar)
                        if (FinanceDataPoint.class == paramAnnotations[paramIndex][annotationIdex].annotationType()) {
                            //if (Long.class != paramTypes[paramIndex] && Double.class != paramTypes[paramIndex]) {
                            if (EFFECTIVE_TYPES.contains(paramTypes[paramIndex].getClass())) {
                                throw new IllegalStateException("Annoation @FinanceDataPoint must works on Long or Double field.");
                            }
                            Number paramValue = (Number)proceedingJoinPoint.getArgs()[paramIndex];
                            proceedingJoinPoint.getArgs()[paramIndex] = this.convertForEnterBiz(paramValue);
                            break;
                        }
                    }

                    //参数值为非空对象, 需要检查所有属性是否有注解@FinanceDataPoint
                    Object paramObject = proceedingJoinPoint.getArgs()[paramIndex];
                    this.processFinanceDataOnObject(paramObject, true);
                }
            }
        }
        Object methodReturnValue = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());

        if (!BaseFintechController.isRequestFromFeignInvoke()) {
            if (methodReturnValue instanceof FintechResponse) {
                Object responseData = ((FintechResponse) methodReturnValue).getData();

                //LOG.info("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ start");
                //LOG.info(JSON.toJSONString(responseData));
                if (responseData instanceof Pagination) {
                    this.processFinanceDataOnObject(((Pagination)responseData).getItems(), false);
                } else {
                    this.processFinanceDataOnObject(responseData, false);
                }
                //LOG.info("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ end");
            }
        }

        return methodReturnValue;
    }

    private Object convertForEnterBiz(Number number) {
        if (Long.class == number.getClass()) {
            return (Long)NumberFormatorUtils.convertFinanceNumberToSave(number).longValue();
        } else {
            return (Double)NumberFormatorUtils.convertFinanceNumberToSave(number).setScale(8, BigDecimal.ROUND_HALF_UP).doubleValue();
        }

    }

    private Object convertForWebShow(Number number) {
        if (Long.class == number.getClass()) {
            return (Long)NumberFormatorUtils.convertFinanceNumberToShow(number).longValue(); // 保留两位小数
        } else {
            return (Double)NumberFormatorUtils.convertFinanceNumberToShow(number).setScale(8, BigDecimal.ROUND_HALF_UP).doubleValue();
        }

    }

    private void processFinanceDataOnObject(Object objectData, boolean isEnterData) {
        try {
            this.processOutputConvert(objectData, isEnterData, 0);
        } catch (IllegalAccessException e) {
            LOG.error("travel object error, " + e.getMessage(), e);
            throw new FInsuranceBaseException(FInsuranceBaseException.SYSTEM_UNKNOWN_ERROR, "Failed to travel object on converting FinanceDataPoint data.", e);
        }
    }

    /**
     * 递归处理对象
     *
     * @param objectData
     * @param isEnterData
     * @param fieldDeepth 用于控制遍历的层次，只对本项目的类进行过滤后， 该字段暂时不用
     * @throws IllegalAccessException
     */
    private void processOutputConvert(Object objectData, boolean isEnterData, int fieldDeepth) throws IllegalAccessException {
        if (objectData == null || Object.class == objectData.getClass() || fieldDeepth > 5) {// 最多递归5层
            return ;
        }
        if (objectData instanceof Collection) {
            Collection<?> items = (Collection<?>)objectData;
            for (Object item : items) {
                processOutputConvert(item, isEnterData, fieldDeepth);
            }
        } else if (objectData instanceof Map) {
            for (Map.Entry<?, ?> entry : ((Map<?, ?>)objectData).entrySet()) {
                if (null != entry.getValue()) {
                    processOutputConvert(entry.getValue(), isEnterData, fieldDeepth + 1);
                }
            }
        } else if (objectData.getClass().getCanonicalName().startsWith("com.fintech")) {// 只对当前项目定义的VO进行处理

            // Field[] fields = objectData.getClass().getDeclaredFields();  //声明的字段无法处理子类从父类衍生过来的字段
            Field[] fields = FieldUtils.getAllFields(objectData.getClass());
            for (Field field : fields) {
                //Object fieldValue = FieldUtils.readDeclaredField(objectData, field.getName(), true);
                Object fieldValue = FieldUtils.readField(field, objectData, true);

                if (null != fieldValue) {
                    //LOG.info("process field:" + field.getName() + " class:" + fieldValue.getClass().getCanonicalName());
                    if (null != field.getAnnotation(FinanceDataPoint.class) && EFFECTIVE_TYPES.contains(field.getType())) {
                        //LOG.info("FinanceData Converter: " + field.getName() + " on object: " + JSON.toJSONString(objectData));
                        //FieldUtils.writeDeclaredField(objectData, field.getName(),
                        //        isEnterData ? this.convertForEnterBiz((Number) fieldValue) : this.convertForWebShow((Number) fieldValue)
                        //        , true);
                        FieldUtils.writeField(field, objectData, isEnterData ? this.convertForEnterBiz((Number) fieldValue) : this.convertForWebShow((Number) fieldValue));
                    } else { // 其他对象
                        processOutputConvert(fieldValue, isEnterData, fieldDeepth + 1);
                    }
                }
            }
        }
    }
}
