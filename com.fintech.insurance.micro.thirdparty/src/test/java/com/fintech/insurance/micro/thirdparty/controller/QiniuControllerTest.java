package com.fintech.insurance.micro.thirdparty.controller;

import com.fintech.insurance.commons.enums.UploadFileType;
import com.fintech.insurance.micro.dto.thirdparty.QiNiuTokenVO;
import com.fintech.insurance.micro.thirdparty.service.QiniuCloudService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Description: (some words)
 * @Author: yongneng liu
 * @Date: 2017/11/15 0015 14:00
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class QiniuControllerTest {

    @Autowired
    QiniuCloudService qiniuService;

    @Test
    public void getQiniuUploadToken() throws Exception {
        String fileType = UploadFileType.IMAGES.getCode();
        UploadFileType targetType = UploadFileType.codeOf(fileType);
        Integer isPublic = 0;
        QiNiuTokenVO token = null;
        if (isPublic == 0) {
            token = this.qiniuService.getPrivateBucketUploadToken();
        } else {
            token = this.qiniuService.getPublicBucketUploadToken();
        }
        //System.out.print(token);
    }

    @Test
    public void getQiniuPrivateDownloadUrl() throws Exception {
    }

    @Test
    public void fromUploadFile() throws Exception {
    }

    @Test
    public void uploadFile() throws Exception {
    }

}