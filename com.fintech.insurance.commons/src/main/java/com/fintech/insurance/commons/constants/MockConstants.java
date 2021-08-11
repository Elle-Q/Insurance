package com.fintech.insurance.commons.constants;

/**
 * dev或者联调时所需的mock参数常量
 */
public interface MockConstants {

    static final String MOCK_TOKEN_TYPE = "mock";

    /**
     * 包含该请求参数的请求，如果值设置为0或者空则当前请求不mock任何用户数据（默认为空），如果为非0，则根据参数_mockUser的值设置用户的信息
     */
    static final String PARAM_MOCK_ENABLED = "_mock";

    /**
     * 该参数表示需要mock的用户的id，针对渠道用户则为sys_user表的id，如果为客户，则为cust_account表的id
     */
    static final String PARAM_MOCK_USER_ID = "_mockUser";

    /**
     * 该参数表示mock的微信appid
     */
    static final String PARAM_MOCK_APP_ID = "_mockAppId";
}
