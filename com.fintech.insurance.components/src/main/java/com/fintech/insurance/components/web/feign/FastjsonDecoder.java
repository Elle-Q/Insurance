package com.fintech.insurance.components.web.feign;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.fasterxml.jackson.databind.RuntimeJsonMappingException;
import feign.FeignException;
import feign.Response;
import feign.Util;
import feign.codec.DecodeException;
import feign.codec.Decoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Type;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/12/6 13:49
 */
public class FastjsonDecoder implements Decoder {

    private static final Logger LOG = LoggerFactory.getLogger(FastjsonDecoder.class);

    private static ValueFilter nullValueFilter = new ValueFilter() {
        @Override
        public Object process(Object object, String name, Object value) {
            if (null == value) {
                return "";
            }
            return value.toString();
        }
    };

    private static final SerializerFeature[] serializeFilters = new SerializerFeature[]{SerializerFeature.WriteEnumUsingToString,
            SerializerFeature.WriteNullStringAsEmpty,
            SerializerFeature.WriteNonStringValueAsString
    };

    @Override
    public Object decode(Response response, Type type) throws IOException, DecodeException, FeignException {
        if (response.status() == 404) return Util.emptyValueOf(type);
        if (response.body() == null) return null;
        Reader reader = response.body().asReader();
        if (!reader.markSupported()) {
            reader = new BufferedReader(reader, 1);
        }
        try {
            // Read the first byte to see if we have any data
            reader.mark(1);
            if (reader.read() == -1) {
                return null; // Eagerly returning null avoids "No content to map due to end-of-input"
            }
            reader.reset();

            Object obj = JSON.parseObject(this.getReaderContent(reader), type);

            LOG.info("parse the respose object as: {}", obj.toString());
            return obj;
            //return mapper.readValue(reader, mapper.constructType(type));
        } catch (RuntimeJsonMappingException e) {
            if (e.getCause() != null && e.getCause() instanceof IOException) {
                throw IOException.class.cast(e.getCause());
            }
            throw e;
        }
    }

    public String getReaderContent(Reader reader) throws IOException {
        StringBuffer buffer = new StringBuffer();

        char cbuf[] = new char[1024];
        int n = -1;
        while ((n = reader.read(cbuf)) != -1){
            buffer.append(new String(cbuf, 0, n));
        }
        return buffer.toString();
    }

    public static void main(String[] args) throws IOException {
        FastjsonDecoder d = new FastjsonDecoder();
        String content = d.getReaderContent(new StringReader("aaaaaaaaaaaaaaaaaaabbbbbbbbbbbbbbbbbbb123"));
        System.out.println(content);
    }
}
