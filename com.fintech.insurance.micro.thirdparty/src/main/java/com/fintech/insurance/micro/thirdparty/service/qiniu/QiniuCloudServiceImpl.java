package com.fintech.insurance.micro.thirdparty.service.qiniu;


import com.fintech.insurance.commons.constants.BasicConstants;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.micro.dto.thirdparty.QiNiuTokenVO;
import com.fintech.insurance.micro.thirdparty.service.QiniuCloudService;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.model.FetchRet;
import com.qiniu.util.Auth;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

/**
 * 七牛上传服务实现类，用于获取上传文件令牌、私有空间下载令牌、服务端上传文件等。
 *
 * @author man liu
 * @version 1.0.0
 * @since 2017/11/24
 */
@Service
public class QiniuCloudServiceImpl implements QiniuCloudService, InitializingBean {

    private static final Logger LOG = LoggerFactory.getLogger(QiniuCloudServiceImpl.class);

    private QiNiuPropertiesBean qiNiuPropertiesBean;//读取配置文件
    private String accessKey ;//账户名
    private String secretKey;//密码
    private String publicBucketName;//公有的空间
    private String publicBucketHost;//公有的访问主机
    private String privateBucketName;//私有的空间
    private String privateBucketHost;//私有的访问主机
    private long tokenExpireMinutes = 0;//koken超时时间
    private long privateAccessTokenExpireMinutes = 0;//私有的认证超时时间

    /**
     * 七牛鉴权客户端
     */
    private Auth qiniuAuth = null;
    private Configuration cfg = null;

    @Autowired
    private RedisTemplate<String, String> stringRedisTemplate;

    public String getAccessKey() {
        return accessKey;
    }

    public String getPublicBucketName() {
        return publicBucketName;
    }

    public String getPublicBucketHost() {
        return publicBucketHost;
    }

    public String getPrivateBucketName() {
        return privateBucketName;
    }

    public String getPrivateBucketHost() {
        return privateBucketHost;
    }

    public long getTokenExpireMinutes() {
        return tokenExpireMinutes;
    }

    public long getPrivateAccessTokenExpireMinutes() {
        return privateAccessTokenExpireMinutes;
    }

    @Autowired
    public void setQiNiuPropertiesBean(QiNiuPropertiesBean qiNiuPropertiesBean) {
        this.qiNiuPropertiesBean = qiNiuPropertiesBean;
    }

    /**
     * 从配置文件中读取配置
     */
    private void initConfigurations() {
        this.accessKey = this.qiNiuPropertiesBean.getAccessKey();
        this.secretKey = this.qiNiuPropertiesBean.getSecretKey();
        this.publicBucketName = this.qiNiuPropertiesBean.getPublicBucket();
        this.publicBucketHost = this.qiNiuPropertiesBean.getPublicBucketHost();
        this.privateBucketName = this.qiNiuPropertiesBean.getPrivateBucket();
        this.privateBucketHost = this.qiNiuPropertiesBean.getPrivateBucketHost();
        this.tokenExpireMinutes = this.qiNiuPropertiesBean.getTokenExpireMinute();
        this.privateAccessTokenExpireMinutes = this.qiNiuPropertiesBean.getPrivateAccessExpireMinutes();

//        this.accessKey = "49-Ruxyz4EdBenYuZ91u6cPjPpHGfvVwN6t2HnPP";
//        this.secretKey = "h6J0HqpjXEJqx8xy4wXU8kfCgFQCXOXbdSp0SbfY";
//        this.publicBucketName = "nomi-public-dev";
//        this.publicBucketHost = this.qiNiuPropertiesBean.getPublicBucketHost();
//        this.privateBucketName = "nomi-private-dev";
//        this.privateBucketHost = this.qiNiuPropertiesBean.getPrivateBucketHost();
//        this.tokenExpireMinutes = this.qiNiuPropertiesBean.getTokenExpireMinute();
//        this.privateAccessTokenExpireMinutes = this.qiNiuPropertiesBean.getPrivateAccessExpireMinutes();getPrivateAccessExpireMinutes
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //初始化配置文件
        this.initConfigurations();
        //初始化七牛
        this.qiniuAuth = Auth.create(this.accessKey, this.secretKey);
        this.cfg = new Configuration(Zone.zone2());//华南
    }


