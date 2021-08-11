package com.fintech.insurance.micro.thirdparty.service.bestsign;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/11/17 14:15
 */

@ActiveProfiles("junit")
@RunWith(PowerMockRunner.class)
public class RequestUrlBuilderTest {

    @Before
    public void init() {
        PowerMockito.mockStatic(BestsignUtil.class);
        PowerMockito.when(BestsignUtil.getRtickStr()).thenReturn("9999999999999999");
        //PowerMockito.when(Utils.convertToUtf8(Mockito.anyString())).thenCallRealMethod();
        PowerMockito.when(BestsignUtil.toJsonString(Mockito.anyString())).thenCallRealMethod();
    }


    @Test
    @PrepareForTest({ BestsignUtil.class })
    public void testGetPostUrlByRsa() throws Exception {
        BestsignPropertiesBean requestBean = new BestsignPropertiesBean();
        requestBean.setHost("https://openapi.bestsign.info");
        requestBean.setDeveloperId("1862727489206354528");
        requestBean.setSignType("rsa");
        requestBean.setPrivateKey("MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKHeZ1tGMrj3EWrUWDGzPTml4QNa3Lh9/vlU+9kPB9G1E1zbTzxYZ7EmzEesEioWuqUGDx/wmMXsqNVI5HOiV1hzvCzhIFYy2S+wgAt8kEgifwhgRBn6qAO919bJ5G+T8e7yhKnZF1esk05SH7pYjd4x1Eso/UHZRpZa/yXBd+jTAgMBAAECgYAEQM3CbjPC/GruvamblLQVIbCp3+dQya67amo7p9NyxSk/FVwdn80JsJVJhNHtXS+GSoR3OGErQi6lfAbUqv1UgKxN35Xe3rPAVkurNxphVOCbvADW/32sGVnbz7UAb6pqrH3m4FNmuGqZeLd3lhVkxCvwEkfG3puFzk8oDPbwUQJBAP+K4XcEtl+bzvAibDwzuX+DNUt2ijKHVXDzaqGHUqvkfEAhGDYV3VND9Jfn+Legi0BCahFnDBCtX2JMcW7Wmj0CQQCiKJc/7OBCpUnZo0ULy2VrGEby0nemIe5fCR5SdqlxlsNvZ4cyRuW2LXjAUniNdHtx6UH9JaYd357DoWfkLJBPAkAbpzfG3Weu6Pl32wHDcgV82wIFbIp/9U01r+G2ISK9Hzii5/HqyGru+8eYOK4dkO4Awi8gOvp/Q4Oy63rK98YxAkEAjoovqbmGyA6TBARIxT1dQO5uLzRiiF57Mn7JcKNt/rMPx/WxGbjIY4NFCYl0/qLNgCwSHXvisY/H9x8CO8gQcQJAaiDJy//mXfvO3stPLZjYPdifBGY5xVdWpB/UrrLZbqw2gO0elkZImykrnDqYFu6rwNj+x7xpHuQ+LgD0yctpRg==");

        RequestUrlBuilder builder = new RequestUrlBuilder();
        builder.setBestsignProperties(requestBean);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("account", "test");

        //String buildUrl = builder.getPostUrlByRsa(params, "/user/reg");
        String buildUrl = builder.buildPOSTReqeustUrl("/user/reg", BestsignUtil.toJsonString(params));
        System.out.println("====result:" + buildUrl);
        String expected = "https://openapi.bestsign.info/user/reg/?developerId=1862727489206354528&rtick=9999999999999999&sign=NhIHWo8iLJK3v%2BbP4EBzlFLheRehxdqLBV4zIAmlg6BapLgtYa1AzlZfniXJBeUmPTYPAhKYF5Sp%2FCFGiDRYqdXld7eG2SIabZIDWoTmd%2BQbbd5Xs4wUL2uzKucSgc2Ip9NLtd5e4LljhSq72kYGjKeOMsZY37JbinnxWvbPuGE%3D&signType=rsa";
        Assert.assertEquals(expected, buildUrl);
    }


    @Test
    @PrepareForTest({ BestsignUtil.class })
    public void testGetUrlByRsa() throws Exception {
        BestsignPropertiesBean requestBean = new BestsignPropertiesBean();
        requestBean.setHost("https://openapi.bestsign.info");
        requestBean.setDeveloperId("1862727489206354528");
        requestBean.setSignType("rsa");
        requestBean.setPrivateKey("MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKHeZ1tGMrj3EWrUWDGzPTml4QNa3Lh9/vlU+9kPB9G1E1zbTzxYZ7EmzEesEioWuqUGDx/wmMXsqNVI5HOiV1hzvCzhIFYy2S+wgAt8kEgifwhgRBn6qAO919bJ5G+T8e7yhKnZF1esk05SH7pYjd4x1Eso/UHZRpZa/yXBd+jTAgMBAAECgYAEQM3CbjPC/GruvamblLQVIbCp3+dQya67amo7p9NyxSk/FVwdn80JsJVJhNHtXS+GSoR3OGErQi6lfAbUqv1UgKxN35Xe3rPAVkurNxphVOCbvADW/32sGVnbz7UAb6pqrH3m4FNmuGqZeLd3lhVkxCvwEkfG3puFzk8oDPbwUQJBAP+K4XcEtl+bzvAibDwzuX+DNUt2ijKHVXDzaqGHUqvkfEAhGDYV3VND9Jfn+Legi0BCahFnDBCtX2JMcW7Wmj0CQQCiKJc/7OBCpUnZo0ULy2VrGEby0nemIe5fCR5SdqlxlsNvZ4cyRuW2LXjAUniNdHtx6UH9JaYd357DoWfkLJBPAkAbpzfG3Weu6Pl32wHDcgV82wIFbIp/9U01r+G2ISK9Hzii5/HqyGru+8eYOK4dkO4Awi8gOvp/Q4Oy63rK98YxAkEAjoovqbmGyA6TBARIxT1dQO5uLzRiiF57Mn7JcKNt/rMPx/WxGbjIY4NFCYl0/qLNgCwSHXvisY/H9x8CO8gQcQJAaiDJy//mXfvO3stPLZjYPdifBGY5xVdWpB/UrrLZbqw2gO0elkZImykrnDqYFu6rwNj+x7xpHuQ+LgD0yctpRg==");

        RequestUrlBuilder builder = new RequestUrlBuilder();
        builder.setBestsignProperties(requestBean);

        Map<String, Object> requestParams = new HashMap<String, Object>();
        requestParams.put("imageName", "testImg");
        requestParams.put("account", "abc");
        //String buildUrl = builder.getUrlByRsa(requestParams, "/signatureImage/user/download");
        String buildUrl = builder.buildGETReqeustUrl("/signatureImage/user/download", requestParams);
        System.out.println("====result:" + buildUrl);
        String expected = "https://openapi.bestsign.info/signatureImage/user/download/?developerId=1862727489206354528&rtick=9999999999999999&sign=TFrLVgGdaRtNIhbvHjtA9FHh5Zxu8b4qiD4bkuAG2n%2Fwo333ehVJE%2B%2BcLporjVlqKcwrdz5h2GNgqAYIb8jyklgh1qZmrchpN3KTz82ak6QMxUsYogZ9TwhmYUS3C1yL810ihYYZA%2Bnub2StWQ5GUk%2BJaS1csM858rTX1DumRUQ%3D&signType=rsa&imageName=testImg&account=abc";
        Assert.assertEquals(expected, buildUrl);
    }


}
