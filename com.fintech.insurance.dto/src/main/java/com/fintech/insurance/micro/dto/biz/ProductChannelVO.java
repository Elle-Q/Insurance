
package com.fintech.insurance.micro.dto.biz;

import com.fintech.insurance.micro.dto.validate.groups.Save;
import com.fintech.insurance.micro.dto.validate.groups.Update;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 产品渠道VO
 *
 * @author liu man
 * @version 1.1.0
 * @since 2017-08-18 17:04:30
 */


public class ProductChannelVO implements Serializable {

    //产品id
    @NotNull(groups = {Update.class, Save.class}, message = "104107")
    private Integer productId;

    //渠道id
    @NotNull(groups = {Update.class, Save.class}, message = "104107")
    private Integer channelId;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }
}

