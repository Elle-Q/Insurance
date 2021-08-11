package com.fintech.insurance.micro.dto.biz;


import com.fintech.insurance.commons.enums.ProductType;
import java.io.Serializable;

/**
 * @Author: Man LIU
 * @Description: 合同文件请求参数vo
 * @Date: 2017/11/9 18:22
 */
public class ContractFileRequestVO implements Serializable {

    //用户账户id
    private String userAccountId;

    //客户手机号码， 由于一个用户可能会有多个手机号码
    private String mobile;

    //产品类型
    private ProductType productType;

    //业务id
    private Integer requisitionId;

    public String getUserAccountId() {
        return userAccountId;
    }

    public void setUserAccountId(String userAccountId) {
        this.userAccountId = userAccountId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }


    public Integer getRequisitionId() {
        return requisitionId;
    }

    public void setRequisitionId(Integer requisitionId) {
        this.requisitionId = requisitionId;
    }

}