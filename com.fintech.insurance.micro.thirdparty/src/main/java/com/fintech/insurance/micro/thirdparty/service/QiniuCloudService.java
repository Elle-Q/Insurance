package com.fintech.insurance.micro.thirdparty.service;

import com.fintech.insurance.micro.dto.thirdparty.QiNiuTokenVO;

import java.io.InputStream;

/**
 * 七牛上传服务接口类，用于获取上传文件令牌、私有空间下载令牌、服务端上传文件等。
 *
 * @author Sean Zhang
 * @version 1.0.0
 * @since 2017/05/24
 */
public interface QiniuCloudService {

    /**
     * 获取公有空间的上传token
     *
     * @return String
     */
    public QiNiuTokenVO getPublicBucketUploadToken();

    /**
     * 获取公有空间的上传token
     *
     * @param key 上传文件的唯一标识，可以包含路径
     * @return String
     */
    public QiNiuTokenVO getPublicBucketUploadToken(String key);

    /**
     * 获取私有空间的上传token
     *
     * @return String
     */
    public QiNiuTokenVO getPrivateBucketUploadToken();

    /**
     * 获取公有空间的上传token
     *
     * @param key 上传文件的唯一标识，可以包含路径
     * @return String
     */
    public QiNiuTokenVO getPrivateBucketUploadToken(String key);

    /**
     * 以文件流形式上传文件到公共空间
     *
     * @param key         上传文件的唯一标识，可以包含路径
     * @param inputStream 文件流
     * @return String 成功返回文件的key，失败返回null
     */
    public String uploadFileToPublicBucket(String key, InputStream inputStream);

    /**
     * 以文件流形式上传文件到私有空间
     *
     * @param key         上传文件的唯一标识，可以包含路径
     * @param inputStream 文件流
     * @return String 成功返回文件的key，失败返回null
     */
    public String uploadFileToPrivateBucket(String key, InputStream inputStream);

    /**
     * 获取公有文件的访问地址
     *
     * @param key 上传文件的唯一标识，可以包含路径
     * @return 文件访问地址
     */
    public String getPublicFileUrl(String key);

    /**
     * /**
     * 获取私有文件的访问地址, 对于图片，可以通过定义model, imageWidth, imageHeight 来获取自定义获取缩略图的尺寸
     *
     * @param key 上传文件的唯一标识，可以包含路径
     * @param model 参考七牛云的文档
     * @param imageWidth 缩略图的宽尺寸

     * @param imageHeight 缩略图的高尺寸
     * @return 文件访问地址
     */
    public String getPrivateFileUrl(String key, String model, String imageWidth, String imageHeight);

    /**
     * 获取访问key
     *
     * @return
     */
    public String getAccessKey();

    /**
     * 获取公有空间名
     *
     * @return
     */
    public String getPublicBucketName();

    /**
     * 获取公有空间访问地址
     *
     * @return
     */
    public String getPublicBucketHost();

    /**
     * 获取私有空间名
     *
     * @return
     */
    public String getPrivateBucketName();

    /**
     * 获取私有空间访问地址
     *
     * @return
     */
    public String getPrivateBucketHost();

    /**
     * 获取上传token过期时间，单位：分钟
     *
     * @return
     */
    public long getTokenExpireMinutes();

    /**
     * 获取私有资源访问token的过期时间，单位：分钟
     *
     * @return
     */
    public long getPrivateAccessTokenExpireMinutes();

    /**
     * 远程抓取文件
     * @param url 远程文件地址
     * @param isPublic 是否公有
     * @param key 七牛对应的key
     * @return
     */
    public String remoteGrabFile(String url, Integer isPublic, String key);

    /**
     * 删除文件
     * @param key 七牛对应的key
     * @param isPublic 是否公有
     * @return
     */
    public void deleteFile(String key, Integer isPublic);

}
