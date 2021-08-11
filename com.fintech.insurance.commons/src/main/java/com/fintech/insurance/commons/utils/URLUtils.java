package com.fintech.insurance.commons.utils;

import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

/**
 * URL帮助类
 */
public class URLUtils {

    public static final String OAUTH_URL_PATTERN = "/uaa/**";
    public static final String MANAGEMENT_CALLBACK_URL_PATTERN = "/management/ext-callback/**";
    public static final String WECHAT_INTEGRATION_URL_PATTERN = "/thirdparty/weixin-integration/**";
    public static final String WECHAT_CUSTOMER_URL_PATTERN = "/wechat/customer/**";
    public static final String WECHAT_CHANNEL_URL_PATTERN = "/wechat/channel/**";
    public static final String MANAGEMENT_URL_PATTERN = "/management/**";

    /**
     * 创建query字符串
     * @param parameters
     * @return
     */
    public static String buildQueryString(Map<String, String> parameters) {
        if (parameters == null || parameters.size() < 1) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder("");
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                if (StringUtils.isNotEmpty(entry.getKey()) && StringUtils.isNotEmpty(entry.getValue())) {
                    sb.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue())).append("&");
                }
            }
            String query = sb.toString();
            if (StringUtils.isNotEmpty(query) && query.endsWith("&")) {
                return query.substring(0, query.length() - 1);
            } else {
                return sb.toString();
            }
        }
    }

    /**
     * 在指定的链接地址上增加query参数
     * @param uri 原地址
     * @param name 参数名称
     * @param value 参数的值
     * @return 增加了新的query参数的链接地址
     * @throws URISyntaxException
     */
    public static URI appendQueryParameter(URI uri, String name, String value) throws URISyntaxException {
        if (uri == null) {
            return null;
        } else {
            String newQuery = uri.getQuery();
            if (StringUtils.isEmpty(newQuery)) {
                newQuery = name + "=" + URLEncoder.encode(value);
            } else {
                newQuery = newQuery + "&" + name + "=" + URLEncoder.encode(value);
            }
            return new URI(uri.getScheme(), uri.getAuthority(), uri.getPath(), newQuery, uri.getFragment());
        }
    }

    /**
     * 在指定的链接地址上增加query参数
     * @param uri 原地址
     * @param name 参数名称
     * @param value 参数的值
     * @return 增加了新的query参数的链接地址
     * @throws URISyntaxException
     */
    public static String appendQueryParameter(String uri, String name, String value) throws URISyntaxException {
        if (StringUtils.isEmpty(uri)) {
            return "";
        } else {
            URI newUri = appendQueryParameter(new URI(uri), name , value);
            return newUri == null ? "" : uri2String(newUri);
        }
    }

    /**
     * 在指定的链接地址上增加多个query参数
     * @param uri 原地址
     * @param parameters 参数的名称和值构成的map
     * @return 增加了新的query参数的链接地址
     * @throws URISyntaxException
     */
    public static URI appendQueryParameters(URI uri, Map<String, String> parameters) throws URISyntaxException {
        if (uri == null) {
            return null;
        } else {
            if (parameters == null || parameters.size() == 0) {
                return uri;
            } else {
                StringBuilder appendQuery = new StringBuilder();
                Iterator<String> parameterKey = parameters.keySet().iterator();
                while (parameterKey.hasNext()) {
                    String key = parameterKey.next();
                    appendQuery.append(key).append("=").append(URLEncoder.encode(parameters.get(key))).append("&");
                }
                String newQuery = uri.getQuery();
                if (StringUtils.isEmpty(newQuery)) {
                    newQuery = appendQuery.toString();
                } else {
                    newQuery = newQuery + "&" + appendQuery.toString();
                }
                if (newQuery.endsWith("&")) {
                    newQuery = newQuery.substring(0, newQuery.length() - 1);
                }
                return new URI(uri.getScheme(), uri.getAuthority(), uri.getPath(), newQuery, uri.getFragment());
            }
        }
    }

    /**
     * 在指定的链接地址上增加多个query参数
     * @param uri 原地址
     * @param parameters 参数的名称和值构成的map
     * @return 增加了新的query参数的链接地址
     * @throws URISyntaxException
     */
    public static String appendQueryParameters(String uri, Map<String, String> parameters) throws URISyntaxException {
        if (StringUtils.isEmpty(uri)) {
            return "";
        } else {
            URI newUri = appendQueryParameters(new URI(uri), parameters);
            return newUri == null ? "" : uri2String(newUri);
        }
    }

    /**
     * 将uri转换为string
     * @param uri
     * @return
     */
    public static String uri2String(URI uri) {
        if (uri == null) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder("");
            if (StringUtils.isNotEmpty(uri.getScheme())) {
                sb.append(uri.getScheme()).append(":");
            }
            if (uri.isOpaque()) {
                sb.append(uri.getSchemeSpecificPart());
            } else {
                if (StringUtils.isNotEmpty(uri.getHost())) {
                    String host = uri.getHost();
                    sb.append("//");
                    if (StringUtils.isNotEmpty(uri.getUserInfo())) {
                        sb.append(uri.getUserInfo()).append("@");
                    }
                    boolean needBrackets = ((host.indexOf(':') >= 0)
                            && !host.startsWith("[")
                            && !host.endsWith("]"));
                    if (needBrackets) {
                        sb.append('[');
                    }
                    sb.append(host);
                    if (needBrackets) {
                        sb.append(']');
                    }
                    if (uri.getPort() != -1) {
                        sb.append(':');
                        sb.append(uri.getPort());
                    }
                } else if (StringUtils.isNotEmpty(uri.getAuthority())) {
                    sb.append("//").append(uri.getAuthority());
                }
                String path = uri.getPath();
                if (StringUtils.isNotEmpty(path)) {
                    sb.append(path);
                }
                if (StringUtils.isNotEmpty(uri.getFragment())) {
                    if (StringUtils.isEmpty(path) || !path.endsWith("/")) {
                        sb.append("/");
                    }
                    sb.append("#").append(uri.getFragment());
                }
                //增加query
                if (StringUtils.isNotEmpty(uri.getQuery())) {
                    sb.append("?").append(uri.getQuery());
                }
            }

            return sb.toString();
        }
    }

    /**
     * 将url转换为string
     * @param url
     * @return
     * @throws URISyntaxException
     */
    public static String url2String(URL url) throws URISyntaxException {
        if (url == null) {
            return "";
        } else {
            return uri2String(url.toURI());
        }
    }
}