    /**
     * 获取公有空间的上传token
     *
     * @return String
     */
    @Override
    public QiNiuTokenVO getPublicBucketUploadToken() {
        QiNiuTokenVO vo = new QiNiuTokenVO();
        //获取公有token
        Long tokenExpireSecond = 3600L;
        String publicToken = null;
        if (this.tokenExpireMinutes > 0) {
            tokenExpireSecond = this.tokenExpireMinutes * 60;
            publicToken = this.qiniuAuth.uploadToken(this.publicBucketName, null, this.tokenExpireMinutes * 60, null);
            if (StringUtils.isNoneBlank(publicToken)) {
                LOG.info("put publicToken cache: {}", publicToken);

            }
        } else {
            publicToken = this.qiniuAuth.uploadToken(this.publicBucketName);
            if (StringUtils.isNoneBlank(publicToken)) {
                LOG.info("put publicToken cache: {}", publicToken);
            }
        }
        vo.setToken(publicToken);
        vo.setTokenExpireSecond(tokenExpireSecond);
        return vo;
    }

    /**
     * 获取公有空间的上传token
     *
     * @param key 上传文件的唯一标识，可以包含路径
     * @return String
     */
    @Override
    public QiNiuTokenVO getPublicBucketUploadToken(String key) {
        QiNiuTokenVO vo = new QiNiuTokenVO();
        //获取公有token
        Long tokenExpireSecond = 3600L;
        //获取公有token
        String publicToken = null;
        if (StringUtils.isEmpty(key)) {
            return this.getPublicBucketUploadToken();
        } else {
            if (this.tokenExpireMinutes > 0) {
                publicToken = this.qiniuAuth.uploadToken(this.publicBucketName, key, this.tokenExpireMinutes * 60, null);
                tokenExpireSecond = this.tokenExpireMinutes * 60;
            } else {
                publicToken = this.qiniuAuth.uploadToken(this.publicBucketName, key);
                if (StringUtils.isNoneBlank(publicToken)) {
                    LOG.info("put publicToken cache: {}", publicToken);
                }
            }
        }
        vo.setToken(publicToken);
        vo.setTokenExpireSecond(tokenExpireSecond);
        return vo;
    }

    /**
     * 获取私有空间的上传token
     *
     * @return String
     */
    @Override
    public QiNiuTokenVO getPrivateBucketUploadToken() {
        QiNiuTokenVO vo = new QiNiuTokenVO();
        //获取私有token
        Long tokenExpireSecond = 3600L;
        //获取私有token
        String privateToken = null;
        if (this.tokenExpireMinutes > 0) {
            privateToken = this.qiniuAuth.uploadToken(this.privateBucketName, null, this.tokenExpireMinutes * 60, null);
            tokenExpireSecond = this.tokenExpireMinutes * 60;
        } else {
            privateToken = this.qiniuAuth.uploadToken(this.privateBucketName);
            if (StringUtils.isNoneBlank(privateToken)) {
                LOG.info("put publicToken cache: {}", privateToken);
            }
        }
        vo.setToken(privateToken);
        vo.setTokenExpireSecond(tokenExpireSecond);
        return vo;
    }

