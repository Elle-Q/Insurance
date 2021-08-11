package com.fintech.insurance.micro.thirdparty.controller;

import com.fintech.insurance.commons.enums.ContractSignUserType;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.cache.RedisSequenceFactory;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.BaseFintechController;
import com.fintech.insurance.micro.api.thirdparty.BestsignServiceAPI;
import com.fintech.insurance.micro.dto.thirdparty.*;
import com.fintech.insurance.micro.thirdparty.service.BestsignService;
import com.fintech.insurance.micro.thirdparty.service.bestsign.BestsignPropertiesBean;
import com.fintech.insurance.micro.thirdparty.service.bestsign.BestsignResponse;
import com.fintech.insurance.micro.thirdparty.service.bestsign.enums.FileType;
import com.fintech.insurance.micro.thirdparty.service.bestsign.enums.IdentityType;
import com.fintech.insurance.micro.thirdparty.service.bestsign.enums.UserType;
import com.fintech.insurance.micro.thirdparty.service.bestsign.exceptions.BestsignBizException;
import com.fintech.insurance.micro.thirdparty.service.bestsign.exceptions.BestsignCertExistingException;
import com.fintech.insurance.micro.thirdparty.service.bestsign.exceptions.BestsignUserExistException;
import com.fintech.insurance.micro.thirdparty.service.bestsign.model.DownloadContractVO;
import com.fintech.insurance.micro.thirdparty.service.bestsign.model.UserCertVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 上上签合同接口
 * @Author: Yong Li
 * @Date: 2017/11/25 10:55
 */
@RestController
public class BestsignController extends BaseFintechController implements BestsignServiceAPI {

    private static final int DEFAULT_DRAFT_CONTRACT_EXPIRED_DAYS = 10;

    private static final Logger LOG = LoggerFactory.getLogger(BestsignController.class);

    private static final String REDIS_KEY_CONTRACT_VERCODE = "contract_sequence";

    @Autowired
    private BestsignPropertiesBean bestsignProperties;

    @Autowired
    private BestsignService bestsignService;

    @Autowired
    private RedisSequenceFactory redisSequenceFactory;

    @Autowired
    private QiniuController qiniuController;

    @Override
    public FintechResponse<String> createPersonalUserAccount(@Validated @RequestBody BestsignUserInfoVO userInfoVO) {
        LOG.info("Create User: " + userInfoVO.toString());

        try {
            try {
                bestsignService.registerUserAccount(UserType.PERSONAL, userInfoVO.getUserIdentityNum(), userInfoVO.getUserName(),
                        userInfoVO.getMail(), userInfoVO.getMobile());
                LOG.info("create account: " + userInfoVO.getUserIdentityNum());
            } catch (BestsignUserExistException e) {
                // user exists.  do nothing.
                LOG.info("account:" + userInfoVO.getUserIdentityNum() + " exists. skip." );
            }

            //设置用户账户的证件信息
            try {
                bestsignService.setPersonalCredential(userInfoVO.getUserIdentityNum(), IdentityType.RESIDENT_ID_CARD,
                        userInfoVO.getUserIdentityNum(), userInfoVO.getUserName(), userInfoVO.getMail(), userInfoVO.getMobile(),
                        userInfoVO.getProvinceOfAddress(), userInfoVO.getCityOfAddress(), userInfoVO.getDetailAddress());
                LOG.info("set user credential success. ");
            } catch (BestsignCertExistingException e) {
                // 用户已经申请过证书， 不能设置证件信息
                LOG.info("user had applied certification, can not allow to set credential.");
                // skip
            }


            //申请证书
            try {
                BestsignResponse<UserCertVO> userCert = bestsignService.applyUserCertification(userInfoVO.getUserIdentityNum());
                return FintechResponse.responseData(String.format("%s-%s", userCert.getData().getCertType(), userCert.getData().getCertNum()));
            } catch (BestsignCertExistingException ex) {
                //该用户账户已经申请到过证书
                return FintechResponse.responseData(bestsignService.getPersonalUserCertification(userInfoVO.getUserIdentityNum()));
            }
        } catch (BestsignBizException e) {
            LOG.error("register user failed, " + e.getMessage(), e);
            throw new FInsuranceBaseException(106311, new Object[]{userInfoVO.getUserIdentityNum(), e.getMessage()});
        }
    }

