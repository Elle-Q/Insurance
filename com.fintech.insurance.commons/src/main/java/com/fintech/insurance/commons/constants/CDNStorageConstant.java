package com.fintech.insurance.commons.constants;

public interface CDNStorageConstant {

    // OSS命名空间的在ipay.AppConfig.xml的配置name
    public static final String BUCKET_NAME = "bucketName";
    // OSS命名空间所在的端点
    public static final String ENDPOINT = "endpoint";
    // OSS的accessKeyId
    public static final String ACCESS_KEY_ID = "accessKeyId";
    // OSS的accessKeySecret
    public static final String ACCESS_KEY_SECRET = "accessKeySecret";
    // OSS上传token的过期时间
    public static final String TOKEN_EXPIRE_MINUTES = "tokenExpireMinutes";

    // 新闻公告文件的前缀目录
    public static final String NEWS_DIR = "news/";

    // 运营报告文件的前缀目录
    public static final String RUN_REPORTS_DIR = "runReports/";

    // 图片资源的前缀目录
    public static final String IMAGE_DIR = "image/";

    // 回款确认上传附件资源
    public static final String PLATFORM_REPAY_FILE_DIR = "platformRepay/";

    // 审核资源的前缀目录
    public static final String CENSORED_DIR = "censored/";

    // 消息推送网页
    public static final String APK_MSG_PUSH_FILE_DIR = "apkMsgPush/";

    // PDF资源的前缀目录
    public static final String PDF_DIR = "pdf/";
}