    /**
     * 获取公有空间的上传token
     *
     * @param key 上传文件的唯一标识，可以包含路径
     * @return String
     */
    @Override
    public QiNiuTokenVO getPrivateBucketUploadToken(String key) {
        QiNiuTokenVO vo = new QiNiuTokenVO();
        //获取私有token
        Long tokenExpireSecond = 3600L;
        //获取私有token
        String privateToken = null;
        if (StringUtils.isEmpty(key)) {
            return this.getPrivateBucketUploadToken();
        } else {
            if (this.tokenExpireMinutes > 0) {
                privateToken = this.qiniuAuth.uploadToken(this.privateBucketName, key, this.tokenExpireMinutes * 60, null);
                tokenExpireSecond = this.tokenExpireMinutes * 60;
            } else {
                privateToken = this.qiniuAuth.uploadToken(this.privateBucketName, key);
                if (StringUtils.isNoneBlank(privateToken)) {
                    LOG.info("put publicToken cache: {} - {}", privateToken, key);
                }
            }
        }
        vo.setToken(privateToken);
        vo.setTokenExpireSecond(tokenExpireSecond);
        return vo;
    }

    /**
     * 以文件流形式上传文件到公共空间
     *
     * @param key         上传文件的唯一标识，可以包含路径
     * @param inputStream 文件流
     * @return String 成功返回文件的key，失败返回null
     */
    @Override
    public String uploadFileToPublicBucket(String key, InputStream inputStream) {
        return this.uploadFile(key, inputStream, this.publicBucketName);
    }

    /**
     * 以文件流形式上传文件到私有空间
     *
     * @param key         上传文件的唯一标识，可以包含路径
     * @param inputStream 文件流
     * @return String 成功返回文件的key，失败返回null
     */
    @Override
    public String uploadFileToPrivateBucket(String key, InputStream inputStream) {
        return this.uploadFile(key, inputStream, this.privateBucketName);
    }

    /**
     * 以文件流形式上传文件
     *
     * @param key         上传文件的唯一标识，可以包含路径
     * @param inputStream 文件流
     * @param bucketName  空间名称
     * @return String 成功返回文件的key，失败返回null
     */
    protected String uploadFile(String key, InputStream inputStream, String bucketName) {
        if (StringUtils.isEmpty(key)) {
            key = RandomStringUtils.random(36);
            LOG.debug("uploadFile with key = " + key );
        }
        if (inputStream == null) {
            LOG.debug("uploadFile inputStream is empty");
            return null;
        }
        UploadManager uploadManager = new UploadManager(this.cfg);
        String uploadToken = this.qiniuAuth.uploadToken(bucketName, key, this.tokenExpireMinutes > 0 ? this.tokenExpireMinutes * 60 : 3600, null);
        try {
            Response response = uploadManager.put(inputStream, key, uploadToken, null, null);
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            return putRet == null || StringUtils.isEmpty(putRet.key) ? null : putRet.key;
        } catch (QiniuException ex) {
            LOG.error("Fail to upload file to qiniu due to handler found", ex);
            throw  new FInsuranceBaseException(106007);
        }
    }

    /**
     * 获取公有文件的访问地址
     *
     * @param key 上传文件的唯一标识，可以包含路径
     * @return 文件访问地址
     */
    @Override
    public String getPublicFileUrl(String key) {
        if (StringUtils.isEmpty(key)) {
            return "";
        }
        try {
            String encodedKey = URLEncoder.encode(key, "utf-8");
            return String.format("%s/%s", this.publicBucketHost, encodedKey);
        } catch (UnsupportedEncodingException e) {
            LOG.error("Fail to create public file access url", e);
            throw  new FInsuranceBaseException(106008);
        }
    }

