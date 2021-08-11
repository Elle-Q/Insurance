package com.fintech.insurance.micro.vo.wechat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: qxy
 * @Description: 渠道端我的首页初始化
 * @Date: 2017/12/8 14:03
 */
public class MyVO implements Serializable{

    // 渠道申请单状态数量统计
    List<CountRequisitionByStatusVO> countRequisitionVOList = new ArrayList<>();

    // 当前登录渠道用户
    LoginChannelUserVO loginChannelUserVO;

    // 当前登录渠道用户
    LoginCustomerUserVO loginCustomerUserVO;

    public LoginCustomerUserVO getLoginCustomerUserVO() {
        return loginCustomerUserVO;
    }

    public void setLoginCustomerUserVO(LoginCustomerUserVO loginCustomerUserVO) {
        this.loginCustomerUserVO = loginCustomerUserVO;
    }

    public List<CountRequisitionByStatusVO> getCountRequisitionVOList() {
        return countRequisitionVOList;
    }

    public void setCountRequisitionVOList(List<CountRequisitionByStatusVO> countRequisitionVOList) {
        this.countRequisitionVOList = countRequisitionVOList;
    }

    public LoginChannelUserVO getLoginChannelUserVO() {
        return loginChannelUserVO;
    }

    public void setLoginChannelUserVO(LoginChannelUserVO loginChannelUserVO) {
        this.loginChannelUserVO = loginChannelUserVO;
    }
}

