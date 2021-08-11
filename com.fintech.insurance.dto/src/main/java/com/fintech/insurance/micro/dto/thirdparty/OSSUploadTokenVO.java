package com.fintech.insurance.micro.dto.thirdparty;

import java.io.Serializable;

/**
 * OSS上传令牌对象
 * 
 * @author Sean Zhang
 * @since 2017/05/23
 * @version 1.0.0
 */
public class OSSUploadTokenVO implements Serializable {

	private static final long serialVersionUID = -6802792841551182309L;

	/**
	 * OSS access id
	 */
	private String accessId;
	
	/**
	 * 上传策略，base64编码
	 */
	private String policy;
	
	/**
	 * 签名
	 */
	private String signature;
	
	/**
	 * 文件上传到的目录
	 */
	private String directory;
	
	/**
	 * 上传地址
	 */
	private String host;
	
	/**
	 * token过期时间，单位为unix时间戳
	 */
	private int expireTime;

	public String getAccessId() {
		return accessId;
	}

	public void setAccessId(String accessId) {
		this.accessId = accessId;
	}

	public String getPolicy() {
		return policy;
	}

	public void setPolicy(String policy) {
		this.policy = policy;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(int expireTime) {
		this.expireTime = expireTime;
	}
}
