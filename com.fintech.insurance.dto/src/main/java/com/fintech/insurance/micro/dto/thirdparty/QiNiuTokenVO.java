package com.fintech.insurance.micro.dto.thirdparty;

import java.io.Serializable;

/**
 * OSS上传令牌对象
 * 
 * @author Sean Zhang
 * @since 2017/05/23
 * @version 1.0.0
 */
public class QiNiuTokenVO implements Serializable {

	private static final long serialVersionUID = -8802792841551182309L;

	/**
	 * 七牛token
	 */
	private String token;
	
	/**
	 * token过期的时间秒
	 */
	private Long tokenExpireSecond;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getTokenExpireSecond() {
		return tokenExpireSecond;
	}

	public void setTokenExpireSecond(Long tokenExpireSecond) {
		this.tokenExpireSecond = tokenExpireSecond;
	}
}
