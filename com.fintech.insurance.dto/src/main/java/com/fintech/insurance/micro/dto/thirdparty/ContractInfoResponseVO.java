package com.fintech.insurance.micro.dto.thirdparty;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/11/25 13:42
 */
public class ContractInfoResponseVO {

    /**
     * 合同文件编号， 用于在上上签服务中获取合同文件的标识
     */
    private String contractFileNum;

    /**
     * 合同图片文件
     */
    private String contractFileQiniuId;

    /**
     * 合同PDF文件
     */
    private String contractPDFQiniuId;

    public ContractInfoResponseVO(String contractFileNum, String contractImageFileId, String contractPDFId) {
        this.contractFileNum = contractFileNum;
        this.contractFileQiniuId = contractImageFileId;
        this.contractPDFQiniuId = contractPDFId;
    }

    public ContractInfoResponseVO() {
    }

    public String getContractFileNum() {
        return contractFileNum;
    }

    public void setContractFileNum(String contractFileNum) {
        this.contractFileNum = contractFileNum;
    }

    public String getContractFileQiniuId() {
        return contractFileQiniuId;
    }

    public void setContractFileQiniuId(String contractFileQiniuId) {
        this.contractFileQiniuId = contractFileQiniuId;
    }

    public String getContractPDFQiniuId() {
        return contractPDFQiniuId;
    }

    public void setContractPDFQiniuId(String contractPDFQiniuId) {
        this.contractPDFQiniuId = contractPDFQiniuId;
    }
}
