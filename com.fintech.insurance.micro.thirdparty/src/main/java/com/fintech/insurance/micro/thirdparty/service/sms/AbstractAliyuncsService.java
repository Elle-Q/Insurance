package com.fintech.insurance.micro.thirdparty.service.sms;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: (some words)
 * @Author: East
 * @Date: 2017/11/13 0013 21:46
 */
public abstract class AbstractAliyuncsService extends AbstractSMSService implements InitializingBean {

    @Autowired
    private AliyuncsProperties aliyuncsProperties;

    /**
     * 产品名称：云通信短信API产品,开发者无需替换
     */
    private String smsProduct = "Dysmsapi";
    /**
     * 产品域名，开发者无需替换
     */
    private String smsDomain = "dysmsapi.aliyuncs.com";
    /**
     * aliyun云服务所属的地域 ID，暂不支持region化
     */
    private String regionId = "cn-shenzhen";
    /**
     * 秘钥Key
     */
    private String accessKeyId = "LTAIeTtdol5Nw1ld";
    /**
     * 秘钥
     */
    private String accessKeySecret = "MHIBhnJelQrbDQ8GyFUfO2W3ceypBB";

    private IAcsClient acsClient;


    public IAcsClient getAcsClient() {
        return acsClient;
    }

    public void setAcsClient(IAcsClient acsClient) {
        this.acsClient = acsClient;
    }

    public AliyuncsProperties getAliyuncsProperties() {
        return aliyuncsProperties;
    }

    public void setAliyuncsProperties(AliyuncsProperties aliyuncsProperties) {
        this.aliyuncsProperties = aliyuncsProperties;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //初始化短信配置
        super.afterPropertiesSet();

        //初始化aliyun环境配置
        if (StringUtils.isBlank(this.aliyuncsProperties.getAccessKeyId())) {
            this.accessKeyId = this.aliyuncsProperties.getAccessKeyId();
        }
        if (StringUtils.isBlank(this.aliyuncsProperties.getAccessKeySecret())) {
            this.accessKeySecret = this.aliyuncsProperties.getAccessKeySecret();
        }

        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        DefaultProfile.addEndpoint(this.regionId, this.regionId, this.smsProduct, this.smsDomain);
        IClientProfile profile = DefaultProfile.getProfile(this.regionId, this.accessKeyId, this.accessKeySecret);
        this.acsClient = new DefaultAcsClient(profile);
    }
}
