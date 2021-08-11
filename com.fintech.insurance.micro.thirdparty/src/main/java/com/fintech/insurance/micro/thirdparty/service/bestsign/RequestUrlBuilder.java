package com.fintech.insurance.micro.thirdparty.service.bestsign;

import com.fintech.insurance.commons.constants.BasicConstants;
import com.fintech.insurance.commons.utils.TextUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.net.URLEncoder;
import java.util.*;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/11/17 12:22
 */
@Component
public class RequestUrlBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(RequestUrlBuilder.class);

    @Autowired
    private BestsignPropertiesBean bestsignProperties;

    /**
     * 根据请求参数生成上上签GET请求URL
     *
     * @param path 上上签接口路径
     * @param requestParams 请求参数
     * @return
     * @throws Exception
     */
    public String buildGETReqeustUrl(String path, Map<String, Object> requestParams) throws Exception {
        return this.buildRequestURL(path, "GET", requestParams, null);
    }

    /**
     * 根据请求参数生成上上签POST请求URL
     *
     * @param path 上上签接口路径
     * @param postBodyJsonStr POST请求参数的json 字符串
     * @return
     * @throws Exception
     */
    public String buildPOSTReqeustUrl(String path, String postBodyJsonStr) throws Exception {
        return this.buildRequestURL(path, "POST", null, postBodyJsonStr);
    }

    private String buildRequestURL(String path, String httpMethod, Map<String, Object> getMethodParams, String postBodyJsonStr) throws Exception {
        if (StringUtils.isNotBlank(path) && path.endsWith("/")) {
            throw new IllegalArgumentException("path parameter no need be flowed with /");
        }
        if (!"POST".equals(httpMethod) && !"GET".equals(httpMethod)) {
            throw new IllegalArgumentException("Not support the HTTP method:" + httpMethod);
        }
        if (null == getMethodParams) {
            getMethodParams = new HashMap<String, Object>();
        }
        String rtick = BestsignUtil.getRtickStr();
        Map<String, Object> signParameters = new HashMap<String, Object>(getMethodParams);
        signParameters.put("developerId", bestsignProperties.getDeveloperId());
        signParameters.put("rtick", rtick);
        signParameters.put("signType", "rsa");

        //对URL参数对进行排序并连接
        List<String> sortParamKeys = new ArrayList<String>(signParameters.keySet());
        Collections.sort(sortParamKeys);

        StringBuilder paramConnectStr = new StringBuilder();
        for (String paramKey : sortParamKeys) {
            String value = null == signParameters.get(paramKey) ? BasicConstants.STRING_BLANK : signParameters.get(paramKey).toString();
            paramConnectStr.append(String.format("%s=%s", paramKey, value));
        }

        // 如果是POST 请求， 需要计算请求数据的MD5
        String postBodyMD5 = "";
        if ("POST".equals(httpMethod)) {
            postBodyMD5 = DigestUtils.md5DigestAsHex(postBodyJsonStr.getBytes("UTF-8"));
        }

        //拼接得到签名字符串
        String sign = String.format("%s/openapi/v3%s/%s", paramConnectStr.toString(), path, postBodyMD5); // 生成签名字符串
        LOG.info("sign string is:" + sign);

        String signDataString = this.getSignData(sign);
        String signData = Base64.encodeBase64String(BestsignUtil.rsaSign(signDataString.getBytes("UTF-8"), bestsignProperties.getPrivateKey()));

        signData = URLEncoder.encode(signData, "UTF-8");

        path = String.format("%s%s/?developerId=%s&rtick=%s&sign=%s&signType=rsa", bestsignProperties.getHost(), path, bestsignProperties.getDeveloperId(), rtick, signData);
        if ("GET".equals(httpMethod)) {
            for (Map.Entry<String, Object> requestParameterEntry : getMethodParams.entrySet()) {
                path = String.format("%s&%s=%s", path, requestParameterEntry.getKey(), null == requestParameterEntry.getValue() ? BasicConstants.STRING_BLANK : requestParameterEntry.getValue());
            }
        }
        LOG.debug("real request url is: " + path);
        return path;
    }

    private String getSignData(final String... args) {
        StringBuilder builder = new StringBuilder();
        int len = args.length;
        for (int i = 0; i < args.length; i++) {
            builder.append(TextUtils.convertToUtf8(args[i]));
            if (i < len - 1) {
                builder.append("\n");
            }
        }

        return builder.toString();
    }

    public void setBestsignProperties(BestsignPropertiesBean bestsignProperties) {
        this.bestsignProperties = bestsignProperties;
    }

    public static void main(String[] args) throws Exception {
        /*RequestBean requestBean = new RequestBean();
        requestBean.setDeveloperId("1862727489206354528");
        requestBean.setSignType("rsa");
        requestBean.setPrivateKey("MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKHeZ1tGMrj3EWrUWDGzPTml4QNa3Lh9/vlU+9kPB9G1E1zbTzxYZ7EmzEesEioWuqUGDx/wmMXsqNVI5HOiV1hzvCzhIFYy2S+wgAt8kEgifwhgRBn6qAO919bJ5G+T8e7yhKnZF1esk05SH7pYjd4x1Eso/UHZRpZa/yXBd+jTAgMBAAECgYAEQM3CbjPC/GruvamblLQVIbCp3+dQya67amo7p9NyxSk/FVwdn80JsJVJhNHtXS+GSoR3OGErQi6lfAbUqv1UgKxN35Xe3rPAVkurNxphVOCbvADW/32sGVnbz7UAb6pqrH3m4FNmuGqZeLd3lhVkxCvwEkfG3puFzk8oDPbwUQJBAP+K4XcEtl+bzvAibDwzuX+DNUt2ijKHVXDzaqGHUqvkfEAhGDYV3VND9Jfn+Legi0BCahFnDBCtX2JMcW7Wmj0CQQCiKJc/7OBCpUnZo0ULy2VrGEby0nemIe5fCR5SdqlxlsNvZ4cyRuW2LXjAUniNdHtx6UH9JaYd357DoWfkLJBPAkAbpzfG3Weu6Pl32wHDcgV82wIFbIp/9U01r+G2ISK9Hzii5/HqyGru+8eYOK4dkO4Awi8gOvp/Q4Oy63rK98YxAkEAjoovqbmGyA6TBARIxT1dQO5uLzRiiF57Mn7JcKNt/rMPx/WxGbjIY4NFCYl0/qLNgCwSHXvisY/H9x8CO8gQcQJAaiDJy//mXfvO3stPLZjYPdifBGY5xVdWpB/UrrLZbqw2gO0elkZImykrnDqYFu6rwNj+x7xpHuQ+LgD0yctpRg==");

        RequestUrlBuilder builder = new RequestUrlBuilder();
        builder.setRequestBean(requestBean);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("account", "test");

        String buildUrl = builder.getPostUrlByRsa(params, "/user/reg");
        System.out.println("====================Built result:");
        System.out.print(buildUrl);*/

        /*
        RequestBean requestBean = new RequestBean();
        requestBean.setDeveloperId("1862727489206354528");
        requestBean.setSignType("rsa");
        requestBean.setPrivateKey("MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKHeZ1tGMrj3EWrUWDGzPTml4QNa3Lh9/vlU+9kPB9G1E1zbTzxYZ7EmzEesEioWuqUGDx/wmMXsqNVI5HOiV1hzvCzhIFYy2S+wgAt8kEgifwhgRBn6qAO919bJ5G+T8e7yhKnZF1esk05SH7pYjd4x1Eso/UHZRpZa/yXBd+jTAgMBAAECgYAEQM3CbjPC/GruvamblLQVIbCp3+dQya67amo7p9NyxSk/FVwdn80JsJVJhNHtXS+GSoR3OGErQi6lfAbUqv1UgKxN35Xe3rPAVkurNxphVOCbvADW/32sGVnbz7UAb6pqrH3m4FNmuGqZeLd3lhVkxCvwEkfG3puFzk8oDPbwUQJBAP+K4XcEtl+bzvAibDwzuX+DNUt2ijKHVXDzaqGHUqvkfEAhGDYV3VND9Jfn+Legi0BCahFnDBCtX2JMcW7Wmj0CQQCiKJc/7OBCpUnZo0ULy2VrGEby0nemIe5fCR5SdqlxlsNvZ4cyRuW2LXjAUniNdHtx6UH9JaYd357DoWfkLJBPAkAbpzfG3Weu6Pl32wHDcgV82wIFbIp/9U01r+G2ISK9Hzii5/HqyGru+8eYOK4dkO4Awi8gOvp/Q4Oy63rK98YxAkEAjoovqbmGyA6TBARIxT1dQO5uLzRiiF57Mn7JcKNt/rMPx/WxGbjIY4NFCYl0/qLNgCwSHXvisY/H9x8CO8gQcQJAaiDJy//mXfvO3stPLZjYPdifBGY5xVdWpB/UrrLZbqw2gO0elkZImykrnDqYFu6rwNj+x7xpHuQ+LgD0yctpRg==");

        RequestUrlBuilder builder = new RequestUrlBuilder();
        builder.setRequestBean(requestBean);

        Map<String, Object> requestParams = new HashMap<String, Object>();
        requestParams.put("imageName", "testImg");
        requestParams.put("account", "abc");
        //String buildUrl = builder.getUrlByRsa(requestParams, "/signatureImage/user/download");
        String buildUrl = builder.buildRequestURL("/signatureImage/user/download", "GET", requestParams, null);
        System.out.println("====result:" + buildUrl);
        */

    }
}
