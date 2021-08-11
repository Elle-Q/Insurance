package com.fintech.insurance.commons.constants;

import java.math.BigDecimal;

/**
 * 该常量类用于封装非业务的常量，如对文本、数据或者系统各个组件的处理开关、特征等。
 */
public class BasicConstants {

    public static final String STRING_BLANK = "";
    public static final String NUMBER_ZERO = "0";
    public static final Double NUMBER_FORMAT_ZERO = 0.00D;
    public static final BigDecimal NUMBER_BIGDECIMAL_FORMAT_ZERO = new BigDecimal(0.00D);

    public static final String YES = "Y";
    public static final String NO = "N";

    public static final String DEFAULT_PAGE_INDEX = "1";
    public static final String DEFAULT_PAGE_SIZE = "20";

    public static final int DAYS_IN_YEAR = 365;

    public static final String CHARSET_UTF8 = "UTF-8";

    public static final String DEFAULT_TEST_SMS_VERIFICATION = "111111";


    //图片验证码过期时间(分钟)
    public static final int IMAGE_VERCODE_EXPIRED_MINUTES = 5;

    //图片验证码过期时间(天)
    public static final int IMAGE_VERCODE_EXPIRED_DAYS = 3;

    //七牛云过期时间(分钟)
    public static final int QINIU_EXPIRED_MINUTES = 200;


    public static final int REQUSITION_MIN_MONTH = 3;//最小月

    public static final int REQUSITION_MAX_MONTH = 10;//最大月

    public static final int REQUSITION_YEAR_MONTH = 12;//一年12月

    public static final int REQUSITION_YEAR_DAYS = 364;//天数
}
