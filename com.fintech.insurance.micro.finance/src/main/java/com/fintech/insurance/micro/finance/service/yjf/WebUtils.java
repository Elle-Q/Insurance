package com.fintech.insurance.micro.finance.service.yjf;

import com.fintech.insurance.commons.constants.BasicConstants;
import com.fintech.insurance.micro.finance.service.yjf.exception.YijifuClientException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.*;
import java.net.*;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class WebUtils {
    private static final Logger LOG = LoggerFactory.getLogger(WebUtils.class);

    private static final String METHOD_POST = "POST";
    private static final String METHOD_GET = "GET";
    private static SSLContext ctx = null;
    private static SSLSocketFactory socketFactory = null;

    private static HostnameVerifier verifier = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    static {
        try {
            ctx = SSLContext.getInstance("TLS");
            ctx.init(new KeyManager[0], new TrustManager[]{new DefaultTrustManager()}, new SecureRandom());

            ctx.getClientSessionContext().setSessionTimeout(15);
            ctx.getClientSessionContext().setSessionCacheSize(1000);

            socketFactory = ctx.getSocketFactory();
        } catch (Exception localException) {
        }
    }

    /**
     * http客户端对象
     */
    private static CloseableHttpClient httpClient = null;

    static {
        PoolingHttpClientConnectionManager connManager = null;
        boolean enableSSL = false;
        if (enableSSL) {
            ConnectionSocketFactory plainsf = PlainConnectionSocketFactory.getSocketFactory();
            LayeredConnectionSocketFactory sslsf = SSLConnectionSocketFactory.getSocketFactory();
            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create().register("http", plainsf).register("https", sslsf).build();
            connManager = new PoolingHttpClientConnectionManager(registry);
        } else {
            connManager = new PoolingHttpClientConnectionManager();
        }

        connManager.setMaxTotal(200);
        connManager.setDefaultMaxPerRoute(10);
        //重试规则
        HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
            @Override
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                if (executionCount >= 5) {// 如果已经重试了5次，就放弃
                    return false;
                }
                if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试
                    return true;
                }
                if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常
                    return false;
                }
                if (exception instanceof InterruptedIOException) {// 超时
                    return false;
                }
                if (exception instanceof UnknownHostException) {// 目标服务器不可达
                    return false;
                }
                if (exception instanceof ConnectTimeoutException) {// 连接被拒绝
                    return false;
                }
                if (exception instanceof SSLException) {// SSL握手异常
                    return false;
                }
                HttpClientContext clientContext = HttpClientContext .adapt(context);
                HttpRequest request = clientContext.getRequest();
                // 如果请求是幂等的，就再次尝试
                if (!(request instanceof HttpEntityEnclosingRequest)) {
                    return true;
                }
                return false;
            }
        };
        httpClient = HttpClients.custom().setConnectionManager(connManager).setRetryHandler(httpRequestRetryHandler).build();
    }

    public static String doPost(String url, Map<String, String> params, int connectTimeout, int readTimeout)
            throws IOException {
        return doPostByFormSubmit(url, params, BasicConstants.CHARSET_UTF8, connectTimeout, readTimeout);
    }

    public static String doPostByFormSubmit(String url, Map<String, String> params, String charset, int connectTimeout, int readTimeout)
            throws IOException {
        String ctype = "application/x-www-form-urlencoded;charset=" + charset;
        String query = buildQuery(params, charset);
        byte[] content = new byte[0];
        if (query != null) {
            content = query.getBytes(charset);
        }
        return postRequest(url, ctype, content);
    }

    @Deprecated
    public static String doPost(String url, String ctype, byte[] content, int connectTimeout, int readTimeout)
            throws IOException {
        HttpURLConnection conn = null;
        OutputStream out = null;
        String rsp = null;
        try {
            try {
                conn = getConnection(new URL(url), "POST", ctype);
                conn.setConnectTimeout(connectTimeout);
                conn.setReadTimeout(readTimeout);
            } catch (IOException e) {
                Map<String, String> map = getParamsFromUrl(url);
                LOG.error("External service [" + url + "], failed by:" + e.getMessage());
                throw e;
            }
            try {
                out = conn.getOutputStream();
                out.write(content);
                rsp = getResponseAsString(conn);
            } catch (IOException e) {
                Map<String, String> map = getParamsFromUrl(url);
                LOG.error("External service [" + url + "], failed by:" + e.getMessage());
                throw e;
            }
        } finally {
            if (out != null) {
                out.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return rsp;
    }

    public static String postRequest(String url, String ctype, byte[] content) throws IOException {
        CloseableHttpResponse response = null;
        try {
            HttpPost postRequest = new HttpPost(url);
            ByteArrayEntity byteArrayEntity = new ByteArrayEntity(content);
            byteArrayEntity.setContentType(ctype);
            postRequest.setEntity(byteArrayEntity);

            response = httpClient.execute(postRequest, HttpClientContext.create());

            HttpEntity entity = response.getEntity();
            String responseStr = EntityUtils.toString(entity, BasicConstants.CHARSET_UTF8);
            EntityUtils.consume(entity);
            return responseStr;
        } catch (Exception e) {
            throw new YijifuClientException("易极付服务调用请求失败: " + e.getMessage());
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    private static byte[] getTextEntry(String fieldName, String fieldValue, String charset)
            throws IOException {
        StringBuilder entry = new StringBuilder();
        entry.append("Content-Disposition:form-data;name=\"");
        entry.append(fieldName);
        entry.append("\"\r\nContent-Type:text/plain\r\n\r\n");
        entry.append(fieldValue);
        return entry.toString().getBytes(charset);
    }

    private static byte[] getFileEntry(String fieldName, String fileName, String mimeType, String charset)
            throws IOException {
        StringBuilder entry = new StringBuilder();
        entry.append("Content-Disposition:form-data;name=\"");
        entry.append(fieldName);
        entry.append("\";filename=\"");
        entry.append(fileName);
        entry.append("\"\r\nContent-Type:");
        entry.append(mimeType);
        entry.append("\r\n\r\n");
        return entry.toString().getBytes(charset);
    }

    public static String doGet(String url, Map<String, String> params)
            throws IOException {
        return doGet(url, params, BasicConstants.CHARSET_UTF8);
    }

    public static String doGet(String url, Map<String, String> params, String charset)
            throws IOException {
        HttpURLConnection conn = null;
        String rsp = null;
        try {
            String ctype = "application/x-www-form-urlencoded;charset=" + charset;
            String query = buildQuery(params, charset);
            try {
                conn = getConnection(buildGetUrl(url, query), "GET", ctype);
            } catch (IOException e) {
                Map<String, String> map = getParamsFromUrl(url);
                LOG.error("External service [" + (String) map.get("service") + "], failed by:" + e.getMessage());
                throw e;
            }
            try {
                rsp = getResponseAsString(conn);
            } catch (IOException e) {
                Map<String, String> map = getParamsFromUrl(url);
                LOG.error("External service [" + (String) map.get("service") + "], failed by:" + e.getMessage());
                throw e;
            }
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return rsp;
    }

    private static HttpURLConnection getConnection(URL url, String method, String ctype)
            throws IOException {
        HttpURLConnection conn = null;
        if ("https".equals(url.getProtocol())) {
            HttpsURLConnection connHttps = (HttpsURLConnection) url.openConnection();
            connHttps.setSSLSocketFactory(socketFactory);
            connHttps.setHostnameVerifier(verifier);
            conn = connHttps;
        } else {
            conn = (HttpURLConnection) url.openConnection();
        }
        conn.setRequestMethod(method);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestProperty("Accept", "text/xml,text/javascript,text/html");
        conn.setRequestProperty("User-Agent", "aop-sdk-java");
        conn.setRequestProperty("Content-Type", ctype);
        return conn;
    }

    private static URL buildGetUrl(String strUrl, String query)
            throws IOException {
        URL url = new URL(strUrl);
        if (StringUtils.isEmpty(query)) {
            return url;
        }
        if (StringUtils.isEmpty(url.getQuery())) {
            if (strUrl.endsWith("?")) {
                strUrl = strUrl + query;
            } else {
                strUrl = strUrl + "?" + query;
            }
        } else if (strUrl.endsWith("&")) {
            strUrl = strUrl + query;
        } else {
            strUrl = strUrl + "&" + query;
        }
        return new URL(strUrl);
    }

    public static String buildQuery(Map<String, String> params, String charset)
            throws IOException {
        if ((params == null) || (params.isEmpty())) {
            return null;
        }
        StringBuilder query = new StringBuilder();
        Set<Map.Entry<String, String>> entries = params.entrySet();
        boolean hasParam = false;
        for (Map.Entry<String, String> entry : entries) {
            String name = (String) entry.getKey();
            String value = (String) entry.getValue();
            if ((StringUtils.isNotEmpty(name)) && (StringUtils.isNotEmpty(value))) {
                if (hasParam) {
                    query.append("&");
                } else {
                    hasParam = true;
                }
                query.append(name).append("=").append(URLEncoder.encode(value, charset));
            }
        }
        return query.toString();
    }

    protected static String getResponseAsString(HttpURLConnection conn)
            throws IOException {
        String charset = getResponseCharset(conn.getContentType());
        InputStream es = conn.getErrorStream();
        if (es == null) {
            return getStreamAsString(conn.getInputStream(), charset);
        }
        String msg = getStreamAsString(es, charset);
        if (StringUtils.isEmpty(msg)) {
            throw new IOException(conn.getResponseCode() + ":" + conn.getResponseMessage());
        }
        throw new IOException(msg);
    }

    private static String getStreamAsString(InputStream stream, String charset)
            throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, charset));
            StringWriter writer = new StringWriter();

            char[] chars = new char['?'];
            int count = 0;
            while ((count = reader.read(chars)) > 0) {
                writer.write(chars, 0, count);
            }
            return writer.toString();
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }

    private static String getResponseCharset(String ctype) {
        String charset = BasicConstants.CHARSET_UTF8;
        if (!StringUtils.isEmpty(ctype)) {
            String[] params = ctype.split(";");
            for (String param : params) {
                param = param.trim();
                if (param.startsWith("charset")) {
                    String[] pair = param.split("=", 2);
                    if ((pair.length != 2) ||
                            (StringUtils.isEmpty(pair[1]))) {
                        break;
                    }
                    charset = pair[1].trim();
                    break;
                }
            }
        }
        return charset;
    }

    public static String decode(String value) {
        return decode(value, BasicConstants.CHARSET_UTF8);
    }

    public static String encode(String value) {
        return encode(value, BasicConstants.CHARSET_UTF8);
    }

    public static String decode(String value, String charset) {
        String result = null;
        if (!StringUtils.isEmpty(value)) {
            try {
                result = URLDecoder.decode(value, charset);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    public static String encode(String value, String charset) {
        String result = null;
        if (!StringUtils.isEmpty(value)) {
            try {
                result = URLEncoder.encode(value, charset);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    private static Map<String, String> getParamsFromUrl(String url) {
        Map<String, String> map = null;
        if ((url != null) && (url.indexOf('?') != -1)) {
            map = splitUrlQuery(url.substring(url.indexOf('?') + 1));
        }
        if (map == null) {
            map = new HashMap();
        }
        return map;
    }

    public static Map<String, String> splitUrlQuery(String query) {
        Map<String, String> result = new HashMap();

        String[] pairs = query.split("&");
        if ((pairs != null) && (pairs.length > 0)) {
            for (String pair : pairs) {
                String[] param = pair.split("=", 2);
                if ((param != null) && (param.length == 2)) {
                    result.put(param[0], param[1]);
                }
            }
        }
        return result;
    }

    public static String buildForm(String baseUrl, Map<String, String> parameters) {
        StringBuffer sb = new StringBuffer();
        sb.append("<form name=\"punchout_form\" method=\"post\" action=\"");
        sb.append(baseUrl);
        sb.append("\">\n");
        sb.append(buildHiddenFields(parameters));

        sb.append("<input type=\"submit\" value=\"立即支付\" style=\"display:none\" >\n");
        sb.append("</form>\n");
        sb.append("<script>document.forms[0].submit();</script>");
        String form = sb.toString();
        return form;
    }

    private static String buildHiddenFields(Map<String, String> parameters) {
        if ((parameters == null) || (parameters.isEmpty())) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        Set<String> keys = parameters.keySet();
        for (String key : keys) {
            String value = (String) parameters.get(key);
            if ((key != null) && (value != null)) {
                sb.append(buildHiddenField(key, value));
            }
        }
        String result = sb.toString();
        return result;
    }

    private static String buildHiddenField(String key, String value) {
        StringBuffer sb = new StringBuffer();
        sb.append("<input type=\"hidden\" name=\"");
        sb.append(key);

        sb.append("\" value=\"");

        String a = value.replace("\"", "&quot;");
        sb.append(a).append("\">\n");
        return sb.toString();
    }

    private static class DefaultTrustManager
            implements X509TrustManager {
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }
    }
}

