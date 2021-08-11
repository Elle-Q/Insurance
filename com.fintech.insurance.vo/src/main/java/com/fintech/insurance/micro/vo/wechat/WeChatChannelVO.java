
package com.fintech.insurance.micro.vo.wechat;


import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class WeChatChannelVO implements Serializable {

    //渠道id
    private Integer id;

    //渠道编号，创建时自动生成，全局唯一
    private String channelCode;

    //渠道名称
    private String channelName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
}