    @Override
    public FintechResponse<String> createDraftContract(@Validated @RequestBody ContractCreationVO creationVO) {
        //生成合同模板
        String templateFileName = String.format("%s.docx", creationVO.getContractTemplateName());
        InputStream stream = ClassLoader.getSystemResourceAsStream(templateFileName);
        if (null == stream) {
//            throw new IOException("Failed to get the template file:" + templateFileName);
            throw new FInsuranceBaseException(106315, new Object[]{templateFileName});
        }
        try {
            File file = ResourceUtils.getFile("classpath:" + templateFileName);
            LOG.error("createDraftContract file:" + file == null ? null : file.getAbsolutePath());
        } catch (FileNotFoundException e) {
            LOG.error("createDraftContract failed with templateFileName=[" + templateFileName +"]" ,e);
        }
        try {
            //把合同word版内容文件上传到上上签
            String contractWordNum = bestsignService.uploadFile(bestsignProperties.getEnterpriseAccount(), stream, FileType.DOC,
                    String.format("%s-%s.%s", creationVO.getMobile(), redisSequenceFactory.generate(REDIS_KEY_CONTRACT_VERCODE), FileType.DOC.getCode()),
                    2);

            return FintechResponse.responseData(this.convertWordToPDFFormat(contractWordNum));
        } catch (BestsignBizException e) {
            LOG.error("Failed to generate user contract file content, " + e.getMessage(), e);
            throw new FInsuranceBaseException(106312, new Object[]{creationVO.getUserAccountId(), e.getMessage()});
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
            throw new FInsuranceBaseException(106315, new Object[]{e.getMessage()});
        }
    }

    @Override
    public FintechResponse<RemoteFileVO> createDraftContractByFileData(@Validated @RequestBody WordFileVO wordFileVO) {
        String fileId = this.createDraftPDFContract(wordFileVO);
        String fileDownloadURL = null;
        try {
            fileDownloadURL = bestsignService.getFileDownloadURL(bestsignProperties.getEnterpriseAccount(), fileId);
            return FintechResponse.responseData(new RemoteFileVO(fileId, fileDownloadURL));
        } catch (BestsignBizException e) {
            throw new FInsuranceBaseException(106315, new Object[]{wordFileVO.getCustomerMobile(), e.getMessage()});
        }
    }

    /*@Override
    public FintechResponse<RemoteFileVO> signContractByFileData(@Validated @RequestBody WordFileVO wordFileVO) {
        String fileId = this.createDraftPDFContract(wordFileVO);
        String fileDownloadURL = null;
        try {
            fileDownloadURL = bestsignService.getFileDownloadURL(bestsignProperties.getEnterpriseAccount(), fileId);


        } catch (BestsignBizException e) {
            throw new FInsuranceBaseException(106315, new Object[]{wordFileVO.getCustomerMobile(), e.getMessage()});
        }
    }*/

    /**
     * 根据word文件在上上签服务器上创建PDF格式的草稿合同， 并返回文件ID
     * @param wordFileVO
     * @return
     */
    private String createDraftPDFContract(WordFileVO wordFileVO) {
        String contractWordNum = null;
        try {
            contractWordNum = bestsignService.uploadFile(bestsignProperties.getEnterpriseAccount(), wordFileVO.getFileData(), FileType.DOC,
                    String.format("%s-%s.%s", wordFileVO.getCustomerMobile(), redisSequenceFactory.generate(REDIS_KEY_CONTRACT_VERCODE), FileType.DOC.getCode()),
                    wordFileVO.getWordPages());

            return this.convertWordToPDFFormat(contractWordNum);
        } catch (BestsignBizException e) {
            LOG.error("Failed to generate user contract file content, " + e.getMessage(), e);
            throw new FInsuranceBaseException(106312, new Object[]{wordFileVO.getCustomerMobile(), e.getMessage()});
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
            throw new FInsuranceBaseException(106315, new Object[]{e.getMessage()});
        }
    }

