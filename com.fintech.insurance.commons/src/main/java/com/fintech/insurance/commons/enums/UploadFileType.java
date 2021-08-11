package com.fintech.insurance.commons.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fintech.insurance.commons.constants.CDNStorageConstant;

/**
 * OSS上传支持的文件类型
 * 
 * @author yongneng liu
 * @since 2017/11/11
 * @version 1.0.0
 */
public enum UploadFileType {

	NEWS("news", "news", "新闻公告文件"),
	OP_REPORTS("op_reports","runReports", "运营报告文件"),
	IMAGES("images", "image", "图片资源"),
	REPAY_ATTACHMENTS("repay_attachments", "platformRepay", "回款确认附件资源"),
	CENSORED("censored","censored", "审核资源"),
	PUSH_FILES("push_files", "apkMsgPush", "消息推送网页"),
	PDF("pdf","pdf", "PDF文件资源"),;

	private String code;
	private String folder;
	private String description;
	
	private UploadFileType(String code,String folder, String description) {
		this.code = code;
		this.folder = folder;
		this.description = description;
	}

	@JsonValue
	public String getCode() {
		return code;
	}

	public String getDescription() {
		return this.description;
	}

	public String getFolder() {
		return folder;
	}

	/**
	 * 获取上传文件的类型
	 * @param code
	 * @return
	 */
	public static UploadFileType codeOf(String code) {
		for (UploadFileType type : UploadFileType.values()) {
			if (type.code.equals(code)) {
				return type;
			}
		}
		throw new IllegalStateException("Not found the mapping UploadFileType for code:" + code);
	}

	@Override
	public String toString() {
		return this.code;
	}
}
