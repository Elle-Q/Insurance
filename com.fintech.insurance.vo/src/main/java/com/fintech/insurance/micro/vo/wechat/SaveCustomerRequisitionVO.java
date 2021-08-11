package com.fintech.insurance.micro.vo.wechat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

//业务单绑定渠道vo
public class SaveCustomerRequisitionVO implements Serializable{
    //业务id
    private Integer id;

    //渠道code
    @NotNull(message = "107036")
    private String channelCode;

    //产品id
    @NotNull(message = "104102")
    private Integer productId;

    //用户账户info id
    private Integer customerAccountInfoId = 0;

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

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getCustomerAccountInfoId() {
        return customerAccountInfoId;
    }

    public void setCustomerAccountInfoId(Integer customerAccountInfoId) {
        this.customerAccountInfoId = customerAccountInfoId;
    }
}