    private String convertWordToPDFFormat(String contractWordNum) {
        try {
            //利用上上签的在线格式转化服务转成PDF
            return bestsignService.convertWordToPDF(bestsignProperties.getEnterpriseAccount(), contractWordNum);

            //将合同内容文件转存到七牛云
            //String contractDownloadUrl = bestsignService.getFileDownloadURL(bestsignProperties.getEnterpriseAccount(), pdfFileId);
            //FintechResponse<String> qiniuResult = qiniuController.remoteGrabFile(contractDownloadUrl, 0, UploadFileType.PDF.getCode());

            /*//设置响应结果
            ContractInfoResponseVO result = new ContractInfoResponseVO();
            result.setContractFileNum(pdfFileId);
            result.setContractFileQiniuId(qiniuResult.getData());
            return result;*/
        } catch (BestsignBizException e) {
            LOG.error("Failed to generate user contract file content, " + e.getMessage(), e);
            throw new FInsuranceBaseException(106316, new Object[]{e.getMessage()});
        }
    }


    @Override
    public FintechResponse<RemoteFileVO> signContract(@Validated @RequestBody ContractSignVO signInfo) {
        String contractNum = null;
        try {
            //创建合同
            contractNum = bestsignService.createDraftContract(bestsignProperties.getEnterpriseAccount(), signInfo.getContractFileNum(),
                    DEFAULT_DRAFT_CONTRACT_EXPIRED_DAYS, signInfo.getTitle(), signInfo.getDescription());

            //设置签署方
            List<String> signers = new ArrayList<String>();
            signers.add(signInfo.getUserAccountId());
            if (signInfo.getContractSignUserType() == ContractSignUserType.ENTERPRISE) {
                signers.add(bestsignProperties.getEnterpriseAccount());
            } else if (signInfo.getContractSignUserType() == ContractSignUserType.LENDER) { //出借人签署
                signers.add(bestsignProperties.getLenderOwnerAccount());
            } else {
                throw new NotImplementedException();
            }

            bestsignService.addSignersOnContract(contractNum, signers);

            //客户签名
            //List<SignLocationVO> userSignLocations = new ArrayList<SignLocationVO>();
            //userSignLocations.add(new SignLocationVO(2, 0.4, 0.4));
            bestsignService.signContract(signInfo.getUserAccountId(), contractNum, signInfo.getUserSignLocations());

            //企业签名
            //List<SignLocationVO> enterpriseSignLocations = new ArrayList<SignLocationVO>();
            //enterpriseSignLocations.add(new SignLocationVO(2, 0.4, 0.4));
            if (signInfo.getContractSignUserType() == ContractSignUserType.ENTERPRISE) {
                bestsignService.signContract(bestsignProperties.getEnterpriseAccount(), contractNum, signInfo.getEnterpriseSignLocations());
            } else if (signInfo.getContractSignUserType() == ContractSignUserType.LENDER) { //出借人签署
                bestsignService.signContract(bestsignProperties.getLenderOwnerAccount(), contractNum, signInfo.getEnterpriseSignLocations());
            } else {
                throw new NotImplementedException();
            }

            //结束合同
            bestsignService.finishContract(contractNum);

            /*//转存到七牛云
            DownloadContractVO vo = bestsignService.getContractDownloadURL(contractNum);
            FintechResponse<String> qiniuResult = qiniuController.remoteGrabFile(vo.getContractList().get(0).getDownloadUrl(), 0, UploadFileType.PDF.getCode());

            //设置响应结果
            ContractInfoResponseVO result = new ContractInfoResponseVO();
            result.setContractFileNum(contractNum);
            result.setContractFileQiniuId(qiniuResult.getData());*/

            DownloadContractVO downloadContractVO = bestsignService.getContractDownloadURL(contractNum);

            //String fileDownloadURL = bestsignService.getFileDownloadURL(bestsignProperties.getEnterpriseAccount(), contractNum);
            return FintechResponse.responseData(new RemoteFileVO(contractNum, downloadContractVO.getContractList().get(0).getDownloadUrl()));
        } catch (BestsignBizException e) {
            LOG.error("Failed to sign contract, " + e.getMessage(), e);
            throw new FInsuranceBaseException(106313, new Object[]{signInfo.getUserAccountId(), e.getMessage()});
        }
    }

    @Override
    public FintechResponse<String> getFileDownloadURL(String fileId) {
        try {
            return FintechResponse.responseData(bestsignService.getFileDownloadURL(bestsignProperties.getEnterpriseAccount(), fileId));
        } catch (BestsignBizException e) {
            throw new FInsuranceBaseException(106317, new Object[]{fileId});
        }
    }
}
