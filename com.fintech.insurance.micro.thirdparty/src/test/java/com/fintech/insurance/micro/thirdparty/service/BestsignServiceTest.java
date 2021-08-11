package com.fintech.insurance.micro.thirdparty.service;

import com.fintech.insurance.commons.utils.ImageUtils;
import com.fintech.insurance.micro.dto.thirdparty.SignLocationVO;
import com.fintech.insurance.micro.thirdparty.service.bestsign.*;
import com.fintech.insurance.micro.thirdparty.service.bestsign.enums.FileType;
import com.fintech.insurance.micro.thirdparty.service.bestsign.enums.IdentityType;
import com.fintech.insurance.micro.thirdparty.service.bestsign.enums.UserType;
import com.fintech.insurance.micro.thirdparty.service.bestsign.exceptions.BestsignBizException;
import com.fintech.insurance.micro.thirdparty.service.bestsign.exceptions.BestsignCertExistingException;
import com.fintech.insurance.micro.thirdparty.service.bestsign.exceptions.BestsignUserExistException;
import com.fintech.insurance.micro.thirdparty.service.bestsign.model.*;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/11/21 17:03
 */
@ActiveProfiles("junit")
public class BestsignServiceTest {

    private static final String PERSONAL_USER_ID = "62010419881219027X";
    private static final String ENTERPRISE_USER_ID = "620104197012080529";

    private BestsignService bestsignService;

    @Before
    public void setup(){
        bestsignService = new BestsignServiceImpl();
        BestsignPropertiesBean requestBean = new BestsignPropertiesBean();
        requestBean.setHost("https://openapi.bestsign.info/openapi/v3");
        requestBean.setDeveloperId("1862727489206354528");
        requestBean.setSignType("rsa");
        requestBean.setPrivateKey("MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKHeZ1tGMrj3EWrUWDGzPTml4QNa3Lh9/vlU+9kPB9G1E1zbTzxYZ7EmzEesEioWuqUGDx/wmMXsqNVI5HOiV1hzvCzhIFYy2S+wgAt8kEgifwhgRBn6qAO919bJ5G+T8e7yhKnZF1esk05SH7pYjd4x1Eso/UHZRpZa/yXBd+jTAgMBAAECgYAEQM3CbjPC/GruvamblLQVIbCp3+dQya67amo7p9NyxSk/FVwdn80JsJVJhNHtXS+GSoR3OGErQi6lfAbUqv1UgKxN35Xe3rPAVkurNxphVOCbvADW/32sGVnbz7UAb6pqrH3m4FNmuGqZeLd3lhVkxCvwEkfG3puFzk8oDPbwUQJBAP+K4XcEtl+bzvAibDwzuX+DNUt2ijKHVXDzaqGHUqvkfEAhGDYV3VND9Jfn+Legi0BCahFnDBCtX2JMcW7Wmj0CQQCiKJc/7OBCpUnZo0ULy2VrGEby0nemIe5fCR5SdqlxlsNvZ4cyRuW2LXjAUniNdHtx6UH9JaYd357DoWfkLJBPAkAbpzfG3Weu6Pl32wHDcgV82wIFbIp/9U01r+G2ISK9Hzii5/HqyGru+8eYOK4dkO4Awi8gOvp/Q4Oy63rK98YxAkEAjoovqbmGyA6TBARIxT1dQO5uLzRiiF57Mn7JcKNt/rMPx/WxGbjIY4NFCYl0/qLNgCwSHXvisY/H9x8CO8gQcQJAaiDJy//mXfvO3stPLZjYPdifBGY5xVdWpB/UrrLZbqw2gO0elkZImykrnDqYFu6rwNj+x7xpHuQ+LgD0yctpRg==");

        RequestUrlBuilder builder = new RequestUrlBuilder();
        builder.setBestsignProperties(requestBean);
        bestsignService.setRequestUrlBuilder(builder);
    }

    @Test(expected = BestsignUserExistException.class)
    public void testRegisterUserAccount() throws BestsignBizException, BestsignUserExistException {
        bestsignService.registerUserAccount(UserType.ENTERPRISE, ENTERPRISE_USER_ID, "张琦", "nomifinance@aliyun.com", "18566226286");
    }

