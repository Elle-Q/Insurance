package com.fintech.insurance.components.web.feign;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import feign.RequestTemplate;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/12/6 13:42
 */
public class FastjsonEncoder implements Encoder {

    private static final Logger LOG = LoggerFactory.getLogger(FastjsonEncoder.class);

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

    public FastjsonEncoder() {
    }

    @Override
    public void encode(Object o, Type type, RequestTemplate requestTemplate) throws EncodeException {
        String jsonStr = JSON.toJSONString(o, FastjsonEncoder.nullValueFilter, FastjsonEncoder.serializeFilters);
        LOG.info("Encode reqeust object: {}", jsonStr);
        requestTemplate.body(jsonStr);
    }
}