    /**
     * 获取私有文件的访问地址
     *
     * @param key 上传文件的唯一标识，可以包含路径
     * @return 文件访问地址
     */
    @Override
    public String getPrivateFileUrl(String key, String model, String imageWidth, String imageHeight) {
        LOG.info("getPrivateFileUrl with key=[" + key + "],model=[" + model + "],imageWidth=[" + imageWidth + "],imageHeight=[" + imageHeight + "]");
        String privateFileUrl = null;
        String privateFileUrlKey = null;
        if (StringUtils.isEmpty(key)) {
            return "";
        }
        try {
            String encodedKey = URLEncoder.encode(key, "utf-8");

            if (StringUtils.isBlank(imageWidth)) {
                imageWidth = imageHeight;
            }
            if (StringUtils.isBlank(imageHeight)) {
                imageHeight = imageWidth;
            }

            if (StringUtils.isNoneBlank(model, imageWidth, imageHeight)) {
                privateFileUrlKey = String.format("%s/%s?imageView2/%s/w/%s/h/%s", this.privateBucketHost, encodedKey, model, imageWidth, imageHeight);
            } else {
                privateFileUrlKey = String.format("%s/%s", this.privateBucketHost, encodedKey);
            }

            privateFileUrl = this.qiniuAuth.privateDownloadUrl(privateFileUrlKey, this.privateAccessTokenExpireMinutes > 0 ? this.privateAccessTokenExpireMinutes * 60 : 3600);
            LOG.info("get privateDownloadUrl privateFileUrlKey=[" + privateFileUrlKey +"],privateFileUrl=["+ privateFileUrl+"]");
            if (StringUtils.isNoneBlank(privateFileUrl)) {
                LOG.info("put publicToken cache: %s - %s", privateFileUrlKey, privateFileUrl);
                return privateFileUrl;
            }
        } catch (Exception e) {
            LOG.error("getPrivateFileUrl failed", e);
            throw  new FInsuranceBaseException(106008);
        }
        return privateFileUrl;
    }

    @Override
    public String remoteGrabFile(String url, Integer isPublic, String key) {
        String fileUrl = null;
        if(isPublic != null && isPublic.equals(1) ){
            fileUrl = publicRemoteGrabFile(url, key);
        }else{
            fileUrl = privateRemoteGrabFile(url, key);
        }
        return fileUrl;
    }

    @Override
    public void deleteFile(String key, Integer isPublic) {
        String bucket = this.publicBucketName;
        if(isPublic == 0) {
            bucket = this.privateBucketName;
        }
        BucketManager bucketManager = new BucketManager(this.qiniuAuth, this.cfg);
        try {
            bucketManager.delete(bucket, key);
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            LOG.error("deleteFile failed", ex);
            throw new FInsuranceBaseException(106005);
        }
    }

    //公有同步上传文件
    private String publicRemoteGrabFile(String url, String key){
        //...其他参数参考类注释
        String bucket = this.publicBucketName;
        String remoteSrcUrl = url;
        BucketManager bucketManager = new BucketManager(this.qiniuAuth, this.cfg);
        String fileUrl = null;
        //抓取网络资源到空间
        try {
            FetchRet fetchRet = bucketManager.fetch(remoteSrcUrl, bucket, key);
            fileUrl = fetchRet.key;
        } catch (QiniuException ex) {
            LOG.error("publicRemoteGrabFile failed", ex);
            throw new FInsuranceBaseException(106006);
        }
        return fileUrl;
    }

    //私有同步上传文件
    private String privateRemoteGrabFile(String url, String key){
        //私有的空间
        String bucket = this.privateBucketName;
        BucketManager bucketManager = new BucketManager(this.qiniuAuth, this.cfg);
        String qiniukey = null;
        //抓取网络资源到空间
        try {
            FetchRet fetchRet = bucketManager.fetch(url, bucket, key);
            qiniukey = fetchRet == null ? null : fetchRet.key;
        } catch (QiniuException ex) {
            LOG.error("privateRemoteGrabFile failde", ex);
            throw new FInsuranceBaseException(106006);
        }
        return qiniukey;
    }

    //获取缓存的值
    private String getStringRedisTemplateValeus(String key){
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        return ops.get(key);
    }

    //保存缓存的值
    private void saveToRedisTemplateValeus(String key, String value){
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.set(key, value, BasicConstants.QINIU_EXPIRED_MINUTES, TimeUnit.MINUTES);
    }
}
