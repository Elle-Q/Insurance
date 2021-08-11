package com.fintech.insurance.components.web;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter4;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MicroJsonMessageConverter extends FastJsonHttpMessageConverter4 {

    public static MicroJsonMessageConverter converterInstance = new MicroJsonMessageConverter();

    private MicroJsonMessageConverter() {
        super();
        //仅支持APPLICATION_JSON_UTF8
        List<MediaType> supportedMediaTypes = Arrays.asList(MediaType.APPLICATION_JSON_UTF8, new MediaType("application", "*+json"));
        this.setSupportedMediaTypes(Collections.unmodifiableList(supportedMediaTypes));

        //MicroParseConfig parseConfig = MicroParseConfig.instance;
        //MicroParseConfig.global = MicroParseConfig.instance;
    }

    @Override
    protected void writeInternal(Object obj, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        super.writeInternal(obj, type, outputMessage);
    }
}
