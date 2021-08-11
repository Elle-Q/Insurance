package com.fintech.insurance.commons.constants;

/**
 * 微信相关的常量
 */
public interface WechatConstants {

    /**
     * 临时二维码场景前缀：用于确认申请单，场景字符串为该前缀加上申请单id
     */
    public static final String QR_TMP_CONFIRM_REQUISITION_PREFIX = "CONFIRM_REQUSITION_";

    /**
     * 确认申请单的事件字符串匹配模式
     */
    public static final String QR_TMP_CONFIRM_REQUISITION_PATTERN = "^CONFIRM_REQUSITION_([1-9]{1})(\\d*)";

    /**
     * 扫码关注且确认申请单
     */
    public static final String QR_TMP_CONFIRM_REQUISITION_PREFIX_SUBSCRIBE = "qrscene_CONFIRM_REQUSITION_";;

    /**
     * 扫码关注且确认申请单
     */
    public static final String QR_TMP_CONFIRM_REQUISITION_SUBSCRIBE_PATTERN = "^qrscene_CONFIRM_REQUSITION_([1-9]{1})(\\d*)";

    /**
     * 模版消息字体颜色
     */
    public static final String TEMPLATE_MESSAGE_COLOR = "#0000CD";

    //模版消息变量
    public static final String TEMPLATE_MESSAGE_PARAM_FIRST = "first";

    public static final String TEMPLATE_MESSAGE_PARAM_KEYWORD1 = "keyword1";

    public static final String TEMPLATE_MESSAGE_PARAM_KEYWORD2 = "keyword2";

    public static final String TEMPLATE_MESSAGE_PARAM_KEYWORD3 = "keyword3";

    public static final String TEMPLATE_MESSAGE_PARAM_KEYWORD4 = "keyword4";

    public static final String TEMPLATE_MESSAGE_PARAM_REMARK = "remark";

}
