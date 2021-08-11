package com.fintech.insurance.micro.dto.thirdparty;

import com.fintech.insurance.commons.enums.ContractSignUserType;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 签署合同时的请求数据封装
 * @Author: Yong Li
 * @Date: 2017/11/25 14:05
 */
public class ContractSignVO {

    /**
     * 合同签署时的用户账号，即为客户的身份证号码
     */
    @NotNull(message="106301")
    private String userAccountId;

    /**
     * 之前生成的合同内容文件的上上签文件编号
     */
    @NotNull(message="106301")
    private String contractFileNum;

    /**
     * 合同标题 可以将自己的业务合同编号、或合同标题放此处
     */
    @NotNull(message="106301")
    private String title;

    private ContractSignUserType contractSignUserType = ContractSignUserType.ENTERPRISE;

    /**
     * 合同描述
     */
    private String description;

    private List<SignLocationVO> userSignLocations = new ArrayList<SignLocationVO>();

    private List<SignLocationVO>  enterpriseSignLocations = new ArrayList<SignLocationVO>();

    public List<SignLocationVO> getUserSignLocations() {
        return userSignLocations;
    }

    public void setUserSignLocations(List<SignLocationVO> userSignLocations) {
        this.userSignLocations = userSignLocations;
    }

    public List<SignLocationVO> getEnterpriseSignLocations() {
        return enterpriseSignLocations;
    }

    public void setEnterpriseSignLocations(List<SignLocationVO> enterpriseSignLocations) {
        this.enterpriseSignLocations = enterpriseSignLocations;
    }

    public String getUserAccountId() {
        return userAccountId;
    }

    public void setUserAccountId(String userAccountId) {
        this.userAccountId = userAccountId;
    }

    public String getContractFileNum() {
        return contractFileNum;
    }

    public void setContractFileNum(String contractFileNum) {
        this.contractFileNum = contractFileNum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ContractSignUserType getContractSignUserType() {
        return contractSignUserType;
    }

    public void setContractSignUserType(ContractSignUserType contractSignUserType) {
        this.contractSignUserType = contractSignUserType;
    }
}
