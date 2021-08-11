package com.fintech.insurance.micro.biz.service.contract;

import com.fintech.insurance.commons.enums.UploadFileType;
import com.fintech.insurance.commons.exceptions.FInsuranceIOException;
import com.fintech.insurance.commons.utils.*;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.micro.biz.persist.dao.ContractDao;
import com.fintech.insurance.micro.biz.persist.dao.ProductRateDao;
import com.fintech.insurance.micro.biz.persist.dao.RequisitionDetailDao;
import com.fintech.insurance.micro.biz.persist.entity.Contract;
import com.fintech.insurance.micro.biz.persist.entity.RequisitionDetail;
import com.fintech.insurance.micro.dto.customer.CustomerVO;
import com.fintech.insurance.micro.dto.finance.FinanceRepaymentPlanVO;
import com.fintech.insurance.micro.dto.finance.RepaymentPlanWordVO;
import com.fintech.insurance.micro.dto.thirdparty.*;
import com.fintech.insurance.micro.feign.customer.CustomerServiceFeign;
import com.fintech.insurance.micro.feign.finance.RefundServiceFeign;
import com.fintech.insurance.micro.feign.thirdparty.BestsignServiceFeign;
import com.fintech.insurance.micro.feign.thirdparty.QiniuBusinessServiceFeign;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sun.misc.IOUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2018/1/6 13:35
 */
