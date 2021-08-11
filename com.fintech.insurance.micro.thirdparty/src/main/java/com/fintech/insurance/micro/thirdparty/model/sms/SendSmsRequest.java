/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.fintech.insurance.micro.thirdparty.model.sms;

import com.aliyuncs.RpcAcsRequest;

/**
 * @author auto create
 */
public class SendSmsRequest extends RpcAcsRequest<SendSmsResponse> {
    /**
     * 短信模板ID
     */
    private String templateCode;
    /**
     * 短信接收号码,支持以逗号分隔的形式进行批量调用，
     * 批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,
     * 验证码类型的短信推荐使用单条调用的方式
     */
    private String phoneNumbers;

    /**
     * 短信模板变量替换JSON串,友情提示:如果JSON中需要带换行符,请参照标准的JSON协议。
     */
    private String templateParam;

    /**
     * 上行短信扩展码,无特殊需要此字段的用户请忽略此字段
     */
    private String smsUpExtendCode;
    /**
     * 外部流水扩展字段
     */
    private String outId;
    /**
     * 短信签名
     */
    private String signName;

    private String resourceOwnerAccount;
    private Long resourceOwnerId;
    private Long ownerId;

    public SendSmsRequest() {
        super("Dysmsapi", "2017-05-25", "SendSms");
    }

    public String getTemplateCode() {
        return this.templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
        if (templateCode != null) {
            putQueryParameter("TemplateCode", templateCode);
        }
    }

    public String getPhoneNumbers() {
        return this.phoneNumbers;
    }

    public void setPhoneNumbers(String phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
        if (phoneNumbers != null) {
            putQueryParameter("PhoneNumbers", phoneNumbers);
        }
    }

    public String getSignName() {
        return this.signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
        if (signName != null) {
            putQueryParameter("SignName", signName);
        }
    }

    public String getResourceOwnerAccount() {
        return this.resourceOwnerAccount;
    }

    public void setResourceOwnerAccount(String resourceOwnerAccount) {
        this.resourceOwnerAccount = resourceOwnerAccount;
        if (resourceOwnerAccount != null) {
            putQueryParameter("ResourceOwnerAccount", resourceOwnerAccount);
        }
    }

    public String getTemplateParam() {
        return this.templateParam;
    }

    public void setTemplateParam(String templateParam) {
        this.templateParam = templateParam;
        if (templateParam != null) {
            putQueryParameter("TemplateParam", templateParam);
        }
    }

    public Long getResourceOwnerId() {
        return this.resourceOwnerId;
    }

    public void setResourceOwnerId(Long resourceOwnerId) {
        this.resourceOwnerId = resourceOwnerId;
        if (resourceOwnerId != null) {
            putQueryParameter("ResourceOwnerId", resourceOwnerId.toString());
        }
    }

    public Long getOwnerId() {
        return this.ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
        if (ownerId != null) {
            putQueryParameter("OwnerId", ownerId.toString());
        }
    }

    public String getSmsUpExtendCode() {
        return this.smsUpExtendCode;
    }

    public void setSmsUpExtendCode(String smsUpExtendCode) {
        this.smsUpExtendCode = smsUpExtendCode;
        if (smsUpExtendCode != null) {
            putQueryParameter("SmsUpExtendCode", smsUpExtendCode);
        }
    }

    public String getOutId() {
        return this.outId;
    }

    public void setOutId(String outId) {
        this.outId = outId;
        if (outId != null) {
            putQueryParameter("OutId", outId);
        }
    }

    @Override
    public Class<SendSmsResponse> getResponseClass() {
        return SendSmsResponse.class;
    }

    @Override
    public String toString() {
        return "SendSmsRequest{" +
                "templateCode='" + templateCode + '\'' +
                ", phoneNumbers='" + phoneNumbers + '\'' +
                ", signName='" + signName + '\'' +
                ", resourceOwnerAccount='" + resourceOwnerAccount + '\'' +
                ", templateParam='" + templateParam + '\'' +
                ", resourceOwnerId=" + resourceOwnerId +
                ", ownerId=" + ownerId +
                ", smsUpExtendCode='" + smsUpExtendCode + '\'' +
                ", outId='" + outId + '\'' +
                '}';
    }
}
