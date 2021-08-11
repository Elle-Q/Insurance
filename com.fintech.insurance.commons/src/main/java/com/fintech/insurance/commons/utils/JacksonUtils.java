package com.fintech.insurance.commons.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fintech.insurance.commons.enums.AdStatus;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.DateFormat;
import java.util.List;
import java.util.Map;


public class JacksonUtils {

    private static final Log log = LogFactory.getLog(JacksonUtils.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    public JacksonUtils() {
        super();
    }

    /**
     * 如需读取集合如List/Map,且不是List<String>这种简单类型时使用
     *
     * @param content
     * @param typeReference
     * @return
     */
    public static <T> T fromJson(String content, TypeReference<T> typeReference) {
        return fromJson(content, typeReference, null);
    }

    public static <T> T fromJson(String content, TypeReference<T> typeReference, DateFormat format) {
        if (StringUtils.isEmpty(content)) {
            return null;
        }

        mapper.setDateFormat(format);

        try {
            return mapper.readValue(content, typeReference);
        } catch (Exception e) {
            log.error("Parse json string error : " + content, e);
            return null;
        }
    }

    /**
     * 如果JSON字符串为Null或"null"字符串,返回Null. 如果JSON字符串为"[]",返回空集合.
     *
     * @param content
     * @param clazz
     * @return
     */
    public static <T> T fromJson(String content, Class<T> clazz) {
        return fromJson(content, clazz, null);
    }

    public static <T> T fromJson(String content, Class<T> clazz, DateFormat format) {
        if (StringUtils.isEmpty(content)) {
            return null;
        }

        mapper.setDateFormat(format);

        try {
            return mapper.readValue(content, clazz);
        } catch (Exception e) {
            log.error("Parse json string error : " + content, e);
            return null;
        }
    }


    /**
     * 如果对象为Null,返回"null". 如果集合为空集合,返回"[]".
     *
     * @param object
     * @return
     */
    public static String toJson(Object object) {
        return toJson(object, null);
    }

    public static String toJson(Object object, DateFormat format) {
        mapper.setDateFormat(format);

        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            log.error("Write to json string error : " + object, e);
            return null;
        }
    }

    /**
     * 如果对象为Null,返回"null"
     *
     * @param object
     * @return
     */
    public static Map<String, Object> toMap(Object object) {
        return toMap(object, null);
    }

    public static Map<String, Object> toMap(Object object, DateFormat format) {
        mapper.setDateFormat(format);

        try {
            String content = mapper.writeValueAsString(object);
            return mapper.readValue(content, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception e) {
            log.error("Write to map error : " + object, e);
            return null;
        }
    }

    public static <T> T fromMap(Map<String, Object> map, Class<T> clazz) {

        return fromMap(map, clazz, null);
    }

    public static <T> T fromMap(Map<String, Object> map, Class<T> clazz, DateFormat format) {
        mapper.setDateFormat(format);

        try {
            String content = mapper.writeValueAsString(map);
            return mapper.readValue(content, clazz);
        } catch (Exception e) {
            log.error("Write from map error : " + map, e);
            return null;
        }
    }
}