public abstract class AbstractContractGenerator implements ContractGeneratorService {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractContractGenerator.class);

    @Autowired
    protected ContractDao contractDao;

    @Autowired
    protected CustomerServiceFeign customerServiceFeign;

    @Autowired
    protected RequisitionDetailDao requisitionDetailDao;

    @Autowired
    protected BestsignServiceFeign bestsignServiceFeign;

    @Autowired
    protected ProductRateDao productRateDao;

    @Autowired
    protected RefundServiceFeign refundServiceFeign;

    @Autowired
    protected QiniuBusinessServiceFeign qiniuBusinessServiceFeign;

    @Value("${fintech.image.waterMark}")
    private String waterMarkText;

    protected String iconImageStr = ImageUtils.getImageContentString(this.getClass().getClassLoader().getResourceAsStream("nuomi_icon.png"));

    public ContractInfoResponseVO buildServiceContract(Integer contractId) {
        return generateFileHolder();
    }

    public ContractInfoResponseVO buildBorrowContract(Integer contractId) {
        return generateFileHolder();
    }

    // 获取客户信息
    protected CustomerVO getContractCustomer(Contract contract) {
        FintechResponse<CustomerVO> customerVOFintechResponse = customerServiceFeign.getCustomerAccountInfoById(contract.getRequisition().getCustomerAccountInfoId());
        if(!customerVOFintechResponse.isOk()){
            throw FInsuranceBaseException.buildFromErrorResponse(customerVOFintechResponse);
        }
        if(customerVOFintechResponse.getData() == null){
            throw new FInsuranceBaseException(107040);
        }
        return customerVOFintechResponse.getData();
    }

    // 获取车辆信息
    protected RequisitionDetail getCarInfo(Contract contract) {
        return requisitionDetailDao.getByRequisition_IdAndContract_Id(contract.getRequisition().getId(),
                contract.getId());
    }

    /**
     * 生成简单的合同占位文件
     * @return
     */
    private ContractInfoResponseVO generateFileHolder() {
        CustomerVO customerVO = new CustomerVO();
        customerVO.setMobile("tempID");
        return generateContractFileData(false, new HashMap<String, Object>(), "合同占位模板.ftl", 1, customerVO, false);
    }


    /**
     * 将上上签的合同文件转储到七牛云上， 并根据当前产品的状态决定是否需要把PDF文件生成图片并存储到七牛云
     * @param bestsignRemoteFile
     * @return
     */
    private ContractInfoResponseVO grabFileToQiniu(RemoteFileVO bestsignRemoteFile) {
        //1. 把上上签的合同PDF 文件转存的七牛云
        FintechResponse<QiniuFileVO> qiniuPDFResp = qiniuBusinessServiceFeign.remoteGrabFile(bestsignRemoteFile.getFileDownloadURL(), 0, UploadFileType.PDF.getCode());
        if (!qiniuPDFResp.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(qiniuPDFResp);
        }

        //2.1 将PDF文件转化为图片
        String tempImagePath = ImageUtils.pdfFileToIamgePdfbox(false, qiniuPDFResp.getData().getFileUrl(), waterMarkText);
        LOG.info("generated Image file path: {}" , tempImagePath);
        if (StringUtils.isBlank(tempImagePath)) {
            throw new FInsuranceBaseException(104528, new Object[]{bestsignRemoteFile.getFileDownloadURL()});
        }
        //2.2 将图片上传到七牛云上
        QiniuFileUploadVO qiniuFileUploadVO = new QiniuFileUploadVO();
        //qiniuFileUploadVO.setFileTempPath(tempImagePath);


        FileInputStream imageInput = null;
        try {
            imageInput = new FileInputStream(tempImagePath);
            qiniuFileUploadVO.setFileData(ImageUtils.readBytes(new FileInputStream(tempImagePath)));
        } catch (FileNotFoundException  e) {
            throw new FInsuranceIOException(e.getMessage());
        } finally {
            org.apache.commons.io.IOUtils.closeQuietly(imageInput);
        }

        qiniuFileUploadVO.setFileType(UploadFileType.IMAGES);
        qiniuFileUploadVO.setIsPublic(0);
        FintechResponse<String> qiniuImageResp = qiniuBusinessServiceFeign.uploadFileWithData(qiniuFileUploadVO);

        if (!qiniuImageResp.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(qiniuImageResp);
        }
        String imageFileId = qiniuImageResp.getData();

        return new ContractInfoResponseVO(bestsignRemoteFile.getFileId(), imageFileId, qiniuPDFResp.getData().getFileId());
    }

    /**
     * 根据指定的参数生成合同文件，并转存到七牛云
     *
     * 根据当前申请单的状态决定是否需要把合同(pdf)文件转成图片存储到七牛云
     *
     * @param valueMap
     * @param templateFileName
     * @param contractFilePageSize
     * @param customerVO 上上签文件ID 以及转存到七牛上的文件ID
     * @return
     */
    protected ContractInfoResponseVO generateContractFileData(boolean isSign, Map<String, Object> valueMap,
                                                              String templateFileName, Integer contractFilePageSize, CustomerVO customerVO, boolean isServiceContract) {
        // 生成word 合同文件
        byte[] contractWordData = WordUtil.createWordData(valueMap, templateFileName, this.getClass());

        WordFileVO wordFileVO = new WordFileVO();
        wordFileVO.setCustomerMobile(customerVO.getMobile());
        wordFileVO.setFileData(contractWordData);
        wordFileVO.setWordPages(contractFilePageSize); // 文件页数

        // 上传到上上签(word 转PDF)
        FintechResponse<RemoteFileVO> bestsignResponse = bestsignServiceFeign.createDraftContractByFileData(wordFileVO);
        if (!bestsignResponse.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(bestsignResponse);
        }
        String bestsignFileId = bestsignResponse.getData().getFileId();

        if (isSign) {
            // 是否需要签署
            ContractSignVO signInfo = isServiceContract ? this.buildServiceContractSignInfo(customerVO.getIdNum(), bestsignFileId) :
                    this.buildBorrowContractSignInfo(customerVO.getIdNum(), bestsignFileId);
            bestsignResponse = bestsignServiceFeign.signContract(signInfo);
            if (!bestsignResponse.isOk()) {
                throw FInsuranceBaseException.buildFromErrorResponse(bestsignResponse);
            }
        }

        // 将上上签的文件转储到七牛: 至于对于已经支付成功的申请单， 生成的合同不转成图片格式存储，保留原始PDF格式.
        // boolean isNeedConvertContractToImage = !(RequisitionStatus.WaitingLoan == currentRequisitonStatus || RequisitionStatus.Loaned == currentRequisitonStatus);
        return grabFileToQiniu(bestsignResponse.getData());
    }

    /**
     * 生成用于填充合同模板的还款计划
     * @param contractNumber
     * @return
     */
    protected List<RepaymentPlanWordVO> buildContractRepaymentPlans(String contractNumber, BigDecimal contractAmount) {
        FintechResponse<List<FinanceRepaymentPlanVO>> repaymentPlanResponse = refundServiceFeign.getListByContractNumber(contractNumber);
        if (!repaymentPlanResponse.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(repaymentPlanResponse);
        }
        /**
         * 说明：由于还款计划表和实际合同有差别，所以对读出的repaymentPlanList做一些处理
         * 主要原因是因为在计算还款计划的时候车险分期最后一期还款本金为0（抵消保证金）
         * 保证金是不允许写入合同的，所以设置最后一期应还本金为保证金，剩余本金为0.00
         */
        List<RepaymentPlanWordVO> wordVOs = new ArrayList<RepaymentPlanWordVO>();
        Double already = 0.0;
        Double special = CalculationFormulaUtils.getAssureMoney(contractAmount, repaymentPlanResponse.getData().size()).doubleValue();
        for (FinanceRepaymentPlanVO planVO : repaymentPlanResponse.getData()) {
            RepaymentPlanWordVO wordVO = new RepaymentPlanWordVO();
            wordVO.setInstalment(String.valueOf(planVO.getCurrentInstalment()));
            wordVO.setPayDay(DateCommonUtils.formatDate(planVO.getRepayDate()));
            wordVO.setPayCapital(NumberFormatorUtils.convertFinanceNumberToShowString(planVO.getRepayCapitalAmount()));
            wordVO.setPayInterest(NumberFormatorUtils.convertFinanceNumberToShowString(planVO.getRepayInterestAmount()));
            wordVO.setTotal(NumberFormatorUtils.convertFinanceNumberToShowString(planVO.getRepayCapitalAmount() + planVO.getRepayInterestAmount()));
            already += planVO.getRepayCapitalAmount();
            wordVO.setLeavingCapital(NumberFormatorUtils.convertFinanceNumberToShowString(contractAmount.doubleValue()-already));
            if (planVO.getCurrentInstalment() == planVO.getTotalInstalment()) {
                wordVO.setPayCapital(NumberFormatorUtils.convertFinanceNumberToShowString(special));
                wordVO.setLeavingCapital("0.00");
                wordVO.setTotal(NumberFormatorUtils.convertFinanceNumberToShowString(special + planVO.getRepayInterestAmount()));
            }
            wordVOs.add(wordVO);
        }
        return wordVOs;
    }

    protected abstract ContractSignVO buildBorrowContractSignInfo(String userAccountId, String bestsignFileId);

    protected abstract ContractSignVO buildServiceContractSignInfo(String userAccountId, String bestsignFileId);
}
