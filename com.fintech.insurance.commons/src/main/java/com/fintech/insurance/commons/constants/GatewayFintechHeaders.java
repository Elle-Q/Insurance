package com.fintech.insurance.commons.constants;

public interface GatewayFintechHeaders {

    static final String USER_AGENT = "User-Agent";
    static final String USER_AGENT_MICROMESSENGER = "micromessenger";

    /**
     * 自定义请求id
     */
    static final String REQUEST_ID = "X-FINTECH-REQ";

    /**
     * 用户名
     */
    static final String PRINCIPAL = "X-PRINCIPAL";

    /**
     * 用户id
     */
    static final String PRINCIPAL_ID = "X-PRINCIPAL-ID";

    /**
     * 用户类型
     */
    static final String PRICIPAL_USERTYPE = "X-PRINCIPAL-USERTYPE";

    /**
     * 外部调用标识，只有经过zuul路由的请求才会在RequestContext中记录该值
     */
    static final String EXTERNAL_INVOKE = "X-EXTERNAL-INVOKE";

    /**
     * 请求来源标识, 可选值参考com.fintech.insurance.commons.enums.RequestSourceType
     */
    static final String CLIENT_INVOKE = "X-FINSURANCE-CLIENT";

    static final String H5_APP_CONTEXT = "X-H5APP-CONTEXT";
}
