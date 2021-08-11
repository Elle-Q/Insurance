package com.fintech.insurance.micro.finance.service.yjf;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/12/8 16:25
 */
public class YjfDigestUtil {

    public static final String UTF8 = "utf-8";
    public static final String SIGN_KEY = "sign";
    public static final String CHANNEL_ID_KEY = "channelId";
    public static final String SIGN_TYPE_KEY = "signType";
    public static final String TIMESTAMP_KEY = "utc_time_stamp";
    private static final Logger LOG = LoggerFactory.getLogger(YjfDigestUtil.class);
    private static final String TIME_ZONE = "UTC";
    private static final String UTC_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS Z";

    public static <T> String digest(Map<String, T> dataMap, String securityCheckKey, DigestALGEnum de) {
        return digest(dataMap, securityCheckKey, de, "utf-8");
    }

    public static String digestWithTimeStamp(Map<String, Object> dataMap, String securityCheckKey, DigestALGEnum de, String charset) {
        dataMap.put("utc_time_stamp", getUTCTime());
        return digest(dataMap, securityCheckKey, de, charset);
    }

    public static String digestWithTimeStamp(Map<String, Object> dataMap, String securityCheckKey, DigestALGEnum de) {
        return digestWithTimeStamp(dataMap, securityCheckKey, de, "utf-8");
    }

    public static <T> String digest(Map<String, T> dataMap, String securityCheckKey, DigestALGEnum de, String encoding) {
        if (dataMap == null) {
            throw new IllegalArgumentException("数据不能为空");
        }
        if (dataMap.isEmpty()) {
            return null;
        }
        if (securityCheckKey == null) {
            throw new IllegalArgumentException("安全校验码数据不能为空");
        }
        if (de == null) {
            throw new IllegalArgumentException("摘要算法不能为空");
        }
        if (StringUtils.isBlank(encoding)) {
            throw new IllegalArgumentException("字符集不能为空");
        }
        TreeMap<String, T> treeMap = new TreeMap(dataMap);
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, T> entry : treeMap.entrySet()) {
            if (entry.getValue() == null) {
                throw new IllegalArgumentException((String) entry.getKey() + "待签名值不能为空");
            }
            if (!((String) entry.getKey()).equals("sign")) {
                sb.append((String) entry.getKey()).append("=").append(entry.getValue().toString()).append("&");
            }
        }
        sb.deleteCharAt(sb.length() - 1);

        sb.append(securityCheckKey);
        try {
            String str = sb.toString();
            byte[] toDigest = str.getBytes(encoding);
            LOG.debug("待签名字符串[" + str + "]");
            MessageDigest md = MessageDigest.getInstance(de.getName());
            md.update(toDigest);
            return new String(Hex.encodeHex(md.digest()));
        } catch (Exception e) {
            throw new RuntimeException("签名失败", e);
        }
    }

    private static String getSign(Map<String, ?> params) {
        Object para = params.get("sign");
        if (para == null) {
            throw new IllegalArgumentException("sign不能为空");
        }
        return para.toString().trim();
    }

    public static void check(Map<String, ?> params, String securityCheckKey, DigestALGEnum de, String charset) {
        if (securityCheckKey == null) {
            throw new IllegalArgumentException("安全校验码不能为空");
        }
        if (params == null) {
            throw new IllegalArgumentException("params对象不能为空");
        }
        if (de == null) {
            throw new IllegalArgumentException("DigestALGEnum对象不能为空");
        }
        Object signTypeObj = params.get("signType");
        String signType = signTypeObj == null ? de.getName() : signTypeObj.toString();
        if (DigestALGEnum.getByName(signType) == null) {
            throw new IllegalArgumentException("不支持的摘要算法类型:" + signType);
        }
        String sign = getSign(params);
        TreeMap<String, ?> treeMap = new TreeMap(params);
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, ?> entry : treeMap.entrySet()) {
            if (entry.getValue() == null) {
                throw new IllegalArgumentException((String) entry.getKey() + " 待签名值不能为空");
            }
            if (!((String) entry.getKey()).equals("sign")) {
                sb.append((String) entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(securityCheckKey);

        String digest = null;
        try {
            String str = sb.toString();
            byte[] toDigest = str.getBytes(charset == null ? "utf-8" : charset);
            LOG.debug("待签名url:" + str);
            MessageDigest md = MessageDigest.getInstance(signType);
            md.update(toDigest);
            digest = new String(Hex.encodeHex(md.digest()));
        } catch (Exception e) {
            throw new RuntimeException("签名失败", e);
        }
        byte[] toDigest;
        if (!sign.equals(digest)) {
            LOG.debug("签名摘要计算结果{" + digest + "}");
            throw new RuntimeException("签名校验失败");
        }
    }

    public static <T> String digest(Map<String, T> data, DigestALGEnum de) {
        return digest(data, null, de);
    }

    public static <T> String digestWithSHA256(Map<String, T> data) {
        return digest(data, null, DigestALGEnum.SHA256);
    }

    public static <T> String digestWithSHA256(Map<String, T> data, String securityCheckKey) {
        return digest(data, securityCheckKey, DigestALGEnum.SHA256);
    }

    public static <T> String digestWithMD5(Map<String, T> data) {
        return digest(data, null, DigestALGEnum.MD5);
    }

    public static <T> String digestWithMD5(Map<String, T> data, String securityCheckKey) {
        return digest(data, securityCheckKey, DigestALGEnum.MD5);
    }

    public static String getUTCTime() {
        DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
        TimeZone utcTime = TimeZone.getTimeZone("UTC");
        utcFormat.setTimeZone(utcTime);
        Calendar calendar = Calendar.getInstance();
        return utcFormat.format(calendar.getTime());
    }

    public static void checkTimeout(String timestamp, long expireTime, TimeUnit timeUnit) {
        DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
        TimeZone utcTime = TimeZone.getTimeZone("UTC");
        utcFormat.setTimeZone(utcTime);
        Date dt = null;
        try {
            dt = utcFormat.parse(timestamp);
        } catch (ParseException e) {
            throw new RuntimeException("时间格式异常", e);
        }
        Date now = new Date();
        if (now.getTime() - dt.getTime() > timeUnit.toMillis(expireTime)) {
            throw new RuntimeException("请求已过期");
        }
    }

    public static enum DigestALGEnum {
        SHA256("SHA-256"), MD5("MD5");

        private String name;

        private DigestALGEnum(String name) {
            this.name = name;
        }

        public static DigestALGEnum getByName(String name) {
            for (DigestALGEnum _enum :DigestALGEnum.values()) {
                if (_enum.getName().equals(name)) {
                    return _enum;
                }
            }
            return null;
        }

        public String getName() {
            return this.name;
        }
    }
}
