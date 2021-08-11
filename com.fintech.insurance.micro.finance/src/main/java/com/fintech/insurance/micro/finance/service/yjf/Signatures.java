package com.fintech.insurance.micro.finance.service.yjf;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.TreeMap;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/12/8 16:46
 */
public class Signatures {

    private static final Logger LOG = LoggerFactory.getLogger(Signatures.class);

    public static String sign(Map<String, String> formData, String secretyKey)
    {
        if (formData == null) {
            throw new IllegalArgumentException("数据不能为空");
        }
        if (formData.isEmpty()) {
            return null;
        }
        if (secretyKey == null) {
            throw new IllegalArgumentException("安全校验码数据不能为空");
        }
        Map<String, String> sortedMap = new TreeMap(formData);
        if (sortedMap.containsKey("sign")) {
            sortedMap.remove("sign");
        }
        StringBuffer stringToSign = new StringBuffer();
        if (sortedMap.size() > 0)
        {
            for (Map.Entry<String, String> entry : sortedMap.entrySet()) {
                if ((StringUtils.isNotBlank((CharSequence)entry.getValue())) && (entry.getValue() != null)) {
                    stringToSign.append((String)entry.getKey()).append("=").append((String)entry.getValue()).append("&");
                }
            }
            stringToSign.deleteCharAt(stringToSign.length() - 1);
        }
        LOG.info("待签字符串[" + stringToSign.toString() + "]");
        stringToSign.append(secretyKey);
        String signature = DigestUtils.md5Hex(stringToSign.toString());
        LOG.info("签名结果[" + signature + "]");
        return signature;
    }

}
