package com.fintech.insurance.micro.finance.service.yjf;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.fintech.insurance.micro.finance.persist.dao.YjfLogDao;
import com.fintech.insurance.micro.finance.persist.entity.YjfLog;
import com.fintech.insurance.micro.finance.service.yjf.exception.YijifuClientException;
import com.fintech.insurance.micro.finance.service.yjf.model.YjfResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/12/8 14:07
 */
@Component
public class YjfHttpClient {

    private static final Logger LOG = LoggerFactory.getLogger(YjfHttpClient.class);
    @Autowired
    private YjfLogDao logDao;

    public String sign(Map<String, String> formData, String securityKey) {
        return executerGetSignStr(formData, securityKey);
    }

    public String doPost(String url, Map<String, String> params, String securityKey) {
        return doPost(url, params, securityKey, 60, 60);
    }

    public String doPost(String url, Map<String, String> params, String securityKey, int connectTimeout, int readTimeout) {
        supplementDefaultAndConfig(params);

        String signStr = sign(params, securityKey);
        params.put("sign", signStr);

        String serviceCode = params.get("service");
        String platformSerialNum = params.get("orderNo");
        logDao.save(YjfLog.createRequestLog(serviceCode, platformSerialNum, JSON.toJSONString(params)));

        String responseStr = null;
        try {
            responseStr = WebUtils.doPost(url, params, connectTimeout * 1000, readTimeout * 1000);
        } catch (IOException e) {
            LOG.error("YJF request error: " + e.getMessage());
            logDao.save(YjfLog.createResponseLog(serviceCode, platformSerialNum, responseStr, null, true, "服务调用系统/网络异常"));
            throw new YijifuClientException("YJF request error:" + e.getMessage());
        }

        YjfResponse responseObj = JSON.parseObject(responseStr, YjfResponse.class);
        if (StringUtils.isBlank(responseObj.getSign())) {
            LOG.warn("服务执行失败，不验签响应报文! 响应报文：" + responseObj);
            logDao.save(YjfLog.createResponseLog(serviceCode, platformSerialNum, responseStr, null,true, "服务执行失败，不验签响应报文!"));
            return responseStr;
        }

        // 验证响应报文的签名是否正确
        boolean isPass = verificationSign(responseStr, securityKey);
        if (!isPass) {
            logDao.save(YjfLog.createResponseLog(serviceCode, platformSerialNum, responseStr, null,true, "服务响应验签失败!"));
            throw new YijifuClientException("服务响应验签失败，响应报文:" + responseStr);
        }
        LOG.info("易极付响应报文:" + responseStr);

        logDao.save(YjfLog.createResponseLog(serviceCode, platformSerialNum, responseStr, responseObj.getResultCodeType() != null ? responseObj.getResultCodeType().name(): null,
                !responseObj.getIsSuccess(), responseObj.getResultMessage()));

        return responseStr;
    }

    public boolean verificationSign(String responseStr, String securitykKey) {
        try {
            /*HashMap<String, String> dataMap = (HashMap) JSON.parseObject(responseStr, new TypeReference() {
            }, new Feature[]{Feature.OrderedField});*/
            Map<String, String> dataMap = (Map<String, String>) JSON.parse(responseStr, new Feature[]{Feature.OrderedField});

            Iterator<Map.Entry<String, String>> it = dataMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry) it.next();
                if (ObjectUtils.isEmpty(entry.getValue())) {
                    it.remove();
                }
            }
            return executerDigest(dataMap, securitykKey);
        } catch (Exception e) {
            LOG.info("认证（签名）错误");
            return false;
        }
    }

    protected <T> boolean executerDigest(Map<String, T> dataMap, String securityCheckKey) {
        boolean isPass = false;
        LOG.info("MAPINFO:" + dataMap);
        try {
            String signType = (String) dataMap.get("signType");
            String signature = (String) dataMap.get("sign");
            dataMap.remove("sign");
            String getSignStr = YjfDigestUtil.digest(dataMap, securityCheckKey, YjfDigestUtil.DigestALGEnum.getByName(signType));
            if (getSignStr.equals(signature)) {
                isPass = true;
                LOG.info("服务[" + dataMap.get("service") + "]响应报文验签成功");
            }
        } catch (Exception e) {
            LOG.info("服务[" + dataMap.get("service") + "]响应报文验签失败:" + e.getMessage());
        }
        return isPass;
    }

    protected String executerGetSignStr(Map<String, String> formData, String securityCheckKey) {
        String result = Signatures.sign(formData, securityCheckKey);
        return result;
    }

    public boolean verificationSign(Map<String, String> dataMap, String securityCheckKey) {
        return executerDigest(dataMap, securityCheckKey);
    }

    protected void supplementDefaultAndConfig(Map<String, String> request) {
        if ((StringUtils.isBlank((CharSequence) request.get("orderNo"))) && (StringUtils.isBlank((CharSequence) request.get("requestNo")))) {
            throw new YijifuClientException("请求号流水号不能为空");
        }
        if (StringUtils.isBlank((CharSequence) request.get("version"))) {
            request.put("version", "1.0");
        }
        if (StringUtils.isBlank((CharSequence) request.get("signType"))) {
            request.put("signType", "MD5");
        }
        if (StringUtils.isBlank((CharSequence) request.get("protocol"))) {
            request.put("protocol", "HTTP_FORM_JSON");
        }
        if (StringUtils.isBlank((CharSequence) request.get("partnerId"))) {
            throw new YijifuClientException("商户ID（partnerId）不能为空");
        }
        if (StringUtils.isBlank("service")) {
            throw new YijifuClientException("服务码不能为空");
        }
        Iterator<Map.Entry<String, String>> it = request.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry) it.next();
            if (StringUtils.isBlank((CharSequence) entry.getValue())) {
                it.remove();
            }
        }
    }

    public String redirect(String url, Map<String, String> requestData, String securityCheckKey) {
        try {
            supplementDefaultAndConfig(requestData);

            String signStr = sign(requestData, securityCheckKey);
            requestData.put("sign", signStr);
            StringBuffer stringBuffer = new StringBuffer();
            for (Map.Entry<String, String> entry : requestData.entrySet()) {
                stringBuffer.append((String) entry.getKey() + "=" + URLEncoder.encode((String) entry.getValue(), "UTF-8") + "&");
            }
            return url + "?" + stringBuffer.substring(0, stringBuffer.length() - 1);
        } catch (Exception e) {
            LOG.warn("请求报文组装失败", e);
            throw new YijifuClientException("组装请求报文错误:" + e.getMessage());
        }
    }

}