    @Test(expected = BestsignBizException.class)
    public void testSetPersonalCredential() throws BestsignBizException {
        try {
            bestsignService.setPersonalCredential(PERSONAL_USER_ID, IdentityType.RESIDENT_ID_CARD, PERSONAL_USER_ID, "刘昱",
                    null, null, null, null, null);
        } catch (BestsignCertExistingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateUserSignatureImage() throws BestsignBizException, IOException {
        bestsignService.createUserSignatureImage(ENTERPRISE_USER_ID);
    }

    @Test
    public void testUploadUserSignatureImage() throws BestsignBizException, IOException {
        InputStream in = ClassLoader.getSystemResourceAsStream("user_image.png");
        String imageBase64String = org.springframework.util.Base64Utils.encodeToString(ImageUtils.readBytes(in));
        bestsignService.uploadUserSignatureImage(ENTERPRISE_USER_ID, imageBase64String, "test");
    }

    @Test
    public void testDownloadUserSignatureImage() throws BestsignBizException, IOException {
        String imageStr = bestsignService.downloadUserSignatureImage(ENTERPRISE_USER_ID, null);
        System.out.print(imageStr);
    }
    @Test
    public void testSetEnterpriseCredential() throws BestsignBizException {
        // 企业用户的申请证书之后 不能再对其进行设置企业信息
        bestsignService.setEnterpriseCredential(ENTERPRISE_USER_ID, "91130623MA09KXL757", "",
                "", "深圳市诺米金融服务有限公司", "测试N", IdentityType.RESIDENT_ID_CARD,
                ENTERPRISE_USER_ID, "13902443576", "13902443576",
                "test2@test.com", "广东", "深圳", "华富路");
    }

    @Test
    public void testGetPersonalUser() throws BestsignBizException {
        BestsignResponse<PersonalCredentialVO> response = bestsignService.getPersonalCredential(PERSONAL_USER_ID);
        Assert.assertEquals(PERSONAL_USER_ID, response.getData().getAccount());
    }

    @Test
    public void testGetEnterpriseUser() throws BestsignBizException {
        try {
            bestsignService.registerUserAccount(UserType.ENTERPRISE, ENTERPRISE_USER_ID, "深圳市诺米金融服务有限公司", "17707040708@178.com", "17707040708");
        } catch (BestsignUserExistException e) {
            e.printStackTrace();
        }

        bestsignService.setEnterpriseCredential(ENTERPRISE_USER_ID, "92410802MA44PT038M", "",
                "", "深圳市诺米金融服务有限公司", "测试3", IdentityType.RESIDENT_ID_CARD,
                ENTERPRISE_USER_ID, "17707040708", "17707040708",
                "test2@test.com", "广东", "深圳", "华富路");

        /*BestsignResponse<EnterpriseCredentialVO> response = bestsignService.getEnterpriseCredential("17707040704");
        Assert.assertEquals("91410305MA44JE1E05", response.getData().getRegCode());
        Assert.assertEquals("", response.getData().getOrgCode());
        Assert.assertEquals("", response.getData().getTaxCode());
        Assert.assertEquals("洛阳志骋房地产经纪有限公司", response.getData().getName());
        Assert.assertEquals("测试2", response.getData().getLegalPersonName());
        Assert.assertEquals(IdentityType.RESIDENT_ID_CARD, response.getData().getLegalPersonIdentityType());
        Assert.assertEquals("431102199010155111", response.getData().getLegalPersonIdentity());
        Assert.assertEquals("17707040704", response.getData().getLegalPersonMobile());
        Assert.assertEquals("17707040704", response.getData().getContactMobile());
        Assert.assertEquals("test2@test.com", response.getData().getContactMail());
        Assert.assertEquals("广东", response.getData().getProvince());
        Assert.assertEquals("深圳", response.getData().getCity());
        Assert.assertEquals("华富路", response.getData().getAddress());*/
    }

    @Test
    public void testApplyUserCertification() throws BestsignBizException {

        //企业用户
        try {
            BestsignResponse<UserCertVO> response = bestsignService.applyUserCertification(PERSONAL_USER_ID);
            System.out.println(response.getData().getCertNum());
        } catch (BestsignCertExistingException e) {
            // do nothing
        }
    }

    @Test
    public void testGetPersonalUserCertification() throws BestsignBizException {
        String expect = "CFCA-33-20171121185621221-05841";
        Assert.assertEquals(expect, bestsignService.getPersonalUserCertification(PERSONAL_USER_ID));
    }

    @Test
    public void testGetEnterpriseCertification() throws BestsignBizException {
        String expect = "ZJCA-11-20171121190321730-06154";
        Assert.assertEquals(expect, bestsignService.getEnterpriseCertification(ENTERPRISE_USER_ID));
    }

    @Test
    public void testUploadFile() throws BestsignBizException, IOException {
        InputStream stream = ClassLoader.getSystemResourceAsStream("bestsign_test_file.txt");
        String fileNum = bestsignService.uploadFile(PERSONAL_USER_ID, stream, FileType.DOC, "bestsign_test_file.txt", 1);
        Assert.assertNotNull(fileNum);
        System.out.println(fileNum);
    }

    @Test
    public void testGetDownloadFileURL() throws BestsignBizException, IOException {
        InputStream stream = ClassLoader.getSystemResourceAsStream("bestsign_test_file.txt");
        String fileNum = bestsignService.uploadFile(PERSONAL_USER_ID, stream, FileType.DOC, "bestsign_test_file.txt", 1);

        String downloadUrl = bestsignService.getFileDownloadURL(PERSONAL_USER_ID, fileNum);
        Assert.assertNotNull(downloadUrl);
        System.out.println(downloadUrl);
    }

    @Test
    public void testFileConvert() throws BestsignBizException, IOException {
        InputStream stream = ClassLoader.getSystemResourceAsStream("bestsign_test_convert.docx");
        String fileNum = bestsignService.uploadFile(PERSONAL_USER_ID, stream, FileType.DOC, "bestsign_test_convert.docx", 1);

        String pdfFileId = bestsignService.convertWordToPDF(PERSONAL_USER_ID, fileNum);
        Assert.assertNotNull(pdfFileId);

        String downloadUrl = bestsignService.getFileDownloadURL(PERSONAL_USER_ID, pdfFileId);
        System.out.println(downloadUrl);
    }


    @Test
    public void testCreateDraftContract() throws BestsignBizException, IOException {
        InputStream stream = ClassLoader.getSystemResourceAsStream("bestsign_test_convert.docx");
        String fileNum = bestsignService.uploadFile(PERSONAL_USER_ID, stream, FileType.DOC, "bestsign_test_convert.docx", 1);

        String wordUrl = bestsignService.getFileDownloadURL(PERSONAL_USER_ID, fileNum);
        System.out.println(wordUrl);

        String pdfFileId = bestsignService.convertWordToPDF(PERSONAL_USER_ID, fileNum);
        String contractNum = bestsignService.createDraftContract(PERSONAL_USER_ID, pdfFileId, 10, "test file ", null);

        Assert.assertNotNull(contractNum);

        List<String> signers = new ArrayList<String>();
        signers.add(PERSONAL_USER_ID);
        signers.add(ENTERPRISE_USER_ID);
        bestsignService.addSignersOnContract(contractNum, signers);

        List<SignLocationVO> signLocations = new ArrayList<SignLocationVO>();
        signLocations.add(new SignLocationVO(1, 0.4, 0.4));
        signLocations.add(new SignLocationVO(1, 0.6, 0.6));
        bestsignService.signContract(PERSONAL_USER_ID, contractNum, signLocations);
        bestsignService.signContract(ENTERPRISE_USER_ID, contractNum, signLocations);
        bestsignService.finishContract(contractNum);

        DownloadContractVO vo = bestsignService.getContractDownloadURL(contractNum);
        System.out.println(BestsignUtil.toJsonString(vo));
    }



}
